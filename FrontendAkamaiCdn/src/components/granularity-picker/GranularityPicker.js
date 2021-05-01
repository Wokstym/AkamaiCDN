import DatePicker from "react-datepicker";
import {Typography} from "@material-ui/core";

const GranularityPicker = (props) => {
    return (
        <div>
        <Typography gutterBottom>
            Granularity:
        </Typography>
        <DatePicker
            selected={props.time}
            onChange={date => props.onTimeSelected(date)}
            showTimeSelect
            showTimeSelectOnly
            timeIntervals={props.timeIntervals}
            timeCaption="Time"
            dateFormat="HH:mm"
            timeFormat="HH:mm"
        />
        </div>
    )
}

export default GranularityPicker;