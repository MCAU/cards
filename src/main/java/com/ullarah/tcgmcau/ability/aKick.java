package com.ullarah.tcgmcau.ability;

import com.ullarah.tcgmcau.mInit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

public class aKick {

    public static void playerKick( final Player player, JSONObject json ){

        final Long kickDelay = (Long) json.get("delay");
        final String kickMessage = (String) json.get( "message" );

        Bukkit.getScheduler().scheduleSyncDelayedTask(mInit.getPlugin(), new Runnable() {
            @Override
            public void run() {
                player.kickPlayer( ChatColor.translateAlternateColorCodes( '&', kickMessage ) );
            }
        }, kickDelay * 20);

    }

}