package entity;

public class ErrorJson {
    private String message;
    private int errCode;

    public ErrorJson(String msg, int code){
        this.message=msg;
        this.errCode=code;
    }
}
