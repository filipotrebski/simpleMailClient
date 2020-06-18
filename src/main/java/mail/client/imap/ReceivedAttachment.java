package mail.client.imap;

public class ReceivedAttachment {
    private final String mimeType;
    private final String name;
    private final byte[] content;

    public ReceivedAttachment(String mimeType, String name, byte[] content) {
        this.mimeType = mimeType;
        this.name = name;
        this.content = content;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getName() {
        return name;
    }

    public byte[] getContent() {
        return content;
    }
}
