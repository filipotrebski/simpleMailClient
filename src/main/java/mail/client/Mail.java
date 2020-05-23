package mail.client;

import java.util.Objects;

public class Mail {
    public String from;
    public String to;
    public String subject;
    public String body;

    public Mail(String from, String to, String subject, String body) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.body = body;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "Mail{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mail mail = (Mail) o;
        return Objects.equals(from, mail.from) &&
                Objects.equals(to, mail.to) &&
                Objects.equals(subject, mail.subject) &&
                Objects.equals(body, mail.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, subject, body);
    }
}
