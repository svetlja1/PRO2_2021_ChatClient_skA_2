package czk.uhk.pro2.models.database;

import czk.uhk.pro2.models.Message;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class JdbcDatabaseOperations implements DatabaseOperations{
    private final Connection connection;
    private Statement statement;

    public JdbcDatabaseOperations(String driver, String url) throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        connection = DriverManager.getConnection(url);
    }

    @Override
    public void addMessage(Message message) {
        try{
            statement = connection.createStatement();
            String sql = "INSERT INTO ChatMessages (author, text, created) VALUES ("
                    + "'"+message.getAuthor()+"',"
                    + "'"+message.getText()+"',"
                    + "'"+ Timestamp.valueOf(message.getCreated())+"'"
                    + ")";
            statement.executeUpdate(sql);

            statement.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public List<Message> getMessages() {
     List<Message> messages = new ArrayList<>();
     try{
       statement = connection.createStatement();
       String sql = "SELECT author, text, created, created FROM ChatMessages";
       ResultSet resultSet = statement.executeQuery(sql);

       while(resultSet.next()){
           Message messageItem = new Message(
                   resultSet.getString("author"), // nebo 0 jako 0. sloupec
                   resultSet.getString("text"),
                   resultSet.getTimestamp("created").toLocalDateTime());
           messages.add(messageItem);
       }


       statement.close();
     } catch (Exception e){
         e.printStackTrace();
     }
     return messages;
    }
}
