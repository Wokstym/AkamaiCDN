import React from 'react';
import {Document, Page, View, Text, StyleSheet} from "@react-pdf/renderer";
import {ThroughputSection, RttSection, PacketLossSection} from "../report-sections";

const BASE_WIDTH = 100;

const styles = StyleSheet.create({
    body: {
        padding: 10
    }
});

const typeMap = {
    "throughput": ThroughputSection,
    "rtt": RttSection,
    "packetLoss": PacketLossSection
}

const headerMap = {
    "throughput": "Throughput",
    "rtt": "Round trip time",
    "packetLoss": "Packet loss"
}

const ReportPdf = ({tputData, rttData, packetLossData, specificUrlData, specificUrlDataType, specificIpData, specificIpDataType}) => {
    return (
        <Document>
            <Page style={styles.body}>
                {tputData.length > 0 && <Text>Throughput data</Text>}
                {tputData.map(ThroughputSection)}
                {rttData.length > 0 && <Text>Round trip time data</Text>}
                {rttData.map(RttSection)}
                {packetLossData.length > 0 && <Text>Packet loss data</Text>}
                {packetLossData.map(PacketLossSection)}
                {specificUrlData.length > 0 && <Text>Specific Url data</Text>}
                {specificUrlData.length > 0 && <Text>{headerMap[specificUrlDataType]}</Text>}
                {specificUrlData.map(typeMap[specificUrlDataType])}
                {specificIpData.length > 0 && <Text>Specific IP data</Text>}
                {specificIpData.length > 0 && <Text>{headerMap[specificIpDataType]}</Text>}
                {specificIpData.map(typeMap[specificIpDataType])}
            </Page>
        </Document>
        )
}

export default ReportPdf