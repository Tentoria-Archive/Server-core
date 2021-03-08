package net.tentoria.server.MapManager;


import net.tentoria.server.AsyncClasses.BorderManager;
import net.tentoria.server.AsyncClasses.SpawnerManager;
import net.tentoria.server.LibFunctions;
import net.tentoria.server.Main;
import net.tentoria.server.mobs.EntityMeta;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.*;

public class Lobby {

    private World lobby;
    private List<Location> spawns;

    private Location border1;
    private Location border2;
    private BorderManager borderm;

    private List<SpawnerManager> spawnerlist;

    private Map<String, Location> dataTags;

    public Lobby(World world){
        lobby = world;
        dataTags = new HashMap<>();
        spawns = new ArrayList<>();
        spawnerlist = new ArrayList<>();
    }

    public void InitiateLobby(){


        if(new File(lobby.getWorldFolder().getPath()+"/data.amf").exists()) {
            File file = new File(lobby.getWorldFolder().getPath()+"/data.amf");
            try {
                Reader reader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(reader);

                String line = "";

                while((line = bufferedReader.readLine()) != null) {
                    String[] strings = line.split("=");
                    String[] coords = strings[1].split(",");
                    Location loc = new Location(lobby, Double.valueOf(coords[0]),Double.valueOf(coords[1]),Double.valueOf(coords[2]));

                    if(strings[0].equals("SPAWNPOINT")){
                        spawns.add(loc);
                        continue;
                    }
                    if(strings[0].equals("BORDER#1")){
                        border1 = loc;
                        continue;
                    }
                    if(strings[0].equals("BORDER#2")){
                        border2 = loc;
                        continue;
                    }
                    if(strings[0].contains("NPC")){
                        Entity entity = LibFunctions.spawnMob(loc, EntityType.VILLAGER);
                        entity.setCustomName(ChatColor.RED+""+ChatColor.BOLD+strings[0].split("#")[1]);
                        entity.setCustomNameVisible(true);
                        EntityMeta entityMeta = new EntityMeta(entity);
                        entityMeta.setNPC(true);
                        entityMeta.setKillable(false);
                        entityMeta.setHasAI(false);
                        LibFunctions.replaceEntityMeta(entityMeta);
                        //Add to NPC manager
                        continue;
                    }
                    if(strings[0].contains("SPAWN")){
                        String[] components = strings[0].split("#");
                        SpawnerManager spawnmanage;
                        if(components.length > 3){
                            try {
                                spawnmanage = new SpawnerManager(loc, this, components[1], Integer.valueOf(components[2]), Integer.valueOf(components[3]));
                                spawnmanage.runTaskLater(Main.getMainPlugin(), Integer.valueOf(components[3]));
                            } catch (Exception err) {
                                Main.getMainPlugin().getLogger().warning("Malformed spawner tag @ " + line);
                                spawnmanage = new SpawnerManager(loc, this, components[1], 5, 200);
                                spawnmanage.runTaskLater(Main.getMainPlugin(), 200);
                            }
                        } else {
                             spawnmanage = new SpawnerManager(loc, this, components[1], 5, 200);
                            spawnmanage.runTaskLater(Main.getMainPlugin(), 200);
                        }
                        spawnerlist.add(spawnmanage);
                        continue;
                    }
                    if(strings[0].contains("MINE")){
                        //copy Spawners
                        //MINE#[type]#[interval]
                    }

                }

                bufferedReader.close();
            } catch (Exception err){
                Main.getMainPlugin().getLogger().info("DATAPOINT AMF ERROR [General]:\n");
                err.printStackTrace();
            }
        } else {
            Main.getMainPlugin().getLogger().info("DATAPOINT AMF ERROR [Not Found]");
        }



        borderm = new BorderManager(border1, border2, this);

        borderm.runTaskLater(Main.getMainPlugin(), 5);
    }

    public BorderManager getBorderm() {
        return borderm;
    }

    public void setBorderm(BorderManager borderm) {
        this.borderm = borderm;
    }

    public List<Location> getSpawns() {
        return spawns;
    }

    public Map<String, Location> getDataTags() {
        return dataTags;
    }

    public List<SpawnerManager> getSpawnerlist() {
        return spawnerlist;
    }

    public World getLobby() {
        return lobby;
    }
}
