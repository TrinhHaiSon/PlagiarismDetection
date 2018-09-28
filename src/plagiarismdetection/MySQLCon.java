/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plagiarismdetection;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author trinhhaison
 */
public class MySQLCon {
    private Connection con;
//    private Statement stmt;
    
    
    public MySQLCon() throws ClassNotFoundException, SQLException{
        Class.forName("com.mysql.jdbc.Driver");  
        con=DriverManager.getConnection("jdbc:mysql://localhost:3360/PlagiarismDetection?characterEncoding=utf8","root","");    
//        stmt = con.createStatement();  
    }
    
    public void close() throws SQLException{
        con.close();
    }
    
    public static String safeTransfer(String str){
        
        String result;
        
        
        result = str.replaceAll("\\\\", "\\\\\\\\");
        result = result.replaceAll("'", "\\\\\'");
        
        
        return result;
    }
    
//    public String vietnameseQuery(String query) throws UnsupportedEncodingException{
////        String result = new String(query.getBytes(), "iso-8859-1");
//        String result = query;
//        return result;
//    }
    
    public int checkWordInDatabase(String word) throws SQLException{
        
        String safeWord = safeTransfer(word);
        
        PreparedStatement statement = con.prepareStatement("Select id from Vocabulary where word LIKE '"+safeWord+"'");
        ResultSet result = statement.executeQuery();
        if(result.next() == true){
            return result.getInt("id");
        }
        statement.close();
        return -1;
    }
    
    public void addWord(String word) throws SQLException{
        
        String safeWord = safeTransfer(word);
        
        PreparedStatement statement = con.prepareStatement("Insert into Vocabulary(word, idf) value('"+safeWord+"', "+1+")");
        statement.execute();
        statement.close();
    }
    
    public void addWord(String word, int idf) throws SQLException{
        
        String safeWord = safeTransfer(word);
        
        PreparedStatement statement = con.prepareStatement("Insert into Vocabulary(word, idf) value('"+safeWord+"', "+idf+")");
        statement.execute();
        statement.close();
    }
    
    public void addFrequency(int id) throws SQLException{
        PreparedStatement statement = con.prepareStatement("Update Vocabulary set idf = (idf + 1) where id = "+id);
        statement.execute();
        statement.close();
    }
    
    public void calculateIdf(int fileNumber) throws SQLException{
        PreparedStatement statement = con.prepareStatement("Update Vocabulary set idf = LOG10("+fileNumber+" / idf)");
        statement.execute();
        statement.close();
    }
}
