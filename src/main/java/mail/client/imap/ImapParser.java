package mail.client.imap;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ImapParser {

    public List<EmailHeader> headerList(String response) throws IOException, ParseException {
        ArrayList<EmailHeader> emailHeaders = new ArrayList<>();
        var pattern = Pattern.compile("(\\* \\d+ FETCH[^\\n]*?\\n(.*?)\\n\\))", Pattern.MULTILINE + Pattern.DOTALL);
        Matcher matcher = pattern.matcher(response);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
        while ((matcher.find())) {
            String group = matcher.group(2);
            Properties properties = new Properties();
            properties.load(new StringReader(group));
            String from = properties.getProperty("From");
            String subject = properties.getProperty("Subject");
            String dateString = properties.getProperty("Date");
            var date = simpleDateFormat.parse(dateString);
            EmailHeader emailHeader = new EmailHeader(from, date, subject);
            System.out.println(group);
            emailHeaders.add(emailHeader);
        }
        return emailHeaders;
    }

    public FolderContent folder(String response) {
        var pattern = Pattern.compile(".* (\\d+) EXISTS.*");
        Matcher matcher = pattern.matcher(response);
        matcher.find();
        int count = Integer.parseInt(matcher.group(1));
        return new FolderContent(count);
    }


    public List<ReceivedEmailPart> extractEmailParts(String response, String boundary) {
        var allParts = Arrays.asList(response.split("--" + boundary));
        var parts = allParts.subList(1, allParts.size() - 1);
        return parts
                .stream()
                .flatMap(s -> this.parseEmailPart(s).stream())
                .collect(Collectors.toList());
    }

    public List<ReceivedEmailPart> parseEmailPart(String part) {
        String[] split = part.replaceAll("\r", "").split("\n\n", 2);
        var headersString = split[0];
        var headers = headersString
                .trim()
                .lines()
                .map(s -> s.split(": ", 2))
                .collect(Collectors.toMap(s -> s[0], s -> s[1]));

        var contentAsString = split[1];
        var contentType = headers.get("Content-Type");
        String contentTypeEncoding = headers.getOrDefault("Content-Transfer-Encoding", "");
//        var body = contentAsString;
        var content = contentAsString.getBytes();
        if (contentTypeEncoding.equals("base64")) {
            content = Base64.getMimeDecoder().decode(contentAsString);
        }

        //Content-Type: multipart/alternative; boundary="===============6360767015533933081=="
        if (headers.get("Content-Type").startsWith("multipart/")) {
            var boundary = headers
                    .get("Content-Type")
                    .replaceAll(".* boundary=", "")
                    .replaceAll("\"", "");
            return extractEmailParts(contentAsString, boundary);
        } else {
            return Collections.singletonList(new ReceivedEmailPart(headers, content));
        }
    }
}
