package com.ullarah.tcgmcau.ability;

import com.ullarah.tcgmcau.mConfig;
import com.ullarah.tcgmcau.mInit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.entity.Player;

import org.json.simple.JSONObject;

public class aWeather {

    public static void setWeather( final Player player, JSONObject json ){

        String weatherType = (String) json.get("type");
        String weatherState = (String) json.get("state");

        Long weatherDuration = null;

        if( json.containsKey("duration") ) weatherDuration = (Long) json.get("duration");

        World world = player.getWorld();

        switch( weatherKeyword.valueOf( weatherType ) ) {

            case CLEAR:
                if( weatherState.equals("LOCAL") ){
                    setWeatherLocal( player, WeatherType.CLEAR, weatherDuration != null ? weatherDuration.intValue() : 60 );
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.YELLOW + mConfig.getLanguageConfig().getString("weather.clear.local")));
                } else {
                    world.setStorm(false);
                    world.setThundering(false);
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.YELLOW + player.getPlayerListName() + ChatColor.WHITE + " " + mConfig.getLanguageConfig().getString("weather.clear.global")));
                }
                break;

            case RAIN:
                if( weatherState.equals("LOCAL") ){
                    setWeatherLocal( player, WeatherType.DOWNFALL, weatherDuration != null ? weatherDuration.intValue() : 60 );
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.YELLOW + mConfig.getLanguageConfig().getString("weather.rain.local")));
                } else {
                    world.setStorm(true);
                    world.setThundering(false);
                    Bukkit.broadcastMessage( ChatColor.translateAlternateColorCodes( '&', ChatColor.YELLOW + player.getPlayerListName() + ChatColor.WHITE + " " + mConfig.getLanguageConfig().getString("weather.rain.global") ) );
                }
                break;

            case THUNDER:
                if( weatherState.equals("LOCAL") ){
                    setWeatherLocal( player, WeatherType.DOWNFALL, weatherDuration != null ? weatherDuration.intValue() : 60 );
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.YELLOW + mConfig.getLanguageConfig().getString("weather.thunder.local")));
                } else {
                    world.setStorm(false);
                    world.setThundering(true);
                    Bukkit.broadcastMessage( ChatColor.translateAlternateColorCodes( '&', ChatColor.YELLOW + player.getPlayerListName() + ChatColor.WHITE + " " + mConfig.getLanguageConfig().getString("weather.thunder.global") ) );
                }
                break;

            case STORM:
                if( weatherState.equals("LOCAL") ){
                    setWeatherLocal( player, WeatherType.DOWNFALL, weatherDuration != null ? weatherDuration.intValue() : 60 );
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.YELLOW + mConfig.getLanguageConfig().getString("weather.storm.local")));
                } else {
                    world.setStorm(true);
                    world.setThundering(true);
                    Bukkit.broadcastMessage( ChatColor.translateAlternateColorCodes( '&', ChatColor.YELLOW + player.getPlayerListName() + ChatColor.WHITE + " " + mConfig.getLanguageConfig().getString("weather.storm.global") ) );
                }
                break;

        }

    }

    private static void setWeatherLocal(final Player player, final WeatherType weather, Integer duration){

        final Integer weatherID;

        weatherID = Bukkit.getScheduler().scheduleSyncRepeatingTask(mInit.getPlugin(), new Runnable() {
            @Override
            public void run() {
                player.setPlayerWeather( weather );
            }
        }, 0, 20);

        Bukkit.getScheduler().scheduleSyncDelayedTask(mInit.getPlugin(), new Runnable() {
            @Override
            public void run() {
                Bukkit.getScheduler().cancelTask(weatherID);
                player.resetPlayerWeather();
            }
        }, duration * 20);

    }

    private enum weatherKeyword {
        CLEAR, RAIN,
        THUNDER, STORM
    }

}
