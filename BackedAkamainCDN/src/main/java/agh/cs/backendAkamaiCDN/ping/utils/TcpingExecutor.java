package agh.cs.backendAkamaiCDN.ping.utils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TcpingExecutor {
    private final int probes;
    private final BufferedReader inputStream;

    @Getter
    private final ArrayList<Double> times;
    private int successfulProbes;

    public static TcpingExecutorBuilder builder() {
        return new TcpingExecutorBuilder();
    }

    public TcpingExecutor parseResult() throws IOException {
        parseIndividualResults();
        parseNumberOfSuccessfulProbes();
        return this;
    }

    public double getPacketLoss() {
        int failedProbes = probes - successfulProbes;
        return (double) failedProbes / (successfulProbes + failedProbes);
    }

    private void parseIndividualResults() throws IOException {
        Pattern pattern = Pattern.compile("time=" + "(.*?)" + "ms", Pattern.DOTALL);
        String input;
        while ((input = inputStream.readLine()) != null && times.size() < probes) {
            Matcher matcher = pattern.matcher(input);
            if (matcher.find()) {
                times.add(Double.parseDouble(matcher.group(1)));
            }
        }
    }

    private void parseNumberOfSuccessfulProbes() throws IOException {
        String input;
        Pattern successfulPattern = Pattern.compile("\\s*" + "(.*?)" + " successful", Pattern.DOTALL);

        while ((input = inputStream.readLine()) != null) {
            Matcher matcher = successfulPattern.matcher(input);
            if (matcher.find()) {
                successfulProbes = Integer.parseInt(matcher.group(1));
            }
        }
    }

    @NoArgsConstructor
    public static class TcpingExecutorBuilder {
        private String siteName;
        private int probes;
        private double interval;

        public TcpingExecutorBuilder siteName(String siteName) {
            this.siteName = siteName;
            return this;
        }

        public TcpingExecutorBuilder interval(double interval) {
            this.interval = interval;
            return this;
        }

        public TcpingExecutorBuilder probes(int probes) {
            this.probes = probes;
            return this;
        }

        public TcpingExecutor execute() throws IOException {
            String command = String.format("tcping -n %s -i %s %s", probes, interval, siteName); // todo change used program to be compatible with linux

            Process p = Runtime.getRuntime().exec(command);
            BufferedReader inputStream = new BufferedReader(new InputStreamReader(p.getInputStream()));

            return new TcpingExecutor(probes, inputStream, new ArrayList<>(), 0);
        }

    }
}



