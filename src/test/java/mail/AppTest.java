/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package mail;

import mail.client.Mail;
import mail.client.SendClient;
import mail.client.imap.ImapClient;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.File;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;

public class AppTest {


    @Test
    public void sendByRun() throws Exception {
        //given
        var args = new String[]{
                "send", "-to", "john@doe.com", "-s", "subject"
        };
        var body = "message\nbody\n\n";
        var expected = new Mail(
                "smtpmail@onet.pl",
                Collections.singletonList("john@doe.com"),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                "subject",
                body.trim());

        //when
        //then
        testSend(args, body, expected);
    }

    @Test
    public void sendByRun2Recipients() throws Exception {
        //given
        var args = new String[]{
                "send",
                "-to", "john@doe.com",
                "-to", "anna@doe.com",
                "-s", "subject"
        };
        var body = "message\nbody\n\n";
        var expected = new Mail(
                "smtpmail@onet.pl",
                Arrays.asList("john@doe.com", "anna@doe.com"),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                "subject",
                body.trim());

        //when
        //then
        testSend(args, body, expected);
    }

    @Test
    public void sendByRunWith2cc() throws Exception {
        //given
        var args = new String[]{
                "send",
                "-to", "john@doe.com",
                "-cc", "anna@doe.com",
                "-cc", "julia@doe.com",
                "-s", "subject"
        };
        var body = "message\nbody\n\n";
        var expected = new Mail(
                "smtpmail@onet.pl",
                Collections.singletonList("john@doe.com"),
                Arrays.asList("anna@doe.com", "julia@doe.com"),
                Collections.emptyList(),
                Collections.emptyList(),
                "subject",
                body.trim());

        //when
        //then
        testSend(args, body, expected);
    }

    @Test
    public void sendByRunWith2bcc() throws Exception {
        //given
        var args = new String[]{
                "send",
                "-to", "john@doe.com",
                "-bcc", "anna@doe.com",
                "-bcc", "julia@doe.com",
                "-s", "subject"
        };
        var body = "message\nbody\n\n";
        var expected = new Mail(
                "smtpmail@onet.pl",
                Collections.singletonList("john@doe.com"),
                Collections.emptyList(),
                Arrays.asList("anna@doe.com", "julia@doe.com"),
                Collections.emptyList(),
                "subject",
                body.trim());

        //when
        //then
        testSend(args, body, expected);
    }

    @Test
    public void sendByRunWith2Attachments() throws Exception {
        //given
        var args = new String[]{
                "send",
                "-to", "john@doe.com",
                "-a", "a.jpg",
                "-a", "b.jpg",
                "-s", "subject"
        };
        var body = "message\nbody\n\n";
        var expected = new Mail(
                "smtpmail@onet.pl",
                Collections.singletonList("john@doe.com"),
                Collections.emptyList(),
                Collections.emptyList(),
                Arrays.asList(new File("a.jpg"), new File("b.jpg")),
                "subject",
                body.trim());

        //when
        //then
        testSend(args, body, expected);
    }




    public void testSend(String[] args, String body, Mail expected) throws Exception {
        //given
        SendClient sendClient = Mockito.mock(SendClient.class);
        ImapClient imapClient = Mockito.mock(ImapClient.class);
        var app = new App(sendClient, imapClient);
        var input = new BufferedReader(new StringReader(body));

        //when
        app.run(input, args);

        //then
        Mockito.verify(sendClient).send(expected);
    }

}
