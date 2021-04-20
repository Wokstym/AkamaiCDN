import {HorizontalGridLines, LineMarkSeries, VerticalGridLines, XAxis, XYPlot, YAxis} from "react-vis";
import "react-vis/dist/style.css";

const TestChart = ({ width, height, data }) => {
    let render = data.some(([key, value]) => value.length > 0) ?
        <XYPlot width={width} height={height} xType={"time"}>
            <XAxis/>
            <YAxis/>
            <HorizontalGridLines/>
            <VerticalGridLines/>
            {data.map(([key, value], index) => <LineMarkSeries key={index} data={value}/>)}
        </XYPlot> :
        <div>No data for chosen period :(</div>
        return (render)
}

export default TestChart;