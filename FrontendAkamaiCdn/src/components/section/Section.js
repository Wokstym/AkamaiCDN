import {DataChart, GranularityPicker} from "../../components";
import {useFetch} from "../../hooks";
import {useState} from "react";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import Typography from "@material-ui/core/Typography";
import "./Section.css";
import {withStyles} from "@material-ui/core/styles";
import {groupBy} from "../../utils";

const Section = (props) => {
    const granularityStartDate = new Date();
    granularityStartDate.setHours(0, props.timeIntervals);
    const [startDate, setStartDate] = useState(new Date());
    const [endDate, setEndDate] = useState(new Date());
    const [granularity, setGranularity] = useState(granularityStartDate);

    let queryParams = {
        startDate: startDate.toJSON(),
        endDate: endDate.toJSON(),
    };

    const {status, data, setData} = useFetch(props.endpoint, queryParams, [
        startDate,
        endDate,
    ]);

    const groupedData = groupBy(data, props.groupBy);

    let points = groupedData.flatMap(([, value]) => {
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
                        newProbes: nextElement.probes,
                        newInterval: nextElement.interval,
                        x: new Date(middleDate),
                        host: currentElement.host
                    })
                }
            }
        }
        return hostPoints
    })
    const parsedData = groupedData.map(([key, value]) => {


        let newValue = [];
        let pointsToTake = (granularity.getHours() * 60 + granularity.getMinutes()) / props.timeIntervals;
        if (pointsToTake === 0) {
            return [key, value];
        }
        for (let i = 0; i < value.length; i += pointsToTake) {
            let currentPoints = [];
            let j = i;
            while (currentPoints.length < pointsToTake && j < value.length) {
                currentPoints.push(value[j]);
                j++;
            }
            let firstPoint = currentPoints[0];
            if (currentPoints.length === 1) {
                newValue.push(firstPoint);
                continue;
            }
            let startReducer = {}
            props.valueFields.forEach(field => startReducer[field] = 0);

            let summedValues = currentPoints.reduce((reducer, next) => {
                let ret = {};
                props.valueFields.forEach(field => ret[field] = reducer[field] + next[field]);
                return ret;
            })

            let finalValues = Object.fromEntries(
                Object.entries(summedValues)
                    .map(([key, value]) => ([key, value / currentPoints.length]))
            );
            newValue.push({...firstPoint, ...finalValues})
        }

        return [key, newValue];
    }).map(([key, value]) => {
        return [key, value.map(data => ({...data, x: props.getX(data), y: props.getY(data)}))]
    })


    const GreyTextTypography = withStyles({
        root: {
            color: "#929596",
        },
    })(Typography);

    return (
        <div className="card">
            <GreyTextTypography variant={"h4"} gutterBottom>
                {props.title}
            </GreyTextTypography>
            {props.renderParamsSection ? props.renderParamsSection(props.endpoint) : undefined}
            <div className="grid-item stats">
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

            <div className="grid-container">
                    <DataChart
                        width={800}
                        height={500}
                        data={parsedData}
                        ylabel={props.yInfo.label}
                        yformat={props.yInfo.format}
                        xlabel="Time"
                        stats={props.stats}
                        probeChanges={points}
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
        </div>
    );
};

export default Section;
