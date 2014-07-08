package com.ullarah.tcgmcau.ability;

import com.ullarah.tcgmcau.mInit;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import org.json.simple.JSONObject;

import java.util.Random;

public class aExperience {

    public static void playerExperience( final Player player, JSONObject json ){

        Long expValue = (Long) json.get("amount");
        String expState = (String) json.get("state");

        if( expState.equals( "RANDOM" ) ){

            String[] expRandomState = {"ADD","REMOVE","RANGE_ADD","RANGE_REMOVE"};
            expState = expRandomState[ new Random().nextInt( expRandomState.length ) ];

        }

        Integer expRange;

        switch ( expKeyword.valueOf( expState ) ) {

            case ADD:
                player.giveExpLevels( expValue.intValue() );
                player.sendMessage( mInit.getMsgPrefix() + "You have received " + ChatColor.GREEN + expValue + ChatColor.WHITE + " experience levels." );
                break;

            case REMOVE:
                player.giveExpLevels( -expValue.intValue() );
                player.sendMessage( mInit.getMsgPrefix() + ChatColor.GREEN + expValue + ChatColor.WHITE + " experience levels have been removed." );
                break;

            case RANGE_ADD:
                expRange = new Random().nextInt( expValue.intValue() );
                player.giveExpLevels( expRange );
                player.sendMessage( mInit.getMsgPrefix() + "You have received " + ChatColor.GREEN + expRange + ChatColor.WHITE + " experience levels." );
                break;

            case RANGE_REMOVE:
                expRange = new Random().nextInt( expValue.intValue() );
                player.giveExpLevels( -expRange );
                player.sendMessage( mInit.getMsgPrefix() + ChatColor.GREEN + expRange + ChatColor.WHITE + " experience levels have been removed." );
                break;

            case CHARITY:
                break;

            case TAX:
                break;

        }

    }

    private enum expKeyword {
        ADD, REMOVE, RANGE_ADD, RANGE_REMOVE,
        CHARITY, TAX
    }

}
