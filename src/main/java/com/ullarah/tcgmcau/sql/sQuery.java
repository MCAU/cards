package com.ullarah.tcgmcau.sql;

import com.ullarah.tcgmcau.command.cAbility;
import com.ullarah.tcgmcau.mInit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.json.simple.parser.ParseException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class sQuery {

    public static UUID getExistingPlayerUUID( String player ) {

        try {

            ResultSet res = mInit.getSqlConnection().sqlQuery("SELECT uuid FROM users WHERE user = ?", new String[]{player});

            if( res.next() ){

                String uuid = res.getString("uuid");

                return UUID.fromString( uuid.substring(0, 8) + "-" + uuid.substring(8, 12) + "-" + uuid.substring(12, 16) + "-" + uuid.substring(16, 20) + "-" + uuid.substring(20, 32) );

            }

        } catch (SQLException e) {

            e.printStackTrace();

            return null;

        }

        return null;

    }

    public static UUID getTempPlayerUUID( String player ) {

        String uuid;

        try {

            ResultSet res = mInit.getSqlConnection().sqlQuery("SELECT uuid FROM users_temp WHERE user = ?", new String[]{player});

            if( res.next() ){

                uuid = res.getString("uuid");

                return UUID.fromString( uuid.substring(0, 8) + "-" + uuid.substring(8, 12) + "-" + uuid.substring(12, 16) + "-" + uuid.substring(16, 20) + "-" + uuid.substring(20, 32) );

            }

        } catch (SQLException e) {

            e.printStackTrace();

            return null;

        }

        return null;

    }

    public static Boolean isPlayerRegistered( String player ){

        try {

            ResultSet res = mInit.getSqlConnection().sqlQuery("SELECT uuid FROM users WHERE user = ?", new String[]{player});

            if( res.next() ) return true;

        } catch (SQLException e) {

            e.printStackTrace();

            return false;

        }

        return false;

    }

    public static Integer getPlayerTotalCardAmount(String player){

        try {

            ResultSet res = mInit.getSqlConnection().sqlQuery("SELECT SUM(amount) AS total FROM stash INNER JOIN cards ON cards.id = stash.card WHERE cards.category BETWEEN '0' AND '9' AND cards.active = '1' AND stash.user = ?", new String[]{player});

            if( res.next() ) return res.getInt("total");

        } catch (SQLException e) {

            e.printStackTrace();

            return 0;

        }

        return 0;

    }

    public static Integer getPlayerCardAmountCategory( String player, Integer category ){

        try {

            ResultSet res = mInit.getSqlConnection().sqlQuery("SELECT SUM(amount) AS total FROM stash INNER JOIN cards ON cards.id = stash.card WHERE cards.category = ? AND cards.active = '1' AND stash.user = ?", new String[]{category.toString(), player});

            if( res.next() ) return res.getInt("total");

        } catch (SQLException e) {

            e.printStackTrace();

            mInit.getSqlConnection().sqlCloseConnection();

            return 0;

        }

        return 0;

    }

    public static Integer getCardCooldownRate( String cardcode ){

        try {

            ResultSet res = mInit.getSqlConnection().sqlQuery("SELECT cooldown FROM cards WHERE code = ?", new String[]{cardcode});

            if( res.next() ) return res.getInt("cooldown");

        } catch (SQLException e) {

            e.printStackTrace();

            return 0;

        }

        return 0;

    }

    public static void resetPlayerPassword( String player, String uuid ){

        mInit.getSqlConnection().sqlUpdate("DELETE FROM users WHERE user = ? AND uuid = ?", new String[]{player, uuid});

    }

    public static void resetPlayerUsername( String playerOld, String playerNew, String uuid ){

        mInit.getSqlConnection().sqlUpdate("UPDATE users SET user = ? WHERE user = ? AND uuid = ?", new String[]{playerNew, playerOld, uuid});

    }

    public static void runRefreshCards() {

        try {

            ResultSet res = mInit.getSqlConnection().sqlQuery("SELECT id, code FROM cards", new String[]{});

            while( res.next() ){

                String cardCode = res.getString("code").toLowerCase();
                String cardID = res.getString("id");

                mInit.cardCode.put(cardCode, cardID);

            }

            Bukkit.getLogger().info( "[" + mInit.getPlugin().getName() + "] Refreshing Enabled Cards" );

        } catch (SQLException e) {

            e.printStackTrace();

        }

    }

    public static int getPlayerSingleCardAmount( Player player, Integer cardID ){

        ResultSet res = mInit.getSqlConnection().sqlQuery("SELECT amount FROM stash WHERE user = ? AND card = ?", new String[]{player.getPlayerListName(), cardID.toString()});

        try {

            if (res.next()) return res.getInt("amount");

        } catch (SQLException e) {

            e.printStackTrace();

            return 0;

        }

        return 0;

    }

    public static int getCardID( String cardCode ){

        ResultSet res = mInit.getSqlConnection().sqlQuery("SELECT id FROM cards WHERE code = ?", new String[]{cardCode});

        try {

            if (res.next()) return res.getInt("id");

        } catch (SQLException e) {

            e.printStackTrace();

            return 0;

        }

        return 0;

    }

    public static void runRedeemCard( Player player, String cardCode, Boolean cardForced ){

        String playerID = player.getUniqueId().toString();

        Boolean isError = false;

        ResultSet res = mInit.getSqlConnection().sqlQuery("SELECT ability FROM cards WHERE code = ?", new String[]{cardCode});

        try {

            if (res.next()) {

                try {

                    cAbility json = new cAbility();

                    String cardAbility = res.getString("ability");

                    json.jsonAbility( cardAbility, player );

                } catch (ParseException e) {

                    isError = true;

                    player.sendMessage(mInit.getMsgPrefix() + ChatColor.RED + "ERROR: " + ChatColor.YELLOW + "redeem.j." + cardCode);

                    mInit.redeemCooldown.remove(playerID);

                } finally {

                    if (!isError && !cardForced) {

                        Integer cardID = getCardID( cardCode );

                        Integer newAmount = getPlayerSingleCardAmount( player, cardID ) - 1;

                        mInit.getSqlConnection().sqlUpdate("UPDATE stash SET amount = ? WHERE card = ? AND user = ?", new String[]{newAmount.toString(), cardID.toString(), player.getPlayerListName()});

                    }

                }

            } else {

                player.sendMessage( mInit.getMsgPrefix() + ChatColor.RED + "Invalid Code." + ChatColor.WHITE + " Please check the code word." );
                mInit.redeemCooldown.remove( playerID );

            }

        } catch (SQLException e) {

            e.printStackTrace();
            mInit.redeemCooldown.remove( playerID );

        }

    }

}
