package mail.client.smtp;

import mail.client.Mail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;

public class DataWriter {

    private static final String CRLF = "\r\n";
    private static final String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss Z";

    public static String write(Mail mail) throws IOException {
        if (mail.getAttachments().size() == 0) {
            return writeWithoutAttachments(mail);
        } else {
            return writeWithAttachments(mail);
        }
    }


    public static String writeWithoutAttachments(Mail mail) {
        var toHeader = mail.getTo().stream().map(s -> "TO: <" + s + ">\r\n").collect(Collectors.joining());
        var ccHeader = mail.getCc().stream().map(s -> "CC: <" + s + ">\r\n").collect(Collectors.joining());
        var bccHeader = mail.getBcc().stream().map(s -> "BCC: <" + s + ">\r\n").collect(Collectors.joining());
        var date = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z").format(new Date());
        return "From: " + mail.getFrom() + CRLF +
                toHeader +
                ccHeader +
                bccHeader +
                "Date: " + date + CRLF +
                "Subject: " + mail.getSubject() + CRLF + CRLF +
                mail.getBody() + CRLF + CRLF +
                ".\r\n";
    }

    public static String writeWithAttachments(Mail mail) throws IOException {
        StringBuilder sb = new StringBuilder();
        var toHeader = mail.getTo().stream().map(s -> "TO: <" + s + ">\r\n").collect(Collectors.joining());
        var ccHeader = mail.getCc().stream().map(s -> "CC: <" + s + ">\r\n").collect(Collectors.joining());
        var bccHeader = mail.getBcc().stream().map(s -> "BCC: <" + s + ">\r\n").collect(Collectors.joining());
        var date = new SimpleDateFormat(DATE_FORMAT).format(new Date());
        var boundary = "--42";
        //headers
        sb
                .append("From: ").append(mail.getFrom()).append(CRLF)
                .append(toHeader)
                .append(ccHeader)
                .append(bccHeader)
                .append("Date: ")
                .append(date).append(CRLF)
                .append("Subject: ")
                .append(mail.getSubject()).append(CRLF)
                .append("Content-Type: multipart/alternative; boundary=\"42\"")
                .append(CRLF);

        //body
        sb.append(boundary).append(CRLF);
        sb.append("Content-Type: text/plain; charset=\"UTF-8\"").append(CRLF);
        sb.append("Content-Transfer-Encoding: quoted-printable").append(CRLF);
        sb.append(CRLF);
        sb.append(mail.body).append(CRLF);


        //attachments
        for (int i = 0; i < mail.getAttachments().size(); i++) {
            File file = mail.getAttachments().get(i);
            String contentType = Files.probeContentType(file.toPath());
            sb.append(boundary).append(CRLF);
            sb.append("Content-Type: ").append(contentType).append("; name=\"").append(file.getName()).append("\"").append(CRLF);
            sb.append("Content-Disposition: attachment; filename=\"").append(file.getName()).append("\"").append(CRLF);
            sb.append("Content-Transfer-Encoding: base64").append(CRLF);
            sb.append("Content-ID: <attachment_").append(i).append(">").append(CRLF);
            sb.append(CRLF);

            Base64.Encoder mimeEncoder = Base64.getMimeEncoder(76, CRLF.getBytes());
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            com.google.common.io.Files.asByteSource(file).copyTo(mimeEncoder.wrap(byteArrayOutputStream));
            String attachmentBase64Encoded = new String(byteArrayOutputStream.toByteArray());
            sb.append(attachmentBase64Encoded).append(CRLF);
        }

        //final boundary
        sb.append(boundary).append("--").append(CRLF);
        sb.append(".").append(CRLF);
        return sb.toString();
    }


    public static void main(String[] args) throws IOException {
        Mail mail = new Mail(
                "a@b",
                Collections.singletonList("b@c"),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.singletonList(new File("/home/krzyh/piny.txt")),
                "temat",
                "Tresc\nemaila"
        );
        System.out.println(writeWithAttachments(mail));
    }
}
