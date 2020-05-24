package mail.args;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandNames = "Read", commandDescription = "Read email")
public class ReadCommand {

    @Parameter(names = "-f", description = "Mail folder", required = false)
    public String folder = "INBOX";

    @Parameter(names = "-i", description = "Email index", required = true)
    public int index;
}
