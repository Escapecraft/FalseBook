package com.bukkit.gemo.FalseBook.IC.Listeners;

import com.bukkit.gemo.FalseBook.IC.ICFactory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class FalseBookICEntityListener implements Listener {

   private ICFactory factory = null;


   public void init(ICFactory factory) {
      this.factory = factory;
   }

   @EventHandler
   public void onEntityExplode(EntityExplodeEvent event) {
      if(!event.isCancelled()) {
         this.factory.handleExplodeEvent(event);
      }
   }

   @EventHandler
   public void onEntityChangeBlock(EntityChangeBlockEvent event) {
      if(!event.isCancelled()) {
         this.factory.handleEntityChangeBlock(event);
      }
   }
}
