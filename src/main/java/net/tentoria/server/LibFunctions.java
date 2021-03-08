package net.tentoria.server;

import net.minecraft.server.v1_8_R3.WorldServer;
import net.tentoria.server.mobs.EntityMeta;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftSlime;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.*;

public class LibFunctions {

    public static Player getPlayer(String name){
        for(Player player: Main.getMainPlugin().getServer().getOnlinePlayers()){
            if(player.getName().equals(name)){
                return player;
            }
        }
        return null;
    }

    public static void replaceEntityMeta(EntityMeta meta){
        if(Main.getMainPlugin().entitymeta.containsKey(meta.getMob().getUniqueId())){
            Main.getMainPlugin().entitymeta.get(meta.getMob().getUniqueId()).setActive(false);
            Main.getMainPlugin().entitymeta.replace(meta.getMob().getUniqueId(), meta);
        } else {
            Main.getMainPlugin().entitymeta.put(meta.getMob().getUniqueId(), meta);
        }
    }



    public static Entity spawnMob(Location loc, EntityType entityType){
        Entity entity = loc.getWorld().spawnEntity(loc, entityType);
        CraftArmorStand tag = (CraftArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        tag.setCustomNameVisible(false);
        tag.setSmall(true);
        tag.setVisible(false);
        tag.setRemoveWhenFarAway(false);
        entity.setPassenger(tag);
        EntityMeta meta = new EntityMeta(entity);
        Main.getMainPlugin().entitymeta.put(entity.getUniqueId(), meta);
        return entity;
    }

}
