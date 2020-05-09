package mail.client;

import junit.framework.TestCase;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;

public class SmtpTest extends TestCase {

    public void testReadResponseSingleLine() throws IOException {
        //given
        var input = new BufferedReader(new StringReader("250 Hello user"));

        //when
        SmtpResponse smtpResponse = Smtp.readResponse(input, false);

        //then
        assertEquals(250, smtpResponse.getCode());
        assertEquals("250 Hello user", smtpResponse.getText());
    }

    public void testReadResponseMultiLine() throws IOException {
        //given
        String s = "250-smtp.poczta.onet.pl\r\n" +
                "250-PIPELINING\r\n" +
                "250-SIZE 90000000\r\n" +
                "250-ETRN\r\n" +
                "250-STARTTLS\r\n" +
                "250-AUTH PLAIN LOGIN XOAUTH2\r\n" +
                "250-AUTH=PLAIN LOGIN XOAUTH2\r\n" +
                "250-ENHANCEDSTATUSCODES\r\n" +
                "250 8BITMIME";
        var input = new BufferedReader(new StringReader(s));

        //when
        SmtpResponse smtpResponse = Smtp.readResponse(input, false);

        //then
        assertEquals(250, smtpResponse.getCode());
        assertEquals(s, smtpResponse.getText());
    }

}