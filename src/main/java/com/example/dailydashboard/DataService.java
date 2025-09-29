package com.example.dailydashboard;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate; // Import LocalDate

public class DataService {

    private static final Path DATA_PATH = Paths.get(System.getProperty("user.home"), ".dailydashboard", "data.json");
    // Updated Gson instance to register our new adapter
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    public static void saveData(AppData data) {
        try {
            Files.createDirectories(DATA_PATH.getParent());
            try (FileWriter writer = new FileWriter(DATA_PATH.toFile())) {
                GSON.toJson(data, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static AppData loadData() {
        if (!Files.exists(DATA_PATH)) {
            return null;
        }
        try (FileReader reader = new FileReader(DATA_PATH.toFile())) {
            return GSON.fromJson(reader, AppData.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}