package mail.client.imap;

import com.google.common.io.Resources;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

import static org.junit.Assert.*;

public class ImapParserTest {


    ImapParser parser = new ImapParser();

    @Test
    public void headerListEmpty() throws Exception {
        //given
        String response = "";

        //when
        List<EmailHeader> emailHeaders = parser.headerList(response);

        //then
        assertTrue(emailHeaders.isEmpty());
    }

    @Test
    public void headerListSingle() throws Exception {
        //given
        var calendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.set(2020, Calendar.MAY, 1, 12, 19, 47);
        calendar.set(Calendar.MILLISECOND, 0);
        var date = calendar.getTime();
        var expected = new EmailHeader("ab@cd.pl", date, "Witamy");
        String response = "* 1 FETCH (BODY[HEADER.FIELDS (TO SUBJECT DATE)] {131}\n" +
                "Date: Fri, 01 May 2020 12:19:47 -0000\n" +
                "Subject: Witamy\n" +
                "From: ab@cd.pl\n" +
                "\n" +
                ")";

        //when
        List<EmailHeader> emailHeaders = parser.headerList(response);

        //then
        assertEquals(1, emailHeaders.size());
        assertEquals(expected, emailHeaders.get(0));
    }

    @Test
    public void headerListMany() throws Exception {
        //given
        var calendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.set(2020, Calendar.MAY, 1, 12, 19, 47);
        calendar.set(Calendar.MILLISECOND, 0);
        var date = calendar.getTime();
        var expected1 = new EmailHeader("ab1@cd.pl", date, "Witam");
        var expected2 = new EmailHeader("ab@cd.pl", date, "Witaj 2");

        String response = "* 1 FETCH (BODY[HEADER.FIELDS (FROM SUBJECT DATE)] {131}\n" +
                "Date: Fri, 01 May 2020 12:19:47 -0000\n" +
                "Subject: Witam\n" +
                "From: ab1@cd.pl\n" +
                "\n" +
                ")\n" +
                "* 2 FETCH (BODY[HEADER.FIELDS (FROM SUBJECT DATE)] {94}\n" +
                "From: ab@cd.pl\n" +
                "Subject: Witaj 2\n" +
                "Date: Fri, 01 May 2020 12:19:47 +0000\n" +
                "\n" +
                ")";

        //when
        List<EmailHeader> emailHeaders = parser.headerList(response);

        //then
        assertEquals(2, emailHeaders.size());
        assertEquals(expected1, emailHeaders.get(0));
        assertEquals(expected2, emailHeaders.get(1));
    }

    @Test
    public void folderContent() {
        //given

        String input = "* FLAGS (\\Answered \\Flagged \\Deleted \\Seen \\Draft)\n" +
                "* OK [PERMANENTFLAGS (\\Answered \\Flagged \\Deleted \\Seen \\Draft \\*)] Flags permitted.\n" +
                "* 15 EXISTS\n" +
                "* 15 RECENT\n" +
                "* OK [UNSEEN 11] First unseen.\n" +
                "* OK [UIDVALIDITY 1588335587] UIDs valid\n" +
                "* OK [UIDNEXT 16] Predicted next UID\n" +
                "* OK [NOMODSEQ] No permanent modsequences";
        //when
        var folder = parser.folder(input);

        //then
        assertEquals(15, folder.getCount());

    }


    @Test
    public void extractEmailParts() throws IOException {
        //given
        String input = Resources.toString(this.getClass().getClassLoader().getResource("multipart-email.txt"), Charset.defaultCharset());

        //when
        var receivedParts = parser.extractEmailParts(input, "===============4542122906753545732==");

        //then

        receivedParts.forEach(part -> {
            var c = new String(part.getContent());
            System.out.println(c);
        });
        assertEquals(3, receivedParts.size());
        assertEquals("text/plain; charset=\"utf-8\"", receivedParts.get(0).getHeaders().get("Content-Type"));

        var content0 = "Sprawdz zalacznik\n" +
                "--\n" +
                "X Y\n";
        assertArrayEquals(content0.getBytes(), receivedParts.get(0).getContent());


        assertEquals("text/html; charset=\"utf-8\"", receivedParts.get(1).getHeaders().get("Content-Type"));
        var content1 = "Sprawdz zalacznik<br /><br />--<br />X Y\nF\n";
        assertArrayEquals(content1.getBytes(), receivedParts.get(1).getContent());

        assertEquals("text/plain", receivedParts.get(2).getHeaders().get("Content-Type"));
        var content2 = "ala ma kota\n" +
                "a kot ma ale\n" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\n";
        assertArrayEquals(content2.getBytes(), receivedParts.get(2).getContent());

    }

    @Test
    public void parseEmailPart() {
        //given
        String input = "\n" +
                "Content-Type: text/plain\n" +
                "Content-Transfer-Encoding: base64\n" +
                "Content-Disposition: attachment; filename=\"plik.txt\"\n" +
                "\n" +
                "YWxhIG1hIGtvdGEKYSBrb3QgbWEgYWxlCmFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFh\n" +
                "YWFhYWFhYWFhYWFhYWEK\n";

        //when
        var receivedEmailPart = parser.parseEmailPart(input).get(0);

        //then
        var content = "ala ma kota\n" +
                "a kot ma ale\n" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\n";
        assertArrayEquals(content.getBytes(), receivedEmailPart.getContent());
        assertEquals("text/plain", receivedEmailPart.getHeaders().get("Content-Type"));
        assertEquals("base64", receivedEmailPart.getHeaders().get("Content-Transfer-Encoding"));
        assertEquals("attachment; filename=\"plik.txt\"", receivedEmailPart.getHeaders().get("Content-Disposition"));
    }


}