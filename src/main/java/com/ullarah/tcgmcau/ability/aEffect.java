package com.ullarah.tcgmcau.ability;

import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class aEffect {

    public static void giveEffect(LivingEntity entity, JSONArray json) {

        for ( Object potionCurrentObject : json ) {

            JSONObject potionCurrentObjectJSON = (JSONObject) potionCurrentObject;

            String potionType = (String) potionCurrentObjectJSON.get( "type" );
            Long potionDuration = (Long) potionCurrentObjectJSON.get( "duration" );
            Long potionStrength = (Long) potionCurrentObjectJSON.get( "strength" );
            Boolean potionAmbient = (Boolean) potionCurrentObjectJSON.get( "ambient" );

            PotionEffect potionCreate = new PotionEffect( PotionEffectType.getByName( potionType ), potionDuration.intValue() * 20, potionStrength.intValue(), potionAmbient );

            entity.addPotionEffect(potionCreate);

        }

    }

}
