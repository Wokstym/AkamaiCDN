import {useEffect, useState} from "react";

import TextField from "@material-ui/core/TextField";
import Button from "@material-ui/core/Button";


const ParamsSection = (props) => {
    const [numberOfProbes, setNumberOfProbes] = useState(100);
    const [intervalBetweenProbes, setIntervalBetweenProbes] = useState(1000);

    useEffect(() => {
        const interval = setInterval(() => {
            const url = new URL("https://localhost:8080" + props.endpoint),
                params = {numberOfProbes : numberOfProbes, interval : intervalBetweenProbes};
            Object.keys(params).forEach(key => url.searchParams.append(key, params[key]))
            fetch(url)
        }, 60000);
        return () => clearInterval(interval);
    }, []);

    return (
        <div>
            <TextField id="standard-basic" label="Number Of Probes" defaultValue={numberOfProbes}/>
            <TextField id="standard-basic" label="Interval" defaultValue={intervalBetweenProbes}/>
            <Button variant="contained" color="primary" onClick={() => {
                console.log("Add straight line to graph") }}>
                Save
            </Button>

        </div>
    );


};

export default ParamsSection;