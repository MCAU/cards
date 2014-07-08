package com.ullarah.tcgmcau.ability;

import org.bukkit.entity.LivingEntity;
import org.json.simple.JSONObject;

public class aTransmute {

    public static void transmuteObject( LivingEntity entity, JSONObject json ) {

        String transmuteType = (String) json.get( "type" );
        JSONObject transmuteCurrent = (JSONObject) json.get( "source" );
        // TODO: Implement transmution

        switch( transformKeyword.valueOf( transmuteType ) ) {

            case ITEM:

                break;

            case ANIMAL:

                break;

            case PLAYER:

                break;

            case MOB:

                break;

        }

    }

    private enum transformKeyword {
        ITEM, ANIMAL,
        PLAYER, MOB
    }

}
