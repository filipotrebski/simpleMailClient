package mail.client;

import java.io.*;

public class Smtp {

    private final InputStream input;
    private final OutputStream output;
    private boolean debug;
    private final BufferedReader reader;

    public Smtp(InputStream input, OutputStream output, boolean debug) {
        this.input = input;
        this.output = output;
        this.debug = debug;
        reader = new BufferedReader(new InputStreamReader(input));
    }

    public void addRecipient(String recipient) throws IOException {
        sendCommand("RCPT", "TO:<" + recipient + ">");
    }

    public SmtpResponse sendMessage(String message) throws IOException {
        return sendCommand("DATA\r\n" + message + "\r\n\r\n");
    }

    public SmtpResponse setSender(String sender) throws IOException {
        return sendCommand("MAIL", "FROM:<" + sender + ">");
    }

    public SmtpResponse helo(String server) throws IOException {
        return sendCommand("HELO", server);
    }

    public SmtpResponse logout(String server) throws IOException {
        return sendCommand("QUIT");
    }

    SmtpResponse sendCommand(String command, String... args) throws IOException {
        StringBuilder toSend = new StringBuilder();

        toSend.append(command);
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
        return readResponse(reader, debug);
    }

    static SmtpResponse readResponse(BufferedReader reader, boolean debug) throws IOException {

        String firstLine;
        var stringBuilder = new StringBuilder();
        do {
            //there is more lines
            firstLine = reader.readLine();
            stringBuilder.append(firstLine).append("\n");
            if (debug) {
                System.out.println("S: " + firstLine);
            }
        } while (firstLine.matches("\\d{3}-.*"));
        var response = stringBuilder.toString().trim();
        var code = Integer.parseInt(response.substring(0, 3));
        return new SmtpResponse(code, response);
    }
}
