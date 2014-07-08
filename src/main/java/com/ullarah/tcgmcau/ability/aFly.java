package com.ullarah.tcgmcau.ability;

import com.ullarah.tcgmcau.mInit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import org.json.simple.JSONObject;

public class aFly {

    public static void playerFly( final Player player, JSONObject json ){

        final Long flyDuration = (Long) json.get("duration");

        player.setAllowFlight(true);

        Bukkit.getScheduler().scheduleSyncDelayedTask(mInit.getPlugin(), new Runnable() {
            @Override
            public void run() {
                player.setFallDistance(-100.0F);
                player.setAllowFlight(false);
                player.setFallDistance(0);
            }
        }, (flyDuration + 5 ) * 20);

    }

}
