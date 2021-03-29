package agh.cs.backendAkamaiCDN.ping.service;

import agh.cs.backendAkamaiCDN.ping.entity.PingEntity;
import agh.cs.backendAkamaiCDN.ping.utils.TcpingExecutor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
@NoArgsConstructor
public class TcpingResultsService {

    private static final int PROBES = 100;

    public Optional<PingEntity> executeTcping(String siteName) {
        try {
            log.info("Executing ping");

            TcpingExecutor parser = TcpingExecutor.builder()
                    .probes(PROBES)
                    .siteName(siteName)
                    .interval(0.01)
                    .execute()
                    .parseResult();

            ArrayList<Double> times = parser.getTimes();
            double packetLoss = parser.getPacketLoss();

            log.info("Ping successful with packet loss: " + packetLoss);

            Double minTime = getMinTime(times);
            Double maxTime = getMaxTime(times);
            Double avgTime = getAvgTime(times);
            Double stdDivTime = getStandardDiv(times, avgTime);
            Date date = new Date(System.currentTimeMillis());

            return Optional.of(PingEntity.builder()
                    .site(siteName)
                    .id(date)
                    .minTime(minTime)
                    .maxTime(maxTime)
                    .averageTime(avgTime)
                    .standardDeviationTime(stdDivTime)
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