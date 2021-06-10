package agh.cs.backendAkamaiCDN.throughput.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;
import java.util.UUID;

@Setter
@Getter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ThroughputEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private UUID id;

    private Date startDate;
    private Date endDate;
    private long maxValue;
    private long minValue;
    private long averageValue;
    private String url;
    private String host;
    private boolean isSentToServer;
}
