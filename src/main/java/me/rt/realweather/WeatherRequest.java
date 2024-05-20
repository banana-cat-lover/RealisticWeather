package me.rt.realweather;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.bukkit.Bukkit.getLogger;

public class WeatherRequest {
    public static int getCurrentWeather(double lat, double lon, String api_key) {
        if (api_key == null) {
            getLogger().info("Incorrect API_KEY");
            return 1;
        }
        String req = String.format("https://api.openweathermap.org/data/2.5/weather?lat=%.2f&lon=%.2f&appid=%s", lat, lon, api_key);

        try {
            URL url = new URL(req);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            StringBuilder sb = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }

            conn.disconnect();

            String jsonResponse = sb.toString();
            JSONObject jsonObj = new JSONObject(jsonResponse);
            JSONArray weatherArray = jsonObj.getJSONArray("weather");
            JSONObject firstWeatherObject = weatherArray.getJSONObject(0);
            return firstWeatherObject.getInt("id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
