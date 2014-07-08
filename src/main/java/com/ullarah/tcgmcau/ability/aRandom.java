package com.ullarah.tcgmcau.ability;

import com.ullarah.tcgmcau.command.cAbility;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Random;

public class aRandom {

    public static void selectRandomAbility( Player player, JSONArray json ){

        JSONObject randomAbility = (JSONObject) json.get( new Random().nextInt( json.size() ) );

        cAbility.runAbilityObject( player, randomAbility );

    }

}
