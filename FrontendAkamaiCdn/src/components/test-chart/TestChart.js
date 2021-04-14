import {HorizontalGridLines, LineMarkSeries, VerticalGridLines, XAxis, XYPlot, YAxis} from "react-vis";
import "react-vis/dist/style.css";
const TestChart = ({ width, height, data }) => (
    <XYPlot width={width} height={height}>
        <XAxis/>
        <YAxis/>
        <HorizontalGridLines />
        <VerticalGridLines />
        <LineMarkSeries data={data} />
    </XYPlot>
)

export default TestChart;