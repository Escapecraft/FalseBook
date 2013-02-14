package com.bukkit.gemo.utils.Permissions.System;

import com.bukkit.gemo.utils.Permissions.System.Permission;
import java.util.HashMap;
import org.bukkit.entity.Player;

public class PermissionPlayer {

   private HashMap cachedPermissions = new HashMap();


   public boolean hasPermission(Player player, String permissionNode, boolean forceUpdate) {
      Permission permission = (Permission)this.cachedPermissions.get(permissionNode);
      if(permission == null) {
         permission = new Permission(player, permissionNode);
         this.cachedPermissions.put(permissionNode, permission);
      }

      return permission.hasPermission(player, forceUpdate);
   }

   public void removePermission(String permissionNode) {
      this.cachedPermissions.remove(permissionNode);
   }

   public void clearPermissions() {
      this.cachedPermissions = new HashMap();
   }
}
