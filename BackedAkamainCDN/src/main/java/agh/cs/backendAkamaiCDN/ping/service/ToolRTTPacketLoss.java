import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ToolRTTPacketLoss {

    public static Double getStandardDiv(ArrayList<Double> values, Double avg){
        double variance = values.stream().map(x -> x - avg).map(x -> x * x).mapToDouble(Double::doubleValue).average().getAsDouble();
        return Math.sqrt(variance);
    }

    public static void runSystemCommand(String command) {

        try {
            Process p = Runtime.getRuntime().exec(command);
            BufferedReader inputStream = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));

            String s = "";
            // reading output stream of the command
            int noResponse = 0;
            int gotReplay = 0;

            ArrayList<Double> times = new ArrayList<>();

            double packetLoss = 0;
            int succesfull = 0;
            int failed = 0;

            while ((s = inputStream.readLine()) != null) {
                if(s.contains("No response")){
                    noResponse += 1;
                }
                if(s.contains("Port is open")){
                    gotReplay += 1;
                }
                String[] time = s.split("time=");
                String[] succ = s.split(" successful");
//                String[] fail = s.split(" failed");
                //time parsing
                try{
//                    System.out.println(Double.parseDouble(time[1].split("ms")[0]));
                    times.add(Double.parseDouble(time[1].split("ms")[0]));
                }catch (Exception e){
//                    System.out.println("didn't find 'time=' in this part");
                }
                //success paring
                try{
                    succesfull = Integer.parseInt(succ[0].replace(" ", ""));
//                    System.out.println("Succesfull: " + succesfull);

                }catch (Exception e){
//                    System.out.println("succ error" + e);
                }
                //fail parsing
                try{
                    failed = Integer.parseInt(s.split(" failed")[0].split("successful, ")[1]);
//                    System.out.println("Failed: " + failed);
                }catch (Exception e){
//                    System.out.println("FAIL ERROR: " + e);
                }

                System.out.println(s);
            }

            packetLoss = (double) failed/(succesfull+failed);

            double minTime = times.stream().min(Double::compare).get();
            double maxTime = times.stream().max(Double::compare).get();
            double avgTime = times.stream().mapToDouble(Double::doubleValue).average().getAsDouble();
            double stdDivTime = getStandardDiv(times, avgTime);

            System.out.println("min: " + minTime);
            System.out.println("max: " + maxTime);
            System.out.println("avg: " + avgTime);
            System.out.println("std div: " + stdDivTime);
            System.out.println("packet loss: " + packetLoss);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String ip = "youtube.com";
        runSystemCommand("tcping " + ip);
    }
}