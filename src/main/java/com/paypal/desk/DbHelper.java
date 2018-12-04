package com.paypal.desk;

import java.sql.*;
import java.util.*;

public class DbHelper {

    private static final Connection connection = getConnection();

    public static void createDB(){
        DDL.createDB(connection);
    }

    private static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/",
                    "root",
                    "root" // im mot drelem parol root, posmotrite kakoi parol u vas, dorogoi grug.
            );

            System.out.println("Connection successful");
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    static int createUser(String firstName, String lastName) {

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into users(first_name, last_name) " +
                    "values (?,?)");
            preparedStatement.setString(1,firstName);
            preparedStatement.setString(2,lastName);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.executeQuery("select id from users");
            resultSet.last();

            return resultSet.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
    static String findUser(int id){
        String sql = "select * from users where id=?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();
            int userId = resultSet.getInt(1);
            String firstName = resultSet.getString(2);
            String lastname = resultSet.getString(3);
            double balance = resultSet.getDouble(4);

            String user = "User {id: '" + userId + "', first_name: '" + firstName + "', last_name: " + lastname + ", balance: " + balance +"}";
            return user;


        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }
    static boolean cashFlow(int userId, double amount) {
        String sqlString ="update users set balance = (balance +?)where id= ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
            preparedStatement.setDouble(1, amount);
            preparedStatement.setInt(2, userId);
            preparedStatement.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    static boolean transaction(int userFrom, int userTo, double amount) {
        String trasql = "insert into transactions (user_from, user_to, transaction_amount) values (?,?,?)";
        cashFlow(userFrom, -amount);
        cashFlow(userTo,amount);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(trasql);
            preparedStatement.setInt(1,userFrom);
            preparedStatement.setInt(2, userTo);
            preparedStatement.setDouble(3, amount);
            preparedStatement.executeUpdate();

            return true;
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    static List<User> listUsers() {
        String sql = "select * from users";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            List<User> userList = new ArrayList<>();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                double balance = resultSet.getDouble("balance");

                userList.add(new User(
                        id, firstName, lastName, balance
                ));
            }
            return userList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    static List<Transaction> transactionList(){
        String sql ="select * from transactions";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            List<Transaction> transactionList = new ArrayList<>();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String userFrom = resultSet.getString("user_from");
                String userTo = resultSet.getString("user_to");
                double amount = resultSet.getDouble("transaction_amount");
                String date = resultSet.getString("transaction_date");

                transactionList.add(new Transaction(
                        id, userFrom, userTo, amount, date
                ));
            }
            return transactionList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Set<Integer> createUsersIdList(){
        Set<Integer> list = new TreeSet<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select id from users");
            while (resultSet.next()){
                list.add(resultSet.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
