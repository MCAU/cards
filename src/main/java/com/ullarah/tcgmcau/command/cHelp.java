package com.ullarah.tcgmcau.command;

import com.ullarah.tcgmcau.mInit;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class cHelp {

    public static void runHelp(CommandSender sender){

        sender.sendMessage("");
        sender.sendMessage(ChatColor.AQUA + "Welcome to MCAUs Collectable Trading Cards");
        sender.sendMessage("------------------------------------------");
        sender.sendMessage(ChatColor.GOLD + " ▪ /cards register " + ChatColor.YELLOW + "- Register to obtain cards." );
        sender.sendMessage(ChatColor.GOLD + " ▪ /cards redeem " + ChatColor.YELLOW + "- Redeem a card." );
        sender.sendMessage(ChatColor.YELLOW + "    You can also use: " + ChatColor.GOLD + "/redeem");
        sender.sendMessage(ChatColor.GOLD + " ▪ /cards gift " + ChatColor.YELLOW + "- Gift a card from your stash to a player." );
        sender.sendMessage(ChatColor.YELLOW + "    You can also use: " + ChatColor.GOLD + "/gift");
        sender.sendMessage(ChatColor.GOLD + " ▪ /cards cat " + ChatColor.YELLOW + "- See your active card categories." );
        sender.sendMessage(ChatColor.GOLD + " ▪ /cards about " + ChatColor.YELLOW + "- About the cards." );
        sender.sendMessage("");

    }

    public static void runStaffHelp(CommandSender sender){

        if ( sender.hasPermission( "cards.staff" ) ) {

            sender.sendMessage("");
            sender.sendMessage("------------- STAFF COMMANDS -------------");
            sender.sendMessage(ChatColor.GOLD + " ▪ /cards pwreset " + ChatColor.YELLOW + "- Reset a players password." );
            sender.sendMessage(ChatColor.RED + "    Their stash will not be removed.");
            sender.sendMessage(ChatColor.GOLD + " ▪ /cards list " + ChatColor.YELLOW + "- List all cards available." );
            sender.sendMessage(ChatColor.GOLD + " ▪ /cards info " + ChatColor.YELLOW + "- View a specific cards information." );
            sender.sendMessage(ChatColor.GOLD + " ▪ /cards purge " + ChatColor.YELLOW + "- Completely wipe a players stash." );
            sender.sendMessage(ChatColor.GOLD + " ▪ /cards give " + ChatColor.YELLOW + "- Give a specific card to a player." );
            sender.sendMessage(ChatColor.GOLD + " ▪ /cards random " + ChatColor.YELLOW + "- Give a random card to a player." );
            sender.sendMessage(ChatColor.GOLD + " ▪ /cards unbrand " + ChatColor.YELLOW + "- Removes a players card brand." );
            sender.sendMessage("");

        } else {

            sender.sendMessage(mInit.getMsgPermDeny());

        }

    }

    public static void runAbout(CommandSender sender){

        sender.sendMessage("");
        sender.sendMessage("About Cards: To be written...");
        sender.sendMessage("");

    }

}
