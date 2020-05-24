package mail.args;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandNames = "List", commandDescription = "List recent emails")
public class ListCommand {

    @Parameter(names = "-f", description = "Mail folder")
    public String folder;
}
