package com.ullarah.tcgmcau.ability;

import com.ullarah.tcgmcau.mInit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.Effect;
import org.bukkit.ChatColor;

import org.bukkit.entity.Player;

import org.json.simple.JSONObject;

public class aSound {

    public static void playSound(Player player, JSONObject json) {

        Location soundLocation;
        Sound soundID = Sound.valueOf( (String) json.get( "id" ) );
        String soundState = (String) json.get( "state" );
        Double soundVolume = (Double) json.get( "volume" );
        Double soundPitch = (Double) json.get( "pitch" );

        switch( soundKeyword.valueOf( soundState ) ) {

            case LOCAL:
                soundLocation = player.getEyeLocation();
                player.playSound( soundLocation, soundID, soundVolume.floatValue(), soundPitch.floatValue() );
                break;

            case GLOBAL:
                for( Player p: Bukkit.getOnlinePlayers() ){
                    soundLocation = p.getEyeLocation();
                    p.playSound( soundLocation, soundID, soundVolume.floatValue(), soundPitch.floatValue() );
                }
                break;

        }

    }

    public static void playRecord(Player player, JSONObject json){

        Material recordID = null;
        String recordName = null;

        switch( recordKeyword.valueOf( String.valueOf( json.get("play") ) ) ){

            case BLOCKS:
                recordID = Material.RECORD_3;
                recordName = "Blocks";
                break;

            case CAT:
                recordID = Material.GREEN_RECORD;
                recordName = "Cat";
                break;

            case CHIRP:
                recordID = Material.RECORD_4;
                recordName = "Chirp";
                break;

            case FAR:
                recordID = Material.RECORD_5;
                recordName = "Far";
                break;

            case MALL:
                recordID = Material.RECORD_6;
                recordName = "Mall";
                break;

            case MELLOHI:
                recordID = Material.RECORD_7;
                recordName = "Mellohi";
                break;

            case STAL:
                recordID = Material.RECORD_8;
                recordName = "Stal";
                break;

            case STRAD:
                recordID = Material.RECORD_9;
                recordName = "Strad";
                break;

            case WARD:
                recordID = Material.RECORD_10;
                recordName = "Ward";
                break;

            case WAIT:
                recordID = Material.RECORD_12;
                recordName = "Wait";
                break;

            case ELEVEN:
                recordID = Material.RECORD_11;
                recordName = "11";
                break;

            case THIRTEEN:
                recordID = Material.GOLD_RECORD;
                recordName = "13";
                break;

        }

        player.playEffect( player.getEyeLocation(), Effect.RECORD_PLAY, recordID );
        player.sendMessage(mInit.getMsgPrefix() + "Playing Record: " + ChatColor.LIGHT_PURPLE + "♫ " + ChatColor.GREEN + recordName + ChatColor.LIGHT_PURPLE + " ♫" );

    }

    private enum soundKeyword {
        LOCAL, GLOBAL
    }

    private enum recordKeyword {
        BLOCKS, CAT, CHIRP,
        FAR, MALL, MELLOHI,
        STAL, STRAD, WARD,
        WAIT, ELEVEN, THIRTEEN
    }

}
