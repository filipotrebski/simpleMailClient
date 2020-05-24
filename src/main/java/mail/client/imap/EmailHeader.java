package mail.client.imap;

import java.util.Date;
import java.util.Objects;

public class EmailHeader {
    private final String from;
    private final Date date;
    private final String subject;

    public EmailHeader(String from, Date date, String subject) {
        this.from = from;
        this.date = date;
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    public String getFrom() {
        return from;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "EmailHeader{" +
                "from='" + from + '\'' +
                ", date=" + date +
                ", subject='" + subject + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmailHeader that = (EmailHeader) o;
        return Objects.equals(from, that.from) &&
                Objects.equals(date, that.date) &&
                Objects.equals(subject, that.subject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, date, subject);
    }
}
