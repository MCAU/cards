package com.ullarah.tcgmcau.ability;

import com.ullarah.tcgmcau.mInit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Pig;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import org.bukkit.inventory.ItemStack;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class aSummon {

    private static List<LivingEntity> summonLivingEntities = new ArrayList<>();

    public static List<LivingEntity> playerSummon( final LivingEntity entity, final JSONObject json ){

        summonLivingEntities.clear();

        final String summonType = (String) json.get( "type" );

        String summonName = null;
        Double summonHealth = 100.0;
        Long summonDelay = null;
        Location summonLocation = entity.getEyeLocation();
        Long summonAmount = null;

        if( json.containsKey( "name" ) ) summonName = (String) json.get("name");

        if( json.containsKey( "health" ) ) summonHealth = (Double) json.get("health");

        if( json.containsKey( "delay" ) ) summonDelay = (Long) json.get("delay");

        if( json.containsKey( "amount" ) ) summonAmount = (Long) json.get("amount");

        if( json.containsKey( "location" ) ){

            JSONObject summonLocationObject = (JSONObject) json.get( "location" );

            World summonLocationWorld = Bukkit.getWorld( String.valueOf( summonLocationObject.get( "world" ) ) );

            Double summonLocationX = (Double) summonLocationObject.get( "x" );
            Double summonLocationY = (Double) summonLocationObject.get( "y" );
            Double summonLocationZ = (Double) summonLocationObject.get( "z" );

            summonLocation = new Location( summonLocationWorld, summonLocationX, summonLocationY, summonLocationZ );

        }

        final Location finalSummonLocation = summonLocation;

        final String finalSummonName = summonName;
        final Double finalSummonHealth = summonHealth;
        final Integer finalSummonAmount = summonAmount != null ? summonAmount.intValue() : 1;

        final World summonWorld = entity.getWorld();

        Bukkit.getScheduler().scheduleSyncDelayedTask(mInit.getPlugin(), new Runnable() {
            @Override
            public void run() {

                for (Integer i = 1; i <= finalSummonAmount; i++) {

                    LivingEntity summonEntity;

                    summonEntity = (LivingEntity) summonWorld.spawnEntity(finalSummonLocation, EntityType.valueOf(summonType));

                    if( json.containsKey( "particles" ) ) aParticle.showParticle( summonEntity, (JSONObject) json.get( "particles" ) );

                    if (json.containsKey("health")) {
                        summonEntity.setMaxHealth(finalSummonHealth);
                        summonEntity.setHealth(finalSummonHealth);
                    }

                    if (json.containsKey("name"))
                        summonEntity.setCustomName(finalSummonName);

                    if (json.containsKey("potion"))
                        aEffect.giveEffect(summonEntity, (JSONArray) json.get("effect"));

                    if (json.containsKey("disguise"))
                        aDisguise.entityDisguise(summonEntity, (JSONObject) json.get("disguise"));

                    if (summonEntity.getType() == EntityType.HORSE) {

                        Horse getHorse = (Horse) summonEntity;

                        getHorse.setOwner((Player) entity);
                        getHorse.setTamed(true);
                        getHorse.getInventory().setSaddle(new ItemStack(Material.SADDLE, 1));

                    }

                    if (summonEntity.getType() == EntityType.PIG) {

                        Pig getPig = (Pig) summonEntity;

                        if (json.containsKey("saddled")) {

                            Boolean isPigSaddled = (Boolean) json.get("saddled");
                            getPig.setSaddle(isPigSaddled);

                        }

                        getPig.setAdult();
                        getPig.setBreed(false);

                    }

                    summonLivingEntities.add(summonEntity);

                }

            }

        }, (summonDelay != null ? summonDelay : 1) * 20);

        return Collections.unmodifiableList( summonLivingEntities );

    }

}
