package Server;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Andrea
 */
public class MySQL {
    
    private Connection con;
    private Statement stmt;

    //Connect to database
    public int Connect(String Server, String UserName, String Password) throws Exception{
        try{
            // This will load the MySQL driver
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(Server, UserName, Password);
            stmt= (Statement) con.createStatement();
        }catch(SQLException e)
        {

            if (e!=null){
                DebugLog mylog = new DebugLog();
                mylog.AppendToLog(e.getMessage());
                return 1;
            }
         }        
        return 0;
    }    
    //Execute Query
    public ResultSet ExecuteQuery(String QueryString) throws Exception{
        ResultSet result = stmt.executeQuery(QueryString);
        return result;
    }
    
    public void ExecuteUpdate(String SQL) throws Exception{
        try{
            int status = stmt.executeUpdate(SQL);
        }catch(SQLException e)
        {
            if (e!=null){
                DebugLog mylog = new DebugLog();
                mylog.AppendToLog(e.getMessage());
            }
        }
    } 
    // Insert new user
    public void InsertUser(String userID,String Name, String SurName, String Password) throws Exception{
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate localDate = LocalDate.now();
        stmt.executeUpdate("INSERT INTO `users` (`ID`, `Name`, `Surname`, `Password`, `CreationDate`, `LastSeen`) VALUES ('" + userID + "', '" + Name + "', '" + SurName + "', '" + Password + "', '" + localDate + "', NULL)");
    }
    
    // Remove User
    public void DeleteUser(String User_ID) throws Exception{
        stmt.executeUpdate("DELETE FROM `users` WHERE `ID`=" + User_ID);
    }
    
    //Print Table to Consol
    public void Print_MySQL_Table(ResultSet result_to_print) throws Exception
    {
        try{
            //Recupera il numero di colonne da stampare
            int ColCount=result_to_print.getMetaData().getColumnCount();
            int[] ColSize = new int[ColCount+1];
            String Columns = "|";
            for  (int i = 1; i<= (ColCount); i++){
                //Recupera la lunghezza della colonna
                ColSize[i]=result_to_print.getMetaData().getColumnDisplaySize(i);
                String ColName=String.format("%" + ColSize[i] + "." + ColSize[i] +"s|", result_to_print.getMetaData().getColumnName(i));
                Columns = Columns + ColName;
            }
            System.out.println(Columns);
            while (result_to_print.next()){
                    //da sistemare
                    String DatiRiga="|";
                    for  (int i = 1; i<= (ColCount); i++){
                        String FormatoColonna = "%" + ColSize[i] + "s|";
                        String DatoColonna = result_to_print.getString(i);
                        DatiRiga = DatiRiga + String.format(FormatoColonna, DatoColonna);
                    }
                    System.out.println(DatiRiga);
            }

        }catch(SQLException e)
        {
            throw e;    
        }
      
    }
}
