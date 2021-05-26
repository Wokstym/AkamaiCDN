package agh.cs.backendAkamaiCDN.throughput.domain;

import lombok.*;

import java.util.Date;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ThroughputEntity {
    @EqualsAndHashCode.Include
    private Long id;

    private Date startDate;
    private Date endDate;
    private long maxValue;
    private long minValue;
    private long averageValue;
    private String host;

    private String ipAddress;
}
