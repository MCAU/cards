package com.ullarah.tcgmcau.command;

import com.ullarah.tcgmcau.mInit;
import com.ullarah.tcgmcau.sql.sQuery;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class cReset {

    public static void runPasswordReset(CommandSender sender, String[] args){

        if ( sender.hasPermission("cards.staff.pwreset") || !( sender instanceof Player ) ) if (args.length == 2) {

            Player resPlayer = mInit.getPlugin().getServer().getPlayer( sQuery.getExistingPlayerUUID( args[1] ) );

            if (resPlayer != null) {

                String resPlayerID = String.valueOf( resPlayer.getUniqueId() ).replace( "-", "" );

                sQuery.resetPlayerPassword( args[1], resPlayerID );

                sender.sendMessage(mInit.getMsgPrefix() + ChatColor.GREEN + "Password has been reset successfully for " + ChatColor.AQUA + args[1]);
                resPlayer.sendMessage(mInit.getMsgPrefix() + "Your password has been reset. Please use the link below register again.");

                cRegister.runRegister(resPlayer);

            } else
                sender.sendMessage(mInit.getMsgPrefix() + ChatColor.GREEN + args[1] + ChatColor.RED + " is not registered for cards.");

        } else
            sender.sendMessage(mInit.getMsgPrefix() + ChatColor.YELLOW + "/cards pwreset <player>");
        else sender.sendMessage(mInit.getMsgPermDeny());

    }

    public static void runUsernameReset(CommandSender sender, String[] args){

        if ( sender.hasPermission("cards.staff.idreset") || !( sender instanceof Player ) ) if (args.length == 3) {

            Player resPlayer = mInit.getPlugin().getServer().getPlayer( sQuery.getExistingPlayerUUID( args[1] ) );

            if (resPlayer != null) {

                String resPlayerID = String.valueOf( resPlayer.getUniqueId() ).replace( "-", "" );

                sQuery.resetPlayerUsername( args[1], args[2], resPlayerID );

                sender.sendMessage(mInit.getMsgPrefix() + ChatColor.GREEN + "Username has been reset successfully. ");
                sender.sendMessage(mInit.getMsgPrefix() + ChatColor.RED + args[1] + ChatColor.WHITE + " >> " + ChatColor.GREEN + args[2]);

                resPlayer.sendMessage(mInit.getMsgPrefix() + "Your username for cards have now been changed.");

            } else
                sender.sendMessage(mInit.getMsgPrefix() + ChatColor.GREEN + args[1] + ChatColor.RED + " is not registered for cards.");

        } else
            sender.sendMessage(mInit.getMsgPrefix() + ChatColor.YELLOW + "/cards idreset <olduser> <newuser>");

        else
            sender.sendMessage(mInit.getMsgPermDeny());

    }

}
