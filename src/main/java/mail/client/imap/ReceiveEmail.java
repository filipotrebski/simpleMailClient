package mail.client.imap;

public class ReceiveEmail {
    private final String emailBody;

    public ReceiveEmail(String emailBody) {
        this.emailBody = emailBody;
    }

    public String getEmailBody() {
        return emailBody;
    }
}
