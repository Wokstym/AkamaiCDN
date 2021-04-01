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
    @Getter
    private double packetLoss;

    public static TcpingExecutorBuilder builder(@NonNull String siteName) {
        return new TcpingExecutorBuilder(siteName);
    }

    private TcpingExecutor(int probes, BufferedReader inputStream) throws IOException {
        this.probes = probes;
        this.times = new ArrayList<>();
        this.packetLoss = 0;
        parseResult(inputStream);
    }

    private void parseResult(BufferedReader inputStream) throws IOException {
        Pattern patternRtt = Pattern.compile("rtt=" + "(.*?)" + " ms", Pattern.DOTALL);
        Pattern patternPacketLoss = Pattern.compile("received, " + "(.*?)" + "% packet loss", Pattern.DOTALL);

        String input;
        while ((input = inputStream.readLine()) != null) {
            Matcher matcherRtt = patternRtt.matcher(input);
            if (matcherRtt.find()) {
                times.add(Double.parseDouble(matcherRtt.group(1)));
            }

            Matcher matcherPacketLoss = patternPacketLoss.matcher(input);
            if (matcherPacketLoss.find()) {
                this.packetLoss = Double.parseDouble(matcherPacketLoss.group(1));
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
            String command = String.format("sudo hping3 -c %s -V -S -p 80 -i u%s %s", probes, interval, siteName);

            Process p = Runtime.getRuntime().exec(command);
            BufferedReader inputStream = new BufferedReader(new InputStreamReader(p.getInputStream()));

            return new TcpingExecutor(probes, inputStream);
        }

    }
}