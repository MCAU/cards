package com.ullarah.tcgmcau.ability;

import com.ullarah.tcgmcau.mInit;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import org.json.simple.JSONObject;

public class aCooldown {

    public static void playerCooldown( final Player player, JSONObject json ){

        String playerID = String.valueOf( player.getUniqueId() ).replace( "-", "" );

        Long cooldownAmount = (Long) json.get("amount");
        String cooldownState = (String) json.get("state");

        switch ( cooldownKeyword.valueOf(cooldownState) ) {

            case ADD:
                mInit.redeemCooldown.put( playerID, cooldownAmount );
                player.sendMessage(mInit.getMsgPrefix() + "Cooldown time has been extended to " + ChatColor.RED + cooldownAmount + ChatColor.WHITE + " seconds!");
                break;

            case REMOVE:
                mInit.redeemCooldown.remove( playerID );
                player.sendMessage(mInit.getMsgPrefix() + ChatColor.GREEN + "Cooldown time has been removed!");
                break;

        }

    }

    private enum cooldownKeyword {
        ADD, REMOVE
    }

}
