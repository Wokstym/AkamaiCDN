package agh.cs.backendAkamaiCDN.ping.service;

import agh.cs.backendAkamaiCDN.ping.entity.PacketLossEntity;
import agh.cs.backendAkamaiCDN.ping.entity.RTTEntity;
import agh.cs.backendAkamaiCDN.ping.utils.ResultsParser;
import agh.cs.backendAkamaiCDN.ping.utils.TcpingExecutor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
@NoArgsConstructor
public class TcpingResultsService {

    public Optional<RTTEntity> execTcpingForRTT(Integer numberOfProbes, Integer interval, @NonNull String host) {
        try {
            log.info("Executing ping for RTT calling : " + host);
            Date startDate = new Date();

            TcpingExecutor executor = TcpingExecutor.builder()
                    .probes(numberOfProbes)
                    .interval(interval)
                    .host(host)
                    .execute();

            ArrayList<Double> times = ResultsParser.parseRTT(executor.getBufferedReader());

            Date endDate = new Date();
            Double minTime = getMinTime(times);
            Double maxTime = getMaxTime(times);
            Double avgTime = getAvgTime(times);
            Double stdDivTime = getStandardDiv(times, avgTime);

            return Optional.of(RTTEntity.builder()
                    .startDate(startDate)
                    .endDate(endDate)
                    .host(host)
                    .minTime(minTime)
                    .maxTime(maxTime)
                    .averageTime(avgTime)
                    .standardDeviationTime(stdDivTime)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<PacketLossEntity> execTcpingForPacketLoss(Integer numberOfProbes, Integer interval, @NonNull String host) {
        try {
            log.info("Executing ping for Packet Loss calling : " + host);
            Date startDate = new Date();

            TcpingExecutor executor = TcpingExecutor.builder()
                    .probes(numberOfProbes)
                    .interval(interval)
                    .host(host)
                    .execute();

            double packetLoss = ResultsParser.parsePacketLoss(executor.getBufferedReader());
            Date endDate = new Date();

            return Optional.of(PacketLossEntity.builder()
                    .startDate(startDate)
                    .endDate(endDate)
                    .host(host)
                    .packetLoss(packetLoss)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private Double getMinTime(ArrayList<Double> times) {
        return times.stream()
                .min(Double::compare)
                .orElse(-1.0);
    }

    private Double getMaxTime(ArrayList<Double> times) {
        return times.stream()
                .max(Double::compare)
                .orElse(-1.0);
    }

    private double getAvgTime(ArrayList<Double> times) {
        return times.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(-1.0);
    }

    public Double getStandardDiv(ArrayList<Double> values, Double avg) {
        double variance = values.stream()
                .map(x -> x - avg)
                .map(x -> x * x)
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
        return Math.sqrt(variance);
    }
}