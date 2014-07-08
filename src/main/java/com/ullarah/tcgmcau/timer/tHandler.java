package com.ullarah.tcgmcau.timer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class tHandler {

    public static Class< ? > getCraftClass(String ClassName){

        String name = Bukkit.getServer().getClass().getPackage().getName();
        String version = name.substring(name.lastIndexOf('.') + 1) + ".";
        String className = "net.minecraft.server." + version + ClassName;

        Class< ? > c = null;

        try{
            c = Class.forName(className);
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        return c;

    }

    public static Object getHandle(Entity entity){

        Object nms_entity = null;
        Method entity_getHandle = getMethod(entity.getClass() , "getHandle");

        try{
            nms_entity = entity_getHandle.invoke(entity);
        }
        catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException e){
            e.printStackTrace();
        }
        return nms_entity;

    }

    public static Object getHandle(Object entity){

        Object nms_entity = null;
        Method entity_getHandle = getMethod(entity.getClass() , "getHandle");

        try{
            nms_entity = entity_getHandle.invoke(entity);
        }
        catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException e){
            e.printStackTrace();
        }
        return nms_entity;

    }

    public static Field getField(Class<?> cl){

        try{
            return cl.getDeclaredField("motX");
        }
        catch (SecurityException | NoSuchFieldException e){
            e.printStackTrace();
        }
        return null;

    }

    public static Method getMethod(Class< ? > cl, String method, Class< ? >[] args){

        for ( Method m : cl.getMethods() )
            if (m.getName().equals(method) && ClassListEqual(args, m.getParameterTypes())) return m;
        return null;

    }

    public static Method getMethod(Class< ? > cl, String method){

        for ( Method m : cl.getMethods() )
            if (m.getName().equals(method)) return m;
        return null;

    }

    private static boolean ClassListEqual(Class<?>[] l1, Class<?>[] l2){

        boolean equal = true;

        if ( l1.length != l2.length )
            return false;
        for ( int i = 0; i < l1.length; i++ )
            if (l1[i] != l2[i]) {
                equal = false;
                break;
            }

        return equal;

    }

}
