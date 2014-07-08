package com.ullarah.tcgmcau.ability;

import com.ullarah.tcgmcau.mInit;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

public class aGhost {

    public static void setPlayerGhost( final Player player, JSONObject json ){

        Long ghostDuration = (Long) json.get( "duration" );
        final Boolean ghostMobIgnore = (Boolean) json.get( "mobignore" );

        mInit.getGhostManager().setGhost(player, true);

        if( ghostMobIgnore ) mInit.playerGhost.put( player.getUniqueId(), true );

        Bukkit.getScheduler().scheduleSyncDelayedTask(mInit.getPlugin(), new Runnable() {
            @Override
            public void run() {

                mInit.getGhostManager().setGhost(player, false);
                if( ghostMobIgnore ) mInit.playerGhost.put( player.getUniqueId(), false );

            }
        }, (ghostDuration + 5 ) * 20);

    }

}
