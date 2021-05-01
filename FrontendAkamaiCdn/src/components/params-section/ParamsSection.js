import {useEffect, useState} from "react";
import {Input, TextField, Typography} from "@material-ui/core";
import {buildQs} from "../../utils";

const ParamsSection = (props) => {
    const [numberOfProbes, setNumberOfProbes] = useState(100);
    const [intervalBetweenProbes, setIntervalBetweenProbes] = useState(1000);

    useEffect(() => {
        const interval = setInterval(() => {
            let url = "http://localhost:8090" + props.endpoint + '/save';
            let params = {numberOfProbes : numberOfProbes, interval : intervalBetweenProbes};
            url += buildQs(params);
            fetch(url);
        }, 60000);
        return () => clearInterval(interval);
    }, [numberOfProbes, intervalBetweenProbes, props]);

    const onChange = (setter) => {
        return (e) => {
            let val = parseInt(e.target.value);
            if(!isNaN(val)){
                setter(val);
            }
            else{
                console.log("Invalid value provided");
            }
        };
    }

    return (
        <div>
            <Typography variant={"h6"} gutterBottom display={'block'}>
                Monitor parameters:
            </Typography>
            <TextField
                type="number"
                label="Number Of Probes"
                value={numberOfProbes}
                onChange={onChange(setNumberOfProbes)}
            />
            <TextField
                type="number"
                label="Interval"
                value={intervalBetweenProbes}
                onChange={onChange(setIntervalBetweenProbes)}
            />
        </div>
    );
};

export default ParamsSection;