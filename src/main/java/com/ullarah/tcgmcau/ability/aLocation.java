package com.ullarah.tcgmcau.ability;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import org.json.simple.JSONObject;

public class aLocation {

    public static void playerLocation( final Player player, JSONObject json ){

        Location playerLocation = player.getLocation();

        World locationWorld = Bukkit.getWorld( (String) json.get( "world" ) );

        Double locationX = (Double) json.get( "x" );
        Double locationY = (Double) json.get( "y" );
        Double locationZ = (Double) json.get( "z" );

        Long locationYaw = null;
        Long locationPitch = null;

        if( json.containsKey( "yaw" ) ) { locationYaw = (Long) json.get("yaw"); }
        if( json.containsKey( "pitch" ) ) { locationPitch = (Long) json.get("pitch"); }

        playerLocation.setWorld( locationWorld );

        if( !locationX.equals(0.0) ){ playerLocation.setX( locationX ); }
        if( !locationY.equals(0.0) ){ playerLocation.setY( locationY ); }
        if( !locationZ.equals(0.0) ){ playerLocation.setZ( locationZ ); }

        if( json.containsKey( "yaw" ) ) { playerLocation.setYaw( locationYaw != null ? locationYaw.floatValue() : 0 ); }
        if( json.containsKey( "pitch" ) ) { playerLocation.setPitch( locationPitch != null ? locationPitch.floatValue() : 0 ); }

        player.teleport( playerLocation );

    }

}
