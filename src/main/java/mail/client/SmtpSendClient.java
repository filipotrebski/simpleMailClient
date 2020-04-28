package mail.client;

import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SmtpSendClient implements SendClient {

    private String host = "127.0.0.1";


    @Override
    public void send(Mail mail) throws Exception {
        System.out.println("Connecting");
        Socket socket = new Socket(host, 1025);
        Smtp smtp = new Smtp(socket.getInputStream(), socket.getOutputStream(), true);
        smtp.helo(host);
        smtp.setSender("aaa@poczta.pl");
        smtp.addRecipient(mail.to);
        var date = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z").format(new Date());
        String mailText = "From: <a@to.do>\r\n" +
                "To: " + mail.getTo() + "\r\n" +
                "Date: " + date + "\r\n" +
                "Subject: " + mail.getSubject() + "\r\n" + "\r\n" +
                mail.getBody() + "\r\n" +
                ".\r\n";
        smtp.sendMessage(mailText);
        smtp.logout(host);
    }

}
