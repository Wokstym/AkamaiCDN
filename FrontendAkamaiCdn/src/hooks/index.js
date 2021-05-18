import {useEffect, useState} from "react";
import {buildQs} from "../utils";
import {Checkbox, FormControlLabel} from "@material-ui/core";

export function useFetch(endpoint, queryParams = {}, deps) {
    const [status, setStatus] = useState("idle");
    const [data, setData] = useState([]);

    useEffect(() => {
        if (!endpoint) return;
        const fetchData = async () => {
            setStatus("fetching");
            try {
                const url = `http://localhost:8090` + endpoint + buildQs(queryParams);
                const response = await fetch(url);
                if (response.ok) {
                    const data = await response.json();
                    setData(data);
                    setStatus("fetched");
                } else {
                    setData([]);
                    setStatus("failed");
                }
            } catch (e) {
                setData([]);
                setStatus("failed");
            }
        };
        fetchData();
    }, deps || []);
    return {status, data, setData};
}

export function useSelectButtons(initValues, data, groupBy, filterFunction, deps=[]){
    const [allValues, setAllValues] = useState(initValues);
    const [selectedValues, setSelectedValues] = useState({});

    useEffect(() => {
        let filteredData = filterFunction ? data.filter(filterFunction) : data;
        setAllValues([...new Set(filteredData.map(data => data[groupBy]))]);
        setSelectedValues(filteredData.reduce((reducer, data) => ({[data[groupBy]]: true, ...reducer}), {}));
    }, [data, ...deps])

    const handleChange = (e) => {
        setSelectedValues({...selectedValues, [e.target.name]: e.target.checked});
    }

    const checkboxes = (<div>
        {
            allValues
            .map((value) => (<FormControlLabel
                control={
                    <Checkbox
                        checked={selectedValues[value]}
                        onChange={handleChange}
                        name={value}
                        color="primary"
                    />
                }
                label={value}
            />))
        }
    </div>)

    return [selectedValues, checkboxes];
}