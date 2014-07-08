package com.ullarah.tcgmcau.ability;

import org.bukkit.entity.Player;

import org.json.simple.JSONObject;

public class aKill {

    public static void playerKill( final Player player, JSONObject json ){

        JSONObject killObject = (JSONObject) json.get( "kill" );

        if( killObject.containsKey( "explosion" ) ){

            Long killExplosion = (Long) killObject.get( "explosion" );

            player.getWorld().createExplosion( player.getLocation(), killExplosion.floatValue() );

            player.setHealth(0.0);

        } else {

            player.setHealth(0.0);

        }

    }

}
