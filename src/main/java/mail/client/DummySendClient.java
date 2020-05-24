package mail.client;

public class DummySendClient implements SendClient {
    @Override
    public void send(Mail mail) throws Exception {
        System.out.println("Sending email " + mail);
    }
}
