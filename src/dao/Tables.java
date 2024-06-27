/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.Connection;
import java.sql.Statement;

/**
 *
 * @author Qasim
 */
public class Tables {
    public static void main(String[] args)
    {
        try
        {
            Connection con=Connectionprovider.getCon();
            Statement st= con.createStatement();
            
            
        }
        catch(Exception e)
        {
            
        }
    }
}
