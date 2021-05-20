package agh.cs.backendAkamaiCDN.ping.application;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TcpingExecutor {
    @Getter
    private final ArrayList<Double> times;
    private final int numberOfProbes;


    public static TcpingExecutorBuilder builder() {
        return new TcpingExecutorBuilder();
    }

    private TcpingExecutor(BufferedReader inputStream, int numberOfProbes) throws IOException {
        this.numberOfProbes = numberOfProbes;
        this.times = new ArrayList<>();
        parseResult(inputStream);
    }

    public double getPacketLoss(){
        return (double) ((numberOfProbes - times.size()) / numberOfProbes) * 100;
    }

    private void parseResult(BufferedReader inputStream) throws IOException {
        Pattern patternRtt = Pattern.compile("rtt=" + "(.*?)" + " ms", Pattern.DOTALL);
        String input;
        while ((input = inputStream.readLine()) != null) {
            Matcher matcherRtt = patternRtt.matcher(input);
            if (matcherRtt.find()) {
                times.add(Double.parseDouble(matcherRtt.group(1)));
            }
        }
    }

    @RequiredArgsConstructor
    public static class TcpingExecutorBuilder {
        private String siteName;
        private int probes = 100;
        private double interval = 1000;

        public TcpingExecutorBuilder interval(double interval) {
            this.interval = interval;
            return this;
        }

        public TcpingExecutorBuilder probes(int probes) {
            this.probes = probes;
            return this;
        }

        public TcpingExecutorBuilder host(String siteName) {
            this.siteName = siteName;
            return this;
        }

        public TcpingExecutor execute() throws IOException, InterruptedException {
            String command = String.format("sudo hping3 -c %s -V -S -p 80 -i u%s %s", probes, interval, siteName);

            Process p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader inputStream = new BufferedReader(new InputStreamReader(p.getInputStream()));

            return new TcpingExecutor(inputStream, probes);
        }

    }
}