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

    public void readGreetings() throws IOException {
        ResponseReader.readResponse(reader, debug);
    }

    public void addRecipient(String recipient) throws IOException {
        sendCommand("RCPT", "TO:<" + recipient + ">");
    }

    public SmtpResponse sendMessage(String message) throws IOException {
        SmtpResponse data = sendCommand("DATA");
        if (data.isSuccess()) {
            return sendCommand(message);
        }
        return data;
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
        if (authResponse.isSuccess()) {
            var userResponse = sendCommand(Base64.getEncoder().encodeToString(user.getBytes()));
            if (!userResponse.isSuccess()) {
                return userResponse;
            }
            return sendCommand(Base64.getEncoder().encodeToString(password.getBytes()));
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
        output.write(toSend.toString().trim().getBytes());
        output.write("\r\n".getBytes());
        output.flush();
        return ResponseReader.readResponse(reader, debug);
    }

}
