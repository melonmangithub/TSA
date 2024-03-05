package com.tsa;
import java.sql.*;

public class Database {

        private Connection conn;
        private PreparedStatement pst;

        public Database() {
            //default constructor

        }

        public void Connect() {
            String url = "jdbc:mysql://localhost:3306/number9";
            String user = "root";
            String password = "Irine2012";

            try {
                conn = DriverManager.getConnection(url, user, password);
                System.out.println("connected");
            } catch (SQLException e) {
                System.out.println("didnt connect");
                throw new RuntimeException(e);
            }

        }

        public void Disconnect() {
            try {
                conn.close();
                System.out.println("disconnected");

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }



}
