package agh.cs.RemoteServerAkamaiCDN.common.web;

import agh.cs.RemoteServerAkamaiCDN.packetLoss.domain.rest.SavePacketLossRequest;
import agh.cs.RemoteServerAkamaiCDN.rtt.domain.rest.SaveRTTRequest;
import agh.cs.RemoteServerAkamaiCDN.throughput.domain.rest.SaveThroughputRequest;
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
