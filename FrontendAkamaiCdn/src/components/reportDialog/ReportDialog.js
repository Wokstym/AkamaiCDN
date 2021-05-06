import React, {useState} from 'react';
import Button from '@material-ui/core/Button';
import {
    Checkbox,
    Dialog,
    DialogTitle,
    List,
    ListItem,
    ListItemIcon,
    ListItemText,
    makeStyles,
    Typography
} from "@material-ui/core";
import DatePicker from "react-datepicker";

const useStyles = makeStyles(theme => ({
    dialogWrapper: {
        padding: theme.spacing(2),
        position: "absolute",
        top: theme.spacing(5)
    }
}))

const ReportDialog = (props) => {
    const [open, setOpen] = React.useState(false);
    const [startDate, setStartDate] = useState(new Date());
    const [endDate, setEndDate] = useState(new Date());
    const CDNList = ["akamai.com", "facebook.com", "google.com", "youtube.com"]
    const paramsList = ["throughput", "rtt", "packet_loss"]
    const [CDNChecked, setCDNChecked] = useState(new Set());
    const [ParamsChecked, setParamsChecked] = useState(new Set());
    const classes = useStyles();

    const handleToggleCDN = (value) => () => {
        if(CDNChecked.has(value)){
            setCDNChecked(checked => new Set([...checked].filter(x => x !== value)))
        } else {
            setCDNChecked(checked => new Set([...checked, value]) )
        }
    };

    const handleToggleParams = (value) => () => {
        if(ParamsChecked.has(value)){
            setParamsChecked(checked => new Set([...checked].filter(x => x !== value)))
        } else {
            setParamsChecked(checked => new Set([...checked, value]) )
        }
    };

    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };

    const handleGenerateReport = () => {
        let queryParams = {
            start_date: startDate.toJSON(),
            end_date: endDate.toJSON(),
        };

        /*const {status, data, setData} = useFetch("rtt", queryParams, [
            startDate,
            endDate,
        ]);
        console.log(data)*/
        alert("Creating PDF")
    }

    return (
        <div>
            <Button variant="outlined" color="primary" onClick={handleClickOpen}>
                Generate Report
            </Button>
            <Dialog open={open} onClose={handleClose} fullWidth={true} maxWidth={"md"} classes={{ paper: classes.dialogWrapper }} >
                <DialogTitle>
                    <Typography variant={"h6"} component={"div"}>
                        {props.title}
                    </Typography>
                </DialogTitle>
                <DatePicker
                    selected={startDate}
                    onChange={(date) => setStartDate(date)}
                    showTimeSelect
                    timeFormat={"HH:mm"}
                    timeIntervals={15}
                    timeCaption={"time"}
                    label={"From"}
                    dateFormat={"MMMM d, yyyy HH:mm"}
                />
                <DatePicker
                    selected={endDate}
                    label={"To"}
                    onChange={(date) => setEndDate(date)}
                    showTimeSelect
                    timeFormat={"HH:mm"}
                    timeIntervals={15}
                    timeCaption={"time"}
                    dateFormat={"MMMM d, yyyy HH:mm"}
                />
                <Typography variant={"h6"} component={"div"}>
                    List of CDNs
                </Typography>
                <List>
                    {CDNList.map((value) => {
                        return (
                            <ListItem key={value} role={undefined} dense button onClick={handleToggleCDN(value)}>
                                <ListItemIcon>
                                    <Checkbox
                                        edge="start"
                                        checked={CDNChecked.has(value)}
                                        tabIndex={-1}
                                        disableRipple
                                    />
                                </ListItemIcon>
                                <ListItemText primary={value} />
                            </ListItem>
                        );
                    })}
                </List>
                <br/>
                <Typography variant={"h6"} component={"div"}>
                    List of parameters
                </Typography>
                <List>
                    {paramsList.map((value) => {
                        return (
                            <ListItem key={value} role={undefined} dense button onClick={handleToggleParams(value)}>
                                <ListItemIcon>
                                    <Checkbox
                                        edge="start"
                                        checked={ParamsChecked.has(value)}
                                        tabIndex={-1}
                                        disableRipple
                                    />
                                </ListItemIcon>
                                <ListItemText primary={value} />
                            </ListItem>
                        );
                    })}
                </List>
                <br/>
                <div>
                    <Button variant="contained" color="secondary" onClick={handleGenerateReport}>
                        Generate
                    </Button>
                    <Button variant="contained" color="default" onClick={handleClose}>
                        Cancel
                    </Button>
                </div>
            </Dialog>
        </div>
    );
};

export default ReportDialog;
