package id.ac.itn.moca.model;

import android.app.Application;

public class NetworkState {

    private final Status status;
    private final String msg;

    public static final NetworkState LOADED;
    public static final NetworkState LOADING;
    public static final NetworkState FAIL;
    public static final NetworkState ENDOFLIST;

    public NetworkState(Status status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    static {
        LOADED = new NetworkState(Status.SUCCESS, "succes_msg");
        LOADING = new NetworkState(Status.RUNNING, "running_msg");
        FAIL = new NetworkState(Status.FAILED, "failed_msg");
        ENDOFLIST = new NetworkState(Status.FAILED, "");
    }

    public Status getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public enum Status {
        RUNNING,
        SUCCESS,
        FAILED
    }
}
