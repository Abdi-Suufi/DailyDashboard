package com.example.dailydashboard;

import com.google.gson.Gson;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class WeatherService {

    private static final String API_KEY = "7498cb58a91c5ae6a5886a8a34ff290b";
    private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather?q=Bristol,uk&appid=" + API_KEY + "&units=metric";

    public static String getWeatherData() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String parseWeatherData(String json) {
        if (json == null) {
            return "Bristol, UK | --°C, --";
        }
        Gson gson = new Gson();
        Map<String, Object> data = gson.fromJson(json, Map.class);
        Map<String, Object> main = (Map<String, Object>) data.get("main");
        double temp = (double) main.get("temp");
        String weather = ((Map<String, String>) ((java.util.List) data.get("weather")).get(0)).get("main");
        return String.format("Bristol, UK | %.0f°C, %s", temp, weather);
    }
}