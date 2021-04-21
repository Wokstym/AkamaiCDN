import {TestChart} from "../../components";
import {useFetch} from "../../hooks";
import {useState} from "react";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import Typography from '@material-ui/core/Typography';

const ThroughputSection = (props) => {
    const [startDate, setStartDate] = useState(new Date());
    const [endDate, setEndDate] = useState(new Date());
    let queryParams = {
        start_date: startDate.toJSON(),
        end_date: endDate.toJSON(),
    }
    const {status, data} = useFetch(
        "/throughput",
        queryParams,
        [startDate, endDate]
    );

    const parsedData = Object.entries(data.reduce((reducer, next) => {
        reducer[next.host] = reducer[next.host] || [];
        reducer[next.host].push({
            ...next,
            x: new Date(next.startDate),
            y: next.max
        });
        return reducer;
    }, {}));

    return (
        <div>
            <Typography variant={"h3"} gutterBottom>
                Throughput
            </Typography>
            <DatePicker
            selected={startDate}
            onChange={date => setStartDate(date)}
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
            onChange={date => setEndDate(date)}
            showTimeSelect
            timeFormat={"HH:mm"}
            timeIntervals={15}
            timeCaption={"time"}
            dateFormat={"MMMM d, yyyy h:mm aa"}
            />
            {
                startDate.getTime() > endDate.getTime() &&

                <Typography color={"error"} variant={"caption"} display={"block"} gutterBottom>
                Error! start datetime is greater than end datetime!
                </Typography>
        }
        <TestChart width={1000} height={500} data={parsedData}/>
    </div>
    )
}

export default ThroughputSection;