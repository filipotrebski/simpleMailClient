package mail.client.smtp;

import mail.client.Mail;
import mail.client.SendClient;
import mail.client.smtp.Smtp;

import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;

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
        smtp.readGreetings();
        smtp.ehlo(host);
        smtp.login(user, password);
        smtp.setSender(mail.getFrom());
        for (String s : mail.getTo()) {
            smtp.addRecipient(s);
        }
        for (String s : mail.getCc()) {
            smtp.addCc(s);
        }
        for (String s : mail.getCc()) {
            smtp.addBcc(s);
        }

        var toHeader = mail.getTo().stream().map(s -> "TO: <" + s + ">\r\n").collect(Collectors.joining());
        var ccHeader = mail.getCc().stream().map(s -> "CC: <" + s + ">\r\n").collect(Collectors.joining());
        var bccHeader = mail.getBcc().stream().map(s -> "BCC: <" + s + ">\r\n").collect(Collectors.joining());

        var date = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z").format(new Date());
        String mailText = "From: " + mail.getFrom() + "\r\n" +
                toHeader +
                ccHeader +
                bccHeader +
                "Date: " + date + "\r\n" +
                "Subject: " + mail.getSubject() + "\r\n" + "\r\n" +
                mail.getBody() + "\r\n" + "\r\n" +
                ".\r\n";
        smtp.sendMessage(mailText);
        smtp.logout(host);
    }

}
