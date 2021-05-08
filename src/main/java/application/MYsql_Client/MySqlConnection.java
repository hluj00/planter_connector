package application.MYsql_Client;

import application.classes.FloatMesurement;
import application.classes.Planter;
import application.classes.PlanterAction;
import application.classes.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class MySqlConnection {


    static Connection connection = null;
    static PreparedStatement prepareStat = null;


    public void makeJDBCConnection() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        try {
            connection = DriverManager.getConnection("jdbc:mysql://192.168.1.166:3306/planter_test", "gitea", "6vglCsmIvlwco9He");
            if (connection != null) {
                log("Connection Successful!");
            } else {
                log("Failed to make connection!");
            }
        } catch (SQLException e) {
            log("MySQL Connection Failed!");
            e.printStackTrace();
            return;
        }


    }

    public void addAirTemperature(FloatMesurement mesurement){
        addFloateMesurement(mesurement, "air_temperature");
    }

    public void addLightLevel(FloatMesurement mesurement){
        addFloateMesurement(mesurement, "light_level");
    }

    public void addWaterLevel(FloatMesurement mesurement){
        addFloateMesurement(mesurement, "water_level");
    }

    public void addSoilMoisture(FloatMesurement mesurement){
        addFloateMesurement(mesurement, "soil_moisture");
    }

    public void addAirHumidity(FloatMesurement mesurement){
        addFloateMesurement(mesurement, "air_humidity");
    }


    private void addFloateMesurement(FloatMesurement mesurement, String table) {
        try {
            String insertQueryStatement = String.format("INSERT  INTO  %s (value,date,planter_id)  VALUES  (?,?,?)",table.toString());

            prepareStat = connection.prepareStatement(insertQueryStatement);
            prepareStat.setFloat(1, mesurement.getMesurement());
            prepareStat.setTimestamp(2, mesurement.getDateTime());
            prepareStat.setInt(3, mesurement.getPlanterId());

            // execute insert SQL statement
            prepareStat.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<User>();;

        try {
            String getQueryStatement = "SELECT * FROM user";
            prepareStat = connection.prepareStatement(getQueryStatement);
            ResultSet rs = prepareStat.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String userName = rs.getString("username");
                String email = rs.getString("email");
                String hash = rs.getString("hash");
                users.add(new User(id, userName, email, hash));
            }
        } catch (
                SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public List<Planter> getAllPlanters() {
        List<Planter> planters = new ArrayList<>();

        try {
            String getQueryStatement = "SELECT p.*, u.`hash` " +
                    "FROM planter p " +
                    "LEFT JOIN `user`  u on (u.id = p.user_id)";
            prepareStat = connection.prepareStatement(getQueryStatement);
            ResultSet rs = prepareStat.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int settingId = rs.getInt("plant_presets_id");
                int userId = rs.getInt("user_id");
                String color = rs.getString("color");
                String hash = rs.getString("hash");
                planters.add(new Planter(id, name, settingId, userId, color, hash));
            }
        } catch (
                SQLException e) {
            e.printStackTrace();
        }

        Collections.sort(planters);
        return planters;
    }

    // Simple log utility
    private static void log(String string) {
        System.out.println(string);

    }

    public List<PlanterAction> findUnexecutedActions(Planter planter){
        List<PlanterAction> actions = new ArrayList<>();

        try {
            String getQueryStatement = "" +
                    "SELECT * FROM action " +
                    "WHERE executed = 0 " +
                    "AND planter_id = ? ";
            prepareStat = connection.prepareStatement(getQueryStatement);
            prepareStat.setInt(1,planter.getId());
            ResultSet rs = prepareStat.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                int type = rs.getInt("type");
                Timestamp createdAt = rs.getTimestamp("created_at");
                int planterId = rs.getInt("planter_id");
                boolean executed = rs.getBoolean("executed");
                actions.add(new PlanterAction(id, type, createdAt, null, planterId, executed));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return actions;
    }

    public void updateActionExecuted(PlanterAction action) {
        try {
            String insertQueryStatement = "UPDATE action " +
                    "SET executed_at = ? , executed = ? " +
                    "WHERE id = ? ";

            prepareStat = connection.prepareStatement(insertQueryStatement);
            prepareStat.setTimestamp(1, action.getExecutedAt());
            prepareStat.setBoolean(2, action.isExecuted());
            prepareStat.setInt(3, action.getId());

            // execute update SQL statement
            prepareStat.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
