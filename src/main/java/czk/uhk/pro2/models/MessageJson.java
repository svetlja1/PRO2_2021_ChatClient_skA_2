package czk.uhk.pro2.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MessageJson {
    private String author;
    private String text;
    private String created;

    public MessageJson(String author, String text, String created) {
        this.author = author;
        this.text = text;
        this.created = created;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String createdString) {
        this.created = createdString;
    }
}
