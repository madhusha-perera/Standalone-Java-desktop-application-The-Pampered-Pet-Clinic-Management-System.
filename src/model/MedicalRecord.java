/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Date;

public class MedicalRecord {
    private String recordCode, petCode, vetCode, diagnosis, treatment, medication;
    private Date   visitDate;

    public String getRecordCode() { return recordCode; }
    public void setRecordCode(String c) { this.recordCode = c; }

    public String getPetCode() { return petCode; }
    public void setPetCode(String p) { this.petCode = p; }

    public String getVetCode() { return vetCode; }
    public void setVetCode(String v) { this.vetCode = v; }

    public Date getVisitDate() { return visitDate; }
    public void setVisitDate(Date d) { this.visitDate = d; }

    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String d) { this.diagnosis = d; }

    public String getTreatment() { return treatment; }
    public void setTreatment(String t) { this.treatment = t; }

    public String getMedication() { return medication; }
    public void setMedication(String m) { this.medication = m; }
}