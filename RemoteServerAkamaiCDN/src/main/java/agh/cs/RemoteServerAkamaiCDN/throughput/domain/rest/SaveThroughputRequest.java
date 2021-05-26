package agh.cs.RemoteServerAkamaiCDN.throughput.domain.rest;

import lombok.*;

import java.util.Date;
import java.util.List;

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

    private String ipAddress;
}
