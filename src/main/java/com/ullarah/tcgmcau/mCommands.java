package com.ullarah.tcgmcau;

import com.ullarah.tcgmcau.command.*;
import com.ullarah.tcgmcau.ext.eMapImage;
import com.ullarah.tcgmcau.sql.sQuery;

import net.minecraft.util.org.apache.commons.lang3.ArrayUtils;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class mCommands implements CommandExecutor, TabCompleter{

    @Override
    public boolean onCommand( CommandSender sender, Command command, String s, String[] args ) {

        if( command.getName().equalsIgnoreCase( "cards" ) )
            commandCards( sender, args );

        if( command.getName().equalsIgnoreCase( "redeem" ) ) if (!(sender instanceof Player))
            sender.sendMessage(mInit.getMsgNoConsole());

        else if (!mInit.getMaintenanceCheck()) if (args.length > 0) {
            String cardCode = args[0].toLowerCase();
            if (!cardCode.matches("(?i)[A-Z0-9]+") || !mInit.cardCode.containsKey(cardCode))
                sender.sendMessage(mInit.getMsgPrefix() + ChatColor.RED + "A invalid code is present.");
            else if (redeemCooldown(sender, cardCode))
                cRedeem.runRedeemCard(sender, cardCode);
        } else sender.sendMessage(mInit.getMsgPrefix() + ChatColor.YELLOW + "/redeem <code>");
        else sender.sendMessage(mInit.getMaintenanceMessage());

        if( command.getName().equalsIgnoreCase( "gift" ) ) if (!(sender instanceof Player))
            sender.sendMessage(mInit.getMsgNoConsole());
        else if (!mInit.getMaintenanceCheck())
            cGift.runGiftCard(sender, args);
        else sender.sendMessage(mInit.getMaintenanceMessage());

        return true;

    }

    private boolean redeemCooldown( CommandSender sender, String cardcode ){

        if( sender.hasPermission( "cards.staff" ) ) return true;

        Player player = (Player) sender;
        String playerID = String.valueOf( player.getUniqueId() ).replace( "-", "" );
        Integer cardCooldown = sQuery.getCardCooldownRate( cardcode );

        if( mInit.redeemCooldown.containsKey( playerID ) ) {

            long secondsLeft = ( ( mInit.redeemCooldown.get( playerID ) / 1000 ) + cardCooldown ) - ( System.currentTimeMillis() / 1000 );

            if( secondsLeft > 0 ) {

                sender.sendMessage( mInit.getMsgPrefix() + "You cannot redeem for another " + ChatColor.YELLOW + secondsLeft + ChatColor.WHITE + " seconds!");

                return false;

            }

        }

        mInit.redeemCooldown.put( playerID, System.currentTimeMillis() );

        return true;

    }

    private void commandCards( CommandSender sender, String[] args ){

        String consoleTools = mInit.getMsgPrefix() + ChatColor.WHITE + "advert | check | force | idreset | pwreset | purge | give | random | unbrand | reload | maintenance | version";

        if ( args.length == 0 ) {

            if( !( sender instanceof Player ) )
                sender.sendMessage( consoleTools );
            else
                cHelp.runHelp(sender);

        }

        else try {

            switch ( validCommands.valueOf( args[0].toUpperCase() ) ) {

                case REGISTER:
                    if( !( sender instanceof Player ) )
                        sender.sendMessage(mInit.getMsgNoConsole());
                    else
                        if( !mInit.getMaintenanceCheck())
                            cRegister.runRegister(sender);
                        else
                            sender.sendMessage(mInit.getMaintenanceMessage());
                    break;

                case CHECK:
                    if( !mInit.getMaintenanceCheck())
                        cCheck.runPlayerCardInfo(sender, args);
                    else
                        sender.sendMessage(mInit.getMaintenanceMessage());
                    break;

                case ABOUT:
                    if( !( sender instanceof Player ) )
                        sender.sendMessage(mInit.getMsgNoConsole());
                    else
                        cHelp.runAbout(sender);
                    break;

                case HELP:
                    if( !( sender instanceof Player ) )
                        sender.sendMessage(mInit.getMsgNoConsole());
                    else
                        cHelp.runHelp(sender);
                    break;

                case SHELP:
                    if( !( sender instanceof Player ) )
                        sender.sendMessage(mInit.getMsgNoConsole());
                    else
                        cHelp.runStaffHelp(sender);
                    break;

                case REDEEM:
                    if( !( sender instanceof Player ) )
                        sender.sendMessage(mInit.getMsgNoConsole());
                    else
                    if( !mInit.getMaintenanceCheck()) if (args.length > 1) {
                        String cardCode = args[1].toLowerCase();
                        if (!cardCode.matches("(?i)[A-Z0-9]+") || !mInit.cardCode.containsKey(cardCode))
                            sender.sendMessage(mInit.getMsgPrefix() + ChatColor.RED + "A invalid code is present.");
                        else if (redeemCooldown(sender, cardCode))
                            cRedeem.runRedeemCard(sender, cardCode);
                    } else sender.sendMessage(mInit.getMsgPrefix() + ChatColor.YELLOW + "/cards redeem <code>");
                    else
                        sender.sendMessage(mInit.getMaintenanceMessage());
                    break;

                case PWRESET:
                    if( !mInit.getMaintenanceCheck())
                        cReset.runPasswordReset(sender, args);
                    else
                        sender.sendMessage(mInit.getMaintenanceMessage());
                    break;

                case IDRESET:
                    if( !mInit.getMaintenanceCheck())
                        cReset.runUsernameReset(sender, args);
                    else
                        sender.sendMessage(mInit.getMaintenanceMessage());
                    break;

                case PURGE:
                    if( !mInit.getMaintenanceCheck())
                        cPurge.runStaffPurge(sender, args);
                    else
                        sender.sendMessage(mInit.getMaintenanceMessage());
                    break;

                case GIVE:
                    if( !mInit.getMaintenanceCheck())
                        cGive.runGiveCard(sender, args);
                    else
                        sender.sendMessage(mInit.getMaintenanceMessage());
                    break;

                case CAT:
                    if( !( sender instanceof Player ) )
                        sender.sendMessage(mInit.getMsgNoConsole());
                    else
                        if( !mInit.getMaintenanceCheck())
                            cCategory.toggleCategoryState(sender, args);
                        else
                            sender.sendMessage(mInit.getMaintenanceMessage());
                    break;

                case GIFT:
                    if( !( sender instanceof Player ) )
                        sender.sendMessage(mInit.getMsgNoConsole());
                    else
                        if( !mInit.getMaintenanceCheck())
                            cGift.runGiftCard(sender, ArrayUtils.subarray(args, 1, args.length));
                        else
                            sender.sendMessage(mInit.getMaintenanceMessage());
                    break;

                case RANDOM:
                    if( !mInit.getMaintenanceCheck())
                        cRandom.runRandomCard(sender, args);
                    else
                        sender.sendMessage(mInit.getMaintenanceMessage());
                    break;

                case UNBRAND:
                    if( !mInit.getMaintenanceCheck())
                        cBrand.runRemoveBrand(sender, args);
                    else
                        sender.sendMessage(mInit.getMaintenanceMessage());
                    break;

                case LIST:
                    if( !( sender instanceof Player ) )
                        sender.sendMessage(mInit.getMsgNoConsole());
                    else
                        if( !mInit.getMaintenanceCheck())
                            cList.runListCards(sender, args);
                        else
                            sender.sendMessage(mInit.getMaintenanceMessage());
                    break;

                case INFO:
                    if( !( sender instanceof Player ) )
                        sender.sendMessage(mInit.getMsgNoConsole());
                    else
                        if( !mInit.getMaintenanceCheck())
                            cInfo.runCardInfo(sender, args);
                        else
                            sender.sendMessage(mInit.getMaintenanceMessage());
                    break;

                case CHANCE:
                    if( !( sender instanceof Player ) )
                        sender.sendMessage(mInit.getMsgNoConsole());
                    else
                        if( !mInit.getMaintenanceCheck())
                            cChance.changeChanceValue(sender,args);
                        else
                            sender.sendMessage(mInit.getMaintenanceMessage());
                    break;

                case RELOAD:
                    if( !mInit.getMaintenanceCheck())
                        cReload.runReload(sender);
                    else
                        sender.sendMessage(mInit.getMaintenanceMessage());
                    break;

                case MAINTENANCE:
                    cMaintenance.runMaintenance(sender, args);
                    break;

                case ADVERT:
                    if( !mInit.getMaintenanceCheck())
                        cAdvert.runAdvert(sender);
                    else
                        sender.sendMessage(mInit.getMaintenanceMessage());
                    break;

                case GAYAGENDA:
                    if( !( sender instanceof Player ) )
                        sender.sendMessage(mInit.getMsgNoConsole());
                    else
                        cSecret.runGayAgenda(sender);
                    break;

                case GHOST:
                    if( !( sender instanceof Player ) )
                        sender.sendMessage(mInit.getMsgNoConsole());
                    else
                        cSecret.toggleGhost(sender);
                    break;

                case MAP:
                    if( !( sender instanceof Player ) )
                        sender.sendMessage(mInit.getMsgNoConsole());
                    else
                        if( sender.hasPermission("cards.staff.map" ) ) if (args.length == 2)
                            eMapImage.getCardImage( (Player) sender, args[1] );
                    else
                        sender.sendMessage(mInit.getMsgPermDeny());
                    break;

                case FORCE:
                    if( !mInit.getMaintenanceCheck())
                        cForce.forcePlayerCardRun( sender, args );
                    else
                        sender.sendMessage(mInit.getMaintenanceMessage());
                    break;

                case VERSION:
                    sender.sendMessage(new String[] {
                            mInit.getMsgPrefix() + "Version " + mInit.getPlugin().getDescription().getVersion(),
                            mInit.getMsgPrefix() + mInit.getPlugin().getDescription().getDescription().split("\n")[1]
                    });
                    break;

                default:
                    if( !( sender instanceof Player ) )
                        sender.sendMessage( consoleTools );
                    else
                        cHelp.runHelp(sender);

            }

        } catch ( IllegalArgumentException e ) {

            if( !( sender instanceof Player ) )
                sender.sendMessage( consoleTools );
            else
                cHelp.runHelp(sender);

        }

    }

    @Override
    public List<String> onTabComplete( CommandSender sender, Command cmd, String label, String[] args ){

        List<String> tabSuggest = new ArrayList<>();

        if( args[0].toLowerCase().equals( "cat" ) ) if (args.length == 2) {
            for (String c : cCategory.validCategory.allValues())
                if (c.startsWith(args[1])) tabSuggest.add(c);
            return tabSuggest;
        }

        if( args[0].toLowerCase().equals( "chance" ) ) if (args.length == 2) {
            for (String c : cChance.validChanceType.allValues())
                if (c.startsWith(args[1])) tabSuggest.add(c);
            return tabSuggest;
        }

        return null;

    }

    private enum validCommands {
        ABOUT, ADVERT, CAT, CHANCE, CHECK,
        GAYAGENDA, GHOST, GIFT, GIVE, HELP,
        IDRESET, INFO, LIST, MAINTENANCE,
        MAP, PURGE, PWRESET, RANDOM, REDEEM,
        REGISTER, RELOAD, SHELP, UNBRAND,
        FORCE, VERSION
    }

}
