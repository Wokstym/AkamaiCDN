import "react-datepicker/dist/react-datepicker.css";
import "./SectionSpecificUrl.css";
import Section from "../section/Section";
import React, { useState } from "react";
import {InputLabel, MenuItem, Select} from "@material-ui/core";

const SectionSpecificUrl = (props) => {

    const valToNameCDN = {
        "youtube.com": "Youtube",
        "akamai.com": "Akamai",
        "netflix.com": "Netflix",
        "facebook.com": "Facebook",
    }

    const valToNameParameter = {
        "throughput": "Throughput",
        "rtt": "Round trip time",
        "packet_loss": "Packet loss",
    }

    const valToValueFields = {
        "throughput": ["min", "max", "avg"],
        "rtt": ["maxTime", "minTime", "averageTime", "standardDeviationTime"],
        "packet_loss": ["packetLoss"],
    }

    const valToGetYFunction = {
        "throughput": (data) => data.max,
        "rtt": (data) => data.averageTime,
        "packet_loss": (data) => data.packetLoss,
    }

    const [selectedCDN, setSelectedCDN] = useState("youtube.com");
    const [selectedParameter, setSelectedParameter] = useState("rtt");

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
                title={valToNameCDN[selectedCDN] + " - specific info"}

                // TODO: To możnaby jakoś zmodyfokować tak, żeby przy zmianie endpointa od razu wypisywało
                // TODO: tak jak obenie zmienia się od razu wykres przy zmianie CDN,
                // TODO: bo tak to trzeba zmienić datę
                endpoint={"/" + selectedParameter}

                getX={(data) => new Date(data.startDate)}
                getY={valToGetYFunction[selectedParameter]}
                filterFunction={ (data) => data.host === selectedCDN}
                groupBy={"url"}
                stats={[
                    // TODO: To wygląda turbo chujowo, ale jak tak nie jest to się nie odświeża zaraz po zmianie
                    ["Host" , "host"],
                    ["Parameter: " + selectedParameter,  ""],
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
