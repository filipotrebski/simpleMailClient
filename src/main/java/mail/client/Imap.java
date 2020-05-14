package mail.client;

import java.io.*;

public class Imap {
    private final OutputStream output;
    private final boolean debug;
    private final BufferedReader reader;

    public Imap(InputStream input, OutputStream output, boolean debug) throws IOException {
        this.output = output;
        this.debug = debug;
        reader = new BufferedReader(new InputStreamReader(input));
        String helloFromServer = reader.readLine();
        if (debug){
            System.out.println("S: " + helloFromServer);
        }
    }

    public ImapResponse login(String user, String password) throws IOException {
        return sendCommand("A1",  "login",  user,  password);
    }

    public ImapResponse selectFolder(String folder) throws IOException {
        //g21 SELECT "INBOX"
        return sendCommand("g21", "SELECT \"" + folder + "\"");
    }

    public ImapResponse listEmails(int start, int end) throws IOException {
//        f fetch 1:10 (BODY[HEADER.FIELDS (To Subject Date)])
        //f fetch 1:10 (BODY[HEADER.FIELDS (Subject)])
        return sendCommand("f",  "fetch " + start + ":" + end + " (BODY[HEADER.FIELDS (From Subject Date)])");
    }

    public ImapResponse emailBody(int number) throws IOException {
        return sendCommand("f1", "FETCH", Integer.toString(number), "BODY[TEXT]");
    }

    public ImapResponse sendCommand(String cmd, String... args) throws IOException {
        StringBuilder toSend = new StringBuilder();

        toSend.append(cmd);
        for (String arg : args) {
            toSend.append(" ");
            toSend.append(arg);
        }
        if (debug) {
            System.out.println("C: " + toSend.toString());
        }
        output.write(toSend.toString().getBytes());
        output.write("\r\n".getBytes());
        output.flush();
        return ResponseReader.readImapResponse(reader, debug, cmd);
    }

}
