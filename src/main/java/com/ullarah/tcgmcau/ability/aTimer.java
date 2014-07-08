package com.ullarah.tcgmcau.ability;

import com.ullarah.tcgmcau.mInit;
import com.ullarah.tcgmcau.timer.tDragonTimer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import org.json.simple.JSONObject;

public class aTimer {

    private static final tDragonTimer timerBar = tDragonTimer.getInstance();

    private static Integer timerDurationLeft;

    public static void showTimer( final Player player, final JSONObject json ){

        final String timerTitle = (String) json.get( "title" );
        final Long timerDuration = (Long) json.get( "duration" );

        String timerMessageStart = null;
        String timerMessageWarn = null;
        String timerMessageEnd = null;

        if( json.containsKey( "message" ) ){
            JSONObject timerMessageObject = (JSONObject) json.get( "message" );

            timerMessageStart = (String) timerMessageObject.get( "start" );
            timerMessageWarn = (String) timerMessageObject.get( "warning" );
            timerMessageEnd = (String) timerMessageObject.get( "finish" );
        }

        final Integer timerID;

        timerDurationLeft = timerDuration.intValue();

        timerBar.bossTimer(player, timerTitle, 100, true);

        if( timerMessageStart != null ) player.sendMessage( mInit.getMsgPrefix() + ChatColor.YELLOW + timerMessageStart );

        timerID = Bukkit.getScheduler().scheduleSyncRepeatingTask(mInit.getPlugin(), new Runnable() {
            @Override
            public void run() {
                Integer timerPercentage = Integer.valueOf( String.format("%2.00f", ( (float) timerDurationLeft / timerDuration ) * 100 ).trim() );
                timerBar.bossTimer(player, timerTitle, timerPercentage, false);
                timerDurationLeft--;
            }
        }, 0, 20);

        if( timerMessageWarn != null ) {
            final String finalTimerMessageWarn1 = timerMessageWarn;
            Bukkit.getScheduler().scheduleSyncDelayedTask(mInit.getPlugin(), new Runnable() {
                @Override
                public void run() {
                    player.sendMessage(mInit.getMsgPrefix() + ChatColor.YELLOW + finalTimerMessageWarn1);
                }
            }, ( timerDuration - 5 ) * 20);
        }

        final String finalTimerMessageEnd = timerMessageEnd;
        Bukkit.getScheduler().scheduleSyncDelayedTask(mInit.getPlugin(), new Runnable() {
            @Override
            public void run() {
                Bukkit.getScheduler().cancelTask(timerID);
                timerBar.bossTimer(player, "", 0, false);
                if( finalTimerMessageEnd != null ) player.sendMessage( mInit.getMsgPrefix() + ChatColor.YELLOW + finalTimerMessageEnd);
            }
        }, ( timerDuration + 5 ) * 20 );



    }


}
