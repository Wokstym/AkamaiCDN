import {Text, View} from "@react-pdf/renderer";
import {Table, TableCell, TableCellHeader, TableRow} from "../table";
import React from "react";

const BASE_WIDTH = 100;

export const ThroughputSection = ([key, values]) => {
    return(
        <View key={key}>
            <Text>{key}</Text>
            <Table>
                <TableRow>
                    <TableCellHeader width={20}>
                        Start date
                    </TableCellHeader>
                    <TableCellHeader width={20}>
                        End date
                    </TableCellHeader>
                    <TableCellHeader width={20}>
                        Min value
                    </TableCellHeader>
                    <TableCellHeader width={20}>
                        Max value
                    </TableCellHeader>
                    <TableCellHeader width={20}>
                        Average
                    </TableCellHeader>
                </TableRow>
                {values.map((r, index) => {
                    let width = BASE_WIDTH / 5
                    return (
                        <TableRow key={index}>
                            <TableCell width={width}>
                                {new Date(r.startDate).toLocaleString()}
                            </TableCell>
                            <TableCell width={width}>
                                {new Date(r.endDate).toLocaleString()}
                            </TableCell>
                            <TableCell width={width}>
                                {r.minValue.toFixed(4)}
                            </TableCell>
                            <TableCell width={width}>
                                {r.maxValue.toFixed(4)}
                            </TableCell>
                            <TableCell width={width}>
                                {r.averageValue.toFixed(4)}
                            </TableCell>
                        </TableRow>
                    )
                })}
            </Table>
        </View>
    )
}

export const RttSection = ([key, values]) => {
    return (
        <View key={key}>
            <Text>{key}</Text>
            <Table>
                <TableRow>
                    <TableCellHeader width={12.5}>
                        Start date
                    </TableCellHeader>
                    <TableCellHeader width={12.5}>
                        End date
                    </TableCellHeader>
                    <TableCellHeader width={12.5}>
                        Min time
                    </TableCellHeader>
                    <TableCellHeader width={12.5}>
                        Max time
                    </TableCellHeader>
                    <TableCellHeader width={12.5}>
                        Average
                    </TableCellHeader>
                    <TableCellHeader width={12.5}>
                        Standard deviation
                    </TableCellHeader>
                    <TableCellHeader width={12.5}>
                        Interval
                    </TableCellHeader>
                    <TableCellHeader width={12.5}>
                        Probes
                    </TableCellHeader>
                </TableRow>
                {values.map((r, index) => {
                    let width = BASE_WIDTH / 8;
                    return(
                        <TableRow key={index}>
                            <TableCell width={width}>{new Date(r.startDate).toLocaleString()}</TableCell>
                            <TableCell width={width}>{new Date(r.endDate).toLocaleString()}</TableCell>
                            <TableCell width={width}>{r.minTime.toFixed(4)}</TableCell>
                            <TableCell width={width}>{r.maxTime.toFixed(4)}</TableCell>
                            <TableCell width={width}>{r.averageTime.toFixed(4)}</TableCell>
                            <TableCell width={width}>{r.standardDeviationTime.toFixed(4)}</TableCell>
                            <TableCell width={width}>{r.interval.toFixed(4)}</TableCell>
                            <TableCell width={width}>{r.probes.toFixed(4)}</TableCell>
                        </TableRow>
                    )
                })}
            </Table>
        </View>
    )
}

export const PacketLossSection = ([key, values]) => {
    return (
        <View key={key}>
            <Text>{key}</Text>
            <Table>
                <TableRow>
                    <TableCellHeader width={20}>
                        Start date
                    </TableCellHeader>
                    <TableCellHeader width={20}>
                        End date
                    </TableCellHeader>
                    <TableCellHeader width={20}>
                        Packet Loss
                    </TableCellHeader>
                    <TableCellHeader width={20}>
                        Interval
                    </TableCellHeader>
                    <TableCellHeader width={20}>
                        Probes
                    </TableCellHeader>
                </TableRow>
                {values.map((r, index) => {
                    let width = BASE_WIDTH / 5;
                    return (
                        <TableRow key={index}>
                            <TableCell width={width}>
                                {new Date(r.startDate).toLocaleString()}
                            </TableCell>
                            <TableCell width={width}>
                                {new Date(r.endDate).toLocaleString()}
                            </TableCell>
                            <TableCell width={width}>
                                {r.packetLoss.toFixed(4)}
                            </TableCell>
                            <TableCell width={width}>
                                {r.interval.toFixed(4)}
                            </TableCell>
                            <TableCell width={width}>
                                {r.probes.toFixed(4)}
                            </TableCell>
                        </TableRow>
                    )
                })}
            </Table>
        </View>
    )
}