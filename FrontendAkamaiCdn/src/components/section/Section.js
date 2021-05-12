import {DataChart, GranularityPicker} from "../../components";
import {useFetch} from "../../hooks";
import {useEffect, useState} from "react";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import Typography from "@material-ui/core/Typography";
import "./Section.css";
import {withStyles} from "@material-ui/core/styles";
import {groupBy} from "../../utils";

function mergeDataPerHost(data, props){
    if(!props.filterFunction){
        // console.log(data)

        let endpointToParameter = {
            "/throughput": "avg",
            "/rtt": "averageTime",
            "/packet_loss": "packetLoss"
        }

        let knownHosts = ["youtube.com", "akamai.com", "facebook.com", "netflix.com"]

        let hostVal = {}
        let perHostCounter = {
            "youtube.com" : 0,
            "akamai.com" : 0,
            "facebook.com" : 0,
            "netflix.com" : 0
        }
        let entityPerHost = {
            "youtube.com" : {},
            "akamai.com" : {},
            "facebook.com" : {},
            "netflix.com" : {}
        }

        for(const sth of data){
            // console.log(sth)
            hostVal[sth['host']] = sth[endpointToParameter[props.endpoint]]
            entityPerHost[sth['host']] = {...entityPerHost[sth['host']], ...sth}
            perHostCounter[sth['host']] = perHostCounter[sth['host']] + 1
        }

        let brandNewData = []

        for (const host of knownHosts){
            // console.log(host)
            let tmp = entityPerHost[host]
            tmp[endpointToParameter[props.endpoint]] = hostVal[host]
            brandNewData.push(tmp)
        }

        // console.log(hostVal)
        // console.log(perHostCounter)
        // console.log(entityPerHost)

        console.log(data)
        console.log(brandNewData)
        return brandNewData
    }else{
        return data
    }
}

function splitByTenSeconds(data, props){

    let newData = []
    for(let i = 0; i<data.length;i+=20){
        let part;
        if (i+20 < data.length){
            part = data.slice(i, i+20)
        }else{
            part = data.slice(i, data.length)
        }
        console.log(part)
        newData.push(mergeDataPerHost(part, props))
    }
    console.log("Got")
    console.log(data)
    console.log("Returning")
    console.log(newData.flat())
}

function getGroupedData(data, whitelist, groupBy) {
    // Calculate the sums and group data (while tracking count)
    // console.log("Data: ", data)
    const reduced = data.reduce(function(m, d) {
        if (!m[d[groupBy]]) {
            m[d[groupBy]] = { ...d,
                count: 1
            };
            return m;
        }
        whitelist.forEach(function(key) {
            m[d[groupBy]][key] += d[key];
        });
        m[d[groupBy]].count += 1;
        return m;
    }, {});

    // console.log("Reduced: ", reduced)

    // Create new array from grouped data and compute the average
    return Object.keys(reduced).map(function(k) {
        const item = reduced[k];
        const itemAverage = whitelist.reduce(function(m, key) {
            m[key] = item[key] / item.count;
            // console.log("key: ", key, "item[key]", item[key], "item.count", item.count)
            return m;
        }, {})
        return {
            ...item, // Preserve any non white-listed keys
            ...itemAverage // Add computed averege for whitelisted keys
        }
    })
}

function getIndexes(data, startTime, endTime){
    let startIdx = -1;
    let endIdx = -1;
    for(let i = 1; i<data.length; i+=1){
        if (data[i-1]['startTime'] <= startTime && data[i]['startTime'] > startTime) startIdx = i;
        if (data[i-1]['startTime'] <= endTime && data[i]['startTime'] > endTime) endIdx = i;
    }
    return [startIdx, endIdx]
}

function getDataIncludingInterval(data, startIdx, interval){
    let gathered = []
    let start = data[startIdx]
    gathered.push(start)
    for(let i=startIdx+1; i<data.length; i+=1){
        // console.log(start['startTime'])
        // console.log("start.startDate", start.startDate, "start + intv", start.startDate+interval, "data[i].startDate", data[i].startDate, "warunek", data[i].startDate < start.startDate+interval)
        if (data[i].startDate < start.startDate+interval) gathered.push(data[i])
    }
    return gathered
}

function getDictionaryPerParameter(data, parameter){
    let newVal = {}
    for(let val of data){

        if(!newVal[val[parameter]]){
            newVal[val[parameter]] = []
        }

        newVal[val[parameter]].push(val)
        // console.log("val[parameter]:", val[parameter], "newVal[val[parameter]]: ", newVal[val[parameter]])
    }

    let evenNewerVal = []

    for(const [key, value] of Object.entries(newVal)){
        evenNewerVal.push([key, value])
    }

    return evenNewerVal
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
    const [hoveredPoint, setHoveredPoint] = useState({});

    let queryParams = {
        start_date: startDate.toJSON(),
        end_date: endDate.toJSON(),
    };

    const {status, data, setData} = useFetch(props.endpoint, queryParams, [
        startDate,
        endDate,
    ]);

    const filterData = props.filterFunction ? data.filter( data => props.filterFunction(data)) : data;

    let parsedData

    //=================NEW-END=======================
    console.log("NEW ALGO")
    let interval = granularity.getHours() * 3600000 + granularity.getMinutes() * 60000

    console.log("FilterData: ", filterData)

    let newValue = [];

    let endpointToParameter = {
        "/throughput": "max",
        "/rtt": "averageTime",
        "/packet_loss": "packetLoss"
    }

    for(let i = 0; i<filterData.length;i+=1){
        let portionData = getDataIncludingInterval(filterData, i, interval)
        // console.log("PortionData: ", portionData)
        // console.log("endpointToParameter[props.endpoint]: ", endpointToParameter[props.endpoint])
        let mergedData = getGroupedData(portionData, [endpointToParameter[props.endpoint]], props.groupBy)
        newValue.push(mergedData)
        i += portionData.length-1
    }

    console.log("newValue: ", newValue)

    parsedData = newValue.flat().map((value) => {
            return {...value, x: props.getX(value), y: props.getY(value)}
        })

    console.log("ParsedData before getting dict", parsedData)
    console.log("Getting dictionary")
    parsedData = getDictionaryPerParameter(parsedData, props.groupBy)
    //=================NEW-END=======================


    //=================OLD=======================
    // console.log("OLD ALGO")
    //     parsedData = groupBy(filterData, props.groupBy).map(([key, value]) => {
    //     let newValue = [];
    //
    //     let pointsToTake = (granularity.getHours() * 60 + granularity.getMinutes()) / props.timeIntervals;
    //     if(pointsToTake === 0){
    //         return [key, value];
    //     }
    //     for(let i = 0; i < value.length; i+= pointsToTake){
    //         let currentPoints = [];
    //         let j = i;
    //         while (currentPoints.length < pointsToTake && j < value.length){
    //             currentPoints.push(value[j]);
    //             j++;
    //         }
    //         let firstPoint = currentPoints[0];
    //         if(currentPoints.length === 1){
    //             newValue.push(firstPoint);
    //             continue;
    //         }
    //         let startReducer = {}
    //         props.valueFields.forEach(field => startReducer[field] = 0);
    //
    //         let summedValues = currentPoints.reduce((reducer, next) => {
    //             let ret = {};
    //             props.valueFields.forEach(field => ret[field] = reducer[field] + next[field]);
    //             return ret;
    //         })
    //
    //         let finalValues = Object.fromEntries(
    //             Object.entries(summedValues)
    //                 .map(([key, value]) => ([key, value / currentPoints.length]))
    //         );
    //         newValue.push({...firstPoint, ...finalValues})
    //     }
    //     return [key, newValue];
    // }).map(([key, value]) => {
    //     return [key, value.map(data => ({...data, x: props.getX(data), y: props.getY(data)}))]
    // })
    //=================OLD-END=======================

    console.log("At the end: ", parsedData)

    const GreyTextTypography = withStyles({
        root: {
            color: "#929596",
        },
    })(Typography);

    return (
        <div className="card">
            <GreyTextTypography variant={"h3"} gutterBottom>
                {props.title}
            </GreyTextTypography>
            {props.renderParamsSection ? props.renderParamsSection(props.endpoint) : undefined}
            <div className="grid-container">
                <div className="grid-item">
                    <DataChart
                        width={800}
                        height={500}
                        data={parsedData}
                        ylabel="Max"
                        xlabel="Time"
                        onNearestXY={(val) => {
                            setHoveredPoint(val);
                        }}
                    />
                </div>
                <div className="grid-item stats">
                    <DatePicker
                        selected={startDate}
                        onChange={(date) => setStartDate(date)}
                        showTimeSelect
                        timeFormat={"HH:mm"}
                        timeIntervals={15}
                        timeCaption={"time"}
                        label={"From"}
                        dateFormat={"MMMM d, yyyy HH:mm"}
                    />
                    <DatePicker
                        selected={endDate}
                        label={"To"}
                        onChange={(date) => setEndDate(date)}
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
                    {props.stats.map(([text, field, fn], index) => {
                        return (
                            <Typography key={index} display={"block"} gutterBottom>
                                {text}: {hoveredPoint[field] && fn ? fn(hoveredPoint[field]) : hoveredPoint[field]}
                            </Typography>
                        )
                    })}
                </div>
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

// if(!props.filterFunction){
    // let lastIndex = [-1, -1]
    //
    // for(let i = startDate.getTime(); i < endDate.getTime(); i+=props.timeIntervals){
    //     let idx = getIndexes(filterData, i, i + props.timeIntervals)
    //     if (idx[0] === -1  && idx[1] === -1){
    //         console.log(lastIndex)
    //         console.log(filterData[lastIndex[1]+1])
    //         i = filterData[lastIndex[1]+1]['startTime']-props.timeIntervals+1;
    //     }else{
    //         lastIndex[0] = idx[0]
    //         lastIndex[1] = idx[1]
    //     }
    //     let portionData = filterData.slice(idx[0], idx[1]);
    //     let mergedData = getGroupedData(portionData, [endpointToParameter[props.endpoint]])
    //     newValue.push(mergedData)
    // }
// }else{
//     parsedData = groupBy(filterData, props.groupBy).map(([key, value]) => {
//         let newValue = [];
//
//         let pointsToTake = (granularity.getHours() * 60 + granularity.getMinutes()) / props.timeIntervals;
//         if(pointsToTake === 0){
//             return [key, value];
//         }
//         for(let i = 0; i < value.length; i+= pointsToTake){
//             let currentPoints = [];
//             let j = i;
//             while (currentPoints.length < pointsToTake && j < value.length){
//                 currentPoints.push(value[j]);
//                 j++;
//             }
//             let firstPoint = currentPoints[0];
//             if(currentPoints.length === 1){
//                 newValue.push(firstPoint);
//                 continue;
//             }
//             let startReducer = {}
//             props.valueFields.forEach(field => startReducer[field] = 0);
//
//             let summedValues = currentPoints.reduce((reducer, next) => {
//                 let ret = {};
//                 props.valueFields.forEach(field => ret[field] = reducer[field] + next[field]);
//                 return ret;
//             })
//
//             let finalValues = Object.fromEntries(
//                 Object.entries(summedValues)
//                     .map(([key, value]) => ([key, value / currentPoints.length]))
//             );
//             newValue.push({...firstPoint, ...finalValues})
//         }
//         return [key, newValue];
//     }).map(([key, value]) => {
//         return [key, value.map(data => ({...data, x: props.getX(data), y: props.getY(data)}))]
//     })
// }