package mail.args;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandNames = "Send", commandDescription = "Send email")
public class SendCommand {
    @Parameter(names = {"-to"}, required = true)
    private String recipient;

    @Parameter(names = {"-s", "-subject"}, required = true)
    private String subject;

    public String getRecipient() {
        return recipient;
    }

    public String getSubject() {
        return subject;
    }
}
