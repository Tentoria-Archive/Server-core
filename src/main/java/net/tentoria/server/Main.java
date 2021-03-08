package net.tentoria.server;

import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntitySlime;
import net.minecraft.server.v1_8_R3.EntityTypes;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.tentoria.pass.PassPlugin;
import net.tentoria.server.AsyncClasses.EntityMetaHandler;
import net.tentoria.server.MapManager.Lobby;
import net.tentoria.server.MapManager.LobbyLoader;
import net.tentoria.server.mobs.EntityMeta;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class Main extends JavaPlugin{

    private static Main plugin;

    private List<String> lobbyList;
    public List<Lobby> lobbyClassList;

    public LinkedHashMap<UUID, EntityMeta> entitymeta;

    public Map<String, String> strings;

    public static Main getMainPlugin(){
        return plugin;
    }

    public List<String> getLobbyList() {
        return lobbyList;
    }

    public void setLobbyList(List<String> lobbyList) {
        this.lobbyList = lobbyList;
    }

    private PassPlugin passPlugin;

    @Override
    public void onEnable() {
        plugin = this;
        passPlugin = (PassPlugin) getMainPlugin().getServer().getPluginManager().getPlugin("PassPlugin");
        lobbyList = new ArrayList<>();
        lobbyClassList = new ArrayList<>();
        strings = new HashMap<>();
        entitymeta = new LinkedHashMap<>();

        LobbyLoader loader = new LobbyLoader();
        loader.loadLobbyMap();

        Lobby lob = loader.lastLobby;
        Main.getMainPlugin().lobbyClassList.add(lob);

        new EntityMetaHandler().runTaskLater(Main.getMainPlugin(), 1);

        EHandler ehandle = new EHandler();
        getServer().getPluginManager().registerEvents(ehandle, this);
        MinecraftServer.getServer().setMotd(ChatColor.RED +"" + ChatColor.BOLD + "              = TENTORIA "+ChatColor.WHITE+""+ChatColor.BOLD+"NETWORK =");
    }

    @Override
    public void onDisable() {
        for (Player player:getServer().getOnlinePlayers()) {
            player.kickPlayer(ChatColor.RED+""+ChatColor.BOLD+"Closing for Maintenance. \n"+ChatColor.DARK_RED+""+ChatColor.BOLD+"We will open again soon.");
        }

        for(Lobby lobby:lobbyClassList){
            File worldFolder = lobby.getLobby().getWorldFolder();
            getServer().unloadWorld(lobby.getLobby(), false);
            deleteWorld(worldFolder);
        }
    }

    public boolean deleteWorld(File path) {
        if(path.exists()) {
            File files[] = path.listFiles();
            for(int i=0; i<files.length; i++) {
                if(files[i].isDirectory()) {
                    deleteWorld(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return(path.delete());
    }

    public PassPlugin getPassPlugin() {
        return passPlugin;
    }

    public void registerEntity(String name, int id, Class<? extends EntityInsentient> nmsClass, Class<? extends EntityInsentient> customClass){
        try {

            List<Map<?, ?>> dataMap = new ArrayList<Map<?, ?>>();
            for (Field f : EntityTypes.class.getDeclaredFields()){
                if (f.getType().getSimpleName().equals(Map.class.getSimpleName())){
                    f.setAccessible(true);
                    dataMap.add((Map<?, ?>) f.get(null));
                }
            }

            if (dataMap.get(2).containsKey(id)){
                dataMap.get(0).remove(name);
                dataMap.get(2).remove(id);
            }

            Method method = EntityTypes.class.getDeclaredMethod("a", Class.class, String.class, int.class);
            method.setAccessible(true);
            method.invoke(null, customClass, name, id);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
