package agh.cs.RemoteServerAkamaiCDN.packetLoss.domain;
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
public class PacketLossEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private Date startDate;
    private Date endDate;
    private String host;
    private String url;
    private Double packetLoss;

    private String ipAddress;

    private int probes;
    private double interval;
}

