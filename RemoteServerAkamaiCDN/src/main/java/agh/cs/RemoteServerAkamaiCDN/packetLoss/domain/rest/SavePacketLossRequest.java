package agh.cs.RemoteServerAkamaiCDN.packetLoss.domain.rest;

import agh.cs.RemoteServerAkamaiCDN.packetLoss.domain.PacketLossEntity;
import lombok.*;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SavePacketLossRequest {
    private List<PacketLossDto> entities;

    @Setter
    @Getter
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PacketLossDto {
        private Date startDate;
        private Date endDate;
        private String host;
        private String url;
        private Double packetLoss;

        private String ipAddress;

        private int probes;
        private double interval;
    }
}
