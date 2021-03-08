package net.tentoria.server.AsyncClasses;

import net.tentoria.server.Main;
import net.tentoria.server.MapManager.Lobby;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class BorderManager extends BukkitRunnable {

    private Location border1a;
    private Location border2a;
    private World world;
    private Lobby lobby;

    private boolean active;

    public BorderManager(Location border1, Location border2, Lobby lob){
        border1a = border1;
        border2a = border2;
        world = border1.getWorld();
        lobby = lob;
    }


    public Lobby getLobby() {
        return lobby;
    }

    @Override
    public void run() {
        try {
            for (Player player : world.getPlayers()) {
                Location loc = player.getLocation();
                if (loc.getX() == Math.min(loc.getX(), border1a.getX()) || loc.getX() == Math.max(loc.getX(), border2a.getX())) {
                    player.teleport(getLobby().getSpawns().get(0));
                }
                if (loc.getY() == Math.min(loc.getY(), border1a.getY()) || loc.getY() == Math.max(loc.getY(), border2a.getY())) {
                    player.teleport(getLobby().getSpawns().get(0));
                }
                if (loc.getZ() == Math.min(loc.getZ(), border1a.getZ()) || loc.getZ() == Math.max(loc.getZ(), border2a.getZ())) {
                    player.teleport(getLobby().getSpawns().get(0));
                }
            }
        } catch (Exception err){

        }

        new BorderManager(border1a, border2a, lobby).runTaskLater(Main.getMainPlugin(), 4);
    }

}
