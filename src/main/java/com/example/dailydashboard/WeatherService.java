package com.example.dailydashboard;

import com.google.gson.Gson;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class WeatherService {

    private static final String API_KEY = "7498cb58a91c5ae6a5886a8a34ff290b";
    private static final String API_URL_TEMPLATE = "http://api.openweathermap.org/data/2.5/weather?q=%s&appid=" + API_KEY + "&units=metric";

    public static String getWeatherData(String location) {
        try {
            String encodedLocation = URLEncoder.encode(location, StandardCharsets.UTF_8);
            String apiUrl = String.format(API_URL_TEMPLATE, encodedLocation);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return response.body();
            } else {
                System.err.println("Error fetching weather data. Status code: " + response.statusCode());
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String parseWeatherData(String json, String location) {
        if (json == null) {
            return location + " | --°C, --";
        }
        try {
            Gson gson = new Gson();
            Map<String, Object> data = gson.fromJson(json, Map.class);
            Map<String, Object> main = (Map<String, Object>) data.get("main");
            double temp = (double) main.get("temp");
            String weather = ((Map<String, String>) ((java.util.List) data.get("weather")).get(0)).get("main");
            String name = (String) data.get("name"); // Get city name from API response for accuracy
            return String.format("%s | %.0f°C, %s", name, temp, weather);
        } catch (Exception e) {
            e.printStackTrace();
            return location + " | Error";
        }
    }
}