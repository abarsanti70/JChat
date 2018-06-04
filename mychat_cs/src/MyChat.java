package Server;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Server.MySQL;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;

/**
 *
 * @author Andrea
 */
public class MyChat {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        
        BufferedReader br = null;
        try {
            MySQL db = new MySQL();
            db.Connect("jdbc:mysql://192.168.178.35/janchat?verifyServerCertificate=false&useSSL=true", "root", "password");
            
            br = new BufferedReader(new InputStreamReader(System.in));

            while (true) {

                System.out.println("1. Print Users Table");
                System.out.println("2. Insert New User");
                System.out.println("3. Delete User");
                System.out.println("4. Login User");
                System.out.println("5. New Login User");
                System.out.println("q. Quit");
                String input = br.readLine();

                if ("q".equals(input)) {
                    System.out.println("Exit!");
                    System.exit(0);
                }
                if ("1".equals(input)){
                    ResultSet result = db.ExecuteQuery("SELECT * FROM users");
                    db.Print_MySQL_Table(result);
                }
                if ("2".equals(input)){
                    //INSERT user
                    System.out.println("UserID : ");
                    String UserID = br.readLine();
                    System.out.println("Nome : ");
                    String Name = br.readLine();
                    System.out.println("Surname : ");
                    String Surname = br.readLine();
                    System.out.println("Password : ");
                    String Pwd = br.readLine();                    
                    db.InsertUser(UserID, Name, Surname, Pwd);
                }
                if ("3".equals(input)){
                    //DELETE USER
                    System.out.println("ID to delete : ");
                    String usrID = br.readLine();
                    db.DeleteUser(usrID);
                }
                
                if ("4".equals(input)){
                    //LOGIN USER
                    System.out.println("User ID : ");
                    String usrID = br.readLine();
                    System.out.println("Password : ");
                    String pwd = br.readLine();
                    //SELECT * FROM `users` WHERE `ID` = 'abarsanti' AND `Password` = 'abc'
                    ResultSet result = db.ExecuteQuery("SELECT COUNT(*) FROM `users` WHERE `ID`='" + usrID + "' AND `Password`='" + pwd + "'");
                    String DatiRiga ="";
                    while (result.next()){
                        int rowCount = Integer.parseInt(result.getString(1));
                        if (rowCount>0){
                            System.out.println("User Autenticated :)");
                        }else{
                            System.out.println("User or password not correct !!!!");                        
                        }
                    }                    
                }
                if ("5".equals(input)){
                    //NEW LOGIN
                    System.out.println("User ID : ");
                    String usrID = br.readLine();
                    System.out.println("Password : ");
                    String pwd = br.readLine();
                    ResultSet result = db.ExecuteQuery("SELECT * FROM `users` WHERE `ID`='" + usrID + "' AND `Password`='" + pwd + "'");
                    if (result.next()){
                        System.out.println("User Autenticated :)");                        
                    }else{
                        System.out.println("User or password not correct !!!!");
                    }
                }
            }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
 }
