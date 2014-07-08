package com.ullarah.tcgmcau.ability;

import com.ullarah.tcgmcau.mInit;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class aStack {

    public static void entityStack( final LivingEntity entity, final JSONObject json ){

        JSONObject stackSourceObject = (JSONObject) json.get( "source" );

        final JSONArray stackPassengerArray = (JSONArray) json.get( "passengers" );

        final List<LivingEntity> stackSourceArray = aSummon.playerSummon( entity, stackSourceObject );

        Bukkit.getScheduler().runTaskLaterAsynchronously( mInit.getPlugin(), new Runnable() {
            @Override
            public void run() {

                final LivingEntity stackSource = stackSourceArray.get(0);

                List<LivingEntity> entityPassengerArray = new ArrayList<LivingEntity>();

                for( Object stackCurrentPassengerEntity : stackPassengerArray ) {

                    JSONObject stackCurrentObjectJSON = (JSONObject) stackCurrentPassengerEntity;

                    entityPassengerArray = aSummon.playerSummon( stackSource, stackCurrentObjectJSON );

                }

                // Why is a new final var needed here?
                final List<LivingEntity> finalEntityPassengerArray = entityPassengerArray;

                Bukkit.getScheduler().runTaskLaterAsynchronously( mInit.getPlugin(), new Runnable() {
                    @Override
                    public void run() {

                        LivingEntity stackPassenger = finalEntityPassengerArray.get(0);
                        stackSource.setPassenger( stackPassenger );

                        if( finalEntityPassengerArray.size() > 1 ){

                            for( int e = 1; e < finalEntityPassengerArray.size(); e++ ){

                                LivingEntity stackPassengerCurrent = finalEntityPassengerArray.get(e - 1);
                                stackPassengerCurrent.setPassenger( finalEntityPassengerArray.get(e) );

                            }

                        }

                    }

                }, 40);

            }

        }, 40);

    }

}
