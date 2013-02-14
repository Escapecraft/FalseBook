package com.bukkit.gemo.FalseBook.World;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class EventList {

   public void onBlockBreak(BlockBreakEvent event) {}

   public void onBlockDamage(BlockDamageEvent event) {}

   public void onBlockPhysics(BlockPhysicsEvent event) {}

   public void onBlockPlace(BlockPlaceEvent event) {}

   public void onPistonExtend(BlockPistonExtendEvent event) {}

   public void onPistonRetract(BlockPistonRetractEvent event) {}

   public void onRedstoneChange(BlockRedstoneEvent event) {}

   public void onSignChange(SignChangeEvent event) {}

   public void onPlayerChat(PlayerChatEvent event) {}

   public void onPlayerInteract(PlayerInteractEvent event) {}

   public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {}

   public void onPlayerJoin(PlayerJoinEvent event) {}

   public void onPlayerKick(PlayerKickEvent event) {}

   public void onPlayerQuit(PlayerQuitEvent event) {}

   public void onPlayerRespawn(PlayerRespawnEvent event) {}

   public void onPlayerTeleport(PlayerTeleportEvent event) {}

   public void onEntityChangeBlock(EntityChangeBlockEvent event) {}

   public void onEntityExplode(EntityExplodeEvent event) {}
}
