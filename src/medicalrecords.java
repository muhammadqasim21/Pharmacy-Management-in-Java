/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Qasim
 */
public class medicalrecords {
   private Patient patient;
   private String testname;
   private String testdate;
   private String results;
   
   public medicalrecords(Patient patient,String testname,String testdate,String results)
   {
       this.patient=patient;
       this.testname=testname;
       this.testdate=testdate;
       this.results=results;
   }
   
}
