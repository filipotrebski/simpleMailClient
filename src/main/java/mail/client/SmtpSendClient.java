package mail.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SmtpSendClient implements SendClient {

    private String host = "127.0.0.1";


    @Override
    public void send(Mail mail) throws Exception {
        System.out.println("Connecting");
        Socket socket = new Socket(host, 1025);

        System.out.println("Have socket");
        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter output = new PrintWriter(socket.getOutputStream());
        readAndPrint(input);
        output.print("HELO " + host + "\r\n");
        output.flush();
        readAndPrint(input);
        output.println("MAIL FROM:<aaa@poczta.onet.pl>\r");
        output.flush();
        readAndPrint(input);
        output.println("RCPT TO:<" + mail.getTo() + ">\r");
        output.flush();
        readAndPrint(input);
        output.println("DATA\r");
        output.flush();
        readAndPrint(input);

        output.println("From: Bob Example <bob@example.com>\r");
        output.println("To: Alice Example " + mail.getTo() + ">\r");
        output.println("Date: Tue, 15 Jan 2008 16:02:43 -0500\r");
        output.println("Subject: " + mail.getSubject()+"\r");
        output.println("\r");
        output.println(mail.getBody()+"\r");
        output.println(".\r");
        output.flush();
        readAndPrint(input);
        output.println("QUIT\r");
        output.flush();
        readAndPrint(input);
        readAndPrint(input);
        readAndPrint(input);
        readAndPrint(input);
        readAndPrint(input);
        readAndPrint(input);
        readAndPrint(input);
        readAndPrint(input);
        readAndPrint(input);
        readAndPrint(input);
        readAndPrint(input);


    }

    private void readAndPrint(BufferedReader input) throws IOException {
        String line = null;
        line = input.readLine();
        System.out.println("S: " + line);

//        while ((line = input.readLine()) != null) {
//            System.out.println("S: " + line);
//    }
}
}
