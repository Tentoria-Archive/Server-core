package net.tentoria.server.mobs;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class EntityMeta {

    private Location home;
    private Entity mob;

    private boolean hasAI = true;
    private boolean fromSpawner = false;
    private boolean isNPC = false;
    private boolean killable = false;
    private boolean dismountable = false;

    private int ticketkill = 5;

    private boolean active = true;


    public EntityMeta(Entity entity){
        mob = entity;
        home = entity.getLocation();

    }

    public Location getHome() {
        return home;
    }

    public void setHome(Location home) {
        this.home = home;
    }

    public Entity getMob() {
        return mob;
    }

    public boolean hasAI() {
        return hasAI;
    }

    public EntityMeta setHasAI(boolean hasAI) {
        this.hasAI = hasAI;
        return this;
    }

    public boolean isFromSpawner() {
        return fromSpawner;
    }

    public EntityMeta setFromSpawner(boolean fromSpawner) {
        this.fromSpawner = fromSpawner;
        return this;
    }

    public boolean isNPC() {
        return isNPC;
    }

    public EntityMeta setNPC(boolean NPC) {
        isNPC = NPC;
        return this;
    }

    public boolean isKillable() {
        return killable;
    }

    public EntityMeta setKillable(boolean killable) {
        this.killable = killable;
        return this;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getTicketkill() {
        return ticketkill;
    }

    public void setTicketkill(int ticketkill) {
        this.ticketkill = ticketkill;
    }

    public boolean isDismountable() {
        return dismountable;
    }

    public EntityMeta setDismountable(boolean dismountable) {
        this.dismountable = dismountable;
        return this;
    }
}
