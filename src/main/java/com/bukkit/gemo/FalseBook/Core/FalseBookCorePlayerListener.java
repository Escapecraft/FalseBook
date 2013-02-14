package com.bukkit.gemo.FalseBook.Core;

import com.bukkit.gemo.utils.Permissions.System.FBPermissionHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class FalseBookCorePlayerListener implements Listener {

   @EventHandler
   public void onPlayerKick(PlayerKickEvent event) {
      if(!event.isCancelled()) {
         FBPermissionHandler.removePlayer(event.getPlayer());
      }
   }

   @EventHandler
   public void onPlayerQuit(PlayerQuitEvent event) {
      FBPermissionHandler.removePlayer(event.getPlayer());
   }
}
