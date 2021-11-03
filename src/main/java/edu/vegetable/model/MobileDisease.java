package edu.vegetable.model;

import java.util.ArrayList;
import java.util.List;

public class MobileDisease {
    private Disease disease;
    private List<Symptom> symptomList = new ArrayList<>();
    private List<Cure> cureList = new ArrayList<>();

    public Disease getDisease() {
        return disease;
    }

    public void setDisease(Disease disease) {
        this.disease = disease;
    }

    public List<Symptom> getSymptomList() {
        return symptomList;
    }

    public void setSymptomList(List<Symptom> symptomList) {
        for(Symptom symptom: symptomList){
            symptom.setDisease(null);
        }
        this.symptomList = symptomList;
    }

    public List<Cure> getCureList() {
        return cureList;
    }

    public void setCureList(List<Cure> cureList) {
        for (Cure cure:cureList){
            cure.setDisease(null);
        }
        this.cureList = cureList;
    }
}
