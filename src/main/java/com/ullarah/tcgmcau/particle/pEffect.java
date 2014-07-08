package com.ullarah.tcgmcau.particle;

import com.ullarah.tcgmcau.particle.pHandler.PackageType;
import com.ullarah.tcgmcau.particle.pHandler.PacketType;
import com.ullarah.tcgmcau.particle.pHandler.SubPackageType;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * ParticleEffect Library v1.4
 *
 * This library was created by @DarkBlade12 based on content related to particles of @microgeek (names and packet values), it allows you to display all Minecraft particle effects on a Bukkit server
 *
 * You are welcome to use it, modify it and redistribute it under the following conditions:
 * 1. Don't claim this class as your own
 * 2. Don't remove this text
 *
 * (Would be nice if you provide credit to me)
 *
 * @author DarkBlade12
 */

public enum pEffect {

    HUGE_EXPLOSION("hugeexplosion"),
    LARGE_EXPLODE("largeexplode"),
    FIREWORKS_SPARK("fireworksSpark"),
    BUBBLE("bubble"),
    SUSPEND("suspend"),
    DEPTH_SUSPEND("depthSuspend"),
    TOWN_AURA("townaura"),
    CRIT("crit"),
    MAGIC_CRIT("magicCrit"),
    SMOKE("smoke"),
    MOB_SPELL("mobSpell"),
    MOB_SPELL_AMBIENT("mobSpellAmbient"),
    SPELL("spell"),
    INSTANT_SPELL("instantSpell"),
    WITCH_MAGIC("witchMagic"),
    NOTE("note"),
    PORTAL("portal"),
    ENCHANTMENT_TABLE("enchantmenttable"),
    EXPLODE("explode"),
    FLAME("flame"),
    LAVA("lava"),
    FOOTSTEP("footstep"),
    SPLASH("splash"),
    WAKE("wake"),
    LARGE_SMOKE("largesmoke"),
    CLOUD("cloud"),
    RED_DUST("reddust"),
    SNOWBALL_POOF("snowballpoof"),
    DRIP_WATER("dripWater"),
    DRIP_LAVA("dripLava"),
    SNOW_SHOVEL("snowshovel"),
    SLIME("slime"),
    HEART("heart"),
    ANGRY_VILLAGER("angryVillager"),
    HAPPY_VILLAGER("happyVillager");

    private static final Map<String, pEffect> NAME_MAP = new HashMap<>();

    private static Constructor<?> packetPlayOutWorldParticles;
    private static Method getHandle;
    private static Field playerConnection;
    private static Method sendPacket;

    private final String name;

    static {
        for (pEffect p : values())
            NAME_MAP.put(p.getName(), p);
        try {
            packetPlayOutWorldParticles = pHandler.getConstructor(PacketType.PLAY_OUT_WORLD_PARTICLES.getPacket(), String.class, float.class, float.class, float.class, float.class, float.class,
                    float.class, float.class, int.class);
            getHandle = pHandler.getMethod("CraftPlayer", SubPackageType.ENTITY, "getHandle");
            playerConnection = pHandler.getField("EntityPlayer", PackageType.MINECRAFT_SERVER, "playerConnection");
            sendPacket = pHandler.getMethod(playerConnection.getType(), "sendPacket", pHandler.getClass("Packet", PackageType.MINECRAFT_SERVER));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private pEffect(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static pEffect fromName(String name) {
        if (name != null)
            for (Entry<String, pEffect> e : NAME_MAP.entrySet())
                if (e.getKey().equalsIgnoreCase(name))
                    return e.getValue();
        return null;
    }

    private static List<Player> getPlayers(Location center, double range) {
        List<Player> players = new ArrayList<>();
        String name = center.getWorld().getName();
        double squared = range * range;
        for (Player p : Bukkit.getOnlinePlayers())
            if (p.getWorld().getName().equals(name) && p.getLocation().distanceSquared(center) <= squared)
                players.add(p);
        return players;
    }

    private static Object instantiatePacket(String name, Location center, float speed, int amount) {
        if (amount < 1)
            throw new PacketInstantiationException();
        try {
            return packetPlayOutWorldParticles.newInstance(name, (float) center.getX(), (float) center.getY(), (float) center.getZ(), 1.0F, 1.0F, 1.0F, speed, amount);
        } catch (Exception e) {
            throw new PacketInstantiationException(e);
        }
    }

    private static void sendPacket(Player p, Object packet) {
        try {
            sendPacket.invoke(playerConnection.get(getHandle.invoke(p)), packet);
        } catch (Exception e) {
            throw new PacketSendingException("Failed to send a packet to player '" + p.getPlayerListName() + "'", e);
        }
    }

    private static void sendPacket(Collection<Player> players, Object packet) {
        for (Player p : players)
            sendPacket(p, packet);
    }

    public void display(Location center, float speed, int amount) {
        sendPacket(getPlayers(center, 20), instantiatePacket(getName(), center, speed, amount));
    }

    private static final class PacketInstantiationException extends RuntimeException {

        /**
         * 
         */
        private static final long serialVersionUID = -1225980945854296888L;

        public PacketInstantiationException() {

            super("Amount cannot be lower than 1");

        }

        public PacketInstantiationException(Throwable cause) {

            super("Packet instantiation failed", cause);

        }

    }

    private static final class PacketSendingException extends RuntimeException {

        /**
         * 
         */
        private static final long serialVersionUID = -1286611207796061048L;

        public PacketSendingException(String message, Throwable cause) {

            super(message, cause);

        }

    }

}