package agh.cs.backendAkamaiCDN.remoteServer.entity;

import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BatchedRequest {
    private List<SavePacketLossRequest.PacketLossDto> packetLossDtos;
    private List<SaveThroughputRequest> throughputRequests;
    private List<SaveRTTRequest.RTTDto> rttDtos;
}