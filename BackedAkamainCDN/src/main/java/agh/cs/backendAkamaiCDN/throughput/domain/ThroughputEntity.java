package agh.cs.backendAkamaiCDN.throughput.domain;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ThroughputEntity {
    private UUID id;

    private Date startDate;
    private Date endDate;
    private long maxValue;
    private long minValue;
    private long averageValue;
    private String host;
}
