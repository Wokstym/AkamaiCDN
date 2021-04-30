import {
    HorizontalGridLines,
    LineMarkSeries,
    VerticalGridLines,
    XAxis,
    XYPlot,
    YAxis,
    ChartLabel
} from "react-vis";
import "react-vis/dist/style.css";
import './DataChart.css'

const DataChart = ({width, height, data, ylabel, xlabel, onNearestXY}) => {

    let xLabelComponent = (
        <ChartLabel
            style={{
                fontWeight: "700",
            }}
            text={xlabel}
            className="alt-x-label"
            includeMargin={false}
            xPercent={0.5}
            yPercent={1.15}
        />
    );

    let yLabelComponent = (
        <ChartLabel
            style={{
                transform: "rotate(-90)",
                fontWeight: "700",
            }}
            text={ylabel}
            className="alt-y-label"
            includeMargin={false}
            xPercent={-0.08}
            yPercent={0.5}
        />
    );

    let render = data.length ? (
        <XYPlot
            width={width}
            height={height}
            xType={"time"}
            margin={{bottom: 100, left: 100}}
        >
            <XAxis/>
            <YAxis
                tickFormat={(el) => {
                    if (typeof el.getMonth !== 'function') {
                        el = Math.round(el / 1024 * 10) / 10
                        return el + " kb"
                    }
                    return el
                }}
            />
            <HorizontalGridLines/>
            <VerticalGridLines/>
            {xLabelComponent}
            {yLabelComponent}

            {data.map(([key, value], index) => {
                return(
                <LineMarkSeries curve={'curveMonotoneX'} key={index} data={value} onNearestXY={onNearestXY}/>
                // todo fix onNearest pointing only at one graph, showing only data from one line
            )})}
        </XYPlot>
    ) : (
        <div>
            {/* todo insert on top of graph info that no data */}
            <XYPlot
                width={width}
                height={height}
                xType={"time"}
                xDomain={[new Date(Date.now() - 86400000), Date()]}
                yDomain={[0, 1]}
                margin={{bottom: 100, left: 100}}
                dontCheckIfEmpty
            >
                <XAxis/>
                <YAxis/>
                <HorizontalGridLines/>
                <VerticalGridLines/>
                {xLabelComponent}
                {yLabelComponent}
            </XYPlot>
        </div>
    );
    return render;
};

export default DataChart;
  