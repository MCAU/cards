package com.ullarah.tcgmcau.ability;

import com.ullarah.tcgmcau.mInit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import org.json.simple.JSONObject;

public class aMessages {

    public static void sayMessage(Player player, JSONObject json) {

        if( json.containsKey( "broadcast" ) ){

            String broadcastMessage = (String) json.get( "broadcast" );

            broadcastMessage = broadcastMessage.replaceAll( "%user%", player.getPlayerListName() );

            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', broadcastMessage));

        }

        if( json.containsKey( "private" ) ){

            String playerMessage = (String) json.get("private");

            playerMessage = playerMessage.replaceAll( "%user%", player.getPlayerListName() );

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', mInit.getMsgPrefix() + playerMessage));

        }

        if( json.containsKey( "say" ) ){

            String playerChat = (String) json.get("say");

            mInit.chatDuration.put( player.getUniqueId().toString(), (long) -1);
            mInit.chatString.put( player.getUniqueId().toString(), playerChat );

            player.chat( ">" );

        }

    }

}
