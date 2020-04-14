package mail.client;

public class SmtpSendClient  implements SendClient{
    @Override
    public void send(Mail mail) throws Exception {
        throw   new RuntimeException(("Not implemented"));
    }
}
