package mail.client;

public class ImapResponse {
    private final String command;
    private final boolean success;
    private final String text;

    public ImapResponse(String command, boolean success, String text) {
        this.command = command;
        this.success = success;
        this.text = text;
    }

    public String getCommand() {
        return command;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "ImapResponse{" +
                "command='" + command + '\'' +
                ", success=" + success +
                ", text='" + text + '\'' +
                '}';
    }
}
