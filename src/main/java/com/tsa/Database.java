package com.tsa;

import java.sql.*;

public class Database {
    static final String URL = "jdbc:mysql://localhost/masterdb";
    static final String USERNAME = "root";
    static final String PASSWORD = "Aa1.Aa1.";

    Connection connection;

    public Database() {
        this.connect();   
    }

    private void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("successfully connected");
        } catch (Exception e) {
            this.connection = null;
            System.out.println("could not connect");
        }
    }

    public int getUser(String username, String password) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM users WHERE (username = ? and passwrd = ?);"
            );

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet results = preparedStatement.executeQuery();
            
            if (results.next()) {
                return results.getInt("id");
            } else {
                return -1;
            }
        } catch (Exception e) {
            return -1;
        }
    }

    public int createUser(String username, String password) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO users (username, passwrd) VALUES (?, ?);",
                Statement.RETURN_GENERATED_KEYS
            );

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            int affected = preparedStatement.executeUpdate();

            if (affected < 1) {
                return -1;
            }

            ResultSet user = preparedStatement.getGeneratedKeys();

            if (user.next()) {
                return user.getInt(1);
            } else {
                return -1;
            }

        } catch (Exception e) {
            System.out.println(e);
            return -1;
        }
    }
}