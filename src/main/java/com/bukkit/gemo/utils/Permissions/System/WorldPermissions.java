package com.bukkit.gemo.utils.Permissions.System;

import com.bukkit.gemo.utils.Permissions.System.PermissionPlayer;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.entity.Player;

public class WorldPermissions {

   public ConcurrentHashMap playerList = new ConcurrentHashMap();


   public PermissionPlayer getPermissionPlayer(String playerName) {
      PermissionPlayer pPlayer = (PermissionPlayer)this.playerList.get(playerName);
      if(pPlayer == null) {
         pPlayer = new PermissionPlayer();
         this.playerList.put(playerName, pPlayer);
      }

      return pPlayer;
   }

   public boolean removePlayer(String playerName) {
      return this.playerList.remove(playerName) != null;
   }

   public boolean hasPermission(Player player, String permissionNode, boolean forceUpdate) {
      return this.getPermissionPlayer(player.getName()).hasPermission(player, permissionNode, forceUpdate);
   }
}
