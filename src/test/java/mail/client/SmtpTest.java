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
        String s = "250-smtp.server.com Hello client.example.com\n" +
                "250-SIZE 1000000\n" +
                "250 AUTH LOGIN PLAIN CRAM-MD5";
        var input = new BufferedReader(new StringReader(s));

        //when
        SmtpResponse smtpResponse = Smtp.readResponse(input, false);

        //then
        assertEquals(250, smtpResponse.getCode());
        assertEquals(s, smtpResponse.getText());
    }

}