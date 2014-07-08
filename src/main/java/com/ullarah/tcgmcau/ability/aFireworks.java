package com.ullarah.tcgmcau.ability;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class aFireworks {

    public static void shootFireworks( final Player player, JSONArray json ){

        for ( Object fireworkCurrentObject : json ) {

            JSONObject fireworkCurrentObjectJSON = (JSONObject) fireworkCurrentObject;

            FireworkEffect.Type getType = FireworkEffect.Type.valueOf( (String) fireworkCurrentObjectJSON.get("type") );

            Boolean getFlicker = (Boolean) fireworkCurrentObjectJSON.get("flicker");
            Boolean getTrail = (Boolean) fireworkCurrentObjectJSON.get("trail");

            Color getColour = DyeColor.valueOf( (String) fireworkCurrentObjectJSON.get("colour") ).getFireworkColor();
            Color getFade = DyeColor.valueOf( (String) fireworkCurrentObjectJSON.get("fade") ).getFireworkColor();

            Long getPower = (Long) fireworkCurrentObjectJSON.get("power");
            Long getAmount = (Long) fireworkCurrentObjectJSON.get("amount");

            for( Integer f = 1; f <= getAmount; f++ ){

                Firework fireworkCurrent = (Firework) player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
                FireworkMeta fireworkMeta = fireworkCurrent.getFireworkMeta();

                FireworkEffect getEffect = FireworkEffect.builder().flicker(getFlicker).withColor(getColour).withFade(getFade).with(getType).trail(getTrail).build();

                fireworkMeta.addEffect( getEffect );
                fireworkMeta.setPower( getPower.intValue() );

                fireworkCurrent.setFireworkMeta(fireworkMeta);

            }

        }

    }

}
