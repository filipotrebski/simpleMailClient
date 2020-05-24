package mail.args;

import com.beust.jcommander.Parameter;

public class MainCommand {

    @Parameter(names = {"-h", "--help"}, description = "Print help")
    public boolean help;
}
