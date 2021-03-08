package net.tentoria.server.MapManager;

import net.tentoria.server.Main;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class LobbyLoader {

    public Lobby lastLobby;

    public boolean loadLobbyMap(){
        try {
            FileUtils.copyDirectory(new File("./maps/lobby"), new File("./lobby"));
        } catch (IOException err) {
            err.printStackTrace();
            return false;
        }

        int lobbyid = 1;

        for(String world:Main.getMainPlugin().getLobbyList()){
            if(Main.getMainPlugin().getLobbyList().contains("lobby-"+String.valueOf(lobbyid))){
                lobbyid++;
            } else {
                break;
            }
        }

        String name = "lobby-"+String.valueOf(lobbyid);

        new File("./lobby").renameTo(new File("./"+name));

        List<String> newList = Main.getMainPlugin().getLobbyList();
        newList.add("lobby-"+String.valueOf(lobbyid));
        Main.getMainPlugin().setLobbyList(newList);

        Main.getMainPlugin().getLogger().info("Loading World");

        World lobby = Bukkit.createWorld(new WorldCreator(name));

        Main.getMainPlugin().getLogger().info("Loaded Lobby");

        Lobby lobb = new Lobby(lobby);
        Main.getMainPlugin().getLogger().info("Made Lobby Class");
        lobb.InitiateLobby();
        Main.getMainPlugin().getLogger().info("Made Lobby Class (Init)");

        lastLobby = lobb;
        return true;
    }

}
