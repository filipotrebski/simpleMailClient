/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package mail;

import com.beust.jcommander.JCommander;
import mail.args.ListCommand;
import mail.args.MainCommand;
import mail.args.ReadCommand;
import mail.args.SendCommand;
import mail.client.Mail;
import mail.client.SendClient;
import mail.client.SmtpSendClient;
import mail.client.imap.EmailHeader;
import mail.client.imap.FolderContent;
import mail.client.imap.ImapClient;
import mail.client.imap.ReceiveEmail;

import java.io.*;
import java.util.List;
import java.util.Properties;

public class App {

    private final SendClient sendClient;
    private ImapClient imapClient;

    public App(SendClient sendClient, ImapClient imapClient) {
        this.sendClient = sendClient;
        this.imapClient = imapClient;
    }

    public static void main(String[] args) throws Exception {
        //   SendClient sendClient = new DummySendClient();
        Properties p = new Properties();
        p.load(new FileInputStream(new File(System.getProperty("user.home") + "/.simplemail/", "settings.properties")));

        SendClient sendClient = new SmtpSendClient(
                p.getProperty("smtp.host", "localhost"),
                Integer.parseInt(p.getProperty("smtp.port", "1025")),
                p.getProperty("user", "u"),
                p.getProperty("password", "p")
        );
        ImapClient imapClient = new ImapClient(p.getProperty("imap.host"), Integer.parseInt(p.getProperty("imap.port")), p.getProperty("user"), p.getProperty("password"));
        var input = new BufferedReader(new InputStreamReader(System.in));
        new App(sendClient, imapClient).run(input, args);
    }

    public void run(BufferedReader input, String... args) throws Exception {
        MainCommand mainCommand = new MainCommand();
        JCommander jCommander = new JCommander(mainCommand);
        jCommander.setProgramName("simpleMailClient");
        SendCommand sendCommand = new SendCommand();
        ListCommand listCommand = new ListCommand();
        ReadCommand readCommand = new ReadCommand();
        jCommander.addCommand("send", sendCommand);
        jCommander.addCommand("list", listCommand);
        jCommander.addCommand("read", readCommand);
        jCommander.parse(args);

        if (mainCommand.help) {
            jCommander.usage();
        } else if (jCommander.getParsedCommand() == null) {
            jCommander.usage();
        } else if (jCommander.getParsedCommand().equals("send")) {
            send(sendCommand, sendClient, input);
        } else if (jCommander.getParsedCommand().equals("list")) {
            System.out.println("Listing");
            list();
        } else if (jCommander.getParsedCommand().equals("read")) {
            read(readCommand.folder, readCommand.index);
        } else {
            jCommander.usage();
        }
    }

    private void read(String folder, int index) throws IOException {
        imapClient.selectFolder(folder);
        ReceiveEmail email = imapClient.getEmail(index);
        System.out.println(email.getEmailBody());
    }

    public void list() throws Exception {
        FolderContent folderContent = imapClient.selectFolder("INBOX");
        List<EmailHeader> emailHeaders = imapClient.listHeaders(1, folderContent.getCount());
        for (int i = 0; i < emailHeaders.size(); i++) {
            EmailHeader emailHeader = emailHeaders.get(i);
            System.out.println((i + 1) + " " + emailHeader.getFrom() + " | " + emailHeader.getSubject());
        }
    }


    public void send(SendCommand sendCommand, SendClient sendClient, BufferedReader input) throws Exception {
        System.out.println("Enter email body, end with empty line");
        StringBuilder stringBuffer = new StringBuilder();
        String line = null;
        while ((line = input.readLine()).length() != 0) {
            stringBuffer.append(line).append("\n");
        }
        var mailToSend = new Mail("smtpmail@onet.pl", sendCommand.getRecipient(), sendCommand.getSubject(), stringBuffer.toString().trim());
        sendClient.send(mailToSend);
    }
}
