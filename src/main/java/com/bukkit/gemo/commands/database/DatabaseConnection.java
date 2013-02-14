package com.bukkit.gemo.commands.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

   private Connection con = null;


   public DatabaseConnection(String host, int port, String database, String userName, String password) {
      try {
         Class.forName("com.mysql.jdbc.Driver");
         this.con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, userName, password);
      } catch (Exception var7) {
         var7.printStackTrace();
      }

      userName = null;
      password = null;
      System.gc();
   }

   public DatabaseConnection(String folder, String databaseName) {
      try {
         Class.forName("org.sqlite.JDBC");
         this.con = DriverManager.getConnection("jdbc:sqlite:" + folder + "/" + databaseName + ".db");
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public Connection getConnection() {
      return this.con;
   }

   public boolean hasConnection() {
      return this.con != null;
   }

   public void closeConnection() {
      try {
         this.con.close();
         this.con = null;
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }
}
