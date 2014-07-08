package com.ullarah.tcgmcau.command;

import com.ullarah.tcgmcau.mInit;
import com.ullarah.tcgmcau.sql.sQuery;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class cForce {

    public static void forcePlayerCardRun(CommandSender sender, String[] args){

        if ( sender.hasPermission("cards.staff.force") || !( sender instanceof Player) ) if (args.length == 3) {

            Player player = mInit.getPlugin().getServer().getPlayer(sQuery.getExistingPlayerUUID(args[1]));

            if (player != null) {

                sQuery.runRedeemCard(player, args[2], true);
                mInit.redeemCooldown.remove( player.getUniqueId().toString() );

            }

        } else
            sender.sendMessage(mInit.getMsgPrefix() + ChatColor.YELLOW + "/cards force <player> <cardcode>");

        else
            sender.sendMessage(mInit.getMsgPermDeny());

    }

}
