import {DataChart, GranularityPicker} from "../../components";
import {useFetch, useSelectButtons} from "../../hooks";
import React, {useEffect, useState} from "react";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import Switch from '@material-ui/core/Switch';
import Typography from "@material-ui/core/Typography";
import "./Section.css";
import {withStyles} from "@material-ui/core/styles";
import {FormControlLabel, InputLabel} from "@material-ui/core";


const GreyTextTypography = withStyles({
    root: {
        color: "#929596",
    },
})(Typography);

function getGroupedData(data, whitelist, groupBy) {
    // Calculate the sums and group data (while tracking count)
    const reduced = data.reduce(function (m, d) {
        if (!m[d[groupBy]]) {
            m[d[groupBy]] = {
                ...d,
                count: 1
            };
            return m;
        }
        whitelist.forEach(function (key) {
            m[d[groupBy]][key] += d[key];
        });
        m[d[groupBy]].count += 1;
        return m;
    }, {});

    // Create new array from grouped data and compute the average
    return Object.keys(reduced).map(function (k) {
        const item = reduced[k];
        const itemAverage = whitelist.reduce(function (m, key) {
            m[key] = item[key] / item.count;
            return m;
        }, {})
        return {
            ...item, // Preserve any non white-listed keys
            ...itemAverage // Add computed averege for whitelisted keys
        }
    })
}

function getDataIncludingInterval(data, startIdx, interval) {
    let gathered = []
    let start = data[startIdx]
    gathered.push(start)
    for (let i = startIdx + 1; i < data.length; i += 1) {
        if (data[i].startDate < start.startDate + interval) gathered.push(data[i])
    }
    return gathered
}

function getDictionaryPerParameter(data, parameter) {
    let newVal = {}
    for (let val of data) {
        if (!newVal[val[parameter]]) {
            newVal[val[parameter]] = []
        }
        newVal[val[parameter]].push(val)
    }

    let evenNewerVal = []

    for (const [key, value] of Object.entries(newVal)) {
        evenNewerVal.push([key, value])
    }

    return evenNewerVal
}

function getBreakingPoints(value) {
    let hostPoints = []

    for (let i = 0; i < value.length; i++) {
        let currentElement = value[i];

        if (i + 1 < value.length) {
            let nextElement = value[i + 1];

            if ((currentElement.probes !== nextElement.probes) || (currentElement.interval !== nextElement.interval)) {
                let firstDate = currentElement.startDate;
                let secondDate = nextElement.startDate;
                let middleDate = (secondDate + firstDate) / 2
                hostPoints.push({
                    oldProbes: currentElement.probes,
                    oldInterval: currentElement.interval,
                    newProbes: nextElement.probes,
                    newInterval: nextElement.interval,
                    x: new Date(middleDate),
                    host: currentElement.host
                })
                i++; // dont compare same elements twice
            }
        }
    }
    return hostPoints
}

function findBadPoints(points) {
    let result = []
    for (let i = 1; i < points.length - 1; i += 1) {
        let nextElement = points[i + 1].y;
        let element = points[i].y;
        let prevElement = points[i - 1].y

        let avgNeighb = (nextElement + prevElement) / 2.0

        if (avgNeighb * 0.2 > element || avgNeighb * 1.8 < element) {
            result.push(points[i])
        }
    }

    return result;
}

const Section = (props) => {
    console.log("Title: ", props.title)
    const granularityStartDate = new Date();
    granularityStartDate.setHours(0, props.timeIntervals);
    const now = new Date();
    now.setHours(0);
    now.setMinutes(0);
    const [startDate, setStartDate] = useState(now);
    const [endDate, setEndDate] = useState(new Date());
    const [granularity, setGranularity] = useState(granularityStartDate);
    const [shouldShowDeviations, setShouldShowDeviations] = useState(false);
    const [showChangeInParameters, setShowChangeInParameters] = useState(false);

    let queryParams = {
        startDate: startDate.toJSON(),
        endDate: endDate.toJSON(),
    };

    const host = props.isServerMode
        ? `https://akamai-cdn.herokuapp.com`
        : `http://localhost:8090`;

    const {status, data, setData} = useFetch(host, props.endpoint, queryParams, [
        startDate,
        endDate,
        props.endpoint,
        granularity,
        host
    ]);

    const [selectedValues, checkboxes] = useSelectButtons(
        [],
        data,
        props.groupBy,
        props.filterFunction,
        [props.endpoint, props.title]
    );

    const filterData = props.filterFunction ? data.filter(data => props.filterFunction(data)) : data;

    let interval = granularity.getHours() * 3600000 + granularity.getMinutes() * 60000

    let newValue = [];

    let endpointToParameter = {
        "/throughput": "maxValue",
        "/rtt": "averageTime",
        "/packet_loss": "packetLoss"
    }

    let points = getBreakingPoints(filterData)

    for (let i = 0; i < filterData.length; i += 1) {
        let portionData = getDataIncludingInterval(filterData, i, interval)
        let mergedData = getGroupedData(portionData, [endpointToParameter[props.endpoint]], props.groupBy)
        newValue.push(mergedData)
        i += portionData.length - 1
    }

    let parsedData = newValue.flat().map((value) => {
        return {...value, x: props.getX(value), y: props.getY(value)}
    })

    parsedData = getDictionaryPerParameter(parsedData, props.groupBy)

    parsedData = parsedData.filter(([key, value]) => selectedValues[key]);

    let badPoints = parsedData.flatMap(e => findBadPoints(e[1]))

    useEffect(() => {
        //console.log(parsedData);
        props.setter(parsedData);
    }, [data, selectedValues])

    return (
        <div className="card">
            <GreyTextTypography variant={"h4"} gutterBottom>
                {props.title}
            </GreyTextTypography>
            {props.renderParamsSection ? props.renderParamsSection(props.endpoint) : undefined}
            <div className="grid-item stats">
                <div>
                    <InputLabel gutterBottom>
                        From
                    </InputLabel>
                    <DatePicker
                        label={"From"}
                        selected={startDate}
                        onChange={(date) => setStartDate(date)}
                        selectsStart
                        startDate={startDate}
                        endDate={endDate}
                        showTimeSelect
                        timeFormat={"HH:mm"}
                        timeIntervals={15}
                        timeCaption={"time"}
                        dateFormat={"MMMM d, yyyy HH:mm"}
                    />
                </div>
                <div>
                    <InputLabel gutterBottom>
                        To
                    </InputLabel>
                    <DatePicker
                        label={"To"}
                        selected={endDate}
                        onChange={(date) => setEndDate(date)}
                        selectsEnd
                        startDate={startDate}
                        endDate={endDate}
                        minDate={startDate}
                        showTimeSelect
                        timeFormat={"HH:mm"}
                        timeIntervals={15}
                        timeCaption={"time"}
                        dateFormat={"MMMM d, yyyy HH:mm"}
                    />
                </div>
                <GranularityPicker
                    time={granularity}
                    onTimeSelected={(time) => {
                        let newTime = granularityStartDate;
                        newTime.setHours(time.getHours(), time.getMinutes())
                        setGranularity(newTime);
                    }}
                    timeIntervals={props.timeIntervals}
                />
                <FormControlLabel
                    control={<Switch size="small" checked={shouldShowDeviations}
                                     onChange={(event) => setShouldShowDeviations(event.target.checked)}/>}
                    label="Show deviations"
                />
                <FormControlLabel
                    control={<Switch size="small" checked={showChangeInParameters}
                                     onChange={(event) => setShowChangeInParameters(event.target.checked)}/>}
                    label="Show change in parameters"
                />
                {checkboxes}
                {startDate.getTime() > endDate.getTime() && (
                    <Typography
                        color={"error"}
                        variant={"caption"}
                        display={"block"}
                        gutterBottom
                    >
                        Error! start datetime is greater than end datetime!
                    </Typography>
                )}
            </div>

            <div>
                <DataChart
                    height={500}
                    data={parsedData}
                    ylabel={props.yInfo.label}
                    yformat={props.yInfo.format}
                    xlabel="Time"
                    stats={props.stats}
                    probeChanges={points}
                    badPoints={badPoints}
                    shouldShowDeviations={shouldShowDeviations}
                    showChangeInParameters={showChangeInParameters}
                />
            </div>
        </div>
    );
};

export default React.memo(Section, (prevProps, nextProps) => {
    return prevProps.title === nextProps.title && prevProps.endpoint === nextProps.endpoint && prevProps.isServerMode === nextProps.isServerMode
});