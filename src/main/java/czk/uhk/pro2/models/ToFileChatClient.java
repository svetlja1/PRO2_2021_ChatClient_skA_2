package czk.uhk.pro2.models;

import czk.uhk.pro2.models.chatFileOperations.ChatDataFileOperations;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ToFileChatClient implements ChatClient{
    private String loggedUser;
    private List<Message> messages;
    private List<String> loggedUsers;

    private List<ActionListener> listenersLoggedUsersChanged = new ArrayList<>();
    private List<ActionListener> listenersMessageUpdated = new ArrayList<>();

    private final ChatDataFileOperations chatDataFileOperations;

    //Gson gson = null;

    private static final String MESSAGES_PATH = "./messages.json";
//predavat budto csv nebo json  v konmstruktoru (rozhrani
    public ToFileChatClient(ChatDataFileOperations chatDataFileOperations) {
        //gson = new GsonBuilder().setPrettyPrinting().create();
        messages = new ArrayList<>();
        loggedUsers = new ArrayList<>();

        this.chatDataFileOperations = chatDataFileOperations;

        readMessageFromFile();
        readUsersFromFile();

    }
    @Override
    public boolean isAuthenticated() {
        return loggedUser!=null;
    }

    @Override
    public void login(String userName) {
        loggedUser = userName;
        loggedUsers.add(userName);
        writeUsersToFile();

        addMessage(new Message(Message.USER_LOGGED_IN, userName));
        raiseEventLoggedUsersChanged();
        raiseEventMessageUpdated();
    }

    @Override
    public void logout() {
        loggedUsers.remove(loggedUser);
        writeUsersToFile();
        addMessage(new Message(Message.USER_LOGGED_OUT, loggedUser));
        loggedUser = null;
        raiseEventLoggedUsersChanged();
        raiseEventMessageUpdated();
    }

    @Override
    public void sendMessage(String text) {
        addMessage(new Message(loggedUser,text));
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

    private void raiseEventLoggedUsersChanged(){
        for (ActionListener al : listenersLoggedUsersChanged){
            al.actionPerformed(new ActionEvent(this, 1, "listenersLoggedUsersChanged"));
        }
    }

    private void raiseEventMessageUpdated(){
        for (ActionListener al : listenersMessageUpdated){
            al.actionPerformed(new ActionEvent(this, 1, "listenersMessageUpdated"));
        }
    }

    private void addMessage(Message message){
        messages.add(message);
        writeMessagesToFile();
        raiseEventMessageUpdated();
    }

    private void writeMessagesToFile(){
        chatDataFileOperations.writeMessages(messages);
    }
    private void readMessageFromFile(){
        messages = chatDataFileOperations.readMessages();
    }
    private void writeUsersToFile(){
        chatDataFileOperations.writeUsers(loggedUsers);
    }
    private void readUsersFromFile(){
        loggedUsers = chatDataFileOperations.readUsers();
    }

}
