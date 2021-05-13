package agh.cs.backendAkamaiCDN.ping.application;

import agh.cs.backendAkamaiCDN.common.CDNConfig;
import agh.cs.backendAkamaiCDN.common.CDNConfig;
import agh.cs.backendAkamaiCDN.ping.domain.PacketLossEntity;
import agh.cs.backendAkamaiCDN.ping.domain.RTTEntity;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@NoArgsConstructor
public class TcpingResultsService {
    public List<RTTEntity> execTcpingForRTT(Integer numberOfProbes, Integer interval, @NonNull CDNConfig.Site site) {

        String mainHost = site.getGeneralHost();
        log.info("Executing ping for RTT for host : " + mainHost);

        return site.getHosts()
                .stream()
                .flatMap(url -> getRttEntity(numberOfProbes, interval, mainHost, url))
                .collect(Collectors.toList());
    }

    private Stream<RTTEntity> getRttEntity(Integer numberOfProbes, Integer interval, String mainHost, String url) {
        try {
            log.info("Executing ping for RTT calling url : " + url);
            Date startDate = new Date();

            TcpingExecutor executor = TcpingExecutor.builder()
                    .probes(numberOfProbes)
                    .interval(interval)
                    .host(url)
                    .execute();

            ArrayList<Double> times = executor.getRTT();

            Date endDate = new Date();
            Double minTime = getMinTime(times);
            Double maxTime = getMaxTime(times);
            Double avgTime = getAvgTime(times);
            Double stdDivTime = getStandardDiv(times, avgTime);

            return Stream.of(RTTEntity.builder()
                    .startDate(startDate)
                    .endDate(endDate)
                    .host(mainHost)
                    .url(url)
                    .probes(numberOfProbes)
                    .interval(interval)
                    .minTime(minTime)
                    .maxTime(maxTime)
                    .averageTime(avgTime)
                    .standardDeviationTime(stdDivTime)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            return Stream.empty();
        }
    }

    public List<PacketLossEntity> execTcpingForPacketLoss(Integer numberOfProbes, Integer interval, @NonNull CDNConfig.Site site) {

        String mainHost = site.getGeneralHost();
        log.info("Executing ping for Packet Loss for host : " + mainHost);

        return site.getHosts()
                .stream()
                .flatMap(url -> getPacketLossEntities(numberOfProbes, interval, mainHost, url))
                .collect(Collectors.toList());
    }

    private Stream<PacketLossEntity> getPacketLossEntities(Integer numberOfProbes, Integer interval, String mainHost, String url){
        try {
            log.info("Executing ping for Packet Loss calling url : " + url);
            Date startDate = new Date();

            TcpingExecutor executor = TcpingExecutor.builder()
                    .probes(numberOfProbes)
                    .interval(interval)
                    .host(url)
                    .execute();

            double packetLoss = executor.getPacketLoss();
            Date endDate = new Date();

            return Stream.of(PacketLossEntity.builder()
                    .startDate(startDate)
                    .endDate(endDate)
                    .host(mainHost)
                    .url(url)
                    .probes(numberOfProbes)
                    .interval(interval)
                    .packetLoss(packetLoss)
                    .build());
        }catch (Exception e){
            return Stream.empty();
        }
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