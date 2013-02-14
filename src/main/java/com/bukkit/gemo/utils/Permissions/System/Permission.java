package com.bukkit.gemo.utils.Permissions.System;

import com.bukkit.gemo.FalseBook.Core.FalseBookCore;
import com.bukkit.gemo.utils.UtilPermissions;
import org.bukkit.entity.Player;

public class Permission {

   private static long OUTDATE_PERMISSION_TIME = 15000L;
   private String permissionNode = "";
   private boolean hasPermission = false;
   private long timestamp = Long.MIN_VALUE;


   public static void setPermissionCacheTime(long permissionCacheTime) {
      OUTDATE_PERMISSION_TIME = permissionCacheTime;
   }

   public Permission(Player player, String permissionNode) {
      this.permissionNode = permissionNode;
      this.updatePermission(player, true);
   }

   private void setPermission(boolean hasPermission) {
      this.hasPermission = hasPermission;
      this.timestamp = System.currentTimeMillis();
   }

   private boolean updatePermission(Player player, boolean forceUpdate) {
      if(forceUpdate || !FalseBookCore.usePermissionCaching() || System.currentTimeMillis() - OUTDATE_PERMISSION_TIME >= this.timestamp) {
         this.setPermission(UtilPermissions.getPermissionFromPermissionPlugin(player, this.permissionNode));
      }

      return this.hasPermission;
   }

   public boolean hasPermission(Player player, boolean forceUpdate) {
      return this.updatePermission(player, forceUpdate);
   }
}
