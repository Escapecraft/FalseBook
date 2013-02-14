package com.bukkit.gemo.utils.Permissions;

import com.bukkit.gemo.utils.Permissions.IPermissions;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;

public class NoPermissions implements IPermissions {

   public boolean isActive() {
      return true;
   }

   public List getGroups(Player player) {
      return new ArrayList();
   }

   public List getGroups(String playername, String worldName) {
      return new ArrayList();
   }

   public boolean permission(Player player, String node) {
      return player.isOp();
   }
}
