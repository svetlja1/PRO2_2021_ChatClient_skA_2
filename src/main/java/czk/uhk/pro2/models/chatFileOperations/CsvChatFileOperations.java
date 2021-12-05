package czk.uhk.pro2.models.chatFileOperations;

import czk.uhk.pro2.models.Message;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CsvChatFileOperations implements ChatDataFileOperations {

    private static final String MESSAGE_FILE = "./messages.csv";
    private static final String USERS_FILE = "./users.csv";
    private String line = "";
    private List<Message> messages;
    private List<String> loggedUsers;
    public CsvChatFileOperations(){
        messages = new ArrayList<>();
        loggedUsers = new ArrayList<>();
    }
    @Override
    public List<Message> readMessages() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(MESSAGE_FILE));

            while((line = br.readLine()) != null) {
                String[] values = line.split(",");
                messages.add(new Message(values[0], values[1], LocalDateTime.parse(values[2])));
            }
            return messages;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    @Override
    public void writeMessages(List<Message> messages) {
        try{
            FileWriter fileWriter = new FileWriter(MESSAGE_FILE);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            PrintWriter printWriter = new PrintWriter(bufferedWriter);
            for (Message message : messages) {
                printWriter.println(message.getAuthor()+","+message.getText()+","+message.getCreated().toString());
            }
            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> readUsers() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(USERS_FILE));

            while((line = br.readLine()) != null) {
                String values = line.toString();
                loggedUsers.add(values);
            }
            return loggedUsers;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    @Override
    public void writeUsers(List<String> users) {
        try{
            FileWriter fileWriter = new FileWriter(USERS_FILE);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            PrintWriter printWriter = new PrintWriter(bufferedWriter);
            for (String user : users) {
                printWriter.println(user);
            }
            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
