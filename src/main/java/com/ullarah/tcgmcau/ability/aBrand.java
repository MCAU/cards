package com.ullarah.tcgmcau.ability;

import com.ullarah.tcgmcau.command.cBrand;
import com.ullarah.tcgmcau.mInit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import org.json.simple.JSONObject;

import java.util.List;

public class aBrand {

    public static void playerBrand( final Player player, JSONObject json ){

        String brandName = (String) json.get( "name" );
        String brandType = (String) json.get( "type" );

        Long brandDuration = (Long) json.get( "duration" );

        List<String> brandWorlds = mInit.getPlugin().getConfig().getStringList("brands");

        mInit.playerPrefix.put( player.getUniqueId(), mInit.getVaultChat().getPlayerPrefix(player) );
        mInit.playerSuffix.put( player.getUniqueId(), mInit.getVaultChat().getPlayerSuffix(player) );

        for (String brandCurrentWorld : brandWorlds) {

            try {

                switch (brandKeyword.valueOf(brandType)) {

                    case PREFIX:
                        mInit.getVaultChat().setPlayerPrefix(player, ChatColor.WHITE + "[" + ChatColor.RESET + brandName + ChatColor.WHITE + "]" + ChatColor.GRAY);
                        break;

                    case SUFFIX:
                        mInit.getVaultChat().setPlayerSuffix(player, ChatColor.WHITE + "[" + ChatColor.RESET + brandName + ChatColor.WHITE + "]" + ChatColor.GRAY);
                        break;

                }

            } catch (NullPointerException e){

                player.sendMessage( mInit.getMsgPrefix() + ChatColor.RED + "Please tell staff that " + ChatColor.YELLOW + brandCurrentWorld + ChatColor.RED + " does not exist." );

            }

        }

        player.sendMessage(mInit.getMsgPrefix() + "You have now been branded " + ChatColor.YELLOW + ChatColor.translateAlternateColorCodes('&', brandName) + ChatColor.WHITE + ".");

        Bukkit.getScheduler().scheduleSyncDelayedTask(mInit.getPlugin(), new Runnable() {
            @Override
            public void run() {
                cBrand.runUnbrandinator( player );
            }
        }, ( brandDuration.intValue() + 5 ) * 20 );

    }

    private enum brandKeyword {
        PREFIX, SUFFIX
    }

}
