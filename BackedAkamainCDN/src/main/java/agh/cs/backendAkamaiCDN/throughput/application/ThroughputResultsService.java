package agh.cs.backendAkamaiCDN.throughput.application;

import agh.cs.backendAkamaiCDN.throughput.domain.ThroughputEntity;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@NoArgsConstructor
public class ThroughputResultsService {

    private static final int DURATION = 5 * 60000;
    private static final int STEP = 1000;

    public Optional<ThroughputEntity> measureThroughput(String url, String name) {
        Date startStamp = new Date();
        try {
            TcpdumpExecutor executor = TcpdumpExecutor.builder(url)
                    .duration(DURATION)
                    .step(STEP)
                    .execute();

            Date endStamp = new Date();
            List<Long> results = executor.getResults();
            if (getMaxValue(results) > 0) {
                return Optional.of(ThroughputEntity.builder()
                        .host(name)
                        .url(url)
                        .startDate(startStamp)
                        .endDate(endStamp)
                        .maxValue(getMaxValue(results))
                        .minValue(getMinValue(results))
                        .averageValue(getAvgValue(results))
                        .build());
            }
        } catch (IOException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private long getMinValue(List<Long> list) {
        return list.stream()
                .filter(x -> x > 0)
                .min(Long::compare)
                .orElse(0L);
    }

    private long getMaxValue(List<Long> list) {
        return list.stream()
                .filter(x -> x > 0)
                .max(Long::compare)
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
