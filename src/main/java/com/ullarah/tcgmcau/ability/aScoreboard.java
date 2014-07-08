package com.ullarah.tcgmcau.ability;

import com.ullarah.tcgmcau.mInit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

public class aScoreboard {

    private static HashMap<UUID, Scoreboard> playerScoreboards = new HashMap<>();
    private static HashMap<UUID, Objective> playerObjectives = new HashMap<>();
    private static NavigableMap<String, Object> playerScores = new TreeMap<>();

    private static SortedMap<String, Object> getByPrefix( NavigableMap<String, Object> myMap, String prefix ) {
        return myMap.subMap( prefix, prefix + Character.MAX_VALUE );
    }

    public static void setScoreboard( final Player player, JSONArray json ){

        for ( Object sbCurrentObject : json ) {

            JSONObject sbCurrentObjectJSON = (JSONObject) sbCurrentObject;

            Integer sbCurrent = json.indexOf( sbCurrentObject );

            String sbEffect = (String) sbCurrentObjectJSON.get( "type" );
            if( sbEffect.length() > 16 ) sbEffect = sbEffect.substring(0,16);

            final Long[] sbDuration = {(Long) sbCurrentObjectJSON.get("duration")};

            final String sbCurrentScore = player.getPlayerListName() + "." + sbEffect;

            ScoreboardManager sbManager;
            final Scoreboard sbBoard;
            final Score sbScore;

            Team sbTeam;

            final Objective sbObjective;

            if( !playerScoreboards.containsKey( player.getUniqueId() ) ){

                sbManager = Bukkit.getScoreboardManager();
                sbBoard = sbManager.getNewScoreboard();

                sbTeam = sbBoard.registerNewTeam( player.getUniqueId().toString().replace( "-", "" ).substring( 0, 16 ) );
                sbTeam.addPlayer( player );

                playerScoreboards.put( player.getUniqueId(), sbBoard );

            } else {

                sbBoard = playerScoreboards.get( player.getUniqueId() );

            }

            if( !playerObjectives.containsKey( player.getUniqueId() ) ){

                sbObjective = sbBoard.registerNewObjective( "Card Effects", "dummy" );
                sbObjective.setDisplaySlot( DisplaySlot.SIDEBAR );
                sbObjective.setDisplayName( ChatColor.AQUA + "" + ChatColor.BOLD + "Card Effects" );

                playerObjectives.put(player.getUniqueId(), sbObjective);

            } else {

                sbObjective = playerObjectives.get( player.getUniqueId() );

            }

            if( playerScores.containsKey( sbCurrentScore ) ) sbBoard.resetScores( sbCurrentScore );

            sbScore = sbObjective.getScore( ChatColor.translateAlternateColorCodes( '&', sbEffect ) );
            sbScore.setScore( sbDuration[0].intValue() );
            playerScores.put( sbCurrentScore, sbScore );

            player.setScoreboard( sbBoard );

            final Integer sbTimer;
            final Integer[] sbDurationFinal = {sbDuration[0].intValue()};

            sbTimer = Bukkit.getScheduler().scheduleSyncRepeatingTask(mInit.getPlugin(), new Runnable() {
                @Override
                public void run() {
                    sbScore.setScore( sbDurationFinal[0] );
                    sbDurationFinal[0]--;
                }
            }, (sbCurrent * 2), 20);

            Bukkit.getScheduler().scheduleSyncDelayedTask(mInit.getPlugin(), new Runnable() {
                @Override
                public void run() {
                    Bukkit.getScheduler().cancelTask( sbTimer );
                    playerScores.remove( sbCurrentScore );
                    sbBoard.resetScores( sbCurrentScore );

                    if( getByPrefix( playerScores, player.getPlayerListName() ).isEmpty() ){
                        playerObjectives.remove( player.getUniqueId() );
                        sbObjective.unregister();
                    }
                }
            }, (sbDuration[0].intValue() + 1) * 20 );

        }

    }

}
