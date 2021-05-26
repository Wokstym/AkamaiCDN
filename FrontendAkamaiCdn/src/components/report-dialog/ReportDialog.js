import React, {useState} from 'react';
import { PDFDownloadLink, PDFViewer } from "@react-pdf/renderer";
import ReportPdf from "./pdf-report/ReportPdf";
import { makeStyles } from '@material-ui/core/styles';
import {Button, Checkbox, FormControlLabel, Modal} from "@material-ui/core";
import {useSelectButtons} from "../../hooks";

const useStyles = makeStyles((theme) => ({
    paper: {
        position: 'absolute',
        width: 400,
        backgroundColor: theme.palette.background.paper,
        border: '2px solid #000',
        boxShadow: theme.shadows[5],
        padding: theme.spacing(2, 4, 3),
        top: '50%',
        left: '50%',
        transform: 'translate(-50%, -50%)'
    },
}));

const ReportDialog = ({tputData, rttData, packetLossData, specificData, specificDataType}) => {

    const classes = useStyles();
    const [open, setOpen] = useState(false);
    const [checkboxesOpen, setCheckboxesOpen] = useState({
        "Throughput": true,
        "Round Trip Time": true,
        "Packet Loss": true,
        "Specific Data": true
    })

    const handleClick = () => {
        setOpen(prevState => !prevState);
    }

    const handleChosen = (e) => {
        setCheckboxesOpen(prevState => ({...prevState, [e.target.name]: e.target.checked}));
    }

    const values = ["Throughput", "Round Trip Time", "Packet Loss", "Specific Data"];

    return (
        <div>
            <Button variant="contained" color={'primary'} onClick={handleClick}>Generate Report</Button>
            <div>
                {values.map(value => (<FormControlLabel
                    control={
                        <Checkbox
                            checked={checkboxesOpen[value]}
                            onChange={handleChosen}
                            name={value}
                            color="primary"
                        />
                    }
                    label={value}
                />))}
            </div>
            <Modal
                open={open}
                onClose={handleClick}
                aria-labelledby="simple-modal-title"
                aria-describedby="simple-modal-description"
            >
                <div className={classes.paper}>
                <PDFDownloadLink document={
                    <ReportPdf
                        tputData={checkboxesOpen["Throughput"] ? tputData : []}
                        rttData={checkboxesOpen["Round Trip Time"] ? rttData : []}
                        packetLossData={checkboxesOpen["Packet Loss"] ? packetLossData : []}
                        specificData={checkboxesOpen["Specific Data"] ? specificData : []}
                        specificDataType={specificDataType}
                    />} fileName={`report-${new Date().toDateString()}.pdf`}>
                    {({ blob, url, loading, error }) =>
                        loading ? 'Loading document...' : 'Download now!'
                    }
                </PDFDownloadLink>
                </div>
            </Modal>
        </div>
    );
};

export default ReportDialog;
