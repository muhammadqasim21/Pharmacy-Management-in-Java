
import dao.Connectionprovider;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Qasim
 */
public class saless {
   private String medname;
   private String quantity;
   private String totalmedsold;
   
   public saless()
   {
       medname=null;
       quantity=null;
       totalmedsold=null;
   }
   public saless(String name,String quantity, String totalmed)
   {
       medname=name;
       this.quantity=quantity;
       totalmedsold=totalmed;
   }
   public double generateSalesReport(DefaultTableModel model)
   {
     try
        {
            
             
            Connection con=Connectionprovider.getCon();
            double total = 0;
            
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select medname,SUM(quantity) as TotalSold from prescription group by medname");
            model.setRowCount(0);
            while (rs.next()) {
                model.addRow(new Object[]{rs.getString("medname"), rs.getString("TotalSold")});
                 total += rs.getDouble("TotalSold");
            }
            return total;
        }
         catch(Exception e)
         {
             JOptionPane.showMessageDialog(null, e);
         }
     return 0;
   }
    
}
