package com.bukkit.gemo.FalseBook.Block.Listeners;

import com.bukkit.gemo.FalseBook.Block.Handler.WorldHandlerBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class FalseBookBlockListener implements Listener {

   private WorldHandlerBlock worldHandler;


   public FalseBookBlockListener(WorldHandlerBlock worldHandler) {
      this.worldHandler = worldHandler;
   }

   @EventHandler
   public void onBlockPistonExtend(BlockPistonExtendEvent event) {
      if(!event.isCancelled()) {
         this.worldHandler.getWorld(event.getBlock().getWorld().getName()).onPistonExtend(event);
      }
   }

   @EventHandler
   public void onBlockPistonRetract(BlockPistonRetractEvent event) {
      if(!event.isCancelled()) {
         if(event.isSticky()) {
            this.worldHandler.getWorld(event.getBlock().getWorld().getName()).onPistonRetract(event);
         }
      }
   }

   @EventHandler
   public void onSignChange(SignChangeEvent event) {
      if(!event.isCancelled()) {
         if(event.getPlayer() instanceof Player) {
            this.worldHandler.getWorld(event.getBlock().getWorld().getName()).onSignChange(event);
         }
      }
   }

   @EventHandler(
      priority = EventPriority.HIGHEST
   )
   public void onBlockRedstoneChange(BlockRedstoneEvent event) {
      this.worldHandler.getWorld(event.getBlock().getWorld().getName()).onRedstoneChange(event);
   }

   @EventHandler
   public void onBlockPlace(BlockPlaceEvent event) {
      if(!event.isCancelled()) {
         this.worldHandler.getWorld(event.getBlock().getWorld().getName()).onBlockPlace(event);
      }
   }

   @EventHandler
   public void onBlockBreak(BlockBreakEvent event) {
      if(!event.isCancelled()) {
         this.worldHandler.getWorld(event.getBlock().getWorld().getName()).onBlockBreak(event);
      }
   }

   @EventHandler
   public void onEntityExplode(EntityExplodeEvent event) {
      if(!event.isCancelled()) {
         this.worldHandler.getWorld(event.getLocation().getWorld().getName()).onEntityExplode(event);
      }
   }

   @EventHandler
   public void onEntityChangeBlock(EntityChangeBlockEvent event) {
      if(!event.isCancelled()) {
         this.worldHandler.getWorld(event.getBlock().getWorld().getName()).onEntityChangeBlock(event);
      }
   }

   @EventHandler(
      priority = EventPriority.HIGHEST
   )
   public void onPlayerInteract(PlayerInteractEvent event) {
      if(!event.isCancelled()) {
         this.worldHandler.getWorld(event.getPlayer().getWorld().getName()).onPlayerInteract(event);
      }
   }
}
