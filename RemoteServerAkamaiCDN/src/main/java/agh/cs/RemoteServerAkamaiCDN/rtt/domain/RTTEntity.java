package agh.cs.RemoteServerAkamaiCDN.rtt.domain;

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
public class RTTEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

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
