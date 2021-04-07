package agh.cs.backendAkamaiCDN.throughput.service;

import agh.cs.backendAkamaiCDN.throughput.entity.ThroughputEntity;
import agh.cs.backendAkamaiCDN.throughput.utils.TcpdumpExecutor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@NoArgsConstructor
public class ThroughputResultsService {
    public Optional<ThroughputEntity> measureThroughput(String host) {
        Date startStamp = new Date();
        try {
            TcpdumpExecutor executor = TcpdumpExecutor.builder(host).execute();
            Date endStamp = new Date();
            List<Long> results = executor.getResults();
            return Optional.of(ThroughputEntity.builder()
                    .startDate(startStamp)
                    .endDate(endStamp)
                    .max(getMaxValue(results))
                    .min(getMinValue(results))
                    .avg(getAvgValue(results))
                    .build());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private long getMinValue(List<Long> list) {
        return list.stream()
                .filter(x -> x > 0)
                .min(Double::compare)
                .orElse(0L);
    }

    private long getMaxValue(List<Long> list) {
        return list.stream()
                .filter(x -> x > 0)
                .max(Double::compare)
                .orElse(0L);
    }

    private long getAvgValue(List<Long> list) {
        return list.stream()
                .filter(x -> x > 0)
                .reduce(Long::sum)
                .map(sum -> sum / list.size())
                .orElse(0L);
    }

}
