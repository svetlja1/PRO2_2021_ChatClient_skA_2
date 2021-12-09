package czk.uhk.pro2.models.chatFileOperations;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import czk.uhk.pro2.models.Message;
import czk.uhk.pro2.models.MessageJson;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonChatFileOperations implements ChatDataFileOperations {
    private Gson gson;
    private static final String MESSAGE_FILE = "./messages.json";
    private static final String USERS_FILE = "./users.json";
    public JsonChatFileOperations(){
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    public List<Message> readMessages() {
        try{
            FileReader reader = new FileReader(MESSAGE_FILE);
            BufferedReader bufferedReader = new BufferedReader(reader);
            StringBuilder jsonText = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null){
                jsonText.append(line);
            }

            Type targetType = new TypeToken<ArrayList<Message>>(){}.getType();

            return gson.fromJson(jsonText.toString(), targetType);
        }catch (IOException e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public void writeMessages(List<Message> messages) {

        String jsonText = gson.toJson(messages);
        try{
            FileWriter writer = new FileWriter(MESSAGE_FILE);
            writer.write(jsonText);
            writer.flush();
            writer.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public List<String> readUsers() {
        try{
            FileReader reader = new FileReader(USERS_FILE);
            BufferedReader bufferedReader = new BufferedReader(reader);
            StringBuilder jsonText = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null){
                jsonText.append(line);
            }

            Type targetType = new TypeToken<ArrayList<String>>(){}.getType();

            return gson.fromJson(jsonText.toString(), targetType);
        }catch (IOException e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public void writeUsers(List<String> users) {
        String jsonText = gson.toJson(users);
        try{
            FileWriter writer = new FileWriter(USERS_FILE);
            writer.write(jsonText);
            writer.flush();
            writer.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
