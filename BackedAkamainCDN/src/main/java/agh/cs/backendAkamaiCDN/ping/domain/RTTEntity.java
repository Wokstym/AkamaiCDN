package agh.cs.backendAkamaiCDN.ping.domain;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RTTEntity {
    private UUID id;

    private Date startDate;
    private Date endDate;
    private String host;
    private String url;
    private Double minTime;
    private Double maxTime;
    private Double averageTime;
    private Double standardDeviationTime;

    private int probes;
    private double interval;
}
