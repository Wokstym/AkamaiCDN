package agh.cs.backendAkamaiCDN.ping.domain;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PacketLossEntity {
    private Long id;

    private Date startDate;
    private Date endDate;
    private String host;
    private String url;
    private Double packetLoss;

    private String ipAddress;

    private int probes;
    private double interval;
}

