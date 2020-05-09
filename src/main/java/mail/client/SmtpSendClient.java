package mail.client;

import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SmtpSendClient implements SendClient {

    private String host;
    private int port;
    private String user;
    private String password;

    public SmtpSendClient(String host, int port, String user, String password) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    @Override
    public void send(Mail mail) throws Exception {
        System.out.println("Connecting to " + host + " on port " + port);
        Socket socket = new Socket(host, port);
        Smtp smtp = new Smtp(socket.getInputStream(), socket.getOutputStream(), true);
        smtp.ehlo(host);
        smtp.login(user, password);
        smtp.setSender(mail.getFrom());
        smtp.addRecipient(mail.getTo());
        var date = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z").format(new Date());
        String mailText = "From: " + mail.getFrom() + "\r\n" +
                "To: " + mail.getTo() + "\r\n" +
                "Date: " + date + "\r\n" +
                "Subject: " + mail.getSubject() + "\r\n" + "\r\n" +
                mail.getBody() + "\r\n" +
                ".\r\n";
        smtp.sendMessage(mailText);
        smtp.logout(host);
    }

}
