package net.tentoria.server.AsyncClasses;

import net.tentoria.server.LibFunctions;
import net.tentoria.server.Main;
import net.tentoria.server.MapManager.Lobby;
import net.tentoria.server.mobs.EntityMeta;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Level;

public class SpawnerManager extends BukkitRunnable {

    private Location spawnloc;
    private World world;
    private Lobby lobby;

    private int tickets;
    private int tickintervl;

    private boolean active;
    private String entitytype;

    public SpawnerManager(Location loc, Lobby lob, String mob, int ticketcost, int tickinterval) {
        spawnloc = loc;
        world = loc.getWorld();
        lobby = lob;

        entitytype = mob;
        tickets = ticketcost;
        tickintervl = tickinterval;
    }

    public Lobby getLobby() {
        return lobby;
    }

    @Override
    public void run() {
        active = true;
        try {
            if (world.getEntities().size() < 500) {
                Entity entity = LibFunctions.spawnMob(spawnloc, EntityType.valueOf(entitytype));
                LivingEntity mobSpawned = (LivingEntity) entity;
                mobSpawned.setRemoveWhenFarAway(false);
                new SpawnerManager(spawnloc, lobby, entitytype, tickets, tickintervl).runTaskLater(Main.getMainPlugin(), tickintervl);
                EntityMeta meta = new EntityMeta(entity);
                meta.setFromSpawner(true);
                meta.setKillable(true);
                meta.setHasAI(true);
                meta.setTicketkill(tickets);
                LibFunctions.replaceEntityMeta(meta);

                entity.setCustomName(ChatColor.YELLOW + "" + ChatColor.BOLD + "SPAWNER MOB (+"+tickets+" tickets)");
                entity.setCustomNameVisible(true);

                entity.getPassenger().setCustomName(String.valueOf(mobSpawned.getMaxHealth()) + " / " + String.valueOf(mobSpawned.getMaxHealth())+ ChatColor.DARK_RED + " ♥");
                entity.getPassenger().setCustomNameVisible(true);
            }
        } catch (Exception err) {
            Main.getMainPlugin().getLogger().log(Level.WARNING, "Interrupted Exception while spawning mobs.");
            err.printStackTrace();
        }
    }
}
