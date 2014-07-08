package com.ullarah.tcgmcau.ability;

import com.ullarah.tcgmcau.mInit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;

import static org.bukkit.Bukkit.getServer;

public class aCommands {

    public static void rawCommands( final Player player, JSONArray json ){

        for ( Object commandCurrentObject : json ) {

            JSONObject commandCurrentObjectJSON = (JSONObject) commandCurrentObject;

            String commandRun = (String) commandCurrentObjectJSON.get( "run" );
            commandRun = commandRun.replaceAll( "%user%", player.getPlayerListName() );

            if ( checkCommandBlacklist( commandRun ) ) {
                player.sendMessage(mInit.getMsgPrefix() + ChatColor.RED + "An illegal command was found in the card. Let staff know immediately!");
            }
            else {
                getServer().dispatchCommand( getServer().getConsoleSender(), commandRun );
            }

        }

    }

    private static boolean checkCommandBlacklist(String input) {

        List<String> blacklistCommands = mInit.getPlugin().getConfig().getStringList("commands.blacklist");

        String blacklistArray[] = input.split(" ");

        for (String disabledCommand : blacklistCommands)
            if (blacklistArray[0].contains(disabledCommand)) {
                Bukkit.getConsoleSender().sendMessage( "[TCGMCAU] " + ChatColor.RED + "BLACKLISTED COMMAND STOPPED: " + ChatColor.GOLD + blacklistArray[0] );
                return true;
            }

        return false;

    }

}
