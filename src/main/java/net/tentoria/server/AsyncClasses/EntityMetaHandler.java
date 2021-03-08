package net.tentoria.server.AsyncClasses;

import net.tentoria.server.Main;
import net.tentoria.server.mobs.EntityMeta;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class EntityMetaHandler extends BukkitRunnable{

    @Override
    public void run() {
        for(EntityMeta meta: Main.getMainPlugin().entitymeta.values()){
            if(!meta.hasAI()){
                ((CraftEntity) meta.getMob()).setMomentum(new Vector(0, 0, 0));
                meta.getMob().teleport(meta.getHome());
            }
        }

        new EntityMetaHandler().runTaskLater(Main.getMainPlugin(), 1);

    }
}
