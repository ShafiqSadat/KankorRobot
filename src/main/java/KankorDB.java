import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KankorDB {

    public static Connection getConnection() throws SQLException {
        String connect = "jdbc:sqlite:./kankor.db";
        return DriverManager.getConnection(connect, "", "");
    }
    public static void addUser(String userID , String name){
        try {
            Connection con = getConnection();
            String SQL = "INSERT INTO users (id,userId,name) VALUES(null,?,?)";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setString(1,userID);
            stmt.setString(2,name);
            stmt.execute();
//            System.out.println("Saved!!!");
            con.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static List<KankorBotModel> getUsers() {
        List<KankorBotModel> categoryModels = new ArrayList<>();
        var SQL = "SELECT * FROM `users`";
        try (var connection = KankorDB.getConnection(); var statement = connection.prepareStatement(SQL); var resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                KankorBotModel userModel = new KankorBotModel();
                userModel.setUserId(resultSet.getString("userId"));
                userModel.setName(resultSet.getString("name"));
                categoryModels.add(userModel);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return categoryModels;
    }

}
