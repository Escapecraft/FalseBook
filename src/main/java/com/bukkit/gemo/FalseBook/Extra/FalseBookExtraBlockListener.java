package com.bukkit.gemo.FalseBook.Extra;

import com.bukkit.gemo.FalseBook.Extra.Handler.WorldHandlerExtra;
import com.bukkit.gemo.FalseBook.Extra.World.FBWorldExtra;
import com.bukkit.gemo.FalseBook.World.FBWorld;
import java.util.ArrayList;
import java.util.Iterator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class FalseBookExtraBlockListener implements Listener {

   private WorldHandlerExtra worldHandler;


   public FalseBookExtraBlockListener(WorldHandlerExtra worldHandler) {
      this.worldHandler = worldHandler;
   }

   public void saveProtectedBlocks() {
      ArrayList worldList = this.worldHandler.getAllWorlds();
      Iterator var4 = worldList.iterator();

      while(var4.hasNext()) {
         FBWorld world = (FBWorld)var4.next();
         FBWorldExtra thisWorld = (FBWorldExtra)world;
         thisWorld.saveProtectedBlocks();
      }

   }

   @EventHandler
   public void onPistonExtend(BlockPistonExtendEvent event) {
      if(!event.isCancelled()) {
         this.worldHandler.getWorld(event.getBlock().getWorld().getName()).onPistonExtend(event);
      }
   }

   @EventHandler
   public void onEntityChangeBlock(EntityChangeBlockEvent event) {
      if(!event.isCancelled()) {
         this.worldHandler.getWorld(event.getBlock().getWorld().getName()).onEntityChangeBlock(event);
      }
   }

   @EventHandler
   public void onEntityExplode(EntityExplodeEvent event) {
      if(!event.isCancelled()) {
         this.worldHandler.getWorld(event.getLocation().getWorld().getName()).onEntityExplode(event);
      }
   }

   @EventHandler
   public void onPistonRetract(BlockPistonRetractEvent event) {
      if(!event.isCancelled()) {
         if(event.isSticky()) {
            this.worldHandler.getWorld(event.getBlock().getWorld().getName()).onPistonRetract(event);
         }
      }
   }

   @EventHandler
   public void onBlockBreak(BlockBreakEvent event) {
      if(!event.isCancelled()) {
         this.worldHandler.getWorld(event.getBlock().getWorld().getName()).onBlockBreak(event);
      }
   }

   @EventHandler(
      priority = EventPriority.HIGHEST
   )
   public void onRedstoneChange(BlockRedstoneEvent event) {
      this.worldHandler.getWorld(event.getBlock().getWorld().getName()).onRedstoneChange(event);
   }

   @EventHandler
   public void onBlockPhysics(BlockPhysicsEvent event) {
      if(!event.isCancelled()) {
         this.worldHandler.getWorld(event.getBlock().getWorld().getName()).onBlockPhysics(event);
      }
   }
}
