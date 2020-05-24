package mail.client;

import mail.client.smtp.SmtpResponse;

import java.io.BufferedReader;
import java.io.IOException;

public class ResponseReader {

    public static SmtpResponse readResponse(BufferedReader reader, boolean debug) throws IOException {

        String line;
        var stringBuilder = new StringBuilder();
        do {
            //there is more lines
            line = reader.readLine();
            stringBuilder.append(line).append("\n");
            if (debug) {
                System.out.println("S: " + line);
//                System.out.println("Matches to read next: " + line.matches("\\d{3}-.*"));
            }

        } while (line.matches("\\d{3}-.*"));
//        System.out.println("S: Reading done\n");

        var response = stringBuilder.toString().trim();
        var code = Integer.parseInt(response.substring(0, 3));
        return new SmtpResponse(code, response);
    }

    public static ImapResponse readImapResponse(BufferedReader reader, boolean debug, String command) throws IOException {
        StringBuilder sb = new StringBuilder();
        while (true) {
            String line = reader.readLine();
            if (debug) {
                System.out.println("S: " + line);
            }
            if (line.matches(command + " (OK|NO) .*")) {
                var success = line.split(" ")[1].equals("OK");
                return new ImapResponse(command, success, sb.toString().trim());
            } else {
                sb.append(line).append("\n");
            }
        }

    }
}
