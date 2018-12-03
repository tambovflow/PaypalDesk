package com.paypal.desk;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DDL {
    public static Connection createDB(Connection con){
        try (Statement statement = con.createStatement()) {
            statement.execute("create database if not exists paypal");
            statement.execute("use paypal");

            statement.execute("create table if not exists users (" +
                    "  id int auto_increment unique," +
                    "  first_name text not null," +
                    "  last_name text not null," +
                    "  balance real not null default 0" +
                    ");");

            statement.execute("create table if not exists  transactions (" +
                    "  id int auto_increment unique," +
                    "  user_from int," +
                    "  user_to int," +
                    "  transaction_amount real not null," +
                    "  transaction_date timestamp not null default now()" +
                    ");");

        } catch (SQLException e){
            e.printStackTrace();
        }
        return con;
    }
}
