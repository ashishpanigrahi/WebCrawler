/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webcrawler.bal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 *
 * @author AshishPanigrahi
 */
public class DB {

    public Connection conn = null;
    public static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";
    public DB() {
        try {
            Class.forName(DB.DRIVER_CLASS);
            String url = "jdbc:mysql://localhost:3306/Crawler?useSSL=false";
            conn = DriverManager.getConnection(url, "root", "Sap@2015");
            System.out.println("Database Connection Successful");

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ResultSet runSql(String sql) throws SQLException {
        Statement sta = conn.createStatement();
        return sta.executeQuery(sql);
    }

    public boolean runSql2(String sql) throws SQLException {
        Statement sta = conn.createStatement();
        return sta.execute(sql);
    }

    @Override
    protected void finalize() throws Throwable {
        if (conn != null || !conn.isClosed()) {
            conn.close();
        }
    }
}
