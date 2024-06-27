
import dao.Connectionprovider;
import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Qasim
 */
public class Pharmacist {
    private List<Patient> patientList;
    //private List<medicines> med;
    public Pharmacist() {
        this.patientList = new ArrayList<>();
    }

    // Method to add a new patient
    public int addPatient(Patient patient) {
        String patientid=patient.getpatientid();
        String patientname=patient.getPatientname();
        String contact= patient.getContact();
        String allergies= patient.getAllergies();
        if(patientid.equals(""))
        {
            JOptionPane.showMessageDialog(null, "ID is required");
        }
        else if(patientname.equals(""))
        {
            JOptionPane.showMessageDialog(null, "Name is required");
        }
        else if(contact.equals(""))
        {
            JOptionPane.showMessageDialog(null, "Contact no. is required");
        }
        else if(contact.length()!=11)
        {
            JOptionPane.showMessageDialog(null, "Invalid phone number");
        }
        else
        {
            try
            {
             //   pharmacist.addPatient(newPatient);
                Connection  con= Connectionprovider.getCon();
                PreparedStatement ps= con.prepareStatement("insert into patient (patientid,name,contact,allergies) values(?,?,?,?)");
                ps.setString(1, patientid);
                ps.setString(2, patientname);
                ps.setString(3, contact);
                ps.setString(4, allergies);
                ps.executeUpdate();
                if(patient.getMedicalRecords()==null)
                {
                    addmedicalrecord(patient,patientid);
                  
                }
                patientList.add(patient);
                
               
                return 1;

            }
            catch(Exception e)
            {
                JOptionPane.showMessageDialog(null, e);
            }
            
        }
        return 0;
    }
    
    private boolean isNumeric(String str) 
    {
        if (str == null) {
            return false;
        }
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    public int login(String username,String password)
    {
        int flag=0;
        try
        {
            Connection  con= Connectionprovider.getCon();
            Statement st=con.createStatement();
            ResultSet rs= st.executeQuery("Select * from pharmacist where username='"+username+"' and password='"+password+"'");
            while(rs.next())
            {
               flag=1;
               return 1;
            }
            if(flag==0)
            {
               return 0;
            }
        }
        catch(Exception e)
        {
           JOptionPane.showMessageDialog(null, e);
        }
        return 0;
    }
    
    public void viewpatients(DefaultTableModel model)
    {
        
        try {
            Connection con = Connectionprovider.getCon();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from patient");
            while (rs.next()) {
                model.addRow(new Object[]{rs.getString("patientid"), rs.getString("name"), rs.getString("contact"), rs.getString("allergies")});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    public int deletepatient(int row, TableModel model)
    {
        String id= model.getValueAt(row,0).toString();
        int a= JOptionPane.showConfirmDialog(null, "Do you want to delete this patient?", "Select",JOptionPane.YES_NO_OPTION);
        if(a==0)
        {
            try
            {
                Connection con=Connectionprovider.getCon();
                PreparedStatement ps=con.prepareStatement("Delete from patient where patientid=?");
                ps.setString(1,id);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Patient Succesfully Deleted");
                return 1;
            }
            catch (Exception e)
            {
                JOptionPane.showMessageDialog(null, e);
            }
        }
        return 0;
    }
    
    public void addmedicalrecord(Patient patient,String patientid)
    {
        int option = JOptionPane.showConfirmDialog(null, "Do you want to add a medical record for this patient?", "Add Medical Record", JOptionPane.YES_NO_OPTION);
            
                    if (option == JOptionPane.YES_OPTION) 
                    {
                        // Prompt the pharmacist to enter medical record details
                        String testName,testDate,results;
                        int flag=0;
                        do {
                                testName = JOptionPane.showInputDialog("Enter the test name:");

                                if (testName == null || testName.trim().equals("")) {
                                    JOptionPane.showMessageDialog(null, "Test name is required");
                                }
                            } while (testName == null || testName.trim().equals(""));
                        do {
                                flag=0;
                                testDate = JOptionPane.showInputDialog("Enter the test date(DD-MM-YYYY):");

                                if (testDate == null || testDate.trim().equals("")) {
                                    JOptionPane.showMessageDialog(null, "Test date is required");
                                }
                                else
                                {
                                    String[] dateParts = testDate.split("-");
                                    if (dateParts.length != 3 ||!isNumeric(dateParts[0]) || !isNumeric(dateParts[1]) || !isNumeric(dateParts[2])) 
                                    {
                                        JOptionPane.showMessageDialog(null, "Invalid Date Format. Please use DD-MM-YYYY format");
                                        flag=1;
                                    }
                                }
                            } while (testDate == null || testDate.trim().equals("")|| flag==1);
                        do {
                                results = JOptionPane.showInputDialog("Enter the test result:");

                                if (results == null || results.trim().equals("")) {
                                    JOptionPane.showMessageDialog(null, "Test result is required");
                                }
                            } while (results == null || results.trim().equals(""));
                       
                        
                        // Create a new medical record
                        medicalrecords medicalRecord = new medicalrecords(patient, testName, testDate, results);

                        // Set the medical record for the patient
                        patient.setMedicalRecord(medicalRecord);
                        
                        // storing medical records in database
                        try
                        {
                            Connection con = Connectionprovider.getCon();
                            PreparedStatement ps;
                            ps=con.prepareStatement("insert into medicalrecords (patientid,test_name,test_date,test_result) values(?,?,?,?)");
                            ps.setString(1, patientid);
                            ps.setString(2, testName);
                            ps.setString(3, testDate);
                            ps.setString(4, results);
                            ps.executeUpdate();
                            JOptionPane.showMessageDialog(null, "Medical record added successfully.");
                        }
                        catch(Exception e)
                        {
                            JOptionPane.showMessageDialog(null, e);

                        }
                        

                        

                        
                    }
    }
    
    public int searchpatient(Patient patient)
    {
        String id=patient.getpatientid();
        
        try{
            Connection  con= Connectionprovider.getCon();
            Statement st=con.createStatement();
            ResultSet rs= st.executeQuery("Select * from patient where patientid='"+id+"'");
                if(rs.next())
                {
                   
                   return 1;
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "Patient not found");
                    return 0;
                }
                
            }
            catch(Exception e)
            {
                JOptionPane.showMessageDialog(null,e);
            }
        return 0;
    }
    public int updatepatient(Patient patient)
    {
        String id=patient.getpatientid();
        String name=patient.getPatientname();
        String contact=patient.getContact();
        String allergies=patient.getAllergies();
        if(name.equals(""))
        {
             JOptionPane.showMessageDialog(null, "Patient name is required");
             return 0;
        }
        else if(contact.equals(""))
        {
             JOptionPane.showMessageDialog(null, "Patient contact is required");
             return 0;
        }
        else
        {
            try
            {
                Connection con= Connectionprovider.getCon();
                PreparedStatement ps = con.prepareStatement("UPDATE patient SET name=?, contact=?, allergies=? WHERE patientid = ?");
                ps.setString(1, name);   // Corrected index
                ps.setString(2, contact);
                ps.setString(3, allergies);
                ps.setString(4, id); 
                int rowsAffected = ps.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Patient information updated successfully");
                    return 1;
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to update patient information");
                    return 0;
                }
            }
            catch(Exception e)
            {
                JOptionPane.showMessageDialog(null, e);
                return 0;
            }
            
        }
                   
    }

    public void viewspecificuser(Patient pat, String[] name, String[] contact, String[] allergies) 
    {
        String id=pat.getpatientid();
        String username,usercontact,userallergies;
        try{
            Connection con=Connectionprovider.getCon();
            Statement st=con.createStatement();
            ResultSet rs= st.executeQuery("select * from patient where patientid='" + id + "'");
            while(rs.next())
            {
                username=rs.getString("name");
                usercontact=rs.getString("contact");
                userallergies=rs.getString("allergies");
                name[0]=username;
                contact[0]=usercontact;
                allergies[0]=userallergies;
            }
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void deleterecord(Patient pat) 
    {
       String id=pat.getpatientid();
       try
       {
            Connection con=Connectionprovider.getCon();
            PreparedStatement ps=con.prepareStatement("Delete from medicalrecords where patientid=?");
            ps.setString(1, id);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Medical records deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "No medical records found for the patient.");
            }
           
       }
       catch(Exception e)
       {
           JOptionPane.showMessageDialog(null, e);
       }
    }

    public void viewmedicines(DefaultTableModel model,JTable table,String branch) {
        try {
            
        Connection con = Connectionprovider.getCon();
       PreparedStatement ps=con.prepareStatement("select * from medicines where branch=?");
       ps.setString(1, branch);
        ResultSet rs = ps.executeQuery();
        
         model.setRowCount(0);
        while (rs.next()) {
            String name = rs.getString("name");
            String quantity = rs.getString("quantity");
            String salt = rs.getString("salt");
            String price = rs.getString("price");
            String code = rs.getString("code");

            model.addRow(new Object[]{rs.getString("name"), rs.getString("quantity"), rs.getString("salt"), rs.getString("price"), rs.getString("code")});
            int number=Integer.parseInt(quantity);;
            if (number < 30) {
                int row = model.getRowCount() - 1;
                table.setRowSelectionInterval(row, row); // Select the row
                table.setSelectionBackground(Color.BLUE); // Set selection background color
                
            }
        }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    public static boolean isNumber(String s) {
        return s.matches("\\d+");
    }
    
    public int addmedicine(medicines med) {
        String name=med.getName();
        String quantity=med.getQuantity();
        String salt=med.getSalt();
        String price=med.getPrice();
        String code= med.getCode();
        String branch=med.getBranch();
        if(name.equals("")||quantity.equals("")||salt.equals("")||price.equals("")||code.equals("")|| branch.equals(""))
        {
             JOptionPane.showMessageDialog(null, "All fields are required.");
             return 0;
        }
        else if(isNumber(quantity) && isNumber(price) && isNumber(code))
        {
            try {
                Connection con = Connectionprovider.getCon();
                PreparedStatement ps = con.prepareStatement("INSERT INTO medicines (name, quantity, salt, price, code,branch) VALUES (?, ?, ?, ?, ?,?)");
                ps.setString(1, name);
                ps.setString(2, quantity);
                ps.setString(3, salt);
                ps.setString(4, price);
                ps.setString(5, code);
                ps.setString(6, branch);
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected > 0) {
                    // Medicine added successfully
                    JOptionPane.showMessageDialog(null, "Medicine added Successfully.");
                    
                    return 1 ;
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to add medicine.");
                    return 0 ;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
                return 0 ;
            }
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Invalid entry in quantity/price/code field.");
            return 0;
        }
    }

    public void viewrecord(DefaultTableModel model, String id) {
        try
        {
            Connection con=Connectionprovider.getCon();
            PreparedStatement ps=con.prepareStatement("Select * from medicalrecords where patientid=?");
            ps.setString(1,id);
            ResultSet rs=ps.executeQuery();
            model.setRowCount(0);
            while (rs.next()) 
            {
                Object[] rowData = 
                {
                    rs.getString("record_id"),
                    rs.getString("patientid"),
                    rs.getString("test_name"),
                    rs.getString("test_date"),
                    rs.getString("test_result")
                };
                model.addRow(rowData);
            }
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void deletemedicine() {
        String medname=null;
        String branchname=null;
        do {
                medname = JOptionPane.showInputDialog("Enter the Medicine name to delete:");

                if (medname == null || medname.trim().equals("")) {
                    JOptionPane.showMessageDialog(null, "Medicine name is required");
                }
            } while (medname == null || medname.trim().equals(""));
        do {
        branchname = JOptionPane.showInputDialog("Enter the Branch name to delete:");

        if (branchname == null || branchname.trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Branch name is required");
        }
            } while (medname == null || medname.trim().equals(""));
        try{
            Connection con=Connectionprovider.getCon();
            PreparedStatement ps=con.prepareStatement("Delete from medicines where name=? and branch=?");
            ps.setString(1, medname);
            ps.setString(2, branchname);
            int rowsAffected = ps.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Medicine deleted successfully");
                    return ;
                } else {
                    JOptionPane.showMessageDialog(null, "Medicine not found");
                    return ;
                }
            
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    public void showPatients(JComboBox<String> comboBox)
    {
        try 
        {
            Connection conn = Connectionprovider.getCon();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT name FROM patient");
            comboBox.removeAllItems();
            while (rs.next()) {
                String patientName = rs.getString("name");
                comboBox.addItem(patientName);
            }
            rs.close();
            stmt.close();
            conn.close();
        } 
        catch (Exception e) {
            JOptionPane.showConfirmDialog(null, e);
        }
    }

    public void showmedicines(JComboBox<String> ComboBox2) {
        try 
        {
            String branchname="Johar Town";
            Connection conn = Connectionprovider.getCon();
            PreparedStatement ps=conn.prepareStatement("SELECT name FROM medicines where branch=?");
            ps.setString(1, branchname);
            
            ResultSet rs = ps.executeQuery();
            
            ComboBox2.removeAllItems();
            while (rs.next()) {
                String patientName = rs.getString("name");
                ComboBox2.addItem(patientName);
            }
            rs.close();
            ps.close();
            conn.close();
        } 
        catch (Exception e) {
            JOptionPane.showConfirmDialog(null, e);
        }
    }

    public void addprescription(String patientname, String medname, String quantity) {
        if(quantity.equals(""))
        {
            JOptionPane.showMessageDialog(null, "Quantity is required");
            return;
        }
        else
        {
            int requestedQuantity=Integer.parseInt(quantity);
            try
            {
                Connection con=Connectionprovider.getCon();
                PreparedStatement ps=con.prepareStatement("Select quantity,price,salt from medicines where name=?");
                ps.setString(1,medname);
                ResultSet rs=ps.executeQuery();
                String availablequantity,price,salt;
                if(rs.next())
                {
                    availablequantity=rs.getString("quantity");
                    price=rs.getString("price");
                    salt=rs.getString("salt");
                    int quantityinstock=Integer.parseInt(availablequantity);
                    if(requestedQuantity>quantityinstock)
                    {
                        JOptionPane.showMessageDialog(null, "Quantity is not available");
                    }
                    else 
                    {
                        try {
                            
                             ps = con.prepareStatement("UPDATE medicines SET quantity = ? WHERE name = ?");

                            // Calculate new quantity
                            int newQuantity = quantityinstock - requestedQuantity;

                            // Check if new quantity is not negative
                            if (newQuantity >= 0) {
                                // Set the new quantity for the medicine
                                ps.setInt(1, newQuantity);
                                ps.setString(2, medname);

                                // Execute the update query
                                int rowsUpdated = ps.executeUpdate();

                                if (rowsUpdated > 0) {
                                    
                                    addsale(medname,quantity,patientname,price,salt);
                                } else {
                                    JOptionPane.showMessageDialog(null, "Failed to update quantity");
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "Requested quantity exceeds available quantity");
                            }
                        } catch (Exception e) {
                            // Handle exceptions appropriately
                            JOptionPane.showMessageDialog(null, e);
                        }
                    }           

                }
            }
            catch(Exception e)
            {
                JOptionPane.showMessageDialog(null, e);
            }
        }
        
    }

    public void addsale(String medname,String quantity, String patientname,String price,String salt) {
        try
        {
            Connection con=Connectionprovider.getCon();
            PreparedStatement ps= con.prepareStatement("Insert into prescription (patientname,medname,quantity,price,salt) values (?,?,?,?,?)");
            ps.setString(1,patientname);
            ps.setString(2,medname);
            ps.setString(3,quantity);
            ps.setString(4,price);
            ps.setString(5,salt);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                // Medicine added successfully
                JOptionPane.showMessageDialog(null, "Medicine added Successfully.");
                return;
                
            } else {
                JOptionPane.showMessageDialog(null, "Failed to add medicine.");
                return  ;
            }
        }
        catch(Exception e)
        {
          JOptionPane.showMessageDialog(null, e);   
        }
        
    }
    
    public double generatereport(DefaultTableModel model)
    {
        saless s=new saless();
        double total= s.generateSalesReport(model);
        return total;
    }
    
    public void getalerts(DefaultTableModel model, javax.swing.JTable jTable1)
    {
         try {
             String branchname="Johar Town";
            Connection con = Connectionprovider.getCon();
            if (con != null) {
                String query = "SELECT name,quantity,price,code FROM medicines WHERE quantity < 30 and branch=?";
                PreparedStatement pst = con.prepareStatement(query);
                pst.setString(1, branchname);
                ResultSet rs = pst.executeQuery();

                model.setRowCount(0);
            while (rs.next()) 
            {
                Object[] rowData = 
                {
                    rs.getString("name"),
                    rs.getString("quantity"),
                    rs.getString("price"),
                    rs.getString("code"),
                };
                model.addRow(rowData);
            }

                pst.close();
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    
    }

    void updatemedicine(DefaultTableModel model, JTable jTable1) {
        int selectedRowIndex = jTable1.getSelectedRow();
         String medname=jTable1.getValueAt(selectedRowIndex,0).toString();
         String prevquantity=jTable1.getValueAt(selectedRowIndex,1).toString();
         int prevquant=Integer.parseInt(prevquantity);
         String branchName = JOptionPane.showInputDialog(null, "Enter Branch Name:");
         String quantity = JOptionPane.showInputDialog(null, "Enter quantity:");
         int quantityint=Integer.parseInt(quantity);
         if (quantity != null && branchName != null && quantityint > 0) 
         {
             try
             {
                Connection con=Connectionprovider.getCon();
                PreparedStatement ps= con.prepareStatement("UPDATE medicines SET quantity = quantity - ? WHERE branch = ? AND quantity >= ?");
                ps.setInt(1, quantityint);
                ps.setString(2, branchName);
                ps.setInt(3, quantityint);
                int rowsAffected = ps.executeUpdate();

                if (rowsAffected > 0) {
                    // Update successful
                    int newquantity=prevquant+quantityint;
                    String newquantityupdated=String.valueOf(newquantity);
                    jTable1.setValueAt(newquantityupdated,selectedRowIndex,1);
                    ps=con.prepareStatement("Update medicines SET quantity=? Where branch=? and name=?");
                    ps.setString(1,newquantityupdated);
                    ps.setString(2, "Johar Town");
                    ps.setString(3, medname);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(null,"Quantity updated successfully.");
                    getalerts(model,jTable1);
                } else {
                    // No rows updated, quantity or branch name might not match
                    JOptionPane.showMessageDialog(null,"Quantity not updated successfully.");
                }
             }
             catch(Exception e)
             {
                 JOptionPane.showMessageDialog(null,e);
             }
        }
    
    }
}
