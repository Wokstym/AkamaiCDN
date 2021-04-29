package agh.cs.backendAkamaiCDN.ping.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Getter
public class TcpingExecutor {
    private BufferedReader bufferedReader;

    public static TcpingExecutorBuilder builder() {
        return new TcpingExecutorBuilder();
    }

    private TcpingExecutor(BufferedReader inputStream) throws IOException {
        this.bufferedReader = inputStream;
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