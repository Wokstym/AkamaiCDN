package agh.cs.backendAkamaiCDN.throughput.utils;

import agh.cs.backendAkamaiCDN.ping.utils.TcpingExecutor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class TcpdumpExecutor {

    @Getter
    private ArrayList<Long> results = new ArrayList<>();

    public static TcpdumpExecutorBuilder builder(@NonNull String host) {
        return new TcpdumpExecutorBuilder(host);
    }

    public TcpdumpExecutor(int duration, int interval, InputStream stream) throws IOException {
        //pomijamy pierwszą linię
        readLine(stream, true);
        System.out.println("###################");
        long start = System.currentTimeMillis();
        long intervalStart = start;
        long last = start;
        long sum = 0;
        while(last - start < duration) {
            long stamp = System.currentTimeMillis();
            long packetSize = readLine(stream, false);
            sum += packetSize;
            if(stamp - last > interval){
                results.add(sum / ((stamp - intervalStart) / 1000));
                sum = 0;
                intervalStart = stamp;
            }
            last = stamp;
        }
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
                    System.out.println("cos nie jest intem");
                }
            }
        }
        return 0;
    }

    @RequiredArgsConstructor
    public static class TcpdumpExecutorBuilder{
        private final String host;
        private int duration = 1 * 60000;
        private int step = 1000;

        public TcpdumpExecutorBuilder duration(int duration){
            this.duration = duration;
            return this;
        }

        public TcpdumpExecutorBuilder step(int step){
            this.step = step;
            return this;
        }

        public TcpdumpExecutor execute() throws IOException {
            String cmd = "tcpdump -n host " + this.host;
            Process process = Runtime.getRuntime().exec(cmd);
            InputStream stream = process.getInputStream();
            return new TcpdumpExecutor(this.duration, this.step, stream);

        }
    }
}
