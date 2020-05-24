package mail.client;

public class SmtpResponse {

    public static int ACTION_OK = 250;

    private final int code;
    private final String text;

    public SmtpResponse(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public int getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

    public boolean isSuccess() {
        //https://en.wikipedia.org/wiki/List_of_SMTP_server_return_codes
        return code >= 200 && code < 400;
    }
}
