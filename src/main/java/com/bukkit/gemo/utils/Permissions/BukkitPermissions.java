package com.bukkit.gemo.utils.Permissions;

import com.bukkit.gemo.utils.Permissions.SuperPermsPermissions;
import com.platymuus.bukkit.permissions.Group;
import com.platymuus.bukkit.permissions.PermissionsPlugin;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class BukkitPermissions extends SuperPermsPermissions {

   private PermissionsPlugin handler = null;


   public BukkitPermissions() {
      Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("PermissionsBukkit");
      if(plugin != null) {
         this.handler = (PermissionsPlugin)plugin;
      }
   }

   public boolean isActive() {
      return this.handler != null;
   }

   public List getGroups(Player player) {
      List groups = super.getGroups(player);
      if(this.handler == null) {
         return groups;
      } else {
         List found = this.handler.getGroups(player.getName());
         if(found.size() == 0) {
            return groups;
         } else {
            Iterator var5 = found.iterator();

            while(var5.hasNext()) {
               Group group = (Group)var5.next();
               groups.add(group.getName());
            }

            return groups;
         }
      }
   }

   public List getGroups(String playerName, String worldName) {
      List groups = super.getGroups(playerName, worldName);
      if(this.handler == null) {
         return groups;
      } else {
         List found = this.handler.getGroups(playerName);
         if(found.size() == 0) {
            return groups;
         } else {
            Iterator var6 = found.iterator();

            while(var6.hasNext()) {
               Group group = (Group)var6.next();
               groups.add(group.getName());
            }

            return groups;
         }
      }
   }

   public boolean permission(Player player, String node) {
      return player.hasPermission(node);
   }
}
