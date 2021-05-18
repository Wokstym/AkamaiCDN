import DatePicker from "react-datepicker";
import {InputLabel, Typography} from "@material-ui/core";

const GranularityPicker = (props) => {
    return (
        <div>
        <InputLabel gutterBottom>
            Granularity
        </InputLabel>
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