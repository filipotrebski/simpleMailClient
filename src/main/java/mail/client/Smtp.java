package mail.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Smtp {

    private final InputStream input;
    private final OutputStream output;

    public Smtp(InputStream input, OutputStream output) {
        this.input = input;
        this.output = output;
    }

    public void addRecipient(String recipient) throws IOException {
        throw new RuntimeException("Not implemented");
    }

    public void sendMessage(String message) throws IOException {
        throw new RuntimeException("Not implemented");
    }

    public void setSender(String sender) throws IOException {
        throw new RuntimeException("Not implemented");
    }

    public void helo(String server) throws IOException {
        throw new RuntimeException("Not implemented");
    }

    public void logout(String server) throws IOException {
        throw new RuntimeException("Not implemented");
    }

    //TODO adding attachments
}
