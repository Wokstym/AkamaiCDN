package agh.cs.backendAkamaiCDN.remoteServer;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RemoteServerEndpoints {

    private static final String PACKET_LOSS_ENDPOINT = "/packet_loss";
    private static final String RTT_ENDPOINT = "/rtt";
    private static final String THROUGHPUT_ENDPOINT = "/throughput";

    private static final String SAVE_ENDPOINT = "/save";
    private static final String ALL_ENDPOINT = "/all";


    private final String remoteServerUrl;

    public URI getPacketLossEndpoint() {
        return UriComponentsBuilder.fromUriString(remoteServerUrl)
                .path(PACKET_LOSS_ENDPOINT)
                .build()
                .toUri();
    }

    public URI getPacketLossSaveEndpoint() {
        return UriComponentsBuilder.fromUriString(remoteServerUrl)
                .path(PACKET_LOSS_ENDPOINT)
                .path(SAVE_ENDPOINT)
                .build()
                .toUri();
    }

    public URI getPacketLossAllEndpoint() {
        return UriComponentsBuilder.fromUriString(remoteServerUrl)
                .path(PACKET_LOSS_ENDPOINT)
                .path(ALL_ENDPOINT)
                .build()
                .toUri();
    }

    public URI getRTTEndpoint() {
        return UriComponentsBuilder.fromUriString(remoteServerUrl)
                .path(RTT_ENDPOINT)
                .build()
                .toUri();
    }

    public URI getRTTSaveEndpoint() {
        return UriComponentsBuilder.fromUriString(remoteServerUrl)
                .path(RTT_ENDPOINT)
                .path(SAVE_ENDPOINT)
                .build()
                .toUri();
    }

    public URI getRTTAllEndpoint() {
        return UriComponentsBuilder.fromUriString(remoteServerUrl)
                .path(RTT_ENDPOINT)
                .path(ALL_ENDPOINT)
                .build()
                .toUri();
    }

    public URI getThroughputEndpoint() {
        return UriComponentsBuilder.fromUriString(remoteServerUrl)
                .path(THROUGHPUT_ENDPOINT)
                .build()
                .toUri();
    }

    public URI getThroughputSaveEndpoint() {
        return UriComponentsBuilder.fromUriString(remoteServerUrl)
                .path(THROUGHPUT_ENDPOINT)
                .path(SAVE_ENDPOINT)
                .build()
                .toUri();
    }

    public URI getThroughputAllEndpoint() {
        return UriComponentsBuilder.fromUriString(remoteServerUrl)
                .path(THROUGHPUT_ENDPOINT)
                .path(ALL_ENDPOINT)
                .build()
                .toUri();
    }

}
