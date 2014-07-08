package com.ullarah.tcgmcau.command;

import com.ullarah.tcgmcau.mConfig;
import com.ullarah.tcgmcau.mInit;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class cCategory {

    public static void toggleCategoryState( CommandSender sender, String[] args ){

        if( args.length < 2 ){

            sender.sendMessage( mInit.getMsgPrefix() + "Category State For: " + ChatColor.AQUA + sender.getName() );
            sender.sendMessage( ChatColor.GRAY + "Very Common" + ChatColor.WHITE + " : " + stateCategory(sender.getName(), "VERY_COMMON") );
            sender.sendMessage( ChatColor.GREEN + "Common" + ChatColor.WHITE + " : " + stateCategory( sender.getName(), "COMMON" ) );
            sender.sendMessage( ChatColor.DARK_AQUA + "Less Common" + ChatColor.WHITE + " : " + stateCategory( sender.getName(), "LESS_COMMON" ) );
            sender.sendMessage( ChatColor.BLUE + "Rare" + ChatColor.WHITE + " : " + stateCategory( sender.getName(), "RARE" ) );
            sender.sendMessage( ChatColor.LIGHT_PURPLE + "Very Rare" + ChatColor.WHITE + " : " + stateCategory( sender.getName(), "VERY_RARE" ) );
            sender.sendMessage( ChatColor.RED + "Extra Rare" + ChatColor.WHITE + " : " + stateCategory( sender.getName(), "EXTRA_RARE" ) );
            sender.sendMessage(mInit.getMsgPrefix() + ChatColor.GOLD + "You can toggle the state of these by doing:");
            sender.sendMessage(mInit.getMsgPrefix() + ChatColor.YELLOW + "/cards cat " + ChatColor.AQUA + "all" + ChatColor.YELLOW + " or " + ChatColor.AQUA + "category");

        } else try {

            switch (validCategory.valueOf(args[1].toUpperCase())) {

                case VERYCOMMON:
                    sender.sendMessage(stateToggle(sender.getName(), "VERY_COMMON", ChatColor.GRAY + "Very Common"));
                    break;

                case COMMON:
                    sender.sendMessage(stateToggle(sender.getName(), "COMMON", ChatColor.GREEN + "Common"));
                    break;

                case LESSCOMMON:
                    sender.sendMessage(stateToggle(sender.getName(), "LESS_COMMON", ChatColor.DARK_AQUA + "Less Common"));
                    break;

                case RARE:
                    sender.sendMessage(stateToggle(sender.getName(), "RARE", ChatColor.BLUE + "Rare"));
                    break;

                case VERYRARE:
                    sender.sendMessage(stateToggle(sender.getName(), "VERY_RARE", ChatColor.LIGHT_PURPLE + "Very Rare"));
                    break;

                case EXTRARARE:
                    sender.sendMessage(stateToggle(sender.getName(), "EXTRA_RARE", ChatColor.RED + "Extra Rare"));
                    break;

                case ALL:
                    String[] allCategories = {"VERY_COMMON", "COMMON", "LESS_COMMON", "RARE", "VERY_RARE", "EXTRA_RARE"};
                    for (String cat : allCategories)
                        stateToggle(sender.getName(), cat, "");
                    sender.sendMessage( mInit.getMsgPrefix() + ChatColor.AQUA + "All card categories have been reversed." );
                    break;

            }

        } catch (IllegalArgumentException e) {

            sender.sendMessage(mInit.getMsgPrefix() + ChatColor.RED + "Wrong Category Prefix!");
            sender.sendMessage(mInit.getMsgPrefix() + ChatColor.YELLOW + "/cards cat " + ChatColor.AQUA + "all" + ChatColor.YELLOW + " or " + ChatColor.AQUA + "category");

        }

    }

    private static String stateToggle( String player, String category, String catname ) {

        String path = player + ".rarity." + category;

        if( mConfig.getPlayerConfig().getBoolean( path ) ) {
            mConfig.getPlayerConfig().set( path, false );
            return mInit.getMsgPrefix() + ChatColor.RED + "You will no longer get " + catname + ChatColor.RED + " cards.";
        } else {
            mConfig.getPlayerConfig().set( path, true );
            return mInit.getMsgPrefix() + ChatColor.GREEN + "You will now get " + catname + ChatColor.GREEN + " cards.";
        }

    }

    private static String stateCategory( String player, String category ) {

        return mConfig.getPlayerConfig().getBoolean(player + ".rarity." + category) ? ChatColor.GREEN + "TRUE" : ChatColor.RED + "FALSE";

    }

    public enum validCategory {
        VERYCOMMON, COMMON, LESSCOMMON,
        RARE, VERYRARE, EXTRARARE,
        ALL;

        public static String[] allValues() {
            validCategory[] states = values();
            String[] names = new String[states.length];

            for (int i = 0; i < states.length; i++) names[i] = states[i].name().toLowerCase();

            return names;
        }

    }

}
