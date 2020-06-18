package mail.client.imap;

import java.util.List;

public class ReceivedEmail {
    private final String body;
    private final List<ReceivedAttachment> attachments;

    public ReceivedEmail(String body, List<ReceivedAttachment> attachments) {
        this.body = body;
        this.attachments = attachments;
    }

    public String getBody() {
        return body;
    }

    public List<ReceivedAttachment> getAttachments() {
        return attachments;
    }
}
