package czk.uhk.pro2.models;

import czk.uhk.pro2.models.database.DatabaseOperations;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class DatabaseChatClient implements ChatClient{
    private String loggedUser;
    private List<Message> messages;
    private List<String> loggedUsers;

    private List<ActionListener> listenersLoggedUsersChanged = new ArrayList<>();
    private List<ActionListener> listenersMessageUpdated = new ArrayList<>();

    private DatabaseOperations databaseOperations;

    public DatabaseChatClient(DatabaseOperations databaseOperations) {
        this.databaseOperations = databaseOperations;
        messages = databaseOperations.getMessages();
        loggedUsers = new ArrayList<>();
    }
    @Override
    public boolean isAuthenticated() {
        return loggedUser!=null;
    }

    @Override
    public void login(String userName) {
        loggedUser = userName;
        loggedUsers.add(userName);
        addMessage(new Message(Message.USER_LOGGED_IN, userName));
        raiseEventLoggedUsersChanged();
        raiseEventMessageUpdated();
    }

    @Override
    public void logout() {
        loggedUsers.remove(loggedUser);
        addMessage(new Message(Message.USER_LOGGED_OUT, loggedUser));
        loggedUser = null;
        raiseEventLoggedUsersChanged();
        raiseEventMessageUpdated();
    }

    @Override
    public void sendMessage(String text) {
        addMessage(new Message(loggedUser,text));
        raiseEventMessageUpdated();
    }

    @Override
    public List<String> getLoggedUsers() {
        return loggedUsers;
    }

    @Override
    public List<Message> getMessages() {
        return messages;
    }

    @Override
    public void addListenerLoggedUsersChanged(ActionListener toAdd) {
        listenersLoggedUsersChanged.add(toAdd);
    }

    @Override
    public void addListenerMessageUpdated(ActionListener toAdd) {
        listenersMessageUpdated.add(toAdd);
    }

    private void raiseEventMessageUpdated(){
        for (ActionListener al : listenersMessageUpdated){
            al.actionPerformed(new ActionEvent(this, 1, "listenersMessageUpdated"));
        }
    }

    private void raiseEventLoggedUsersChanged(){
        for (ActionListener al : listenersLoggedUsersChanged){
            al.actionPerformed(new ActionEvent(this, 1, "listenersLoggedUsersChanged"));
        }
    }

    private void addMessage(Message message){
        messages.add(message);
        databaseOperations.addMessage(message);
        raiseEventMessageUpdated();
    }
}
