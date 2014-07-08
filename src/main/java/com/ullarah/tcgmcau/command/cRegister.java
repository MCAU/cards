package com.ullarah.tcgmcau.command;

import com.ullarah.tcgmcau.mConfig;
import com.ullarah.tcgmcau.mInit;
import com.ullarah.tcgmcau.ext.eBCrypt;
import com.ullarah.tcgmcau.sql.sQuery;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class cRegister {

    public static void runRegister(CommandSender sender){

        Player player = (Player) sender;
        String playerID = player.getUniqueId().toString().replace( "-", "" );

        Player regPlayer = mInit.getPlugin().getServer().getPlayer( sQuery.getExistingPlayerUUID( player.getPlayerListName() ) );

        if( regPlayer == null ) {

            String tempCode = registerCode( player, playerID );

            sender.sendMessage( mInit.getMsgPrefix() + ChatColor.YELLOW + "http://cards.mcau.org/register/" + player.getPlayerListName() + "/" + tempCode );
            sender.sendMessage( mInit.getMsgPrefix() + "To join cards, please click on the link above." );

        } else {

            sender.sendMessage(mInit.getMsgPrefix() + "Already registered. Did you change your username?");
            sender.sendMessage(mInit.getMsgPrefix() + mConfig.getLanguageConfig().getString("registered"));

        }

    }

    private static String registerCode( Player player, String uuid ) {

        Integer passLength = 16 - Integer.valueOf( mInit.getPlugin().getConfig().getString( "password.length" ) );

        String passCode = Long.toHexString( Double.doubleToLongBits( Math.random() ) ).toUpperCase().substring( passLength );

        String passHash = eBCrypt.hashpw( passCode, eBCrypt.gensalt( Integer.valueOf( mInit.getPlugin().getConfig().getString( "password.salt" ) ) ) );

        mInit.getSqlConnection().sqlUpdate("DELETE FROM users_temp WHERE uuid = ?", new String[]{uuid});
        mInit.getSqlConnection().sqlUpdate("INSERT INTO users_temp VALUES (null, ?, ?, ?, '0' )", new String[]{player.getPlayerListName(), passHash, uuid});

        return passCode;

    }

}
