package com.ullarah.tcgmcau.command;

import com.ullarah.tcgmcau.ability.*;
import com.ullarah.tcgmcau.mInit;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.Random;

public class cAbility {

    private final JSONParser jsonParser = new JSONParser();

    public void jsonAbility(String json, Player player) throws ParseException {

        final JSONObject abilityObject = (JSONObject) jsonParser.parse(json);

        if( abilityObject.containsKey("chance") ) {

            JSONObject chanceObject = (JSONObject) abilityObject.get( "chance" );

            Integer chanceMaximum = new Random().nextInt( 100 );
            Long chanceValue = (Long) chanceObject.get("value");

            if( chanceMaximum < chanceValue ) runAbilityObject(player, abilityObject);
            else player.sendMessage( mInit.getMsgPrefix() + ChatColor.YELLOW + ChatColor.translateAlternateColorCodes( '&', String.valueOf( chanceObject.get("message") ) ) );

        } else runAbilityObject(player, abilityObject);

    }

    public static void runAbilityObject( Player player, JSONObject abilityObjectJSON ) {

        for( Object currentKey : abilityObjectJSON.keySet() ){

            try{

                switch( validAbilities.valueOf( currentKey.toString().toUpperCase() ) ){

                    case MESSAGE:
                        aMessages.sayMessage( player, (JSONObject) abilityObjectJSON.get( currentKey ) );
                        break;

                    case RECORD:
                        aSound.playRecord( player, (JSONObject) abilityObjectJSON.get( currentKey ) );
                        break;

                    case SOUND:
                        aSound.playSound( player, (JSONObject) abilityObjectJSON.get( currentKey ) );
                        break;

                    case EFFECT:
                        aEffect.giveEffect( player, (JSONArray) abilityObjectJSON.get( currentKey ) );
                        break;

                    case POTION:
                        aPotion.givePotion( player, (JSONArray) abilityObjectJSON.get( currentKey ) );
                        break;

                    case FLY:
                        aFly.playerFly( player, (JSONObject) abilityObjectJSON.get( currentKey ) );
                        break;

                    case WEATHER:
                        aWeather.setWeather( player, (JSONObject) abilityObjectJSON.get( currentKey ) );
                        break;

                    case TRANSFORM:
                        aDisguise.entityDisguise( player, (JSONObject) abilityObjectJSON.get( currentKey ) );
                        break;

                    case LOCATION:
                        aLocation.playerLocation( player, (JSONObject) abilityObjectJSON.get( currentKey ) );
                        break;

                    case ITEM:
                        aItem.giveItem( player, (JSONArray) abilityObjectJSON.get( currentKey ) );
                        break;

                    case CHEST:
                        aChest.showChest( player, (JSONObject) abilityObjectJSON.get( currentKey ) );
                        break;

                    case PORTABLE:
                        aPortable.openInterface( player, (JSONObject) abilityObjectJSON.get( currentKey ) );
                        break;

                    case FIREWORKS:
                        aFireworks.shootFireworks( player, (JSONArray) abilityObjectJSON.get( currentKey ) );
                        break;

                    case PARTICLES:
                        aParticle.showParticle( player, (JSONObject) abilityObjectJSON.get( currentKey ) );
                        break;

                    case MONEY:
                        aMoney.playerMoney( player, (JSONObject) abilityObjectJSON.get( currentKey ) );
                        break;

                    case STACK:
                        aStack.entityStack( player, (JSONObject) abilityObjectJSON.get( currentKey ) );
                        break;

                    case SUMMON:
                        aSummon.playerSummon( player, (JSONObject) abilityObjectJSON.get( currentKey ) );
                        break;

                    case TIME:
                        aTime.setTime( player, (JSONObject) abilityObjectJSON.get( currentKey ) );
                        break;

                    case EXPERIENCE:
                        aExperience.playerExperience( player, (JSONObject) abilityObjectJSON.get( currentKey ) );
                        break;

                    case HEAD:
                        aHead.giveHead( player, (JSONObject) abilityObjectJSON.get( currentKey ) );
                        break;

                    case KICK:
                        aKick.playerKick( player, (JSONObject) abilityObjectJSON.get( currentKey ) );
                        break;

                    case KILL:
                        aKill.playerKill( player, (JSONObject) abilityObjectJSON.get( currentKey ) );
                        break;

                    case BRAND:
                        aBrand.playerBrand( player, (JSONObject) abilityObjectJSON.get( currentKey ) );
                        break;

                    case COMMAND:
                        aCommands.rawCommands( player, (JSONArray) abilityObjectJSON.get( currentKey ) );
                        break;

                    case TIMER:
                        aTimer.showTimer( player, (JSONObject) abilityObjectJSON.get( currentKey ) );
                        break;

                    case SCOREBOARD:
                        aScoreboard.setScoreboard( player, (JSONArray) abilityObjectJSON.get( currentKey ) );
                        break;

                    case GHOST:
                        aGhost.setPlayerGhost( player, (JSONObject) abilityObjectJSON.get( currentKey ) );
                        break;

                    case ARMOUR:
                        aArmour.giveArmour( player, (JSONArray) abilityObjectJSON.get( currentKey ) );
                        break;

                    case COOLDOWN:
                        aCooldown.playerCooldown( player, (JSONObject) abilityObjectJSON.get( currentKey ) );
                        break;

                    case CHAT:
                        aChat.chatReplace( player, (JSONObject) abilityObjectJSON.get(currentKey) );
                        break;

                    case BUTCHER:
                        aButcher.killEntities( player, (JSONObject) abilityObjectJSON.get(currentKey) );
                        break;

                    case UNIQUE:
                        aUnique.runUniqueAbility( player, (JSONArray) abilityObjectJSON.get(currentKey) );
                        break;

                    case RANDOM:
                        aRandom.selectRandomAbility( player, (JSONArray) abilityObjectJSON.get(currentKey) );
                        break;

                    case CREDIT:
                        cSecret.showCredits( player );
                        break;

                    case CHANCE:
                        break;

                }

            } catch ( IllegalArgumentException e ){

                e.printStackTrace();
                player.sendMessage( mInit.getMsgPrefix() + ChatColor.RED + "Card Error. " + ChatColor.YELLOW + "Tell staff this code: RAO.json" );

            }

        }

    }

    private enum validAbilities {
        MESSAGE, SOUND, RECORD, POTION,
        FLY, WEATHER, TRANSFORM, LOCATION,
        ITEM, CHEST, PORTABLE, FIREWORKS,
        PARTICLES, MONEY, SUMMON, TIME,
        EXPERIENCE, HEAD, KICK, KILL,
        BRAND, COMMAND, TIMER, CHANCE,
        EFFECT, ARMOUR, COOLDOWN, CHAT,
        SCOREBOARD, GHOST, CREDIT, STACK,
        BUTCHER, UNIQUE, RANDOM
    }

}
  
