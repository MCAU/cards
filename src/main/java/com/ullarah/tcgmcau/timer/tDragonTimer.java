package com.ullarah.tcgmcau.timer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class tDragonTimer
{
    private static tDragonTimer instance;
    private final Map< String , Dragon > dragonMap = new HashMap<>();

    public static tDragonTimer getInstance()
    {
        if ( tDragonTimer.instance == null )
            tDragonTimer.instance = new tDragonTimer();

        return tDragonTimer.instance;
    }

    void setStatus(Player player, String text, int percent, boolean reset) throws IllegalArgumentException, SecurityException,
            InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, NoSuchFieldException
    {
        Dragon dragon;

        if ( dragonMap.containsKey(player.getPlayerListName()) && !reset )
        {
            dragon = dragonMap.get(player.getPlayerListName());
        }
        else
        {
            dragon = new Dragon(text, player.getLocation().add(0 , -500 , 0), percent);
            Object mobPacket = dragon.getSpawnPacket();
            sendPacket(player , mobPacket);
            dragonMap.put(player.getPlayerListName() , dragon);
        }

        if ( text.equals("") )
        {
            Object destroyPacket = dragon.getDestroyPacket();
            sendPacket(player , destroyPacket);
            dragonMap.remove(player.getPlayerListName());
        }
        else
        {
            dragon.setName(text);
            dragon.setHealth(percent);
            Object metaPacket = dragon.getMetaPacket(dragon.getWatcher());
            Object teleportPacket = dragon.getTeleportPacket(player.getLocation().add(0 , -500 , 0));
            sendPacket(player , metaPacket);
            sendPacket(player , teleportPacket);
        }
    }

    private void sendPacket(Player player, Object packet)
    {
        try
        {
            Object nmsPlayer = tHandler.getHandle(player);
            Field con_field = nmsPlayer.getClass().getField("playerConnection");
            Object con = con_field.get(nmsPlayer);
            Method packet_method = tHandler.getMethod(con.getClass(), "sendPacket");
            packet_method.invoke(con , packet);
        }
        catch (SecurityException | IllegalArgumentException | InvocationTargetException | NoSuchFieldException | IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }

    private class Dragon
    {

        private static final int MAX_HEALTH = 200;
        private int id;
        private final int x;
        private final int y;
        private final int z;
        private final int pitch = 0;
        private final int yaw = 0;
        private final byte xvel = 0;
        private final byte yvel = 0;
        private final byte zvel = 0;
        private float health;
        private final boolean visible = false;
        private String name;
        private final Object world;

        private Object dragon;

        public Dragon( String name , Location loc , int percent )
        {
            this.name = name;
            this.x = loc.getBlockX();
            this.y = loc.getBlockY();
            this.z = loc.getBlockZ();
            this.health = percent / 100F * MAX_HEALTH;
            this.world = tHandler.getHandle(loc.getWorld());
        }

        public void setHealth(int percent)
        {
            this.health = percent / 100F * MAX_HEALTH;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public Object getSpawnPacket() throws IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException,
                InvocationTargetException, NoSuchMethodException
        {
            Class< ? > Entity = tHandler.getCraftClass("Entity");
            Class< ? > EntityLiving = tHandler.getCraftClass("EntityLiving");
            Class< ? > EntityEnderDragon = tHandler.getCraftClass("EntityEnderDragon");
            dragon = EntityEnderDragon.getConstructor(tHandler.getCraftClass("World")).newInstance(world);

            Method setLocation = tHandler.getMethod(EntityEnderDragon, "setLocation", new Class<?>[]
                    {double.class, double.class, double.class, float.class, float.class});
            setLocation.invoke(dragon , x , y , z , pitch , yaw);

            Method setInvisible = tHandler.getMethod(EntityEnderDragon, "setInvisible", new Class<?>[]
                    {boolean.class});
            setInvisible.invoke(dragon , visible);

            Method setCustomName = tHandler.getMethod(EntityEnderDragon, "setCustomName", new Class<?>[]
                    {String.class});
            setCustomName.invoke(dragon , name);

            Method setHealth = tHandler.getMethod(EntityEnderDragon, "setHealth", new Class<?>[]
                    {float.class});
            setHealth.invoke(dragon , health);

            Field motX = tHandler.getField(Entity);
            motX.set(dragon , xvel);

            Field motY = tHandler.getField(Entity);
            motY.set(dragon , yvel);

            Field motZ = tHandler.getField(Entity);
            motZ.set(dragon , zvel);

            Method getId = tHandler.getMethod(EntityEnderDragon, "getId", new Class<?>[]{});
            this.id = (Integer) getId.invoke(dragon);

            Class< ? > PacketPlayOutSpawnEntityLiving = tHandler.getCraftClass("PacketPlayOutSpawnEntityLiving");

            return PacketPlayOutSpawnEntityLiving.getConstructor(new Class< ? >[]
                    { EntityLiving }).newInstance(dragon);
        }

        public Object getDestroyPacket() throws IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException,
                InvocationTargetException, NoSuchMethodException, NoSuchFieldException
        {
            Class< ? > PacketPlayOutEntityDestroy = tHandler.getCraftClass("PacketPlayOutEntityDestroy");

            Object packet = PacketPlayOutEntityDestroy.getConstructor().newInstance();

            Field a = PacketPlayOutEntityDestroy.getDeclaredField("a");
            a.setAccessible(true);
            a.set(packet , new int[]
                    { id });

            return packet;
        }

        public Object getMetaPacket(Object watcher) throws IllegalArgumentException, SecurityException, InstantiationException,
                IllegalAccessException, InvocationTargetException, NoSuchMethodException
        {
            Class< ? > DataWatcher = tHandler.getCraftClass("DataWatcher");

            Class< ? > PacketPlayOutEntityMetadata = tHandler.getCraftClass("PacketPlayOutEntityMetadata");

            return PacketPlayOutEntityMetadata.getConstructor(new Class< ? >[]
                    { int.class , DataWatcher , boolean.class }).newInstance(id , watcher , true);
        }

        public Object getTeleportPacket(Location loc) throws IllegalArgumentException, SecurityException, InstantiationException,
                IllegalAccessException, InvocationTargetException, NoSuchMethodException
        {
            Class< ? > PacketPlayOutEntityTeleport = tHandler.getCraftClass("PacketPlayOutEntityTeleport");

            return PacketPlayOutEntityTeleport.getConstructor(new Class< ? >[]
                    { int.class , int.class , int.class , int.class , byte.class , byte.class }).newInstance(this.id , loc.getBlockX() * 32 ,
                    loc.getBlockY() * 32 , loc.getBlockZ() * 32 , (byte) ( (int) loc.getYaw() * 256 / 360 ) ,
                    (byte) ( (int) loc.getPitch() * 256 / 360 ));
        }

        public Object getWatcher() throws IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException,
                InvocationTargetException, NoSuchMethodException
        {
            Class< ? > Entity = tHandler.getCraftClass("Entity");
            Class< ? > DataWatcher = tHandler.getCraftClass("DataWatcher");

            Object watcher = DataWatcher.getConstructor(new Class< ? >[]
                    { Entity }).newInstance(dragon);

            Method a = tHandler.getMethod(DataWatcher, "a", new Class<?>[]
                    {int.class, Object.class});

            a.invoke(watcher , 0 , (byte) 0x20);
            a.invoke(watcher , 6 , health);
            a.invoke(watcher , 7 , 0);
            a.invoke(watcher , 8 , (byte) 0);
            a.invoke(watcher , 10 , name);
            a.invoke(watcher , 11 , (byte) 1);
            return watcher;
        }

    }

    public void bossTimer( Player sender, String message, Integer percentage, Boolean reset ) {

        try {
            setStatus(sender, message, percentage, reset);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | NoSuchFieldException e) {
            e.printStackTrace();
        }

    }

}
