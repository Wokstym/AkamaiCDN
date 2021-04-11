package agh.cs.backendAkamaiCDN.throughput.utils;

import agh.cs.backendAkamaiCDN.ping.utils.TcpingExecutor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
public class TcpdumpExecutor {

    @Getter
    private ArrayList<Long> results = new ArrayList<>();

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public static TcpdumpExecutorBuilder builder(@NonNull List<String> hosts) {
        return new TcpdumpExecutorBuilder(hosts);
    }

    public TcpdumpExecutor(int duration, int interval, InputStream stream) throws IOException, InterruptedException, ExecutionException {
        long start = System.currentTimeMillis();
        long intervalStart = start;
        long lastLoop = start;
        long lastLine = start;
        long sum = 0;
        long stamp = start;
        Future<Long> value = null;
        while(lastLoop - start < duration) {
            stamp = System.currentTimeMillis();
            if(value == null){
                value = readLineAsync(stream, false);
            }
            if(value.isDone()){
                long result = value.get();
                sum += result;
                log.info(String.valueOf(result));
                if(stamp - lastLine > interval){
                    log.info("Adding new interval with value: " + sum);
                    if(sum > 0){
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
        if(sum > 0){
            results.add(1000 * sum / (stamp - intervalStart + 1));
        }
    }

    private Future<Long> readLineAsync(InputStream stream, boolean skip){
        return executorService.submit(() -> readLine(stream, skip));
    }

    private long readLine(InputStream stream, boolean skip) throws IOException {
        var reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        if(stream != null){
            String[] arr = reader.readLine().split(", ");
            if(!skip) {
                String[] length = arr[arr.length - 1].split(" ");
                try {
                    long lengthValue = Integer.parseInt(length[length.length - 1]);
                    return lengthValue;
                } catch (Exception e){
                    log.info("Unknown result from tcpdump");
                }
            }
        }
        return 0;
    }

    @RequiredArgsConstructor
    public static class TcpdumpExecutorBuilder{
        private final List<String> hosts;
        private int duration = 5 * 60000;
        private int step = 1000;

        public TcpdumpExecutorBuilder duration(int duration){
            this.duration = duration;
            return this;
        }

        public TcpdumpExecutorBuilder step(int step){
            this.step = step;
            return this;
        }

        public TcpdumpExecutor execute() throws IOException, ExecutionException, InterruptedException {
            List<InetAddress> addresses = new ArrayList<>();

            for(String host : this.hosts){
                addresses.addAll(Arrays.asList(InetAddress.getAllByName(host)));
            }

            String hosts = "";
            for(int i = 0; i < addresses.size(); i++){
                if(i == 0){
                    hosts += addresses.get(i).getHostAddress();
                }
                else{
                    hosts += " or host " + addresses.get(i).getHostAddress();
                }
            }
            log.info("IPs from current host"   + " :" + hosts.replaceAll("or host", ""));
            String cmd = "tcpdump -n host " + hosts;
            Process process = Runtime.getRuntime().exec(cmd);
            InputStream stream = process.getInputStream();
            return new TcpdumpExecutor(this.duration, this.step, stream);

        }
    }
}
