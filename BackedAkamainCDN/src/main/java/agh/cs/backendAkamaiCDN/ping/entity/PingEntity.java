package agh.cs.backendAkamaiCDN.ping.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PingEntity {

    @Id
    private Date id;

    private Double minTime;
    private Double maxTime;
    private Double averageTime;
    private Double standardDeviationTime;

    private Double packetLoss;
}
