package com.ullarah.tcgmcau.ability;

import com.ullarah.tcgmcau.mInit;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.DisguiseConfig;
import me.libraryaddict.disguise.disguisetypes.*;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;

import org.json.simple.JSONObject;

public class aDisguise {

    public static void entityDisguise( final LivingEntity entity, JSONObject json ) {

        final String disguiseType = (String) json.get("type");
        final Boolean disguiseViewable = (Boolean) json.get("view");
        final Boolean disguiseOnFire = (Boolean) json.get("fire");

        FlagWatcher disguiseWatcher;

        if ( disguiseType.equals( "PLAYER" ) ) {

            String disguisePlayerName = (String) json.get("name");
            String disguisePlayerNameFinal = disguisePlayerName.replaceAll( "%user%", entity.getCustomName() );

            PlayerDisguise disguiseTransform = new PlayerDisguise( disguisePlayerNameFinal );
            DisguiseAPI.disguiseToAll(entity, disguiseTransform);
            disguiseWatcher = disguiseTransform.getWatcher();

        } else {

            MobDisguise disguiseTransform = new MobDisguise(DisguiseType.valueOf(disguiseType), true);
            DisguiseAPI.disguiseToAll(entity, disguiseTransform);
            disguiseWatcher = disguiseTransform.getWatcher();

        }

        DisguiseType.valueOf(disguiseType);

        DisguiseConfig.setUndisguiseOnWorldChange(false);
        DisguiseConfig.setViewDisguises(disguiseViewable != null ? disguiseViewable : false);

        disguiseWatcher.setBurning(disguiseOnFire != null ? disguiseOnFire : false);

        DisguiseConfig.setKeepDisguiseOnEntityDespawn(true);

        if (json.containsKey("duration")) {

            final Long disguiseDuration = (Long) json.get("duration");

            Bukkit.getScheduler().scheduleSyncDelayedTask(mInit.getPlugin(), new Runnable() {
                @Override
                public void run() {
                    DisguiseAPI.undisguiseToAll(entity);
                }
            }, (disguiseDuration + 5) * 20);

        }

    }

}