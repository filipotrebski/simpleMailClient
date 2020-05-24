package mail.client;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class Mail {

    public String from;
    public List<String> to;
    public List<String> cc;
    public List<String> bcc;
    public List<File> attachments;
    public String subject;
    public String body;


    public Mail(String from, List<String> to, List<String> cc, List<String> bcc, List<File> attachments, String subject, String body) {
        this.from = from;
        this.to = to;
        this.cc = cc;
        this.bcc = bcc;
        this.attachments = attachments;
        this.subject = subject;
        this.body = body;
    }

    public String getFrom() {
        return from;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public List<String> getTo() {
        return to;
    }

    public List<String> getCc() {
        return cc;
    }

    public List<String> getBcc() {
        return bcc;
    }

    public List<File> getAttachments() {
        return attachments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mail mail = (Mail) o;
        return Objects.equals(from, mail.from) &&
                Objects.equals(to, mail.to) &&
                Objects.equals(cc, mail.cc) &&
                Objects.equals(bcc, mail.bcc) &&
                Objects.equals(attachments, mail.attachments) &&
                Objects.equals(subject, mail.subject) &&
                Objects.equals(body, mail.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, cc, bcc, attachments, subject, body);
    }

    @Override
    public String toString() {
        return "Mail{" +
                "from='" + from + '\'' +
                ", to=" + to +
                ", cc=" + cc +
                ", bcc=" + bcc +
                ", attachments=" + attachments +
                ", subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
