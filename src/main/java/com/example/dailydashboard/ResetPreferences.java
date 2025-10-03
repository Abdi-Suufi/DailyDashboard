package com.example.dailydashboard;

import java.util.prefs.Preferences;

public class ResetPreferences {

    public static void main(String[] args) {
        System.out.println("Attempting to reset application preferences...");

        // Use the same class as in Main.java to access the correct preference node
        Preferences prefs = Preferences.userNodeForPackage(Main.class);

        try {
            // Remove the "userName" key that the app checks for
            prefs.remove("userName");
            prefs.flush(); // Ensure the change is written to storage

            System.out.println("Preference 'userName' has been cleared successfully.");
            System.out.println("You can now run the main application to test the setup screen.");

        } catch (Exception e) {
            System.err.println("An error occurred while resetting preferences:");
            e.printStackTrace();
        }
    }
}