package net.tentoria.server;


import net.tentoria.server.MapManager.Lobby;
import net.tentoria.server.MapManager.LobbyLoader;
import net.tentoria.server.mobs.EntityMeta;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import org.bukkit.event.EventHandler;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.ArrayList;
import java.util.List;


public class EHandler implements Listener {


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        event.getPlayer().teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
        event.setJoinMessage("");

        boolean foundLobby = false;

        for (Lobby lobby: Main.getMainPlugin().lobbyClassList) {
            if(lobby.getLobby().getPlayers().size() < 60){
                event.getPlayer().teleport(lobby.getSpawns().get(0));
                foundLobby = true;
                break;
            }
        }

        if(!foundLobby){
            event.getPlayer().sendMessage(ChatColor.BLUE + "Hub >> " +ChatColor.GRAY + "It seems we have no available lobbies. Creating one...");
            LobbyLoader lobbyLoad = new LobbyLoader();
            boolean lobbyloaded = lobbyLoad.loadLobbyMap();
            Main.getMainPlugin().lobbyClassList.add(lobbyLoad.lastLobby);

            if(lobbyloaded){
                event.getPlayer().teleport(lobbyLoad.lastLobby.getSpawns().get(0));
            } else {
                event.getPlayer().kickPlayer(ChatColor.RED + "" + ChatColor.BOLD + "Error:\nThere were no available servers...");
            }
        }

    }


    @EventHandler
    public void onAchieve(PlayerAchievementAwardedEvent event){
        event.setCancelled(true);
    }
    @EventHandler
    public void onBucket(PlayerBucketEmptyEvent event){
        if(event.getPlayer().getWorld().getName().toLowerCase().contains("lobby")) {
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onBucketEmpty(PlayerBucketFillEvent event){
        if(event.getPlayer().getWorld().getName().toLowerCase().contains("lobby")) {
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onPlace(BlockPlaceEvent event){
        if(event.getPlayer().getWorld().getName().toLowerCase().contains("lobby")) {
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onBreak(BlockBreakEvent event){
        if(event.getPlayer().getWorld().getName().toLowerCase().contains("lobby")) {
            event.setCancelled(true);
            event.setExpToDrop(0);
            event.getBlock().setType(event.getBlock().getType());
        }
    }
    @EventHandler
    public void onPunch(EntityDamageByEntityEvent event){
        if(event.getEntity().getWorld().getName().toLowerCase().contains("lobby")) {

            if(Main.getMainPlugin().entitymeta.getOrDefault(event.getEntity().getUniqueId()).isFromSpawner(), new EntityMeta(event.getEntity())) {
                if (((LivingEntity) event.getEntity()).getHealth() - event.getDamage() < 0) {
                    LivingEntity entity = (LivingEntity) event.getEntity();
                    event.getEntity().setCustomName("0 / " + String.valueOf(entity.getMaxHealth()) + ChatColor.DARK_RED + " ♥");
                } else {
                    LivingEntity entity = (LivingEntity) event.getEntity();
                    entity.getPassenger().setCustomName(String.valueOf(Math.round(entity.getHealth()) - Math.round(event.getDamage())) + " / " + String.valueOf(entity.getMaxHealth()) + ChatColor.DARK_RED + " ♥");
                }
            }

            if(event.getDamager().getType().equals(EntityType.PLAYER)){
                if(Main.getMainPlugin().entitymeta.get(event.getEntity().getUniqueId()).isKillable()){
                    Player player = (Player) event.getDamager();
                    if(Main.getMainPlugin().entitymeta.get(event.getEntity().getUniqueId()).isFromSpawner()) {
                        if (((LivingEntity) event.getEntity()).getHealth() - event.getDamage() < 0) {
                            Main.getMainPlugin().getPassPlugin().getPlayerSQLList().get(player.getUniqueId()).addTickets(Main.getMainPlugin().entitymeta.get(event.getEntity().getUniqueId()).getTicketkill());
                            player.sendMessage(ChatColor.GOLD + "Tickets >> " + ChatColor.YELLOW + "+"+String.valueOf(Main.getMainPlugin().entitymeta.get(event.getEntity().getUniqueId()).getTicketkill())+" Tickets!");
                        }
                    }
                } else {
                    event.setCancelled(true);
                }
            }

        }
    }

    @EventHandler
    public void onTarget(EntityTargetEvent event){
        if(!(Main.getMainPlugin().entitymeta.getOrDefault(event.getEntity().getUniqueId(), new EntityMeta(event.getEntity())).hasAI())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDeath (EntityDeathEvent event){
        if(Main.getMainPlugin().entitymeta.getOrDefault(event.getEntity().getUniqueId(), new EntityMeta(event.getEntity())).isFromSpawner()){
            event.setDroppedExp(0);
            event.getDrops().clear();
        }

        if(event.getEntity().getType() != EntityType.PLAYER){
            if(event.getEntity().getPassenger() != null) {
                List<Entity> elist = new ArrayList<>();

                Entity lastEntity = event.getEntity().getPassenger();

                while (lastEntity.getPassenger() != null) {
                    elist.add(0, lastEntity);
                }

                for(Entity e:elist){
                    e.remove();
                }
            }
        } else {
            Main.getMainPlugin().getLogger().info("Not Player!");
        }
    }

    @EventHandler
    public void onInventoryEdit(InventoryClickEvent event){
        if(event.getInventory().getHolder() instanceof Player){
            if(!(event.getWhoClicked().getGameMode() == GameMode.CREATIVE)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void dismoutEvent(EntityDismountEvent event){
        if(event.getEntity().getWorld().getName().toLowerCase().contains("lobby")) {
            if (event.getDismounted().getType() != EntityType.PLAYER) {
                event.getDismounted().remove();
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event){
        if(event.getPlayer().getWorld().getName().toLowerCase().contains("lobby")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerSwitchWorld(PlayerChangedWorldEvent event){
        if(event.getPlayer().getWorld().getName().toLowerCase().contains("lobby")) {
            PlayerInventory inventory = event.getPlayer().getInventory();
            inventory.clear();

            ItemStack navigatorcompass = new ItemStack(Material.COMPASS);
            ItemMeta meta = navigatorcompass.getItemMeta();
            meta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Navigator");
            navigatorcompass.setItemMeta(meta);

            ItemStack toolitem = new ItemStack(Material.DIAMOND_SWORD);
            meta = toolitem.getItemMeta();
            meta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Multi Tool [Right Click to Switch]");
            toolitem.setItemMeta(meta);

            inventory.setItem(0, navigatorcompass);
            inventory.setItem(8, toolitem);
        }
    }

    @EventHandler
    public void onItemInteract(PlayerInteractEvent event){
        if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (event.getPlayer().getWorld().getName().toLowerCase().contains("lobby")) {
                event.setCancelled(true);
                PlayerInventory inventory = event.getPlayer().getInventory();
                if (event.getItem().getItemMeta().getDisplayName().contains("Navigator")) {
                    event.getPlayer().sendMessage(ChatColor.AQUA + "Navigator >> "+ChatColor.GRAY+"Opening Navigator...");
                    event.getPlayer().sendMessage(ChatColor.AQUA + "Navigator >> "+ChatColor.GRAY+"Ha, gotcha, Still working on it if you're reading this James.");
                }
                if (event.getItem().getItemMeta().getDisplayName().contains("Multi Tool")) {
                    if (event.getItem().getType().equals(Material.DIAMOND_SWORD)) {
                        ItemStack toolitem = new ItemStack(Material.DIAMOND_PICKAXE);
                        ItemMeta meta = toolitem.getItemMeta();
                        meta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Multi Tool [Right Click to Switch]");
                        toolitem.setItemMeta(meta);
                        inventory.setItem(8, toolitem);
                    }
                    if (event.getItem().getType().equals(Material.DIAMOND_PICKAXE)) {
                        ItemStack toolitem = new ItemStack(Material.DIAMOND_SWORD);
                        ItemMeta meta = toolitem.getItemMeta();
                        meta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Multi Tool [Right Click to Switch]");
                        toolitem.setItemMeta(meta);
                        inventory.setItem(8, toolitem);
                    }
                }
            }
        }
    }

}
