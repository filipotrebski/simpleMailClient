package mail.client;

import java.io.*;
import java.util.Base64;

public class Smtp {

    private final OutputStream output;
    private final boolean debug;
    private final BufferedReader reader;

    public Smtp(InputStream input, OutputStream output, boolean debug) {
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

    public SmtpResponse ehlo(String server) throws IOException {
        return sendCommand("EHLO", server);
    }

    public SmtpResponse login(String user, String password) throws IOException {
        var authResponse = sendCommand("AUTH LOGIN");
        if (authResponse.getCode() < 400){
            sendCommand(Base64.getEncoder().encodeToString(user.getBytes()));
            var response = sendCommand(Base64.getEncoder().encodeToString(password.getBytes()));
            return response;
        } else {
            return authResponse;
        }
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

        String line;
        var stringBuilder = new StringBuilder();
        do {
            //there is more lines
            line = reader.readLine();
            stringBuilder.append(line).append("\n");
            if (debug) {
                System.out.println("S: " + line);
                System.out.println("Matches to read next: " + line.matches("\\d{3}-.*"));
            }

        } while (line.matches("\\d{3}-.*"));
        System.out.println("S: Reading done\n");

        var response = stringBuilder.toString().trim();
        var code = Integer.parseInt(response.substring(0, 3));
        return new SmtpResponse(code, response);
    }
}
