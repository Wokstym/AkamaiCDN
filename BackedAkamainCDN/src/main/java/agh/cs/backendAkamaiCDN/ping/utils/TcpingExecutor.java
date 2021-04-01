package agh.cs.backendAkamaiCDN.ping.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TcpingExecutor {
    @Getter
    private final ArrayList<Double> times;
    private final int probes;
    private int successfulProbes;

    public static TcpingExecutorBuilder builder(@NonNull String siteName) {
        return new TcpingExecutorBuilder(siteName);
    }

    private TcpingExecutor(int probes, BufferedReader inputStream) throws IOException {
        this.probes = probes;
        this.times = new ArrayList<>();
        this.successfulProbes = 0;
        parseResult(inputStream);
    }

    public double getPacketLoss() {
        int failedProbes = probes - successfulProbes;
        return (double) failedProbes / (successfulProbes + failedProbes);
    }

    private void parseResult(BufferedReader inputStream) throws IOException {
        parseIndividualResults(inputStream);
        parseNumberOfSuccessfulProbes(inputStream);
    }

    private void parseIndividualResults(BufferedReader inputStream) throws IOException {
        Pattern pattern = Pattern.compile("time=" + "(.*?)" + "ms", Pattern.DOTALL);
        String input;
        while ((input = inputStream.readLine()) != null && times.size() < probes) {
            Matcher matcher = pattern.matcher(input);
            if (matcher.find()) {
                times.add(Double.parseDouble(matcher.group(1)));
            }
        }
    }

    private void parseNumberOfSuccessfulProbes(BufferedReader inputStream) throws IOException {
        String input;
        Pattern successfulPattern = Pattern.compile("\\s*" + "(.*?)" + " successful", Pattern.DOTALL);

        while ((input = inputStream.readLine()) != null) {
            Matcher matcher = successfulPattern.matcher(input);
            if (matcher.find()) {
                successfulProbes = Integer.parseInt(matcher.group(1));
            }
        }
    }

    @RequiredArgsConstructor
    public static class TcpingExecutorBuilder {
        private final String siteName;
        private int probes = 1;
        private double interval = 3;

        public TcpingExecutorBuilder interval(double interval) {
            this.interval = interval;
            return this;
        }

        public TcpingExecutorBuilder probes(int probes) {
            this.probes = probes;
            return this;
        }

        public TcpingExecutor execute() throws IOException {
            String command = String.format("sudo hping3 -c %s -V -S -p 80 %s", probes, siteName);

            Process p = Runtime.getRuntime().exec(command);
            BufferedReader inputStream = new BufferedReader(new InputStreamReader(p.getInputStream()));

            return new TcpingExecutor(probes, inputStream);
        }

    }
}