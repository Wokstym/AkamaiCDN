import {Section, SectionSpecific} from './components';
import './App.css'
import ParamsSection from "./components/params-section/ParamsSection";
import ReportDialog from "./components/report-dialog/ReportDialog";
import {Format} from "./utils";
import React, {useState} from "react";
import Switch from "@material-ui/core/Switch";
import {FormControlLabel} from "@material-ui/core";

function App() {
    const [currentTputData, setCurrentTputData] = useState([]);
    const [currentRttData, setCurrentRttData] = useState([]);
    const [currentPacketLossData, setCurrentPacketLossData] = useState([]);
    const [currentUrlSpecificData, setCurrentUrlSpecificData] = useState([]);
    const [specificUrlDataType, setSpecificUrlDataType] = useState("rtt");
    const [currentIpSpecificData, setCurrentIpSpecificData] = useState([]);
    const [specificIpDataType, setSpecificIpDataType] = useState("rtt");


    const isServerMode = process.env.REACT_APP_ENVIRONMENT === 'PRODUCTION'

    return (
        <main className="main">
            <ReportDialog
                tputData={currentTputData}
                rttData={currentRttData}
                packetLossData={currentPacketLossData}
                specificUrlData={currentUrlSpecificData}
                specificUrlDataType={specificUrlDataType}
                specificIpData={currentIpSpecificData}
                specificIpDataType={specificIpDataType}
                isServerMode={isServerMode}
            >
            </ReportDialog>
            <Section
                setter={setCurrentTputData}
                isServerMode={isServerMode}
                title={"Throughput"}
                endpoint={"/throughput"}
                getX={(data) => new Date(data.startDate)} getY={(data) => data.maxValue}
                yInfo={{
                    label: "Max",
                    format: Format.byte
                }}
                groupBy={"host"}
                stats={[
                    ["Host", "host"],
                    ["Max", "maxValue"],
                    ["Min", "minValue"],
                    ["Average", "averageValue"],
                    ["Start date", "startDate", (startDate) => new Date(startDate).toLocaleString("pol-PL")],
                    ["End date", "endDate", (endDate) => new Date(endDate).toLocaleString("pol-PL")]
                ]}
                timeIntervals={5}
                valueFields={["minValue", "maxValue", "averageValue"]}
            />
            <Section
                setter={setCurrentRttData}
                isServerMode={isServerMode}
                title={"Round trip time"}
                endpoint={"/rtt"}
                getX={(data) => new Date(data.startDate)}
                getY={(data) => data.averageTime}
                yInfo={{
                    label: "Avg time",
                    format: Format.millisecond
                }}
                groupBy={"host"}
                stats={[
                    ["Host", "host"],
                    ["Max", "maxTime"],
                    ["Min", "minTime"],
                    ["Average", "averageTime"],
                    ["Standard deviation", "standardDeviationTime"],
                    ["Start date", "startDate", (startDate) => new Date(startDate).toLocaleString("pol-PL")],
                    ["End date", "endDate", (endDate) => new Date(endDate).toLocaleString("pol-PL")],
                ]}
                renderParamsSection={
                    (endpoint) => !isServerMode
                        ? (<ParamsSection endpoint={endpoint}/>)
                        : null
                }
                valueFields={["maxTime", "minTime", "averageTime", "standardDeviationTime"]}
                timeIntervals={10}
            />
            <Section
                setter={setCurrentPacketLossData}
                isServerMode={isServerMode}
                title={"Packet loss"}
                endpoint={"/packet_loss"}
                getX={(data) => new Date(data.startDate)}
                getY={(data) => data.packetLoss}
                yInfo={{
                    label: "Packet loss",
                    format: Format.percent
                }}
                groupBy={"host"}
                stats={[
                    ["Host", "host"],
                    ["Packet loss", "packetLoss"],
                    ["Start date", "startDate", (startDate) => new Date(startDate).toLocaleString("pol-PL")],
                    ["End date", "endDate", (endDate) => new Date(endDate).toLocaleString("pol-PL")],
                ]}
                renderParamsSection={
                    (endpoint) => !isServerMode
                        ? (<ParamsSection endpoint={endpoint}/>)
                        : null
                }
                valueFields={["packetLoss"]}
                timeIntervals={10}
            />
            <SectionSpecific
                isServerMode={isServerMode}
                setter={setCurrentUrlSpecificData}
                typeSetter={setSpecificUrlDataType}
                groupBy={"url"}
                titleDesc={"specific info"}
            />
            {isServerMode &&
                <SectionSpecific
                    isServerMode={isServerMode}
                    setter={setCurrentIpSpecificData}
                    typeSetter={setSpecificIpDataType}
                    groupBy={"ipAddress"}
                    titleDesc={"data by IP"}
                />
            }
        </main>
    );
}

export default App;
