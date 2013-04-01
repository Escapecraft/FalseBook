package com.bukkit.gemo.utils.Permissions;

import com.bukkit.gemo.FalseBook.Core.FalseBookCore;
import com.bukkit.gemo.utils.Permissions.IPermissions;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.craftbukkit.v1_5_R2.entity.CraftHumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

public class SuperPermsPermissions implements IPermissions {

   private String groupPrefix = "falsebook.group.";


   public boolean isActive() {
      return true;
   }

   public boolean permission(Player player, String node) {
      try {
         return player.hasPermission(node);
      } catch (NoSuchMethodError var4) {
         return player.isOp();
      }
   }

   public List getGroups(Player player) {
      return this.getGroups(player.getName(), player.getWorld().getName());
   }

   public List getGroups(String playerName, String worldName) {
      Player player = FalseBookCore.getPlayer(playerName);
      if(player == null) {
         return new ArrayList();
      } else {
         ArrayList groups = new ArrayList();

         try {
            Method method = CraftHumanEntity.class.getDeclaredMethod("getEffectivePermissions", new Class[0]);
            if(method != null) {
               Iterator var7 = player.getEffectivePermissions().iterator();

               while(var7.hasNext()) {
                  PermissionAttachmentInfo pai = (PermissionAttachmentInfo)var7.next();
                  if(pai.getPermission().startsWith(this.groupPrefix)) {
                     groups.add(pai.getPermission().substring(this.groupPrefix.length()));
                  }
               }
            }
         } catch (NoSuchMethodException var8) {
            ;
         }

         return groups;
      }
   }
}
