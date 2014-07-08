package com.ullarah.tcgmcau.command;

import com.ullarah.tcgmcau.mInit;
import com.ullarah.tcgmcau.sql.sQuery;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;

public class cGift {

    public static void runGiftCard(CommandSender sender, String[] args){

        if ( ( sender instanceof Player ) ) if (args.length >= 2 && args.length <= 3) {

            Player player = (Player) sender;

            if ( !args[0].equals(player.getPlayerListName()) ) {

                Player recPlayer = mInit.getPlugin().getServer().getPlayer( sQuery.getExistingPlayerUUID( args[0] ) );
                String recCard = args[1].toLowerCase();

                Integer recAmount = 1;

                if (args.length == 3 && !(Integer.valueOf(args[2]) <= 0))
                    if (args[2].matches("[0-9]+")) recAmount = Integer.valueOf(args[2]);
                    else
                        sender.sendMessage(mInit.getMsgPrefix() + ChatColor.RED + "Quantity has to be a number greater than 0.");

                if (recPlayer != null) {

                    sQuery.runRefreshCards();

                    String cardName = null;
                    String cardTag = mInit.cardCode.get(recCard);

                    try {

                        ResultSet res = mInit.getSqlConnection().sqlQuery("SELECT name FROM cards WHERE code = ?", new String[]{recCard});
                        if(res.next()) cardName = res.getString("name");

                        res = mInit.getSqlConnection().sqlQuery("SELECT * FROM stash WHERE card = ? AND user = ?", new String[]{cardTag, player.getPlayerListName()});

                        if (res.next()) if (recAmount <= res.getInt("amount")) {

                            Integer getAmount = res.getInt("amount");

                            if (getAmount >= 1) if (mInit.cardCode.get(recCard) != null) try {

                                res = mInit.getSqlConnection().sqlQuery("SELECT * FROM stash WHERE card = ? AND user = ?", new String[]{cardTag, recPlayer.getPlayerListName()});

                                if (res.next()) {

                                    Integer setAmount = res.getInt("amount") + recAmount;

                                    mInit.getSqlConnection().sqlUpdate("UPDATE stash SET amount = ? WHERE card = ? AND user = ?", new String[]{setAmount.toString(), mInit.cardCode.get(recCard), recPlayer.getPlayerListName()});

                                } else {

                                    mInit.getSqlConnection().sqlUpdate("INSERT INTO stash VALUES ( null, ?, ?, ? )", new String[]{recPlayer.getPlayerListName(), mInit.cardCode.get(recCard), recAmount.toString()});

                                }

                                Integer finalAmount = getAmount - recAmount;

                                mInit.getSqlConnection().sqlUpdate("UPDATE stash SET amount = ? WHERE card = ? AND user = ?", new String[]{finalAmount.toString(), mInit.cardCode.get(recCard), player.getPlayerListName()});

                                sender.sendMessage(mInit.getMsgPrefix() + "You gifted " + ChatColor.GREEN + recPlayer.getPlayerListName() + ChatColor.WHITE + ", " + ChatColor.GOLD + recAmount + " " + ChatColor.AQUA + cardName + ChatColor.WHITE + " card.");

                                recPlayer.sendMessage(mInit.getMsgPrefix() + "You received " + ChatColor.GOLD + recAmount + " " + ChatColor.AQUA + cardName + ChatColor.WHITE + " card from " + ChatColor.GREEN + player.getPlayerListName() + ChatColor.WHITE + ".");

                            } catch (SQLException e) {

                                e.printStackTrace();

                            }

                        } else
                            sender.sendMessage(mInit.getMsgPrefix() + ChatColor.RED + "You don't have enough of those cards.");

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                } else
                    sender.sendMessage(mInit.getMsgPrefix() + ChatColor.GREEN + args[0] + ChatColor.RED + " is not online or not registered.");
            } else sender.sendMessage(mInit.getMsgPrefix() + ChatColor.RED + "You cannot gift cards to yourself.");
        }
        else sender.sendMessage(mInit.getMsgPrefix() + ChatColor.YELLOW + "/gift <player> <code> [quantity]");
        else sender.sendMessage(mInit.getMsgPermDeny());

    }

}
