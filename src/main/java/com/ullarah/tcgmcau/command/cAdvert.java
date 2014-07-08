package com.ullarah.tcgmcau.command;

import com.ullarah.tcgmcau.mInit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class cAdvert {

    public static void runAdvert(CommandSender sender){

        if ( sender.hasPermission("cards.staff.advert") || !( sender instanceof Player ) ){

            String advertWho;

            if( sender instanceof Player ){
                Player player = (Player) sender;
                advertWho = player.getPlayerListName();
            } else advertWho = "CONSOLE";

            Bukkit.broadcastMessage(" ");
            Bukkit.broadcastMessage( ChatColor.WHITE + ChatColor.BOLD.toString() + "MCAU Collectable Trading Cards!" );
            Bukkit.broadcastMessage( ChatColor.AQUA + ChatColor.BOLD.toString() + "XP" + ChatColor.WHITE + " cards! " + ChatColor.GREEN + ChatColor.BOLD.toString() + "MONEY" + ChatColor.WHITE + " cards! " + ChatColor.GOLD + ChatColor.BOLD.toString() + "DISGUISE" + ChatColor.WHITE + " cards! " );
            Bukkit.broadcastMessage( ChatColor.LIGHT_PURPLE + ChatColor.BOLD.toString() + "ITEM" + ChatColor.WHITE + " cards! " + ChatColor.YELLOW + ChatColor.BOLD.toString() + "FLY" + ChatColor.WHITE + " cards! " + ChatColor.RED + ChatColor.BOLD.toString() + "SUMMON" + ChatColor.WHITE + " cards! ");
            Bukkit.broadcastMessage( ChatColor.WHITE + "What are you waiting for? Register now! " + ChatColor.GOLD + ChatColor.BOLD + "/cards register" );
            Bukkit.broadcastMessage( ChatColor.GRAY + "This advertisement brought to you by " + advertWho + "." );
            Bukkit.broadcastMessage( " " );

        } else sender.sendMessage(mInit.getMsgPermDeny());

    }

}
