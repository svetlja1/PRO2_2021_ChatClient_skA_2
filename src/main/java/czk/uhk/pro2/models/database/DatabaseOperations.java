package czk.uhk.pro2.models.database;

import czk.uhk.pro2.models.Message;

import java.util.List;

public interface DatabaseOperations {
    void addMessage(Message message);
    List<Message> getMessages();
}
