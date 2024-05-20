package me.rt.realweather;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import java.util.*;

public final class RealWeather extends JavaPlugin {
    private static RealWeather instance;

    Set<Integer> thunderstorm = Set.of(2);
    Set<Integer> rain = Set.of(3, 5, 6);

    BukkitTask updateWeatherTask;
    Data d = new Data();
    int minutesBetweenUpdates;
    boolean shutdown = false;

    @Override
    public void onEnable() {
        instance = this;
        d.initialize();
        BukkitScheduler scheduler = getServer().getScheduler();
        updateWeatherTask = scheduler.runTaskTimerAsynchronously(this, this::runWeatherUpdate, 0L, 20L * 60 * minutesBetweenUpdates);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static RealWeather getInstance() {
        return instance;
    }

    private void runWeatherUpdate() {
        BukkitScheduler scheduler = getServer().getScheduler();
        WeatherType weatherType = getCurrentWeather(d.getLatitude(), d.getLongitude());
        if (!shutdown) {
            scheduler.runTask(this, () -> {
                setWeather(weatherType);
            });
        }
        int congifIntervalBetweenUpdates = 5;
        if (congifIntervalBetweenUpdates != minutesBetweenUpdates) {
            minutesBetweenUpdates = congifIntervalBetweenUpdates;
            updateWeatherTask.cancel();
            updateWeatherTask = scheduler.runTaskTimerAsynchronously(this, this::runWeatherUpdate, 20L * 60 * minutesBetweenUpdates, 20L * 60 * minutesBetweenUpdates);
        }
    }

    private void setWeather(WeatherType weatherType) {
        World world = Bukkit.getWorlds().get(0);
         if (weatherType.equals(WeatherType.CLEAR)) {
            world.setStorm(false);
            world.setThundering(false);
         } else if (weatherType.equals(WeatherType.RAIN)) {
            world.setStorm(true);
            world.setThundering(false);
         } else if (weatherType.equals(WeatherType.THUNDERSTORM)) {
            world.setStorm(true);
            world.setThundering(true);
        }
        world.setWeatherDuration(20 * 60 * minutesBetweenUpdates);
    }

    private WeatherType getCurrentWeather(double lat, double lon) {
        int weatherCode = WeatherRequest.getCurrentWeather(lat, lon, Data.getApiKey());
        if (rain.contains(weatherCode)) {
            return WeatherType.RAIN;
        } else if (thunderstorm.contains(weatherCode)) {
            return WeatherType.THUNDERSTORM;
        } else {
            return WeatherType.CLEAR;
        }
    }

}
