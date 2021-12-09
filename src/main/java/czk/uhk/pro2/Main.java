package czk.uhk.pro2;

import czk.uhk.pro2.gui.MainFrame;
import czk.uhk.pro2.models.*;
import czk.uhk.pro2.models.chatFileOperations.ChatDataFileOperations;
import czk.uhk.pro2.models.chatFileOperations.CsvChatFileOperations;
import czk.uhk.pro2.models.chatFileOperations.XmlChatFileOperations;

public class Main {

    public static void main(String[] args) {
        /*String databaseDriver = "org.apache.derby.jdbc.EmbeddedDriver";
        String url = "jdbc:derby:ClientChatDb";*/

        ChatDataFileOperations chatDataFileOperations;
        ChatClient chatClient;

        //chatDataFileOperations = new XmlChatFileOperations();
        //chatClient = new ToFileChatClient(chatDataFileOperations);
        chatClient = new WebChatClient();

        MainFrame mainFrame = new MainFrame(800,600, chatClient);
        mainFrame.setVisible(true);

/*
        try{
            DbInitializer dbInitializer = new DbInitializer(databaseDriver, url);
            //dbInitializer.init();

            DatabaseOperations databaseOperations = new JdbcDatabaseOperations(databaseDriver, url);
            chatClient = new DatabaseChatClient(databaseOperations);

            MainFrame mainFrame = new MainFrame(800,600, chatClient);
            mainFrame.setVisible(true);
        }catch (Exception e){
            e.printStackTrace();
        }
*/
        }

        // udělat XML chat file operations
    //práce s tabulkou u zkoušky LoggedUsersTableModel, udělejte si XML kter budete naíčtat do tabulky
    //ať umíte poskládat aktiví prvky do taublkly XML tab, umístti to do GUI, formulář abyste dávali do tabulky ay se propisovali do tabulky, třeba to nebude XML ale JSON

    //práce s db asi ve zk nebude


    // pomocnou třídu Messages vzít string převízt do LocalDateTime
        private static void TestChat() {
            ChatClient chatClient = new InMemoryChatClient();
            System.out.println("Loging in");
            chatClient.login("svetlja");
            System.out.println("IS logged" + chatClient.isAuthenticated());
            System.out.println("Logged users");
            for (String user : chatClient.getLoggedUsers()) {
                System.out.println(user);
            }
            System.out.println("sending msg1");
            chatClient.sendMessage("čus");
            System.out.println("sending msg2");
            chatClient.sendMessage("zdar");
            System.out.println("loging out");
            chatClient.logout();
            System.out.println("IS logged" + chatClient.isAuthenticated());
            for (Message msg : chatClient.getMessages()) {
                System.out.println(msg);

            }
        }
}
