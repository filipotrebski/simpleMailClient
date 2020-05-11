package mail.client;

import junit.framework.TestCase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

public class ResponseReaderTest extends TestCase {

    public void testReadResponseSingleLine() throws IOException {
        //given
        var input = new BufferedReader(new StringReader("250 Hello user"));

        //when
        var smtpResponse = ResponseReader.readResponse(input, false);

        //then
        assertEquals(250, smtpResponse.getCode());
        assertEquals("250 Hello user", smtpResponse.getText());
    }

    public void testReadResponseMultiLine() throws IOException {
        //given
        String s = "250-smtp.poczta.onet.pl\n" +
                "250-PIPELINING\n" +
                "250-SIZE 90000000\n" +
                "250-ETRN\n" +
                "250-STARTTLS\n" +
                "250-AUTH PLAIN LOGIN XOAUTH2\n" +
                "250-AUTH=PLAIN LOGIN XOAUTH2\n" +
                "250-ENHANCEDSTATUSCODES\n" +
                "250 8BITMIME";
        var input = new BufferedReader(new StringReader(s));

        //when
        var smtpResponse = ResponseReader.readResponse(input, false);

        //then
        assertEquals(250, smtpResponse.getCode());
        assertEquals(s, smtpResponse.getText());
    }


    public void testSingleLineFailingImapResponse() throws IOException {
        //given
        var input = new BufferedReader(new StringReader("a NO [ALERT] Invalid base64 data in continued response"));

        //when
        var response = ResponseReader.readImapResponse(input, false, "a");

        //then
        assertEquals(response.getText(), "");
        assertFalse(response.isSuccess());
    }

    public void testSingleLineSuccessImapResponse() throws IOException {
        //given
        var input = new BufferedReader(new StringReader("A1 OK [CAPABILITY IMAP4 ... N] Logged in"));

        //when
        var response = ResponseReader.readImapResponse(input, false, "A1");

        //then
        assertEquals(response.getText(), "");
        assertTrue(response.isSuccess());
    }

    public void testMultiLineImapResponse() throws IOException {
        //given
        var input = new BufferedReader(new StringReader("* FLAGS (\\Answered \\Flagged \\Deleted \\Seen \\Draft)\n" +
                "* OK [PERMANENTFLAGS (\\Answered \\Flagged \\Deleted \\Seen \\Draft \\*)] Flags permitted.\n" +
                "* 15 EXISTS\n" +
                "* 15 RECENT\n" +
                "* OK [UNSEEN 11] First unseen.\n" +
                "* OK [UIDVALIDITY 1588335587] UIDs valid\n" +
                "* OK [UIDNEXT 16] Predicted next UID\n" +
                "* OK [NOMODSEQ] No permanent modsequences\n" +
                "g21 OK [READ-WRITE] Select completed (0.000 + 0.000 secs).\n"));

        //when
        var response = ResponseReader.readImapResponse(input, false, "g21");

        //then
        assertEquals(response.getText(), "* FLAGS (\\Answered \\Flagged \\Deleted \\Seen \\Draft)\n" +
                "* OK [PERMANENTFLAGS (\\Answered \\Flagged \\Deleted \\Seen \\Draft \\*)] Flags permitted.\n" +
                "* 15 EXISTS\n" +
                "* 15 RECENT\n" +
                "* OK [UNSEEN 11] First unseen.\n" +
                "* OK [UIDVALIDITY 1588335587] UIDs valid\n" +
                "* OK [UIDNEXT 16] Predicted next UID\n" +
                "* OK [NOMODSEQ] No permanent modsequences");
        assertTrue(response.isSuccess());

    }

}