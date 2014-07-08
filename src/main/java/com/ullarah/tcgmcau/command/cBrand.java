package com.ullarah.tcgmcau.command;

import com.ullarah.tcgmcau.mInit;

import com.ullarah.tcgmcau.sql.sQuery;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class cBrand {

    public static void runRemoveBrand(CommandSender sender, String[] args){

        if ( sender.hasPermission("cards.staff.brand") || !( sender instanceof Player ) ) if (args.length == 2) {

            Player player = mInit.getPlugin().getServer().getPlayer(sQuery.getExistingPlayerUUID(args[1]));

            if (player != null) {

                runUnbrandinator( player );

                if(sender.hasPermission("cards.staff.brand")) sender.sendMessage(mInit.getMsgPrefix() + "Branding for " + ChatColor.GREEN + args[1] + ChatColor.WHITE + " has been removed.");

            } else sender.sendMessage(mInit.getMsgPrefix() + ChatColor.GREEN + args[1] + ChatColor.RED + " is not online.");

        } else
            sender.sendMessage(mInit.getMsgPrefix() + ChatColor.YELLOW + "/cards unbrand <player>");

        else sender.sendMessage(mInit.getMsgPermDeny());

    }

    public static void runUnbrandinator(Player player){

        List<String> brandWorlds = mInit.getPlugin().getConfig().getStringList("brands");

        for ( String brandCurrentWorld : brandWorlds )
            try {

                mInit.getVaultChat().setPlayerPrefix( player, mInit.playerPrefix.get( player.getUniqueId() ) );
                mInit.getVaultChat().setPlayerSuffix( player, mInit.playerSuffix.get( player.getUniqueId() ) );

                if (player.isOnline()) {
                    mInit.playerPrefix.remove(player.getUniqueId());
                    mInit.playerSuffix.remove(player.getUniqueId());
                }

            } catch (NullPointerException e) {

                player.sendMessage(mInit.getMsgPrefix() + "The world " + ChatColor.RED + brandCurrentWorld + ChatColor.WHITE + " does not exist. Check your config.");

            }

        player.sendMessage(mInit.getMsgPrefix() + "Your branding has been removed.");

    }

}
