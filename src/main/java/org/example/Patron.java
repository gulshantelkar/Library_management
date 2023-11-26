package org.example;

import java.util.ArrayList;
import java.util.List;

public class Patron {
    private int patronID;
    private String name;
    private String contactInformation;

    public Patron(int patronID, String name, String contactInformation) {
        this.patronID = patronID;
        this.name = name;
        this.contactInformation = contactInformation;
    }

    public int getPatronID() {
        return patronID;
    }

    public void setPatronID(int patronID) {
        this.patronID = patronID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactInformation() {
        return contactInformation;
    }

    public void setContactInformation(String contactInformation) {
        this.contactInformation = contactInformation;
    }

    public static void addPatron(List<Patron> patronCollection, Patron newPatron) {
        patronCollection.add(newPatron);
        System.out.println("Patron added successfully: " + newPatron.getName());
    }


    public static void updatePatron(List<Patron> patronCollection, int patronID, Patron updatedPatron) {
        for (int i = 0; i < patronCollection.size(); i++) {
            if (patronCollection.get(i).getPatronID() == patronID) {
                patronCollection.set(i, updatedPatron);
                System.out.println("Patron updated successfully: " + updatedPatron.getName());
                return;
            }
        }
        System.out.println("Patron with ID " + patronID + " not found.");
    }

    public static void removePatron(List<Patron> patronCollection, int patronID) {
        patronCollection.removeIf(patron -> patron.getPatronID() == patronID);
        System.out.println("Patron removed successfully with ID: " + patronID);
    }


    public static Patron searchPatron(List<Patron> patronCollection, int patronID) {
        for (Patron patron : patronCollection) {
            if (patron.getPatronID() == patronID) {
                return patron;
            }
        }
        System.out.println("Patron with ID " + patronID + " not found.");
        return null;
    }
}
