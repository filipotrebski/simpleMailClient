package mail.client.imap;

import mail.client.Imap;
import mail.client.ImapResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

public class ImapClient {

    private String host;
    private int port;
    private String user;
    private String password;
    private final Socket socket;
    private final Imap imap;

    public ImapClient(String host, int port, String user, String password) throws IOException {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        socket = new Socket(host, port);
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        imap = new Imap(inputStream, outputStream, true);
        imap.login(user,password);
    }

    public FolderContent selectFolder(String  folder) throws IOException {
        ImapResponse imapResponse = imap.selectFolder(folder);
        return new ImapParser().folder(imapResponse.getText());
    }

    public List<EmailHeader> listHeaders(int from, int to) throws  Exception{
        ImapResponse imapResponse = imap.listEmails(from, to);
        //TODO check error code
        List<EmailHeader> emailHeaders = new ImapParser().headerList(imapResponse.getText());
        return emailHeaders;
    }

    public ReceiveEmail getEmail(int number) throws IOException {
        return new ReceiveEmail(imap.emailBody(number).getText());
    }

}