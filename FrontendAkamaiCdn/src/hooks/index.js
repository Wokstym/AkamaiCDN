import {useEffect, useState} from "react";
import {buildQs} from "../utils";

export function useFetch(endpoint, queryParams = {}, deps) {
    const [status, setStatus] = useState("idle");
    const [data, setData] = useState([]);

    useEffect(() => {
        if (!endpoint) return;
        const fetchData = async () => {
            setStatus("fetching");
            try {
                const url = `http://localhost:8090` + endpoint + buildQs(queryParams);
                console.log(url);
                const response = await fetch(url);
                console.log(response);
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
