package agh.cs.backendAkamaiCDN.remoteServer.entity;

import lombok.*;

import java.util.Date;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SaveThroughputRequest {
    private Date startDate;
    private Date endDate;
    private long maxValue;
    private long minValue;
    private long averageValue;
    private String host;
}
