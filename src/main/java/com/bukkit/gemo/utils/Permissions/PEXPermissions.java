package com.bukkit.gemo.utils.Permissions;

import com.bukkit.gemo.utils.Permissions.IPermissions;
import com.bukkit.gemo.utils.Permissions.SuperPermsPermissions;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class PEXPermissions extends SuperPermsPermissions implements IPermissions {

   public boolean isActive() {
      return true;
   }

   public boolean permission(Player player, String node) {
      boolean permission = super.permission(player, node);
      return permission?true:PermissionsEx.getPermissionManager().has(player, node);
   }

   public List getGroups(String playerName, String worldName) {
      PermissionUser user = PermissionsEx.getPermissionManager().getUser(playerName);
      if(user == null) {
         return new ArrayList();
      } else {
         PermissionGroup[] groups = user.getGroups(worldName);
         ArrayList groupMap = new ArrayList();
         PermissionGroup[] var9 = groups;
         int var8 = groups.length;

         for(int var7 = 0; var7 < var8; ++var7) {
            PermissionGroup group = var9[var7];
            this.getInheritedGroups(groupMap, group);
         }

         return groupMap;
      }
   }

   public List getGroups(Player player) {
      return this.getGroups(player.getName(), player.getWorld().getName());
   }

   public void getInheritedGroups(ArrayList groupMap, PermissionGroup group) {
      if(!groupMap.contains(group.getName())) {
         groupMap.add(group.getName());
         PermissionGroup[] inhGroups = group.getParentGroups();
         PermissionGroup[] var7 = inhGroups;
         int var6 = inhGroups.length;

         for(int var5 = 0; var5 < var6; ++var5) {
            PermissionGroup grp = var7[var5];
            this.getInheritedGroups(groupMap, grp);
         }
      }

   }
}
