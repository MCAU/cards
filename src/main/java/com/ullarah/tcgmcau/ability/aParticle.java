package com.ullarah.tcgmcau.ability;

import com.ullarah.tcgmcau.mInit;
import com.ullarah.tcgmcau.particle.pEffect;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;

import org.json.simple.JSONObject;

public class aParticle {

    public static void showParticle( final LivingEntity entity, final JSONObject json ) {

        final String particleDisplay = (String) json.get( "effect" );
        final Double particleSpeed = (Double) json.get( "speed" );
        final Long particleAmount = (Long) json.get( "amount" );
        final Long particleDuration = (Long) json.get( "duration" );

        Location particleLocation = entity.getLocation();

        if( json.containsKey( "location" ) ){

            JSONObject particleLocationObject = (JSONObject) json.get( "location" );

            World particleLocationWorld = Bukkit.getWorld( String.valueOf( particleLocationObject.get( "world" ) ) );

            Double particleLocationX = (Double) particleLocationObject.get( "x" );
            Double particleLocationY = (Double) particleLocationObject.get( "y" );
            Double particleLocationZ = (Double) particleLocationObject.get( "z" );

            particleLocation = new Location( particleLocationWorld, particleLocationX, particleLocationY, particleLocationZ );

        }

        final Integer effectID;
        final Location finalParticleLocation = particleLocation;

        effectID = Bukkit.getScheduler().scheduleSyncRepeatingTask(mInit.getPlugin(), new Runnable() {
            @Override
            public void run() {
                if( json.containsKey( "location" ) )
                    pEffect.fromName( particleDisplay ).display( finalParticleLocation, particleSpeed.floatValue(), particleAmount.intValue() );
                else pEffect.fromName( particleDisplay ).display( entity.getLocation(), particleSpeed.floatValue(), particleAmount.intValue() );
            }
        }, 0, 5);

        Bukkit.getScheduler().scheduleSyncDelayedTask(mInit.getPlugin(), new Runnable() {
            @Override
            public void run() {
                Bukkit.getScheduler().cancelTask(effectID);
            }
       }, particleDuration * 20);

    }

}
