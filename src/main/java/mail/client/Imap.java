package mail.client;

import java.io.*;

public class Imap {
    private final OutputStream output;
    private final boolean debug;
    private final BufferedReader reader;

    public Imap(InputStream input, OutputStream output, boolean debug) {
        this.output = output;
        this.debug = debug;
        reader = new BufferedReader(new InputStreamReader(input));
    }

    public ImapResponse login(String user, String password) throws IOException {
        return sendCommand("A1 login " + user + " " + password);
    }

    public ImapResponse selectFolder(String folder) throws IOException {
        return sendCommand("A1 list \"" + folder + "/\" \"*\"");
    }

    public ImapResponse listEmails(int start, int end) throws IOException {
//        f fetch 1:10 (BODY[HEADER.FIELDS (To Subject Date)])
        return sendCommand("f fetch " + start + ":" + end + " (BODY[HEADER.FIELDS (To Subject Date)]");
    }

    public ImapResponse sendCommand(String cmd) throws IOException {
        output.write(cmd.getBytes());
        output.write("\r\n".getBytes());
        output.flush();
        return ResponseReader.readImapResponse(reader, debug, cmd);
    }
}
