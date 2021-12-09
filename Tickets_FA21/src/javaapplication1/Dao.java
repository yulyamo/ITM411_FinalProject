/**
 * Student: Yulia Moiseyenko
 * 12/08/2021
 * Help Desk System Project
 */
package javaapplication1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class Dao {
  // instance fields
  static Connection connect = null;
  Statement statement = null;

  public Connection getConnection() {
    // Setup the connection with the DB
    try {
      connect = DriverManager
        .getConnection("jdbc:mysql://www.papademas.net:3307/tickets?autoReconnect=true&useSSL=false" +
          "&user=fp411&password=411");
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return connect;
  }

  // CRUD implementation

  public void createTables() {
    // variables for SQL Query table creations
    final String createTicketsTable = "CREATE TABLE yumois_tickets(ticket_id INT AUTO_INCREMENT PRIMARY KEY, ticket_issuer VARCHAR(30), ticket_description VARCHAR(200), opened VARCHAR(100), status VARCHAR(100))";
    final String createUsersTable = "CREATE TABLE ymois_users(uid INT AUTO_INCREMENT PRIMARY KEY, uname VARCHAR(30), upass VARCHAR(30), admin int)";

    try {

      // execute queries to create tables
      statement = getConnection().createStatement();//get connected
      statement.executeUpdate(createTicketsTable);
      statement.executeUpdate(createUsersTable);
      System.out.println("Created tables in given database...");
      // end create table
      
      // close connection/statement object
      statement.close();
      connect.close();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    // call add users to user table
    addUsers();
  }

  public void addUsers() {
    // variables for SQL Query inserts
    String sql;
    Statement statement;
    BufferedReader br;
    List < List < String >> array = new ArrayList < > (); // list to hold (rows & cols)

    // read data from file with users and passwords
    try {
      br = new BufferedReader(new FileReader(new File("./userlist.csv")));
      //declare variable
      String row;
      //use loop to add all users and passwords to users table in DB
      while ((row = br.readLine()) != null) {
        array.add(Arrays.asList(row.split(",")));
      }
    } catch (Exception e) {
      System.out.println("There is a problem. File cannot be loaded. ");
    }

    try {
      // Setup the connection with the DB
      statement = getConnection().createStatement();

      // create loop to grab each array index containing a list of values and insert into user table
      for (List < String > rowData: array) {
        sql = "insert into ymois_users(uname,upass,admin) " + "values('" + rowData.get(0) + "'," + " '" +
        rowData.get(1) + "','" + rowData.get(2) + "');";
        statement.executeUpdate(sql);
      }
      System.out.println("Inserts into the table completed!");
      // close statement object
      statement.close();

    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  public int insertRecords(String ticketName, String ticketDesc) {
    int id = 0;
    try {
      statement = getConnection().createStatement();
      String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
      statement.executeUpdate("Insert into yumois_tickets (ticket_issuer, ticket_description, opened, status) values(" + " '" + ticketName + "','" + ticketDesc + "','" + timeStamp + "','" + "OPEN" + "')", Statement.RETURN_GENERATED_KEYS);

      // retrieve ticket id number newly auto generated upon record insertion
      ResultSet resultSet = null;
      resultSet = statement.getGeneratedKeys();
      if (resultSet.next()) {
        // retrieve first field in table
        id = resultSet.getInt(1);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return id;
  }

  //READ records in the table
  public ResultSet readRecords() {
	
    ResultSet results = null;
    try {
      statement = getConnection().createStatement();
      results = statement.executeQuery("SELECT * FROM yumois_tickets");
      //connect.close();
    } catch (SQLException e1) {
      e1.printStackTrace();
    }
    return results;
  }

  //VIEW a RECORD
  public ResultSet selectRecords(int tid) {
    ResultSet results = null;
    try {
      statement = getConnection().createStatement();
      results = statement.executeQuery("SELECT * FROM yumois_tickets WHERE ticket_id = " + tid);
      //connect.close();
    } catch (SQLException e1) {
      e1.printStackTrace();
    }
    return results;
  }
  
  //UPDATE
  public void updateRecords(String tid, String desc, String status) {
  
    try {
      Statement stmt = getConnection().createStatement();
      ResultSet rs = stmt.executeQuery("SELECT ticket_description FROM yumois_tickets WHERE ticket_id = " + tid);
      //connect.close();
      String results = null;
      while (rs.next()) {
        results = rs.getString("ticket_description");
      }

      PreparedStatement ps = connect.prepareStatement("UPDATE yumois_tickets SET ticket_description = ?, status = ? WHERE ticket_id = ?");
      // set the prepared statement parameters
      ps.setString(1, desc);
      ps.setString(2, status);
      ps.setString(3, tid);
      ps.executeUpdate();
      ps.close();

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  //DELETE
  public int deleteRecords(int tid) {
    try {
      statement = getConnection().createStatement();
      String sql = "DELETE FROM yumois_tickets WHERE ticket_id = " + tid;
      statement.executeUpdate(sql);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return tid;
  }
}