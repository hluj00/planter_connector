package application.MYsql_Client;

import application.classes.FloatMesurement;
import application.classes.Planter;
import application.classes.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class MySqlConnection {


    static Connection connection = null;
    static PreparedStatement prepareStat = null;


    public MySqlConnection() {

//        try {
//            log("-------- Simple Crunchify Tutorial on how to make JDBC connection to MySQL DB locally on macOS ------------");
//            makeJDBCConnection();
//
//            log("\n---------- Adding company 'Crunchify LLC' to DB ----------");
//            addDataToDB("Crunchify, LLC.", "NYC, US", 5, "https://crunchify.com");
//            addDataToDB("Google Inc.", "Mountain View, CA, US", 50000, "https://google.com");
//            addDataToDB("Apple Inc.", "Cupertino, CA, US", 30000, "http://apple.com");
//
//            log("\n---------- Let's get Data from DB ----------");
//            getDataFromDB();
//
//            crunchifyPrepareStat.close();
//            crunchifyConn.close(); // connection close
//
//        } catch (SQLException e) {
//
//            e.printStackTrace();
//        }
    }

    public void makeJDBCConnection() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            log("Congrats - Seems your MySQL JDBC Driver Registered!");
        } catch (ClassNotFoundException e) {
            log("Sorry, couldn't found JDBC driver. Make sure you have added JDBC Maven Dependency Correctly");
            e.printStackTrace();
            return;
        }

        try {
            // DriverManager: The basic service for managing a set of JDBC drivers.
            connection = DriverManager.getConnection("jdbc:mysql://192.168.2.199:3306/planter_test", "gitea", "6vglCsmIvlwco9He");
            //connection = DriverManager.getConnection("jdbc:mysql://192.168.1.166:3306/planter_test", "gitea", "6vglCsmIvlwco9He");
            if (connection != null) {
                log("Connection Successful! Enjoy. Now it's time to push data");
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
            log( String.format(" added successfully %s", table));
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
                users.add(new User(id, userName, email));
            }
        } catch (
                SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public List<Planter> getAllPlanters() {
        List<Planter> planters = new ArrayList<Planter>();;

        try {
            String getQueryStatement = "SELECT * FROM planter";
            prepareStat = connection.prepareStatement(getQueryStatement);
            ResultSet rs = prepareStat.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int settingId = rs.getInt("plant_presets_id");
                int userId = rs.getInt("user_id");
                String color = rs.getString("color");
                planters.add(new Planter(id, name, settingId, userId, color));
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


}
