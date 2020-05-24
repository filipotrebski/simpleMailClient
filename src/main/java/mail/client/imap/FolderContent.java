package mail.client.imap;

import java.util.Objects;

public class FolderContent {
    private final int count;

    public FolderContent(int count) {
        this.count = count;
    }


    public int getCount() {
        return count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FolderContent that = (FolderContent) o;
        return count == that.count;
    }

    @Override
    public int hashCode() {
        return Objects.hash(count);
    }

    @Override
    public String toString() {
        return "FolderContent{" +
                "count=" + count +
                '}';
    }
}
