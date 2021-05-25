import React from 'react';
import { Document, Page, View, Text } from "@react-pdf/renderer";
import { Table, TableHeader, TableBody, TableCell, DataTableCell } from "@david.kucsai/react-pdf-table";

const ReportPdf = ({tputData, rttData, packetLossData, specificData}) => {

    return (
        <Document>
            <Page>
                <Text>
                    Throughput Data
                </Text>
                {tputData.map(([key, values]) => {
                    console.log(values);
                    return(
                        <View key={key}>
                            <Text>
                                {key}
                            </Text>
                            <Table data={values}>
                                <TableHeader>
                                    <TableCell>
                                        Start date
                                    </TableCell>
                                    <TableCell>
                                        End date
                                    </TableCell>
                                    <TableCell>
                                        Min value
                                    </TableCell>
                                    <TableCell>
                                        Max value
                                    </TableCell>
                                    <TableCell>
                                        Average
                                    </TableCell>
                                </TableHeader>
                                <TableBody>
                                    <DataTableCell getContent={(r) => new Date(r.startDate).toLocaleString()}/>
                                    <DataTableCell getContent={(r) => new Date(r.endDate).toLocaleString()}/>
                                    <DataTableCell getContent={(r) => r.minValue.toString()}/>
                                    <DataTableCell getContent={(r) => r.maxValue.toString()}/>
                                    <DataTableCell getContent={(r) => r.averageValue.toString()}/>
                                </TableBody>
                            </Table>
                        </View>
                    )
                })}}
            </Page>
            <Page>
                <Text>
                    Round Trip Time Data
                </Text>
                {rttData.map(([key, values]) => {
                    console.log(values)
                    return (
                        <View key={key}>
                            <Text>
                                {key}
                            </Text>
                            <Table data={values}>
                                <TableHeader>
                                    <TableCell>
                                        Start date
                                    </TableCell>
                                    <TableCell>
                                        End date
                                    </TableCell>
                                    <TableCell>
                                        Min time
                                    </TableCell>
                                    <TableCell>
                                        Max time
                                    </TableCell>
                                    <TableCell>
                                        Average
                                    </TableCell>
                                    <TableCell>
                                        Standard deviation
                                    </TableCell>
                                    <TableCell>
                                        Interval
                                    </TableCell>
                                    <TableCell>
                                        Probes
                                    </TableCell>
                                </TableHeader>
                                <TableBody>
                                    <DataTableCell getContent={(r) => new Date(r.startDate).toLocaleString()}/>
                                    <DataTableCell getContent={(r) => new Date(r.endDate).toLocaleString()}/>
                                    <DataTableCell getContent={(r) => r.minTime.toString()}/>
                                    <DataTableCell getContent={(r) => r.maxTime.toString()}/>
                                    <DataTableCell getContent={(r) => r.averageTime.toString()}/>
                                    <DataTableCell getContent={(r) => r.standardDeviationTime.toString()}/>
                                    <DataTableCell getContent={(r) => r.interval.toString()}/>
                                    <DataTableCell getContent={(r) => r.probes.toString()}/>
                                </TableBody>
                            </Table>
                        </View>
                    )
                })}}
            </Page>
            <Page>
                <Text>
                    Packet Loss Data
                </Text>
                {packetLossData.map(([key, values]) => {
                    console.log(values)
                    return (
                        <View key={key}>
                            <Text>
                                {key}
                            </Text>
                            <Table data={values}>
                                <TableHeader>
                                    <TableCell>
                                        Start date
                                    </TableCell>
                                    <TableCell>
                                        End date
                                    </TableCell>
                                    <TableCell>
                                        Packet Loss
                                    </TableCell>
                                    <TableCell>
                                        Interval
                                    </TableCell>
                                    <TableCell>
                                        Probes
                                    </TableCell>
                                </TableHeader>
                                <TableBody>
                                    <DataTableCell getContent={(r) => new Date(r.startDate).toLocaleString()}/>
                                    <DataTableCell getContent={(r) => new Date(r.endDate).toLocaleString()}/>
                                    <DataTableCell getContent={(r) => r.packetLoss.toString()}/>
                                    <DataTableCell getContent={(r) => r.interval.toString()}/>
                                    <DataTableCell getContent={(r) => r.probes.toString()}/>
                                </TableBody>
                            </Table>
                        </View>
                    )
                })}}
            </Page>
        </Document>
        )
}

export default ReportPdf