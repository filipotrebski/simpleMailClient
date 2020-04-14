package mail.args;

import com.beust.jcommander.Parameter;

public class Send {
    @Parameter(names = {"-to"}, required = true)
    private String recipient;

    @Parameter(names = {"-s", "-subject"},required = true)
    private String subject;

    public String getRecipient() {
        return recipient;
    }

    public String getSubject() {
        return subject;
    }
}

