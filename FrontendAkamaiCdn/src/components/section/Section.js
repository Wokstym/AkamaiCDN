import {DataChart} from "../../components";
import {useFetch} from "../../hooks";
import {useEffect, useState} from "react";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import Typography from "@material-ui/core/Typography";
import "./Section.css";
import {withStyles} from "@material-ui/core/styles";
import {groupBy} from "../../utils";

const Section = (props) => {
    const [startDate, setStartDate] = useState(new Date());
    const [endDate, setEndDate] = useState(new Date());
    let queryParams = {
        start_date: startDate.toJSON(),
        end_date: endDate.toJSON(),
    };
    const {status, data, setData} = useFetch(props.endpoint, queryParams, [
        startDate,
        endDate,
    ]);

    const [hoveredPoint, setHoveredPoint] = useState({});

    const parsedData = groupBy(data, props.groupBy).map(([key, value]) => {
        return [key, value.map(data => ({...data, x: props.getX(data), y: props.getY(data)}))]
    })

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
                        dateFormat={"MMMM d, yyyy h:mm aa"}
                    />
                    <DatePicker
                        selected={endDate}
                        label={"To"}
                        onChange={(date) => setEndDate(date)}
                        showTimeSelect
                        timeFormat={"HH:mm"}
                        timeIntervals={15}
                        timeCaption={"time"}
                        dateFormat={"MMMM d, yyyy h:mm aa"}
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
        </div>
    );
};

export default Section;
