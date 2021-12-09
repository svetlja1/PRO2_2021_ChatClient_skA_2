package czk.uhk.pro2.models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import czk.uhk.pro2.models.api.MessageRequest;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class WebChatClient implements ChatClient{
    private String loggedUser;
    private List<Message> messages;
    private List<MessageJson> messagesJson;
    private List<String> loggedUsers;
    private String token;

    private List<ActionListener> listenersLoggedUsersChanged = new ArrayList<>();
    private List<ActionListener> listenersMessageUpdated = new ArrayList<>();

    private Gson gson;
    private final String BASE_URL = "http://fimuhkpro22021.aspifyhost.cz";
    public WebChatClient() {
        gson = new Gson();
        messages = new ArrayList<>();
        loggedUsers = new ArrayList<>();
        messagesJson = new ArrayList<>();

        Runnable refreshLoggedUsersRun = () -> {
            Thread.currentThread().setName("RefreshLoggedUsers");
            try {
            while(true) {
                if (isAuthenticated()) {
                    refreshLoggedUsers();
                    TimeUnit.SECONDS.sleep(5);
                }

            }
                } catch (Exception e) {
                    e.printStackTrace();
                }


/*
            while(true) {
                if (isAuthenticated()) {
                    refreshLoggedUsers();
                }
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            */

        };
        Thread threadLoggedUsers = new Thread(refreshLoggedUsersRun);
        threadLoggedUsers.start();
    }
    @Override
    public boolean isAuthenticated() {
        return token!=null;
    }

    @Override
    public void login(String userName) {
            String url = BASE_URL + "/api/Chat/Login";
            HttpPost post = new HttpPost(url);
            StringEntity body = new StringEntity("\""+userName+"\"", "utf-8");
            body.setContentType("application/json");
            post.setEntity(body);

            try(CloseableHttpClient httpClient = HttpClients.createDefault();
                CloseableHttpResponse response = httpClient.execute(post)) {
//do příště refresh zpráv podobný jako refresh uživatelů zas to bude get s tim rozdilem ž ty zprávy
                //tady vidíme v jaykm form se vrací,se vrací v ok formatu,ale java strasi,gson nam tam datumm rozdeloval
                //jako objekt ale tady reseny jako tx retezek
                //mezifazi mezi parsovanim jak prevyst datu mz tohoto formatu aby tmou json rozumel
                //vyresit refreshZprav z API
            if(response.getStatusLine().getStatusCode() == 200){
                token = EntityUtils.toString(response.getEntity());
                token = token.replaceAll("\"", "").trim();
                loggedUser = userName;
                addMessage(new Message(Message.USER_LOGGED_IN, loggedUser));
                refreshLoggedUsers();
            }

            } catch (Exception e){

                e.printStackTrace();
            }

    }

    @Override
    public void logout() {
        try{
            String url = BASE_URL + "/api/Chat/Logout";
            HttpPost post = new HttpPost(url);
            StringEntity body = new StringEntity("\""+token+"\"", "utf-8");
            body.setContentType("application/json");
            post.setEntity(body);

            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(post);

            if(response.getStatusLine().getStatusCode() == 204){
                addMessage(new Message(Message.USER_LOGGED_OUT, loggedUser));
             token = null;
             loggedUser = null;
                refreshLoggedUsers();
            }

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void sendMessage(String text) {
        addMessage(new Message(loggedUser, text));
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
        try{
            MessageRequest msgRequest = new MessageRequest(token,message);
            String url = BASE_URL + "/api/Chat/SendMessage";
            HttpPost post = new HttpPost(url);
            StringEntity body = new StringEntity(gson.toJson(msgRequest), "utf-8");
            body.setContentType("application/json");
            post.setEntity(body);

            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(post);

            if(response.getStatusLine().getStatusCode() == 204){
                refreshMessages();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        messages.add(message);
        raiseEventMessageUpdated();
        //raiseEventLoggedUsersChanged();
    }

    private void refreshMessages() {
        try{
            String url = BASE_URL + "/api/Chat/GetMessages";
            HttpGet get = new HttpGet(url);
            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(get);

            if(response.getStatusLine().getStatusCode() == 200){
                String resultJson = null;
                resultJson = EntityUtils.toString(response.getEntity());

                messagesJson = gson.fromJson(resultJson,new TypeToken<ArrayList<MessageJson>>(){}.getType());
                for(int i=0;i<messagesJson.size();i++){
                        String created = messagesJson.get(i).getCreated();
                        created = created.substring(0, created.length()-6);
                        messages.add(new Message(messagesJson.get(i).getAuthor(), messagesJson.get(i).getText(), LocalDateTime.parse(created)));

                }
                raiseEventMessageUpdated();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private void refreshLoggedUsers(){
        try{
            String url = BASE_URL + "/api/Chat/GetLoggedUsers";
            HttpGet get = new HttpGet(url);
            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(get);

            if(response.getStatusLine().getStatusCode() == 200){
                String resultJson = null;
                resultJson = EntityUtils.toString(response.getEntity());

                loggedUsers = gson.fromJson(resultJson,new TypeToken<ArrayList<String>>(){}.getType());
                //raiseEventMessageUpdated() - asi ne;
                raiseEventLoggedUsersChanged();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }



    }
}
