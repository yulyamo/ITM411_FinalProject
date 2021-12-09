/**
 * Student: Yulia Moiseyenko
 * 12/08/2021
 * Help Desk System Project
 */
package javaapplication1;

import java.awt.Color;
import java.awt.GridLayout; //useful for layouts
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
//controls-label text fields, button
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class Login extends JFrame {

  Dao conn;

  public Login() {

    super("IIT HELP DESK LOGIN");
    conn = new Dao();
    conn.createTables();
    conn.addUsers();
    setSize(500, 270);
    setLayout(new GridLayout(5, 2));
    setLocationRelativeTo(null); // centers window

    //set custom background color
    Color very_light_green = new Color(234, 254, 234);
    getContentPane().setBackground(very_light_green);

    //set borders
    ((JComponent) getContentPane()).setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.green), BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder())));

    // SET UP CONTROLS
    JLabel lblUsername = new JLabel("Username", JLabel.LEFT);
    JLabel lblPassword = new JLabel("Password", JLabel.LEFT);
    JLabel lblStatus = new JLabel(" ", JLabel.CENTER);

    //create text field
    JTextField txtUname = new JTextField(10);

    JPasswordField txtPassword = new JPasswordField();
    JButton btn = new JButton("Submit");
    JButton btnExit = new JButton("Exit");

    JLabel l1 = new JLabel("PLEASE ENTER ", JLabel.RIGHT);
    JLabel l2 = new JLabel("YOU CREDENTIALS", JLabel.LEFT);

    // constraints
    lblStatus.setToolTipText("Contact help desk to unlock password");
    lblUsername.setHorizontalAlignment(JLabel.CENTER);
    lblPassword.setHorizontalAlignment(JLabel.CENTER);

    // ADD OBJECTS TO FRAME
    add(l1); //title row
    add(l2);
    add(lblUsername); // 1st row filler
    add(txtUname);
    add(lblPassword); // 2nd row
    add(txtPassword);
    add(btn); // 3rd row
    add(btnExit);
    add(lblStatus); // 4th row

    btn.addActionListener(new ActionListener() {
      int count = 0; // count agent

      @Override
      public void actionPerformed(ActionEvent e) {
        boolean admin = false;
        count = count + 1;
        // verify credentials of user

        String query = "SELECT * FROM ymois_users WHERE uname = ? and upass = ?;";
        try (PreparedStatement stmt = conn.getConnection().prepareStatement(query)) {
          stmt.setString(1, txtUname.getText());
          stmt.setString(2, txtPassword.getText());
          ResultSet rs = stmt.executeQuery();
          if (rs.next()) {
            admin = rs.getBoolean("admin"); // get table column value
            new Tickets(admin); //open Tickets file / GUI interface
            setVisible(false); // HIDE THE FRAME
            dispose(); // CLOSE OUT THE WINDOW
          } else
            lblStatus.setText("Try again! " + (3 - count) + " / 3 attempt(s) left");
        } catch (SQLException ex) {
          ex.printStackTrace();
        }

      }
    });
    btnExit.addActionListener(e -> System.exit(0)); //set exit

    setVisible(true); // SHOW THE FRAME
  }

  public static void main(String[] args) {

    new Login();
  }
}