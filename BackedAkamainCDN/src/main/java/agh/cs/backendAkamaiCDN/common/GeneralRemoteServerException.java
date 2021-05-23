package agh.cs.backendAkamaiCDN.common;

public class GeneralRemoteServerException extends RuntimeException {
    public GeneralRemoteServerException(Exception e) {
        super("Bad response from remote server " + e.getCause());
    }
}
