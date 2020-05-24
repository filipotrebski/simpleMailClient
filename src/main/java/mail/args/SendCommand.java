package mail.args;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import java.io.File;
import java.util.Collections;
import java.util.List;

@Parameters(commandNames = "Send", commandDescription = "Send email")
public class SendCommand {


    @Parameter(names = {"-to"}, required = true)
    private List<String> recipient;

    @Parameter(names = {"-cc"})
    private List<String> cc = Collections.emptyList();

    @Parameter(names = {"-bcc"})
    private List<String> bcc = Collections.emptyList();

    @Parameter(names = {"-s", "-subject"}, required = true)
    private String subject;

    @Parameter(names = {"-a", "--attachment"}, converter = FileConverter.class)
    private List<File> attachments = Collections.emptyList();

    public List<String> getRecipient() {
        return recipient;
    }

    public String getSubject() {
        return subject;
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

    private static class FileConverter implements IStringConverter<File> {
        @Override
        public File convert(String value) {
            return new File(value);
        }
    }
}
