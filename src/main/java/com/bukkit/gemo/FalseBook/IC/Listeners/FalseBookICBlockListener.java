package com.bukkit.gemo.FalseBook.IC.Listeners;

import com.bukkit.gemo.FalseBook.IC.ICFactory;
import com.bukkit.gemo.utils.SignUtils;
import com.bukkit.gemo.utils.UtilPermissions;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;

public class FalseBookICBlockListener implements Listener {

   private ICFactory factory = null;


   public void init(ICFactory factory) {
      this.factory = factory;
   }

   @EventHandler
   public void onBlockPistonExtend(BlockPistonExtendEvent event) {
      if(!event.isCancelled()) {
         this.factory.handlePistonExtend(event);
      }
   }

   @EventHandler
   public void onBlockPistonRetract(BlockPistonRetractEvent event) {
      if(!event.isCancelled()) {
         this.factory.handlePistonRetract(event);
      }
   }

   @EventHandler
   public void onBlockBreak(BlockBreakEvent event) {
      if(!event.isCancelled()) {
         this.factory.handleBlockBreak(event);
      }
   }

   @EventHandler
   public void onSignChange(SignChangeEvent event) {
      if(!event.isCancelled()) {
         Player player = event.getPlayer();
         if(event.getLine(1).equalsIgnoreCase("[x]") && !UtilPermissions.playerCanUseCommand(player, "*") && !UtilPermissions.playerCanUseCommand(player, "falsebook.blocks.hiddenswitch.create") && !UtilPermissions.playerCanUseCommand(player, "falsebook.blocks.*")) {
            event.setCancelled(true);
            SignUtils.cancelSignCreation(event, "You are not allowed to build hidden switches.");
         } else {
            this.factory.handleSignChange(event);
         }
      }
   }

   @EventHandler(
      priority = EventPriority.HIGHEST
   )
   public void onBlockRedstoneChange(BlockRedstoneEvent event) {
      if(event.getNewCurrent() > 0 != event.getOldCurrent() > 0) {
         Block block = event.getBlock();
         this.signRedstoneEvent(block.getRelative(1, 0, 0), event, 0, 1);
         this.signRedstoneEvent(block.getRelative(-1, 0, 0), event, 0, 1);
         this.signRedstoneEvent(block.getRelative(0, 0, 1), event, 0, 1);
         this.signRedstoneEvent(block.getRelative(0, 0, -1), event, 0, 1);
      }
   }

   public void signRedstoneEvent(Block block, BlockRedstoneEvent event, int delayTicks, int searchTry) {
      this.factory.handleRedstoneEvent(block, event, delayTicks, searchTry);
   }
}
