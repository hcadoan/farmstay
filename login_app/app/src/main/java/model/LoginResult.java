package model;

public class LoginResult {

    private String authenticate_jwt;
    private String msg;
    private String msg_vi;

    public String getAuthenticate_jwt() {
        return authenticate_jwt;
    }

    public void setAuthenticate_jwt(String authenticate_jwt) {
        this.authenticate_jwt = authenticate_jwt;
    }

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
                "authenticate_jwt='" + authenticate_jwt + '\'' +
                ", msg='" + msg + '\'' +
                ", msg_vi='" + msg_vi + '\'' +
                '}';
    }
}
