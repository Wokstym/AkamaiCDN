package agh.cs.backendAkamaiCDN.ping.service;

import agh.cs.backendAkamaiCDN.ping.entity.PingEntity;
import lombok.NoArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

@NoArgsConstructor
public class TcpingResultsService {
    private final int numberOfProbes = 100;

    public PingEntity executeTcping(String siteName) {
        try {
            Process p = Runtime.getRuntime().exec(
                    "tcping -n " + numberOfProbes
                    + " -i 0,01 " + //Interval
                    siteName);
            
            BufferedReader inputStream = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));

            ArrayList<Double> times = getTimesOfProbesArray(inputStream);

            int successfulProbes = getNumberOfSuccessfulProbes(inputStream);
            int failedProbes = numberOfProbes - successfulProbes;
            double packetLoss = (double) failedProbes / (successfulProbes + failedProbes);

            Double minTime = times.stream().min(Double::compare).orElseGet(() -> (double) -1);
            Double maxTime = times.stream().max(Double::compare).orElseGet(() -> (double) -1);
            Double avgTime = times.stream().mapToDouble(Double::doubleValue).average().orElseGet(() -> (double) -1);
            Double stdDivTime = getStandardDiv(times, avgTime);
            Date date = new Date(System.currentTimeMillis());

            return PingEntity.builder().
                    id(date).
                    minTime(minTime).
                    maxTime(maxTime).
                    averageTime(avgTime).
                    standardDeviationTime(stdDivTime).
                    packetLoss(packetLoss).
                    build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new PingEntity();
    }

    public Double getStandardDiv(ArrayList<Double> values, Double avg){
        double variance = values.stream().
                map(x -> x - avg).
                map(x -> x * x).
                mapToDouble(Double::doubleValue).
                average().orElseGet(() -> (double) 0);
        return Math.sqrt(variance);
    }

    private ArrayList<Double> getTimesOfProbesArray(BufferedReader inputStream) throws IOException {
        String input;
        ArrayList<Double> times = new ArrayList<>();
        int countProbes = 0;
        while ((input = inputStream.readLine()) != null && countProbes < numberOfProbes) {
            String[] time = input.split("time=");
            try{
                times.add(Double.parseDouble(time[1].split("ms")[0]));
            }catch (Exception e){
                System.out.println("times");
            }
            countProbes++;
        }
        return times;
    }

    private int getNumberOfSuccessfulProbes(BufferedReader inputStream) throws IOException {
        String input;
        int successful = 0;
        while ((input = inputStream.readLine()) != null) {
            String[] successfulResult = input.split(" successful");
            try{
                successful = Integer.parseInt(successfulResult[0].replace(" ", ""));
            }catch (Exception ex){
            }
        }
        return successful;
    }
}