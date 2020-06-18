package mail.client.imap;

import java.util.Map;

public class ReceivedEmailPart {
    private final Map<String,String> headers;
    private final byte[] content;

    public ReceivedEmailPart(Map<String, String> headers, byte[] content) {
        this.headers = headers;
        this.content = content;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public byte[] getContent() {
        return content;
    }
}
