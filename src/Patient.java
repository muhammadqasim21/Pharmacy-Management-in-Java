/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Qasim
 */
public class Patient {
    private String patientID;
    private String patientName;
    private String contact;
    private String allergies;
    medicalrecords m;
//constructor
    public Patient()
    {
        this.patientID = null;
        this.patientName = null;
        this.contact = null;
        this.allergies=null;
    }
    public Patient(String patientID, String patientName, String contact,String allergies) {
        this.patientID = patientID;
        this.patientName = patientName;
        this.contact = contact;
        this.allergies=allergies;
    }
    public void setid(String id)
    {
        this.patientID=id;
    }
    public void setname(String name)
    {
        this.patientName=name;
    }
    public void setContact(String contact)
    {
        this.contact=contact;
    }
    public void setAllergies(String allergies)
    {
        this.allergies=allergies;
    }
    
    public String getpatientid()
    {
        return patientID;
    }
    public String getPatientname()
    {
        return patientName;
    }
    public String getContact()
    {
        return contact;
    }
    public String getAllergies()
    {
        return allergies;
    }
    public medicalrecords getMedicalRecords()
    {
        return m;
    }
    public void setMedicalRecord(medicalrecords m)
    {
        this.m=m;
    }
    
}
