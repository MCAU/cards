package com.ullarah.tcgmcau.command;

import com.ullarah.tcgmcau.mInit;
import com.ullarah.tcgmcau.sql.sQuery;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import org.bukkit.entity.Player;
import org.json.simple.parser.ParseException;

import java.sql.ResultSet;
import java.sql.SQLException;

public class cRedeem {

    public static void runRedeemCard(CommandSender sender, String cardcode){

        try{

            Player player = (Player) sender;
            String playerID = String.valueOf( player.getUniqueId() ).replace( "-", "" );

            ResultSet res = mInit.getSqlConnection().sqlQuery("SELECT user FROM users WHERE uuid = ?", new String[]{playerID});

            if ( res.next() ) {

                String currentName = res.getString("user");

                if(mInit.getMaintenanceCheck()){

                    sender.sendMessage( mInit.getMsgPrefix() + ChatColor.RED + mInit.getMaintenanceMessage());
                    mInit.redeemCooldown.remove( playerID );

                } else {

                    if( currentName.equals( player.getPlayerListName() ) ) runValidCard(sender, cardcode);
                    else {
                        sender.sendMessage(mInit.getMsgPrefix() + ChatColor.RED + "Verification failed. " + ChatColor.WHITE + "Did you change your username?.");
                        mInit.redeemCooldown.remove( playerID );
                    }

                }

            }

            else {
                sender.sendMessage(mInit.getMsgPrefix() + "You need to be registered first.");
                mInit.redeemCooldown.remove( playerID );
            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

    }

    private static void runValidCard(CommandSender sender, String cardcode) {

        Player player = (Player) sender;
        String playerID = String.valueOf( player.getUniqueId() ).replace( "-", "" );

        String cardCode = cardcode.toLowerCase();

        String codeInvalid = mInit.getMsgPrefix() + ChatColor.RED + "Invalid Code.";
        String codeCheck = ChatColor.WHITE + " Please check the code word.";

        sQuery.runRefreshCards();

        if ( !cardCode.matches("(?i)[A-Z0-9]+") || !mInit.cardCode.containsKey( cardCode ) ) {

            sender.sendMessage(mInit.getMsgPrefix() + ChatColor.RED + "A invalid code is present.");
            mInit.redeemCooldown.remove( playerID );

            return;

        } else {

            try {

                ResultSet res = mInit.getSqlConnection().sqlQuery("SELECT amount FROM stash WHERE user = ? AND card = ?", new String[]{player.getPlayerListName(), mInit.cardCode.get(cardCode)});

                if (res.next()) {

                    Integer amount = res.getInt("amount");

                    if (amount == 0) {

                        sender.sendMessage(mInit.getMsgPrefix() + ChatColor.RED + "You do not have any of the " + ChatColor.WHITE + ChatColor.BOLD + cardCode + ChatColor.RESET + ChatColor.RED + " cards to redeem.");
                        mInit.redeemCooldown.remove( playerID );

                        return;

                    }

                }

            } catch (SQLException e) {

                e.printStackTrace();

            }

        }

        try {

            ResultSet res = mInit.getSqlConnection().sqlQuery("SELECT id, name FROM cards WHERE code = ? AND active='1'", new String[]{cardcode});

            if (!res.next()) {

                sender.sendMessage(codeInvalid + codeCheck);

            } else {

                Integer cardID = res.getInt("id");
                String cardName = res.getString("name");

                Integer cardAmount = sQuery.getPlayerSingleCardAmount( player, cardID );

                if (cardAmount > 0) {

                    sQuery.runRedeemCard( player, cardCode, false );

                } else {

                    sender.sendMessage("You do not have any of the " + cardName + " cards to redeem.");
                    mInit.redeemCooldown.remove( playerID );

                }

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

    }

}
