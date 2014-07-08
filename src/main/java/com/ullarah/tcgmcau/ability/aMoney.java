package com.ullarah.tcgmcau.ability;

import com.ullarah.tcgmcau.mInit;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import org.json.simple.JSONObject;

import java.util.Random;

public class aMoney {

    public static void playerMoney( final Player player, JSONObject json ){

        Long moneyAmount = (Long) json.get("amount");
        String moneyState = (String) json.get("state");

        if( moneyState.equals( "RANDOM" ) ){

            String[] moneyRandomState = {"ADD","REMOVE","RANGE_ADD","RANGE_REMOVE"};
            moneyState = moneyRandomState[ new Random().nextInt( moneyRandomState.length ) ];

        }

        Integer moneyRange;

        switch ( moneyKeyword.valueOf( moneyState ) ) {

            case ADD:
                mInit.getVaultEcon().depositPlayer(player, moneyAmount);
                player.sendMessage(mInit.getMsgPrefix() + "You have received " + ChatColor.GREEN + "$" + moneyAmount + ChatColor.WHITE + " in your balance!");
                break;

            case REMOVE:
                mInit.getVaultEcon().withdrawPlayer(player, moneyAmount);
                player.sendMessage(mInit.getMsgPrefix() + ChatColor.RED + "$" + moneyAmount + ChatColor.WHITE + " has been removed from your balance.");
                break;

            case RANGE_ADD:
                moneyRange = new Random().nextInt( moneyAmount.intValue() );
                mInit.getVaultEcon().depositPlayer(player, moneyRange);
                player.sendMessage(mInit.getMsgPrefix() + "You have received " + ChatColor.GREEN + "$" + moneyRange + ChatColor.WHITE + " in your balance!");
                break;

            case RANGE_REMOVE:
                moneyRange = new Random().nextInt( moneyAmount.intValue() );
                mInit.getVaultEcon().withdrawPlayer(player, moneyRange);
                player.sendMessage(mInit.getMsgPrefix() + ChatColor.RED + "$" + moneyRange + ChatColor.WHITE + " has been removed from your balance.");
                break;


            case CHARITY:
                break;

            case TAX:
                break;

        }

    }

    private enum moneyKeyword {
        ADD, REMOVE, RANGE_ADD, RANGE_REMOVE,
        CHARITY, TAX
    }

}
