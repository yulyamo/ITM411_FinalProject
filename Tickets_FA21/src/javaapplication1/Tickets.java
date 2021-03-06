
/**
 * Student: Yulia Moiseyenko
 * 12/08/2021
 * Help Desk System Project
 */
package javaapplication1;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;

@SuppressWarnings("serial")
public class Tickets extends JFrame implements ActionListener {

  // class level member objects
  Dao dao = new Dao(); // for CRUD operations
  Boolean chkIfAdmin = false;

  //create custom colors
  Color dark_green = new Color(0, 102, 0);
  Color light_green = new Color(214, 253, 214);
  Color very_light_green = new Color(245, 255, 245);

  public Tickets(Boolean isAdmin) {
	  
	  if (isAdmin != chkIfAdmin) {
      System.out.println("Logged in as Admin.");
      // show the tables at startup
      try {

        // Use JTable built in functionality to build a table model and
        // display the table model off your result set!!!
        JTable jt = new JTable(ticketsJTable.buildTableModel(dao.readRecords()));
        jt.setBounds(30, 40, 200, 400);
        jt.setBackground(light_green);
        jt.getTableHeader().setBackground(dark_green);
        jt.getTableHeader().setForeground(Color.white);
        JScrollPane sp = new JScrollPane(jt);
        add(sp);
        setVisible(true); // refreshes or repaints frame on screen
        System.out.println("Ticket view created.");
        chkIfAdmin = true;
      } catch (SQLException e1) {
        System.out.println("Ticket view failed.");
        e1.printStackTrace();
      }
    } else {
      System.out.println("Logged in as User.");
    }

    chkIfAdmin = isAdmin;
    createMenu();
    prepareGUI();

  }

  // Main menu object items
  private JMenu mnuFile = new JMenu("File");
  private JMenu mnuAdmin = new JMenu("Admin");
  private JMenu mnuTickets = new JMenu("Tickets");

  // Sub menu item objects for all Main menu item objects
  JMenuItem mnuItemExit;//exit
  JMenuItem mnuItemUpdate;//update
  JMenuItem mnuItemDelete; //delete
  JMenuItem mnuItemOpenTicket; //open new ticket
  JMenuItem mnuItemViewTicket; //view tickets
  JMenuItem mnuItemSelectTicket; //select a ticket

  private void createMenu() {

    /* Initialize sub menu items **************************************/

    // initialize sub menu item for File main menu
    mnuItemExit = new JMenuItem("Exit");
    // add to File main menu item
    mnuFile.add(mnuItemExit);

    if (chkIfAdmin == true) {
      // initialize first sub menu items for Admin main menu
      mnuItemUpdate = new JMenuItem("Update Ticket");
      // add to Admin main menu item
      mnuAdmin.add(mnuItemUpdate);

      // initialize second sub menu items for Admin main menu
      mnuItemDelete = new JMenuItem("Delete Ticket");
      // add to Admin main menu item
      mnuAdmin.add(mnuItemDelete);
    }

    // initialize first sub menu item for Tickets main menu
    mnuItemOpenTicket = new JMenuItem("Open Ticket");
    // add to Ticket Main menu item
    mnuTickets.add(mnuItemOpenTicket);

    // initialize second sub menu item for Tickets main menu
    mnuItemViewTicket = new JMenuItem("View Ticket");
    // add to Ticket Main menu item
    mnuTickets.add(mnuItemViewTicket);

    // initialize any more desired sub menu items below
    // initialize third sub menu item for Tickets main menu
    mnuItemSelectTicket = new JMenuItem("Select Ticket");
    // add to Ticket Main menu item
    mnuTickets.add(mnuItemSelectTicket);

    //adding action listeners for each item
    mnuItemExit.addActionListener(this);
    //mnuItemRefresh.addActionListener(this);
    if (chkIfAdmin == true) { //on show these on admin
      mnuItemUpdate.addActionListener(this);
      mnuItemDelete.addActionListener(this);
    }
    mnuItemOpenTicket.addActionListener(this);
    mnuItemViewTicket.addActionListener(this);
    mnuItemSelectTicket.addActionListener(this);

  }

  private void prepareGUI() {
    // create JMenu bar
    JMenuBar bar = new JMenuBar();
    bar.add(mnuFile); // add main menu items in order, to JMenuBar
    if (chkIfAdmin == true) { //only show this to admin
      bar.add(mnuAdmin);
    }
    bar.add(mnuTickets); //show to regular users
    // add menu bar components to frame
    setJMenuBar(bar);

    addWindowListener(new WindowAdapter() {
      // define a window close operation
      public void windowClosing(WindowEvent wE) {
        System.exit(0);
      }
    });
    // set frame options
    Color very_light_green = new Color(245, 255, 245);
    setSize(800, 400);
    getContentPane().setBackground(very_light_green);
    setLocationRelativeTo(null);
    setVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    //set background colors for message boxes
    UIManager.put("OptionPane.background", (very_light_green));
    UIManager.put("Panel.background", (very_light_green));

    // implement actions for sub menu items
    if (e.getSource() == mnuItemExit) {
      System.out.println("Ticket system exited.");
      System.exit(0);
    } 
    //if Open ticket option selected
    else if (e.getSource() == mnuItemOpenTicket) {
      // get ticket information
      String ticketName = JOptionPane.showInputDialog(null, "Enter your name");
      String ticketDesc = JOptionPane.showInputDialog(null, "Enter a ticket description");
      if (ticketName == null || (ticketName != null && ("".equals(ticketName))) || ticketDesc == null || (ticketDesc != null && ("".equals(ticketDesc)))) {
        JOptionPane.showMessageDialog(null, "Ticket creation failed! Reason: empty name or description.");
        System.out.println("Ticket creation failed! Reason: empty name or description.");
      } else {
    	 
        // insert ticket information to database
        int id = dao.insertRecords(ticketName, ticketDesc);

        // display results if a ticket was created
        if (id != 0) {
          System.out.println("Ticket ID : " + id + " created!");
          JOptionPane.showMessageDialog(null, "Ticket ID: " + id + " has been created");

          try {

            JTable jt = new JTable(ticketsJTable.buildTableModel(dao.readRecords()));
            jt.setBounds(30, 40, 200, 400);
            jt.setBackground(light_green);
            jt.getTableHeader().setBackground(dark_green);
            jt.getTableHeader().setForeground(Color.white);
            JScrollPane sp = new JScrollPane(jt);
            add(sp);
            setVisible(true);
            System.out.println("Ticket view created.");
          } catch (SQLException e1) {
            System.out.println("Ticket view failed.");
            e1.printStackTrace();
          }
        } else
          System.out.println("Ticket creation failed.");
      }
    } 
    //if View Tickets option selected
    else if (e.getSource() == mnuItemViewTicket) {

      // retrieve all tickets details for viewing in JTable
      try {

        JTable jt = new JTable(ticketsJTable.buildTableModel(dao.readRecords()));
        jt.setBounds(30, 40, 200, 400);
        jt.setBackground(light_green);
        jt.getTableHeader().setBackground(dark_green);
        jt.getTableHeader().setForeground(Color.white);
        JScrollPane sp = new JScrollPane(jt);
        add(sp);
        setVisible(true); // refreshes or repaints frame on screen
        System.out.println("Ticket view created.");
      } catch (SQLException e1) {
        System.out.println("Ticket view failed.");
        e1.printStackTrace();
      }
    }
    //select specific ticket to view
    else if (e.getSource() == mnuItemSelectTicket) {

      String ticketId = JOptionPane.showInputDialog(null, "Enter the ticket ID");

      if (ticketId == null || (ticketId != null && ("".equals(ticketId)))) {
        JOptionPane.showMessageDialog(null, "Ticket view failed: empty id.");
        System.out.println("Ticket view failed: empty id.");
      } else {
        // retrieve tickets details for viewing in JTable
        int tid = Integer.parseInt(ticketId);
        try {

          // Use JTable built in functionality to build a table model and
          // display the table model off your result set!!!
          JTable jt = new JTable(ticketsJTable.buildTableModel(dao.selectRecords(tid)));
          JFrame f2 = new JFrame("Select Ticket");
          jt.setBounds(30, 40, 200, 400);
          jt.setBackground(light_green);
          jt.getTableHeader().setBackground(dark_green);
          jt.getTableHeader().setForeground(Color.white);
          JScrollPane sp = new JScrollPane(jt);
          f2.add(sp);
          f2.setSize(400, 500);
          f2.setVisible(true);
          f2.setLocationRelativeTo(null);
          System.out.println("Ticket view created.");
        } catch (SQLException e1) {
          System.out.println("Ticket view failed.");
          e1.printStackTrace();
        }
      }
    }
    //if Update option selected
    else if (e.getSource() == mnuItemUpdate) {
      // get ticket information
      String ticketId = JOptionPane.showInputDialog(null, "Enter ticket ID to update");
      String ticketDesc = JOptionPane.showInputDialog(null, "Enter a new description");
      String ticketStatus = JOptionPane.showInputDialog(null, "Update the ticket status");

      if (ticketId == null || (ticketId != null && ("".equals(ticketId))) || ticketStatus == null || (ticketStatus != null && ("".equals(ticketStatus)))) {
        JOptionPane.showMessageDialog(null, "Ticket update failed: empty id / status.");
        System.out.println("Ticket update failed: empty id / status.");
      } else {
        // insert ticket information to database
        int tid = Integer.parseInt(ticketId);

        //go to dao.java to update records
        dao.updateRecords(ticketId, ticketDesc, ticketStatus);

        // display results on the console and GUI
        if (tid != 0) {
          System.out.println("Ticket ID : " + tid + " has been updated!");
          JOptionPane.showMessageDialog(null, "Ticket id: " + tid + " updated!");
        } else
          System.out.println("Ticket update failed.");

        try {

          JTable jt = new JTable(ticketsJTable.buildTableModel(dao.selectRecords(tid)));
          JFrame f3 = new JFrame("Updated Ticket");
          jt.setBounds(30, 40, 200, 400);
          jt.setBackground(light_green);
          jt.getTableHeader().setBackground(dark_green);
          jt.getTableHeader().setForeground(Color.white);
          JScrollPane sp = new JScrollPane(jt);
          f3.add(sp);
          f3.setSize(500, 600);
          f3.setVisible(true); // refreshes or repaints frame on screen
          f3.setLocationRelativeTo(null);
          System.out.println("Ticket view created.");
        } catch (SQLException e1) {
          System.out.println("Ticket view failed.");
          e1.printStackTrace();
        }

      }
    } else if (e.getSource() == mnuItemDelete) {
      // get ticket information
      String ticketId = JOptionPane.showInputDialog(null, "Enter ticket ID to delete");

      if (ticketId == null || (ticketId != null && ("".equals(ticketId)))) {
        JOptionPane.showMessageDialog(null, "Ticket deletion failed: empty ticket ID.");
        System.out.println("Ticket deletion failed: empty ticket ID.");
      } else {
        // check ticket information to database
        int tid = Integer.parseInt(ticketId);

        int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete ticket " + tid + "?", "Warning!", JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
          int id = dao.deleteRecords(tid);

          // display results if successful or not to console / dialog box
          if (id != 0) {
            System.out.println("Ticket ID : " + id + " has been deleted.");
            JOptionPane.showMessageDialog(null, "Ticket ID: " + id + " deleted!");
          } else
            System.out.println("Ticket cannot be deleted!");
        } else {
          JOptionPane.showMessageDialog(null, "Ticket " + tid + " was not deleted.");
        }
      }
    }

  }

}