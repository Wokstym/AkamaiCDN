package agh.cs.backendAkamaiCDN.remoteServer.entity;

import lombok.*;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SaveRTTRequest {
    private List<RTTDto> entities;

    @Setter
    @Getter
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RTTDto {
        private Date startDate;
        private Date endDate;
        private String host;
        private String url;
        private Double minTime;
        private Double maxTime;
        private Double averageTime;
        private Double standardDeviationTime;

        private String ipAddress;

        private int probes;
        private double interval;
    }
}
