package com.ullarah.tcgmcau.command;

import com.ullarah.tcgmcau.mConfig;
import com.ullarah.tcgmcau.mInit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class cChance {

    private static void changeMessage(CommandSender sender, String type, Integer value, Boolean change){

        String msgChancePrefix = mInit.getMsgPrefix() + "Chance Value: " + ChatColor.AQUA + type + ChatColor.WHITE;

        sender.sendMessage(change ? msgChancePrefix + " changed to " + ChatColor.YELLOW + value + ChatColor.WHITE + " percent." : msgChancePrefix + " currently at " + ChatColor.YELLOW + value + ChatColor.WHITE + " percent.");

    }

    public static void changeChanceValue(CommandSender sender, String[] args) {

        if (sender.hasPermission("cards.staff.chance") || !(sender instanceof Player)) if (args.length == 3)

            if (Integer.valueOf(args[2]) <= 100 && Integer.valueOf(args[2]) >= 0) {

                Integer value = Integer.valueOf(args[2]);

                chanceValue(sender, args[1], value, true);

            } else sender.sendMessage(mInit.getMsgPrefix() + ChatColor.RED + "Value must be between " + ChatColor.GOLD + "0" + ChatColor.RED + " and " + ChatColor.GOLD + "100");

        else if (args.length == 2) chanceValue(sender, args[1], 0, false);

        else sender.sendMessage(mInit.getMsgPrefix() + ChatColor.YELLOW + "/cards chance <type> [value]");

        else sender.sendMessage(mInit.getMsgPermDeny());

    }

    private static void chanceValue( CommandSender sender, String type, Integer value, Boolean change ){

        FileConfiguration chanceConfig = mConfig.getChanceConfig();

        switch( validChanceType.valueOf( type.toUpperCase() ) ){

            // -------------------------
            // Card Rarities
            // -------------------------
            case VERY_COMMON:
                if( change ) chanceConfig.set("rarity.VERY_COMMON", value);
                else value = chanceConfig.getInt("rarity.VERY_COMMON");
                changeMessage(sender, validChanceType.VERY_COMMON.getName(), value, change);
                break;
            case COMMON:
                if( change ) chanceConfig.set("rarity.COMMON", value);
                else value = chanceConfig.getInt("rarity.COMMON");
                changeMessage(sender, validChanceType.COMMON.getName(), value, change);
                break;
            case LESS_COMMON:
                if( change ) chanceConfig.set("rarity.LESS_COMMON", value);
                else value = chanceConfig.getInt("rarity.LESS_COMMON");
                changeMessage(sender, validChanceType.LESS_COMMON.getName(), value, change);
                break;
            case RARE:
                if( change ) chanceConfig.set("rarity.RARE", value);
                else value = chanceConfig.getInt("rarity.RARE");
                changeMessage(sender, validChanceType.RARE.getName(), value, change);
                break;
            case VERY_RARE:
                if( change ) chanceConfig.set("rarity.VERY_RARE", value);
                else value = chanceConfig.getInt("rarity.VERY_RARE");
                changeMessage(sender, validChanceType.VERY_RARE.getName(), value, change);
                break;
            case EXTRA_RARE:
                if( change ) chanceConfig.set("rarity.EXTRA_RARE", value);
                else value = chanceConfig.getInt("rarity.EXTRA_RARE");
                changeMessage(sender, validChanceType.EXTRA_RARE.getName(), value, change);
                break;


            // -------------------------
            // Passive Mobs
            // -------------------------
            case BAT:
                if( change ) chanceConfig.set("chance.passive.BAT", value);
                else value = chanceConfig.getInt("chance.passive.BAT");
                changeMessage(sender, validChanceType.BAT.getName(), value, change);
                break;
            case CHICKEN:
                if( change ) chanceConfig.set("chance.passive.CHICKEN", value);
                else value = chanceConfig.getInt("chance.passive.CHICKEN");
                changeMessage(sender, validChanceType.CHICKEN.getName(), value, change);
                break;
            case COW:
                if( change ) chanceConfig.set("chance.passive.COW", value);
                else value = chanceConfig.getInt("chance.passive.COW");
                changeMessage(sender, validChanceType.COW.getName(), value, change);
                break;
            case HORSE:
                if( change ) chanceConfig.set("chance.passive.HORSE", value);
                else value = chanceConfig.getInt("chance.passive.HORSE");
                changeMessage(sender, validChanceType.HORSE.getName(), value, change);
                break;
            case MUSHROOM_COW:
                if( change ) chanceConfig.set("chance.passive.MUSHROOM_COW", value);
                else value = chanceConfig.getInt("chance.passive.MUSHROOM_COW");
                changeMessage(sender, validChanceType.MUSHROOM_COW.getName(), value, change);
                break;
            case OCELOT:
                if( change ) chanceConfig.set("chance.passive.OCELOT", value);
                else value = chanceConfig.getInt("chance.passive.OCELOT");
                changeMessage(sender, validChanceType.OCELOT.getName(), value, change);
                break;
            case PIG:
                if( change ) chanceConfig.set("chance.passive.PIG", value);
                else value = chanceConfig.getInt("chance.passive.PIG");
                changeMessage(sender, validChanceType.PIG.getName(), value, change);
                break;
            case SHEEP:
                if( change ) chanceConfig.set("chance.passive.SHEEP", value);
                else value = chanceConfig.getInt("chance.passive.SHEEP");
                changeMessage(sender, validChanceType.SHEEP.getName(), value, change);
                break;
            case SQUID:
                if( change ) chanceConfig.set("chance.passive.SQUID", value);
                else value = chanceConfig.getInt("chance.passive.SQUID");
                changeMessage(sender, validChanceType.SQUID.getName(), value, change);
                break;
            case VILLAGER:
                if( change ) chanceConfig.set("chance.passive.VILLAGER", value);
                else value = chanceConfig.getInt("chance.passive.VILLAGER");
                changeMessage(sender, validChanceType.VILLAGER.getName(), value, change);
                break;


            // -------------------------
            // Neutral Mobs
            // -------------------------
            case ENDERMAN:
                if( change ) chanceConfig.set("chance.neutral.ENDERMAN", value);
                else value = chanceConfig.getInt("chance.neutral.ENDERMAN");
                changeMessage(sender, validChanceType.ENDERMAN.getName(), value, change);
                break;
            case IRON_GOLEM:
                if( change ) chanceConfig.set("chance.neutral.IRON_GOLEM", value);
                else value = chanceConfig.getInt("chance.neutral.IRON_GOLEM");
                changeMessage(sender, validChanceType.IRON_GOLEM.getName(), value, change);
                break;
            case PIG_ZOMBIE:
                if( change ) chanceConfig.set("chance.neutral.PIG_ZOMBIE", value);
                else value = chanceConfig.getInt("chance.neutral.PIG_ZOMBIE");
                changeMessage(sender, validChanceType.PIG_ZOMBIE.getName(), value, change);
                break;
            case WOLF:
                if( change ) chanceConfig.set("chance.neutral.WOLF", value);
                else value = chanceConfig.getInt("chance.neutral.WOLF");
                changeMessage(sender, validChanceType.WOLF.getName(), value, change);
                break;


            // -------------------------
            // Hostile Mobs
            // -------------------------
            case BLAZE:
                if( change ) chanceConfig.set("chance.hostile.BLAZE", value);
                else value = chanceConfig.getInt("chance.hostile.BLAZE");
                changeMessage(sender, validChanceType.BLAZE.getName(), value, change);
                break;
            case CAVE_SPIDER:
                if( change ) chanceConfig.set("chance.hostile.CAVE_SPIDER", value);
                else value = chanceConfig.getInt("chance.hostile.CAVE_SPIDER");
                changeMessage(sender, validChanceType.CAVE_SPIDER.getName(), value, change);
                break;
            case CREEPER:
                if( change ) chanceConfig.set("chance.hostile.CREEPER", value);
                else value = chanceConfig.getInt("chance.hostile.CREEPER");
                changeMessage(sender, validChanceType.CREEPER.getName(), value, change);
                break;
            case ENDER_DRAGON:
                if( change ) chanceConfig.set("chance.hostile.ENDER_DRAGON", value);
                else value = chanceConfig.getInt("chance.hostile.ENDER_DRAGON");
                changeMessage(sender, validChanceType.ENDER_DRAGON.getName(), value, change);
                break;
            case GHAST:
                if( change ) chanceConfig.set("chance.hostile.GHAST", value);
                else value = chanceConfig.getInt("chance.hostile.GHAST");
                changeMessage(sender, validChanceType.GHAST.getName(), value, change);
                break;
            case GIANT:
                if( change ) chanceConfig.set("chance.hostile.GIANT", value);
                else value = chanceConfig.getInt("chance.hostile.GIANT");
                changeMessage(sender, validChanceType.GIANT.getName(), value, change);
                break;
            case MAGMA_CUBE:
                if( change ) chanceConfig.set("chance.hostile.MAGMA_CUBE", value);
                else value = chanceConfig.getInt("chance.hostile.MAGMA_CUBE");
                changeMessage(sender, validChanceType.MAGMA_CUBE.getName(), value, change);
                break;
            case SILVERFISH:
                if( change ) chanceConfig.set("chance.hostile.SILVERFISH", value);
                else value = chanceConfig.getInt("chance.hostile.SILVERFISH");
                changeMessage(sender, validChanceType.SILVERFISH.getName(), value, change);
                break;
            case SLIME:
                if( change ) chanceConfig.set("chance.hostile.SLIME", value);
                else value = chanceConfig.getInt("chance.hostile.SLIME");
                changeMessage(sender, validChanceType.SLIME.getName(), value, change);
                break;
            case SPIDER:
                if( change ) chanceConfig.set("chance.hostile.SPIDER", value);
                else value = chanceConfig.getInt("chance.hostile.SPIDER");
                changeMessage(sender, validChanceType.SPIDER.getName(), value, change);
                break;
            case WITCH:
                if( change ) chanceConfig.set("chance.hostile.WITCH", value);
                else value = chanceConfig.getInt("chance.hostile.WITCH");
                changeMessage(sender, validChanceType.WITCH.getName(), value, change);
                break;
            case WITHER:
                if( change ) chanceConfig.set("chance.hostile.WITHER", value);
                else value = chanceConfig.getInt("chance.hostile.WITHER");
                changeMessage(sender, validChanceType.WITHER.getName(), value, change);
                break;


            // -------------------------
            // Skeleton Types
            // -------------------------
            case SKELETON_NORMAL:
                if( change ) chanceConfig.set("chance.hostile.SKELETON.NORMAL", value);
                else value = chanceConfig.getInt("chance.hostile.SKELETON.NORMAL");
                changeMessage(sender, validChanceType.SKELETON_NORMAL.getName(), value, change);
                break;
            case SKELETON_WITHER:
                if( change ) chanceConfig.set("chance.hostile.SKELETON.WITHER", value);
                else value = chanceConfig.getInt("chance.hostile.SKELETON.WITHER");
                changeMessage(sender, validChanceType.SKELETON_WITHER.getName(), value, change);
                break;


            // -------------------------
            // Zombie Types
            // -------------------------
            case ZOMBIE_NORMAL:
                if( change ) chanceConfig.set("chance.hostile.ZOMBIE.NORMAL", value);
                else value = chanceConfig.getInt("chance.hostile.ZOMBIE.NORMAL");
                changeMessage(sender, validChanceType.ZOMBIE_NORMAL.getName(), value, change);
                break;
            case ZOMBIE_VILLAGER:
                if( change ) chanceConfig.set("chance.hostile.ZOMBIE.VILLAGER", value);
                else value = chanceConfig.getInt("chance.hostile.ZOMBIE.VILLAGER");
                changeMessage(sender, validChanceType.ZOMBIE_VILLAGER.getName(), value, change);
                break;
            case ZOMBIE_BABY:
                if( change ) chanceConfig.set("chance.hostile.ZOMBIE.BABY", value);
                else value = chanceConfig.getInt("chance.hostile.ZOMBIE.BABY");
                changeMessage(sender, validChanceType.ZOMBIE_BABY.getName(), value, change);
                break;


            // -------------------------
            // mcMMO Abilities
            // -------------------------
            case ACROBATICS:
                if( change ) chanceConfig.set("chance.skills.ACROBATICS", value);
                else value = chanceConfig.getInt("chance.skills.ACROBATICS");
                changeMessage(sender, validChanceType.ACROBATICS.getName(), value, change);
                break;
            case ALCHEMY:
                if( change ) chanceConfig.set("chance.skills.ALCHEMY", value);
                else value = chanceConfig.getInt("chance.skills.ALCHEMY");
                changeMessage(sender, validChanceType.ALCHEMY.getName(), value, change);
                break;
            case ARCHERY:
                if( change ) chanceConfig.set("chance.skills.ARCHERY", value);
                else value = chanceConfig.getInt("chance.skills.ARCHERY");
                changeMessage(sender, validChanceType.ARCHERY.getName(), value, change);
                break;
            case AXES:
                if( change ) chanceConfig.set("chance.skills.AXES", value);
                else value = chanceConfig.getInt("chance.skills.AXES");
                changeMessage(sender, validChanceType.AXES.getName(), value, change);
                break;
            case EXCAVATION:
                if( change ) chanceConfig.set("chance.skills.EXCAVATION", value);
                else value = chanceConfig.getInt("chance.skills.EXCAVATION");
                changeMessage(sender, validChanceType.EXCAVATION.getName(), value, change);
                break;
            case FISHING:
                if( change ) chanceConfig.set("chance.skills.FISHING", value);
                else value = chanceConfig.getInt("chance.skills.FISHING");
                changeMessage(sender, validChanceType.FISHING.getName(), value, change);
                break;
            case HERBALISM:
                if( change ) chanceConfig.set("chance.skills.HERBALISM", value);
                else value = chanceConfig.getInt("chance.skills.HERBALISM");
                changeMessage(sender, validChanceType.HERBALISM.getName(), value, change);
                break;
            case MINING:
                if( change ) chanceConfig.set("chance.skills.MINING", value);
                else value = chanceConfig.getInt("chance.skills.MINING");
                changeMessage(sender, validChanceType.MINING.getName(), value, change);
                break;
            case REPAIR:
                if( change ) chanceConfig.set("chance.skills.REPAIR", value);
                else value = chanceConfig.getInt("chance.skills.REPAIR");
                changeMessage(sender, validChanceType.REPAIR.getName(), value, change);
                break;
            case SMELTING:
                if( change ) chanceConfig.set("chance.skills.SMELTING", value);
                else value = chanceConfig.getInt("chance.skills.SMELTING");
                changeMessage(sender, validChanceType.SMELTING.getName(), value, change);
                break;
            case SWORDS:
                if( change ) chanceConfig.set("chance.skills.SWORDS", value);
                else value = chanceConfig.getInt("chance.skills.SWORDS");
                changeMessage(sender, validChanceType.SWORDS.getName(), value, change);
                break;
            case TAMING:
                if( change ) chanceConfig.set("chance.skills.TAMING", value);
                else value = chanceConfig.getInt("chance.skills.TAMING");
                changeMessage(sender, validChanceType.TAMING.getName(), value, change);
                break;
            case UNARMED:
                if( change ) chanceConfig.set("chance.skills.UNARMED", value);
                else value = chanceConfig.getInt("chance.skills.UNARMED");
                changeMessage(sender, validChanceType.UNARMED.getName(), value, change);
                break;
            case WOODCUTTING:
                if( change ) chanceConfig.set("chance.skills.WOODCUTTING", value);
                else value = chanceConfig.getInt("chance.skills.WOODCUTTING");
                changeMessage(sender, validChanceType.WOODCUTTING.getName(), value, change);
                break;


            // -------------------------
            // Save Configuration
            // -------------------------
            case SAVE:
                mConfig.saveConfig();
                sender.sendMessage(mInit.getMsgPrefix() + ChatColor.AQUA + "Saved Chance Configuration...");

        }

    }

    public enum validChanceType {

        // Card Rarities
        VERY_COMMON("Very Common"), COMMON("Common"), LESS_COMMON("Less Common"),
        RARE("Rare"), VERY_RARE("Very Rare"), EXTRA_RARE("Extra Rare"),

        // Passive Mobs
        BAT("Bat"), CHICKEN("Chicken"), COW("Cow"), HORSE("Horse"), MUSHROOM_COW("Mooshroom"),
        OCELOT("Ocelot"), PIG("Pig"), SHEEP("Sheep"), SQUID("Squid"), VILLAGER("Villager"),

        // Neutral Mobs
        ENDERMAN("Enderman"), IRON_GOLEM("Iron Golem"), PIG_ZOMBIE("Pig Zombie"), WOLF("Wolf"),

        // Hostile Mobs
        BLAZE("Blaze"), CAVE_SPIDER("Cave Spider"), CREEPER("Creeper"), ENDER_DRAGON("Ender Dragon"),
        GHAST("Ghast"), GIANT("Giant"), MAGMA_CUBE("Magma Cube"), SILVERFISH("Silverfish"),
        SLIME("Slime"), SPIDER("Spider"), WITCH("Witch"), WITHER("Wither"),

        // Skeleton Types
        SKELETON_NORMAL("Normal Skeleton"), SKELETON_WITHER("Wither Skeleton"),

        // Zombie Types
        ZOMBIE_NORMAL("Zombie Normal"), ZOMBIE_VILLAGER("Zombie Villager"), ZOMBIE_BABY("Zombie Baby"),

        // mcMMO Abilities
        ACROBATICS("Acrobatics Skill"), ALCHEMY("Alchemy Skill"), ARCHERY("Archery Skill"),
        AXES("Axe Skill"), EXCAVATION("Excavation Skill"), FISHING("Fishing Skill"),
        HERBALISM("Herbalism Skill"), MINING("Mining Skill"), REPAIR("Repair Skill"),
        SMELTING("Smelting Skill"), SWORDS("Sword Skill"), TAMING("Taming Skill"),
        UNARMED("Unarmed Skill"), WOODCUTTING("Woodcutting Skill"),

        SAVE("");

        private final String name;

        private validChanceType(String s) {
            name = s;
        }

        public String toString(){
            return getName();
        }

        public String getName() {
            return name;
        }

        public static String[] allValues() {
            validChanceType[] states = values();
            String[] allValues = new String[states.length];

            for (int i = 0; i < states.length; i++) allValues[i] = states[i].name().toLowerCase();

            return allValues;
        }

    }

}