package com.ullarah.tcgmcau.ability;

import com.ullarah.tcgmcau.mInit;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;

public class aUnique {

    public static void runUniqueAbility( final Player player, JSONArray json ) {

        for ( Object uniqueCurrentObject : json ) {

            final JSONObject uniqueCurrentObjectJSON = (JSONObject) uniqueCurrentObject;

            String uniqueType = (String) uniqueCurrentObjectJSON.get("type");

            final Double launchPower;

            switch( uniqueKeyword.valueOf( uniqueType ) ) {

                case LAUNCH:
                    launchPower = (Double) uniqueCurrentObjectJSON.get("power");

                    player.setFallDistance( -100.0F );
                    player.teleport( player.getLocation().add( 0,1 ,0 ) );
                    player.getPlayer().setVelocity( player.getPlayer().getLocation().getDirection().multiply( launchPower / 2 ) );
                    player.getPlayer().setVelocity( new Vector( player.getPlayer().getVelocity().getX(), player.getPlayer().getVelocity().getY() + launchPower, player.getPlayer().getVelocity().getZ() ) );

                    Bukkit.getScheduler().scheduleSyncDelayedTask(mInit.getPlugin(), new Runnable() {
                        @Override
                        public void run() {
                            player.setFallDistance(0);
                        }
                    }, (long) ( ( launchPower * 5 ) * 20 ) );

                    if( uniqueCurrentObjectJSON.containsKey("sound") ) aSound.playSound(player, (JSONObject) uniqueCurrentObjectJSON.get("sound"));
                    if( uniqueCurrentObjectJSON.containsKey("particles") ) aParticle.showParticle( player, (JSONObject) uniqueCurrentObjectJSON.get("particles") );
                    break;

                case LAUNCHENTITY:
                    JSONObject ejectEntityObject = (JSONObject) uniqueCurrentObjectJSON.get( "summon" );
                    launchPower = (Double) uniqueCurrentObjectJSON.get("power");
                    final List<LivingEntity> ejectEntitySummon = aSummon.playerSummon( player, ejectEntityObject );
                    Bukkit.getScheduler().runTaskLaterAsynchronously( mInit.getPlugin(), new Runnable() {
                        @Override
                        public void run() {

                            final LivingEntity ejectEntity = ejectEntitySummon.get(0);

                            ejectEntity.teleport( ejectEntity.getLocation().add( 0, 1, 0 ) );
                            ejectEntity.setVelocity( ejectEntity.getLocation().getDirection().multiply( launchPower / 2 ) );
                            ejectEntity.setVelocity( new Vector( ejectEntity.getVelocity().getX(), ejectEntity.getVelocity().getY() + launchPower, ejectEntity.getVelocity().getZ() ) );

                            if( uniqueCurrentObjectJSON.containsKey("sound") ) aSound.playSound(player, (JSONObject) uniqueCurrentObjectJSON.get("sound"));
                            if( uniqueCurrentObjectJSON.containsKey("particles") ) aParticle.showParticle( player, (JSONObject) uniqueCurrentObjectJSON.get("particles") );

                            if( uniqueCurrentObjectJSON.containsKey("explodetimer") ) {
                                Long explodeTimer = (Long) uniqueCurrentObjectJSON.get("explodetimer");
                                Bukkit.getScheduler().scheduleSyncDelayedTask(mInit.getPlugin(), new Runnable() {
                                    @Override
                                    public void run() {
                                        ejectEntity.getWorld().createExplosion( ejectEntity.getEyeLocation(), 0.0F );
                                        ejectEntity.setHealth(0.0);
                                    }
                                }, explodeTimer * 20);
                            }

                        }

                    }, 40);
                    break;

                case DOUBLEDROP:
                    JSONArray doubledropBlocks = (JSONArray) uniqueCurrentObjectJSON.get( "blocks" );
                    Long dropTimer = (Long) uniqueCurrentObjectJSON.get("droptimer");

                    mInit.playerDoubleDrop.put( player.getUniqueId(), doubledropBlocks );

                    Bukkit.getScheduler().scheduleSyncDelayedTask(mInit.getPlugin(), new Runnable() {
                        @Override
                        public void run() {
                            mInit.playerDoubleDrop.remove( player.getUniqueId() );
                        }
                    }, dropTimer * 20);
                    break;

                case SPIDERCLIMB:
                    final Long climbDuration = (Long) uniqueCurrentObjectJSON.get( "duration" );

                    mInit.playerSpider.put( player.getUniqueId(), true );

                    Bukkit.getScheduler().scheduleSyncDelayedTask(mInit.getPlugin(), new Runnable() {
                        @Override
                        public void run() {
                            mInit.playerSpider.remove( player.getUniqueId() );
                        }
                    }, climbDuration.intValue() * 20 );
                    break;

                case GLOWSTEP:
                    final Long glowstepDuration = (Long) uniqueCurrentObjectJSON.get( "duration" );

                    mInit.playerGlow.put( player.getUniqueId(), true );

                    Bukkit.getScheduler().scheduleSyncDelayedTask(mInit.getPlugin(), new Runnable() {
                        @Override
                        public void run() {
                            mInit.playerGlow.remove( player.getUniqueId() );
                        }
                    }, glowstepDuration.intValue() * 20 );
                    break;

                case WATERLAVA:
                    final Long waterlavaDuration = (Long) uniqueCurrentObjectJSON.get( "duration" );

                    mInit.playerWaterLava.put( player.getUniqueId(), true );

                    Bukkit.getScheduler().scheduleSyncDelayedTask(mInit.getPlugin(), new Runnable() {
                        @Override
                        public void run() {
                            mInit.playerWaterLava.remove( player.getUniqueId() );
                        }
                    }, waterlavaDuration.intValue() * 20 );
                    break;

            }

        }

    }

    private enum uniqueKeyword {
        LAUNCH, LAUNCHENTITY,
        DOUBLEDROP, SPIDERCLIMB,
        GLOWSTEP, WATERLAVA
    }

}
