import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KankorDB {

    public static Connection getConnection() throws SQLException {
//        String connect = "jdbc:sqlite:./kankor.db";
        String connect = "jdbc:sqlite:C:\\Users\\Shafi\\Desktop\\kank.db";
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

    public static void addResults(ResultsModel results){
        try {
            Connection con = getConnection();
            String SQL = "INSERT INTO results (id,kankorID,name, fatherName, grandFatherName, sex, province, school, points, result) VALUES(null,?,?,?,?,?,?,?,?,?)";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setString(1,results.getKankorID()+"");
            stmt.setString(2,results.getName()+"");
            stmt.setString(3,results.getFatherName()+"");
            stmt.setString(4,results.getGrandFatherName()+"");
            stmt.setString(5,results.getSex()+"");
            stmt.setString(6,results.getProvince()+"");
            stmt.setString(7,results.getSchool()+"");
            stmt.setString(8,results.getPoints()+"");
            stmt.setString(9,results.getResult()+"");
            stmt.execute();
//            System.out.println("Saved!!!");
            con.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static List<ResultsModel> getResultsFromDataBase(String name, String fatherName, String grandFatherName) {
        List<ResultsModel> categoryModels = new ArrayList<>();
        var SQL = "SELECT * FROM `results` WHERE name = ? AND fatherName = ? AND grandFatherName = ?";
        try (var connection = KankorDB.getConnection(); var statement = connection.prepareStatement(SQL)) {
            statement.setString(1,name);
            statement.setString(2,fatherName);
            statement.setString(3,grandFatherName);
            var resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ResultsModel resultsModel = new ResultsModel();
                resultsModel.setKankorID(resultSet.getString("kankorID"));
                resultsModel.setName(resultSet.getString("name"));
                resultsModel.setFatherName(resultSet.getString("fatherName"));
                resultsModel.setGrandFatherName(resultSet.getString("grandFatherName"));
                resultsModel.setSex(resultSet.getString("sex"));
                resultsModel.setProvince(resultSet.getString("province"));
                resultsModel.setSchool(resultSet.getString("school"));
                resultsModel.setPoints(resultSet.getString("points"));
                resultsModel.setResult(resultSet.getString("result"));
                categoryModels.add(resultsModel);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return categoryModels;
    }

}
