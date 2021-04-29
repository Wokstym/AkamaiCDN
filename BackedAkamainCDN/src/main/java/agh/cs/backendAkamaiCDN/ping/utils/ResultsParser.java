package agh.cs.backendAkamaiCDN.ping.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResultsParser {
    public static ArrayList<Double> parseRTT(BufferedReader inputStream) throws IOException {
        Pattern patternRtt = Pattern.compile("rtt=" + "(.*?)" + " ms", Pattern.DOTALL);
        ArrayList<Double> times = new ArrayList<>();
        String input;
        while ((input = inputStream.readLine()) != null) {
            Matcher matcherRtt = patternRtt.matcher(input);
            if (matcherRtt.find()) {
                times.add(Double.parseDouble(matcherRtt.group(1)));
            }
        }
        return times;
    }

    public static double parsePacketLoss(BufferedReader inputStream) throws IOException {
        Pattern patternPacketLoss = Pattern.compile("received, " + "(.*?)" + "% packet loss", Pattern.DOTALL);

        double packetLoss = 0;
        String input;
        while ((input = inputStream.readLine()) != null) {
            Matcher matcherPacketLoss = patternPacketLoss.matcher(input);
            if (matcherPacketLoss.find()) {
                packetLoss = Double.parseDouble(matcherPacketLoss.group(1));
            }
        }
        return packetLoss;
    }
}
