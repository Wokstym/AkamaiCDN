import "react-datepicker/dist/react-datepicker.css";
import "./SectionSpecificUrl.css";
import Section from "../section/Section";
import React, {useEffect, useState} from "react";
import {InputLabel, MenuItem, Select} from "@material-ui/core";
import {Format} from "../../utils";

const SectionSpecificUrl = (props) => {

    const valToNameCDN = {
        "youtube.com": "Youtube",
        "akamai.com": "Akamai",
        "netflix.com": "Netflix",
        "facebook.com": "Facebook",
    }

    const valToNameParameter = {
        "throughput": "Max",
        "rtt": "Avg time",
        "packet_loss": "Packet loss",
    }

    const valToFormat = {
        "throughput": Format.byte,
        "rtt": Format.millisecond,
        "packet_loss": Format.byte,
    }

    const valToValueFields = {
        "throughput": ["minValue", "maxValue", "averageValue"],
        "rtt": ["maxTime", "minTime", "averageTime", "standardDeviationTime"],
        "packet_loss": ["packetLoss"],
    }

    const valToParameter = {
        "throughput": "maxValue",
        "rtt": "averageTime",
        "packet_loss": "packetLoss",
    }


    const valToGetYFunction = {
        "throughput": (data) => data.maxValue,
        "rtt": (data) => data.averageTime,
        "packet_loss": (data) => data.packetLoss,
    }

    const [selectedCDN, setSelectedCDN] = useState("youtube.com");
    const [selectedParameter, setSelectedParameter] = useState("rtt");

    useEffect(() => {
        props.typeSetter(selectedParameter);
    }, [selectedParameter])

    return (
        <div className="card">
            <InputLabel id="label">CDN</InputLabel>
            <Select labelId="label" id="select" value={selectedCDN} onChange={ event => {setSelectedCDN(event.target.value)}}>
                <MenuItem value="youtube.com">Youtube</MenuItem>
                <MenuItem value="akamai.com">Akamai</MenuItem>
                <MenuItem value="netflix.com">Netflix</MenuItem>
                <MenuItem value="facebook.com">Facebook</MenuItem>
            </Select>

            <InputLabel id="label">Parameter</InputLabel>
            <Select labelId="label" id="select" value={selectedParameter} onChange={ event => {setSelectedParameter(event.target.value)}}>
                <MenuItem value="rtt">Round trip time</MenuItem>
                <MenuItem value="throughput">Throughput</MenuItem>
                <MenuItem value="packet_loss">Packet loss</MenuItem>
            </Select>
            <Section
                setter={props.setter}
                title={valToNameCDN[selectedCDN] + " - specific info"}
                endpoint={"/" + selectedParameter}
                getX={(data) => new Date(data.startDate)}
                getY={valToGetYFunction[selectedParameter]}
                filterFunction={ (data) => data.host === selectedCDN}
                groupBy={"url"}
                yInfo={{
                    label: valToNameParameter[selectedParameter],
                    format: valToFormat[selectedParameter]
                }}
                stats={[
                    ["Host" , "host"],
                    ["Parameter",  valToParameter[selectedParameter]],
                    ["Start date", "startDate", (startDate) => new Date(startDate).toLocaleString("pol-PL")],
                    ["End date", "endDate", (endDate) => new Date(endDate).toLocaleString("pol-PL")],
                ]}
                valueFields={valToValueFields[selectedParameter]}
                timeIntervals={10}
            />
        </div>
    );
};

export default SectionSpecificUrl;
