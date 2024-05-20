package me.rt.realweather;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Bukkit.getLogger;

public class Data {
    static List<String> data = new ArrayList<>();
    private static String API_KEY;
    public void initialize() {
        File file = new File(RealWeather.getInstance().getDataFolder().getAbsolutePath() + "/data.txt");
        if (!file.exists()) {
            try {
                file.getParentFile().mkdir();
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;

            while ((line = reader.readLine()) != null) {
                data.add(line.trim());
                getLogger().info(data.get(data.size()-1));
            }
            if (data.size() > 0) {
                API_KEY = data.get(0);
            } else {
                API_KEY = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public float getLatitude() {
        float lat = 0.0f;
        try {
            if (data.size() > 1) {
                lat = Float.parseFloat(data.get(1));
            }
        }
        catch (NumberFormatException e) {
            getLogger().info("Invalid latitude and longitude provided. Expected format is /set-weather-location XX.XXX YY.YYY");
        }

        return lat;
    }

    public float getLongitude() {
        float lon = 0.0f;
        try {
            if (data.size() > 0) {
                lon = Float.parseFloat(data.get(2));
            }
        }
        catch (NumberFormatException e) {
            getLogger().info("Invalid latitude and longitude provided. Expected format is /set-weather-location XX.XXX YY.YYY");
        }

        return lon;
    }

    public static String getApiKey() {
        return API_KEY;
    }

}
