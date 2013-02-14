package com.bukkit.gemo.FalseBook.Mechanics;

import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class MechanicListener {

   public void onLoad() {}

   public void onUnload() {}

   public void reloadMechanic() {}

   public void onCommand(CommandSender sender, Command command, String label, String[] args) {}

   public boolean isActivatedByRedstone(Block block, BlockRedstoneEvent event) {
      return false;
   }

   public void onBlockBreak(BlockBreakEvent event) {}

   public void onBlockPlace(BlockPlaceEvent event) {}

   public void onBlockRedstoneChange(BlockRedstoneEvent event) {}

   public void onBlockPistonExtend(BlockPistonExtendEvent event) {}

   public void onBlockPistonRetract(BlockPistonRetractEvent event) {}

   public void onSignChange(SignChangeEvent event) {}

   public void onPlayerInteract(PlayerInteractEvent event, boolean isWallSign, boolean isSignPost) {}

   public void onPlayerPreLogin(PlayerPreLoginEvent event) {}

   public void onPlayerLogin(PlayerLoginEvent event) {}

   public void onPlayerQuit(PlayerQuitEvent event) {}

   public void onPlayerTeleport(PlayerTeleportEvent event) {}

   public void onPlayerRespawn(PlayerRespawnEvent event) {}

   public void onEntityExplode(EntityExplodeEvent event) {}

   public void onEntityChangeBlock(EntityChangeBlockEvent event) {}
}
