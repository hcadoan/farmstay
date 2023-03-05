package model;

public class Response {

    private String msg;
    private String msg_vi;


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg_vi() {
        return msg_vi;
    }

    public void setMsg_vi(String msg_vi) {
        this.msg_vi = msg_vi;
    }

    @Override
    public String toString() {
        return "LoginResult{" +
                ", msg='" + msg + '\'' +
                ", msg_vi='" + msg_vi + '\'' +
                '}';
    }
}
