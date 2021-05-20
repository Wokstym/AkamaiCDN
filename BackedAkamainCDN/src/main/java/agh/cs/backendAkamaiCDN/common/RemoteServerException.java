package agh.cs.backendAkamaiCDN.common;

public class RemoteServerException extends RuntimeException {
    public RemoteServerException() {
        super("Bad response from remote server");
    }
}
