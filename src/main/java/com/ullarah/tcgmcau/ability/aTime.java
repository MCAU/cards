package com.ullarah.tcgmcau.ability;

import com.ullarah.tcgmcau.mConfig;
import com.ullarah.tcgmcau.mInit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

import org.json.simple.JSONObject;

public class aTime {

    public static void setTime( final Player player, JSONObject json ){

        String timeType = (String) json.get("type");
        String timeState = (String) json.get("state");

        Long timeDuration = null;

        if( json.containsKey("duration") ) timeDuration = (Long) json.get("duration");

        World world = player.getWorld();

        switch( timeKeyword.valueOf( timeType ) ) {

            case SUNRISE:
                if( timeState.equals("LOCAL") ){
                    setTimeLocal( player, (long) 23000, timeDuration != null ? timeDuration.intValue() : 60 );
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.YELLOW + mConfig.getLanguageConfig().getString("time.sunrise.local")));
                } else {
                    world.setTime(23000);
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.YELLOW + player.getPlayerListName() + ChatColor.WHITE + " " + mConfig.getLanguageConfig().getString("time.sunrise.global")));
                }
                break;

            case MORNING:
                if( timeState.equals("LOCAL") ){
                    setTimeLocal( player, (long) 0, timeDuration != null ? timeDuration.intValue() : 60 );
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.YELLOW + mConfig.getLanguageConfig().getString("time.morning.local")));
                } else {
                    world.setTime(0);
                    Bukkit.broadcastMessage( ChatColor.translateAlternateColorCodes( '&', ChatColor.YELLOW + player.getPlayerListName() + ChatColor.WHITE + " " + mConfig.getLanguageConfig().getString("time.morning.global") ) );
                }
                break;

            case DAY:
                if( timeState.equals("LOCAL") ){
                    setTimeLocal( player, (long) 6000, timeDuration != null ? timeDuration.intValue() : 60 );
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.YELLOW + mConfig.getLanguageConfig().getString("time.day.local")));
                } else {
                    world.setTime(6000);
                    Bukkit.broadcastMessage( ChatColor.translateAlternateColorCodes( '&', ChatColor.YELLOW + player.getPlayerListName() + ChatColor.WHITE + " " + mConfig.getLanguageConfig().getString("time.day.global") ) );
                }
                break;

            case SUNSET:
                if( timeState.equals("LOCAL") ){
                    setTimeLocal( player, (long) 12000, timeDuration != null ? timeDuration.intValue() : 60 );
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.YELLOW + mConfig.getLanguageConfig().getString("time.sunset.local")));
                } else {
                    world.setTime(12000);
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.YELLOW + player.getPlayerListName() + ChatColor.WHITE + " " + mConfig.getLanguageConfig().getString("time.sunset.global")));
                }
                break;

            case NIGHT:
                if( timeState.equals("LOCAL") ){
                    setTimeLocal( player, (long) 18000, timeDuration != null ? timeDuration.intValue() : 60 );
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.YELLOW + mConfig.getLanguageConfig().getString("time.night.local")));
                } else {
                    world.setTime(18000);
                    Bukkit.broadcastMessage( ChatColor.translateAlternateColorCodes( '&', ChatColor.YELLOW + player.getPlayerListName() + ChatColor.WHITE + " " + mConfig.getLanguageConfig().getString("time.night.global") ) );
                }
                break;

        }

    }

    private static void setTimeLocal(final Player player, final Long time, Integer duration){

        final Integer timeID;

        timeID = Bukkit.getScheduler().scheduleSyncRepeatingTask(mInit.getPlugin(), new Runnable() {
            @Override
            public void run() {
                player.setPlayerTime(time, false);
            }
        }, 0, 20);

        Bukkit.getScheduler().scheduleSyncDelayedTask(mInit.getPlugin(), new Runnable() {
            @Override
            public void run() {
                Bukkit.getScheduler().cancelTask(timeID);
                player.resetPlayerTime();
            }
        }, duration * 20);

    }

    private enum timeKeyword {
        SUNRISE, MORNING, DAY,
        SUNSET, NIGHT,
    }

}
