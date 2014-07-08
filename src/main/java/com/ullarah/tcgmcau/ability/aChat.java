package com.ullarah.tcgmcau.ability;

import com.ullarah.tcgmcau.mInit;

import org.apache.commons.lang.StringUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import org.json.simple.JSONObject;

import java.util.*;

public class aChat implements Listener {

    private static String chatMode;

    public static void chatReplace(final Player player, JSONObject json){

        final String playerID = player.getUniqueId().toString();

        String chatType = (String) json.get( "type" );
        Long chatDuration = (Long) json.get( "duration" );

        String chatText = "";

        if( json.containsKey( "text" ) ){
            chatText = (String) json.get( "text" );
            chatText = chatText.replaceAll( "%user%", player.getPlayerListName() );
        }

        chatMode = chatType;

        mInit.chatDuration.put( playerID, chatDuration );
        mInit.chatString.put( playerID, chatText );

        Bukkit.getScheduler().scheduleSyncDelayedTask(mInit.getPlugin(), new Runnable() {
            @Override
            public void run() {
                mInit.chatDuration.remove( playerID );
                mInit.chatString.remove( playerID );
            }
        }, chatDuration * 20);

    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerChat( final AsyncPlayerChatEvent event ){

        String playerID = event.getPlayer().getUniqueId().toString();

        if( mInit.chatDuration.containsKey( playerID ) ){

            String chatString;

            if( mInit.chatDuration.get( playerID ).equals( (long) -1 ) ) chatMode = "CARDMESSAGE";

            switch( chatKeyword.valueOf( chatMode ) ){

                case CARDMESSAGE:
                    chatString = mInit.chatString.get( playerID );
                    event.setMessage( ChatColor.translateAlternateColorCodes( '&', chatString ) );
                    mInit.chatDuration.remove( playerID );
                    mInit.chatString.remove( playerID );
                    break;

                case HODOR:
                    chatString = mInit.chatString.get( playerID );
                    event.setMessage( ChatColor.translateAlternateColorCodes( '&', chatString ) );
                    break;

                case MIRROR:
                    chatString = new StringBuilder( event.getMessage() ).reverse().toString();
                    event.setMessage( chatString );
                    break;

                case REVERSE:
                    String[] chatReverseArray = StringUtils.split( event.getMessage() );
                    Collections.reverse(Arrays.asList(chatReverseArray));
                    event.setMessage( StringUtils.join( chatReverseArray, " " ) );
                    break;

                case SHUFFLE:
                    String[] chatShuffleArray = StringUtils.split( event.getMessage() );
                    Collections.reverse( Arrays.asList( chatShuffleArray ) );
                    event.setMessage( StringUtils.join( chatShuffleArray, " " ) );
                    break;

                case MULTIPLE:
                    chatString = mInit.chatString.get( playerID );
                    String[] chatMultipleArray = StringUtils.split( event.getMessage() );
                    int randomMultipleWord = new Random().nextInt( chatMultipleArray.length );
                    chatMultipleArray[ randomMultipleWord ] = "&f" + chatMultipleArray[ randomMultipleWord ].replaceAll("[^?]", chatString) + "&r";
                    event.setMessage( ChatColor.translateAlternateColorCodes( '&', StringUtils.join( chatMultipleArray, " " ) ) );
                    break;

                case SINGLE:
                    chatString = mInit.chatString.get( playerID );
                    String[] chatSingleArray = StringUtils.split( event.getMessage() );
                    int randomSingleWord = new Random().nextInt( chatSingleArray.length );
                    chatSingleArray[ randomSingleWord ] = "&f" + chatString + "&r";
                    event.setMessage( ChatColor.translateAlternateColorCodes( '&', StringUtils.join( chatSingleArray, " " ) ) );
                    break;

            }

        }

    }

    private enum chatKeyword {
        CARDMESSAGE,
        HODOR, MIRROR, REVERSE,
        SHUFFLE, MULTIPLE, SINGLE
    }

}