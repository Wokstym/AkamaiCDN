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
public class PacketLossEntity {
    private UUID id;

    private Date startDate;
    private Date endDate;
    private String host;
    private String url;
    private Double packetLoss;

    private int probes;
    private double interval;
}

