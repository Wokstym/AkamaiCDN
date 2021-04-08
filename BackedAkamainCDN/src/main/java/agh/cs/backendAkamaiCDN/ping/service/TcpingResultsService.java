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
            Date startDate = new Date();

            TcpingExecutor executor = TcpingExecutor.builder(siteName)
                    .probes(PROBES)
                    .interval(1000) //1 packet every 1 ms
                    .execute();

            ArrayList<Double> times = executor.getTimes();
            double packetLoss = executor.getPacketLoss();

            log.info("Ping successful with packet loss: " + packetLoss);

            Date endDate = new Date();
            Double minTime = getMinTime(times);
            Double maxTime = getMaxTime(times);
            Double avgTime = getAvgTime(times);
            Double stdDivTime = getStandardDiv(times, avgTime);

            return Optional.of(PingEntity.builder()
                    .startDate(startDate)
                    .endDate(endDate)
                    .site(siteName)
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