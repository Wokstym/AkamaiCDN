package agh.cs.backendAkamaiCDN.throughput.application;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Slf4j
public class TcpdumpExecutor {

    @Getter
    private final ArrayList<Long> results = new ArrayList<>();

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public static TcpdumpExecutorBuilder builder(@NonNull String host) {
        return new TcpdumpExecutorBuilder(host);
    }

    public TcpdumpExecutor(int duration, int interval, InputStream stream) throws InterruptedException, ExecutionException {
        long start = System.currentTimeMillis();
        long intervalStart = start;
        long lastLoop = start;
        long lastLine = start;
        long sum = 0;
        long stamp = start;
        Future<Long> value = null;
        while (lastLoop - start < duration) {
            stamp = System.currentTimeMillis();
            if (value == null) {
                value = readLineAsync(stream, false);
            }
            if (value.isDone()) {
                long result = value.get();
                sum += result;
                log.info(String.valueOf(result));
                if (stamp - lastLine > interval) {
                    log.info("Adding new interval with value: " + sum);
                    if (sum > 0) {
                        results.add(1000 * sum / (stamp - intervalStart));
                    }
                    sum = 0;
                    intervalStart = stamp;
                }
                lastLine = System.currentTimeMillis();
                value = null;
            }
            lastLoop = stamp;
        }
        if (sum > 0) {
            results.add(1000 * sum / (stamp - intervalStart + 1));
        }
    }

    private Future<Long> readLineAsync(InputStream stream, boolean skip) {
        return executorService.submit(() -> readLine(stream, skip));
    }

    private long readLine(InputStream stream, boolean skip) throws IOException {
        var reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
        if (stream != null) {
            String[] arr = reader.readLine().split(", ");
            if (!skip) {
                String[] length = arr[arr.length - 1].split(" ");
                try {
                    return Integer.parseInt(length[length.length - 1]);
                } catch (Exception e) {
                    log.info("Unknown result from tcpdump");
                }
            }
        }
        return 0;
    }

    @RequiredArgsConstructor
    public static class TcpdumpExecutorBuilder {
        private final String host;
        private int duration = 5 * 60000;
        private int step = 1000;

        public TcpdumpExecutorBuilder duration(int duration) {
            this.duration = duration;
            return this;
        }

        public TcpdumpExecutorBuilder step(int step) {
            this.step = step;
            return this;
        }

        public TcpdumpExecutor execute() throws IOException, ExecutionException, InterruptedException {

            List<String> addresses = this.getInetAddresses(this.host)
                    .stream()
                    .map(InetAddress::getHostAddress)
                    .collect(Collectors.toList());

            String hosts = String.join(" or host ", addresses);

            log.info("IPs from current host" + " :" + hosts.replaceAll(" or host", ","));
            String cmd = "tcpdump -n host " + hosts;
            Process process = Runtime.getRuntime().exec(cmd);
            InputStream stream = process.getInputStream();
            return new TcpdumpExecutor(this.duration, this.step, stream);

        }

        private List<InetAddress> getInetAddresses(String host) {
            try {
                return Arrays.asList(InetAddress.getAllByName(host));
            } catch (UnknownHostException e) {
                return Collections.emptyList();
            }
        }
    }
}
