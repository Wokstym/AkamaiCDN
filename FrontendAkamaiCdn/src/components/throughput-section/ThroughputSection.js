import { TestChart } from "../../components";
import { useFetch } from "../../hooks";
import { useState } from "react";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import Typography from "@material-ui/core/Typography";
import "./ThroughputSection.css";
import { withStyles } from "@material-ui/core/styles";

const ThroughputSection = (props) => {
	const [startDate, setStartDate] = useState(new Date());
	const [endDate, setEndDate] = useState(new Date());
	let queryParams = {
		start_date: startDate.toJSON(),
		end_date: endDate.toJSON(),
	};
	const { status, data } = useFetch("/throughput", queryParams, [
		startDate,
		endDate,
	]);

	const [hoveredPoint, setHoveredPoint] = useState({});

	const parsedData = Object.entries(
		data.reduce((reducer, next) => {
			reducer[next.host] = reducer[next.host] || [];
			reducer[next.host].push({
				...next,
				x: new Date(next.startDate),
				y: next.max,
			});
			return reducer;
		}, {})
	);

	const GreyTextTypography = withStyles({
		root: {
			color: "#929596",
		},
	})(Typography);

	return (
		<div className="card">
			<GreyTextTypography variant={"h3"} gutterBottom>
				Throughput
			</GreyTextTypography>

			<div className="grid-container">
				<div class="grid-item">
					<TestChart
						width={800}
						height={500}
						data={parsedData}
						ylabel="Max"
						xlabel="Time"
						onNearestXY={(val, { index }) => {
							setHoveredPoint(val);
						}}
					/>
				</div>
				<div class="grid-item stats">
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

					<Typography display={"block"} gutterBottom>
						Host: {hoveredPoint.host}
					</Typography>
					<Typography display={"block"} gutterBottom>
						Max: {hoveredPoint.max}
					</Typography>
					<Typography display={"block"} gutterBottom>
						Min: {hoveredPoint.min}
					</Typography>
					<Typography display={"block"} gutterBottom>
						Start date: {new Date(hoveredPoint.startDate).toLocaleString("pol-PL")}
					</Typography>
					<Typography display={"block"} type="date" gutterBottom>
						End date: {new Date(hoveredPoint.endDate).toLocaleString("pol-PL")}
					</Typography>
					<Typography display={"block"} gutterBottom>
						Average date: {hoveredPoint.avg}
					</Typography>
				</div>
			</div>
		</div>
	);
};

export default ThroughputSection;
