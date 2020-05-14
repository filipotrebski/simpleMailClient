package mail.client.imap;

import org.junit.Test;

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
        calendar.set(Calendar.MILLISECOND,0);
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
        calendar.set(Calendar.MILLISECOND,0);
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

}