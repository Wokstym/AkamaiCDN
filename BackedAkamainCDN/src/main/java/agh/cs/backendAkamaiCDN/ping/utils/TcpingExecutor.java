package agh.cs.backendAkamaiCDN.ping.utils;

import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TcpingExecutor {

    private final BufferedReader bufferedReader;

    public static TcpingExecutorBuilder builder() {
        return new TcpingExecutorBuilder();
    }

    private TcpingExecutor(BufferedReader inputStream) {
        this.bufferedReader = inputStream;
    }

    public ArrayList<Double> getRTT() throws IOException {
        Pattern patternRtt = Pattern.compile("rtt=" + "(.*?)" + " ms", Pattern.DOTALL);
        ArrayList<Double> times = new ArrayList<>();
        String input;
        while ((input = bufferedReader.readLine()) != null) {
            Matcher matcherRtt = patternRtt.matcher(input);
            if (matcherRtt.find()) {
                times.add(Double.parseDouble(matcherRtt.group(1)));
            }
        }
        return times;
    }

    public double getPacketLoss() throws IOException {
        Pattern patternPacketLoss = Pattern.compile("received, " + "(.*?)" + "% packet loss", Pattern.DOTALL);

        double packetLoss = 0;
        String input;
        while ((input = bufferedReader.readLine()) != null) {
            Matcher matcherPacketLoss = patternPacketLoss.matcher(input);
            if (matcherPacketLoss.find()) {
                packetLoss = Double.parseDouble(matcherPacketLoss.group(1));
            }
        }
        return packetLoss;
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

        public TcpingExecutor execute() throws IOException {
            String command = String.format("sudo hping3 -c %s -V -S -p 80 -i u%s %s", probes, interval, siteName);

            Process p = Runtime.getRuntime().exec(command);
            BufferedReader inputStream = new BufferedReader(new InputStreamReader(p.getInputStream()));

            return new TcpingExecutor(inputStream);
        }

    }
}