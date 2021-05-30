import {
    ChartLabel,
    Crosshair,
    DiscreteColorLegend,
    Hint,
    HorizontalGridLines,
    LineMarkSeries,
    MarkSeries,
    VerticalGridLines,
    XAxis,
    FlexibleWidthXYPlot,
    YAxis
} from "react-vis";
import "react-vis/dist/style.css";
import './DataChart.css'
import Typography from "@material-ui/core/Typography";
import {useState} from "react";

const DataChart = ({
                       height,
                       data,
                       ylabel,
                       yformat,
                       xlabel,
                       stats,
                       probeChanges,
                       badPoints,
                       shouldShowDeviations,
                       showChangeInParameters
                   }) => {
    const [hintPoint, setHintPoint] = useState();
    const [isCursorOnPlot, setIsCursorOnPlot] = useState(false);

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
        <div id="container">
            <DiscreteColorLegend className="legend" height={200} width={300} items={data.map(([key,]) => {
                return key
            })}/>
            <div className={"chart-container"}>
            <FlexibleWidthXYPlot
                height={height}
                xType={"time"}
                margin={{bottom: 100, left: 100}}
                onMouseEnter={() => setIsCursorOnPlot(true)}
                onMouseLeave={() => setIsCursorOnPlot(false)}
            >
                <XAxis/>
                <YAxis
                    tickFormat={(el) => {
                        let format = yformat.find(val => el < val.to) ||
                            yformat[yformat.length - 1]
                        return `${Math.round(el / format.divider * 10) / 10}  ${format.unit}`
                    }}
                />
                {xLabelComponent}
                {yLabelComponent}
                <HorizontalGridLines/>
                <VerticalGridLines/>
                {shouldShowDeviations ?
                    <MarkSeries
                        data={badPoints}
                        color={"#FF0000"}
                        size={10}
                    /> : null
                }
                {showChangeInParameters?
                    probeChanges.map((val, index) => {
                        return (<Crosshair
                            titleFormat={() => (
                                {title: "Change", value: ""})}
                            itemsFormat={(d) => {
                                return [
                                    {title: 'Old Probes', value: d[0].oldProbes},
                                    {title: 'New Probes', value: d[0].newProbes},
                                    {title: 'Old Interval', value: d[0].oldInterval},
                                    {title: 'New Interval', value: d[0].newInterval}
                                ]
                            }}
                            key={index}
                            values={[val]}/>)
                    }): null
                }
                {
                    data.map(([key, value], index) => (
                        <LineMarkSeries
                            wobbly
                            curve={'curveMonotoneX'}
                            key={index}
                            data={value}
                        />
                    ))
                }
                {
                    <MarkSeries
                        data={data.flatMap(el => el[1])}
                        onNearestXY={(val) => setHintPoint(val)}
                        opacity={0}
                    />
                }
                {isCursorOnPlot && hintPoint ?
                    <Hint value={hintPoint}>
                        <div style={{
                            background: 'white',
                            borderRadius: '25px',
                            padding: '10px',
                            paddingTop: '0.1px',
                            paddingBottom: '0.1px',
                            color: 'black',
                            '-webkit-box-shadow': '5px 5px 15px rgba(0,0,0,0.4)'

                        }}>
                            <p>{
                                stats.map(([text, field, fn], index) => {
                                    return (
                                        <Typography key={index} display={"block"} gutterBottom>

                                            {text}: {hintPoint[field] && fn ? fn(hintPoint[field]) : hintPoint[field]}
                                        </Typography>
                                    )
                                })
                            }</p>
                        </div>
                    </Hint> : null
                }
            </FlexibleWidthXYPlot>
            </div>
        </div>
    ) : (
        <div id="container">
            <Typography className="error" display={"block"} gutterBottom>
                No data for chosen date range!
            </Typography>
            <div className={"chart-container"}>
            <FlexibleWidthXYPlot
                height={height}
                xType={"time"}
                xDomain={[new Date(Date.now() - 86400000), Date()]}
                yDomain={[0, 1]}
                margin={{bottom: 100, left: 100}}
                dontCheckIfEmpty
            >
                <XAxis/>
                <YAxis tickFormat={() => ""}/>
                <HorizontalGridLines/>
                <VerticalGridLines/>
                {xLabelComponent}
                {yLabelComponent}
            </FlexibleWidthXYPlot>
        </div>
        </div>
    );
    return render;
};

export default DataChart;
  