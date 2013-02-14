package com.bukkit.gemo.FalseBook.Core;

import com.bukkit.gemo.FalseBook.Block.FalseBookBlockCore;
import com.bukkit.gemo.FalseBook.Block.Config.ConfigHandler;
import com.bukkit.gemo.FalseBook.Cart.FalseBookCartCore;
import com.bukkit.gemo.FalseBook.Core.FalseBookCore;
import com.bukkit.gemo.FalseBook.Extra.FalseBookExtraCore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

public class FalseBookCoreWorldListener implements Listener {

   private FalseBookCore plugin;


   public FalseBookCoreWorldListener(FalseBookCore instance) {
      this.plugin = instance;
   }

   @EventHandler
   public void onWorldLoad(WorldLoadEvent event) {
      if(this.plugin.getServer().getPluginManager().getPlugin("FalseBookBlock") != null) {
         FalseBookBlockCore extraCore = (FalseBookBlockCore)this.plugin.getServer().getPluginManager().getPlugin("FalseBookBlock");
         if(extraCore.isEnabled()) {
            ConfigHandler.getOrCreateSettings(event.getWorld().getName());
         }
      }

      if(this.plugin.getServer().getPluginManager().getPlugin("FalseBookCart") != null) {
         FalseBookCartCore extraCore1 = (FalseBookCartCore)this.plugin.getServer().getPluginManager().getPlugin("FalseBookCart");
         if(extraCore1.isEnabled()) {
            FalseBookCartCore.getOrCreateSettings(event.getWorld().getName());
         }
      }

      if(this.plugin.getServer().getPluginManager().getPlugin("FalseBookExtra") != null) {
         FalseBookExtraCore extraCore2 = (FalseBookExtraCore)this.plugin.getServer().getPluginManager().getPlugin("FalseBookExtra");
         if(extraCore2.isEnabled()) {
            FalseBookExtraCore.getOrCreateSettings(event.getWorld().getName());
         }
      }

   }
}
