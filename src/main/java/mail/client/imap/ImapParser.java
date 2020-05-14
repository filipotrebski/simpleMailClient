package mail.client.imap;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImapParser {

    public List<EmailHeader> headerList(String response) throws IOException, ParseException {
        ArrayList<EmailHeader> emailHeaders = new ArrayList<>();
        var pattern = Pattern.compile("(\\* \\d+ FETCH[^\\n]*?\\n(.*?)\\n\\))", Pattern.MULTILINE + Pattern.DOTALL);
        Matcher matcher = pattern.matcher(response);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z",Locale.ENGLISH);
        while ((matcher.find())){
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
        FolderContent folderContent = new FolderContent(count);
        return folderContent;
    }
}
