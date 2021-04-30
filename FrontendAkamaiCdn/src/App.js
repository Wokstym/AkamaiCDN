import {Section,} from './components';
import './App.css'
import ParamsSection from "./components/params-section/ParamsSection";

function App() {

    return (
        <main className="main">
            <Section
                title={"Throughput"}
                endpoint={"/throughput"}
                getX={(data) => new Date(data.startDate)}
                getY={(data) => data.max}
                groupBy={"host"}
                stats={[
                    ["Host", "host"],
                    ["Max", "max"],
                    ["Min", "min"],
                    ["Average", "avg"],
                    ["Start date", "startDate", (startDate) => new Date(startDate).toLocaleString("pol-PL")],
                    ["End date", "endDate", (endDate) => new Date(endDate).toLocaleString("pol-PL")]
                ]}
            />
            <Section
                title={"Round trip time"}
                endpoint={"/rtt"}
                getX={(data) => new Date(data.startDate)}
                getY={(data) => data.averageTime}
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
                    (endpoint) => (<ParamsSection endpoint={endpoint}/>)
                }
            />
            <Section
                title={"Packet loss"}
                endpoint={"/packet_loss"}
                getX={(data) => new Date(data.startDate)}
                getY={(data) => data.packetLoss}
                groupBy={"host"}
                stats={[
                    ["Host", "host"],
                    ["Max", "maxTime"],
                    ["Min", "minTime"],
                    ["Average", "averageTime"],
                    ["Standard deviation", "standardDeviationTime"],
                    ["Packet loss", "packetLoss"],
                    ["Start date", "startDate", (startDate) => new Date(startDate).toLocaleString("pol-PL")],
                    ["End date", "endDate", (endDate) => new Date(endDate).toLocaleString("pol-PL")],
                ]}
                renderParamsSection={
                        (endpoint) => (<ParamsSection endpoint={endpoint}/>)
                }
            />
        </main>
    );
}

export default App;
