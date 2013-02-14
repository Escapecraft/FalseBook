package com.bukkit.gemo.utils.Permissions.System;

import com.bukkit.gemo.utils.Permissions.System.WorldPermissions;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import org.bukkit.entity.Player;

public class FBPermissionHandler {

   public static HashMap worldList = new HashMap();


   private static void addWorld(String worldName, WorldPermissions permissions) {
      worldList.put(worldName, permissions);
   }

   private static WorldPermissions getWorldForPlayer(Player player) {
      WorldPermissions wPermissions = (WorldPermissions)worldList.get(player.getWorld().getName());
      if(wPermissions == null) {
         wPermissions = new WorldPermissions();
         addWorld(player.getWorld().getName(), wPermissions);
      }

      return wPermissions;
   }

   public static boolean hasPermission(Player player, String permissionNode, boolean forceUpdate) {
      return getWorldForPlayer(player).hasPermission(player, permissionNode, forceUpdate);
   }

   public static void removePlayer(Player player) {
      Iterator var2 = worldList.entrySet().iterator();

      while(var2.hasNext()) {
         Entry entry = (Entry)var2.next();
         ((WorldPermissions)entry.getValue()).removePlayer(player.getName());
      }

   }
}
