package com.ullarah.tcgmcau.ability;

import com.ullarah.tcgmcau.mInit;

import org.bukkit.ChatColor;
import org.bukkit.Location;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

import org.json.simple.JSONObject;

import java.util.List;

public class aButcher {

    public static void killEntities( final Player player, JSONObject json ){

        Double butcherRadius = (Double) json.get( "radius" );
        String butcherType = (String) json.get( "type" );

        List<Entity> enemies = player.getNearbyEntities( butcherRadius, butcherRadius, butcherRadius );

        int butcherCount = 0;
        Location mobLocation;

        for( Entity e : enemies )
            if( e instanceof Monster) {

                switch( deathType.valueOf( butcherType ) ){

                    case HEALTH:
                        ((Monster) e).setHealth(0.0);
                        break;

                    case FIRE:
                        e.setFireTicks(3200);
                        break;

                    case EXPLOSION:
                        mobLocation = ((Monster) e).getEyeLocation();
                        mobLocation.getWorld().createExplosion( mobLocation, 0.0F );
                        ((Monster) e).setHealth(0.0);
                        break;

                    case REMOVE:
                        e.remove();
                        break;

                    case PARTICLEDEATH:
                        aParticle.showParticle( (LivingEntity) e, (JSONObject) json.get( "particles" ) );
                        ((Monster) e).setHealth(0.0);
                        break;

                    case PARTICLEREMOVE:
                        aParticle.showParticle( (LivingEntity) e, (JSONObject) json.get( "particles" ) );
                        e.remove();
                        break;

                    case LIGHTNING:
                        mobLocation = ((Monster) e).getEyeLocation();
                        mobLocation.getWorld().strikeLightning( mobLocation );
                        ((Monster) e).setHealth(0.0);

                }

                butcherCount++;

            }

        player.sendMessage( mInit.getMsgPrefix() + "Killed " + ChatColor.RED + butcherCount + ChatColor.WHITE + " monsters." );

    }

    private enum deathType {
        HEALTH, FIRE, EXPLOSION, REMOVE,
        PARTICLEDEATH, PARTICLEREMOVE,
        LIGHTNING
    }

}
