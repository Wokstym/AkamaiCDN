package agh.cs.backendAkamaiCDN.ping.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Setter
@Getter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PingEntity {

    @Id
    private Date id;

    private String site;
    private Double minTime;
    private Double maxTime;
    private Double averageTime;
    private Double standardDeviationTime;

    private Double packetLoss;
}
