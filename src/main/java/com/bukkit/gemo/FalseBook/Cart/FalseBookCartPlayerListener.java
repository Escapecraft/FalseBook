package com.bukkit.gemo.FalseBook.Cart;

import com.bukkit.gemo.FalseBook.Cart.CartHandler;
import com.bukkit.gemo.FalseBook.Cart.FalseBookCartCore;
import com.bukkit.gemo.FalseBook.Cart.FalseBookMinecart;
import com.bukkit.gemo.utils.ChatUtils;
import com.bukkit.gemo.utils.UtilPermissions;
import org.bukkit.ChatColor;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.StorageMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class FalseBookCartPlayerListener implements Listener {

   @EventHandler
   public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
      if(!(event.getRightClicked() instanceof StorageMinecart)) {
         if(CartHandler.isInLockMode(event.getPlayer())) {
            CartHandler.removeFromLockMode(event.getPlayer());
            ChatUtils.printInfo(event.getPlayer(), "[FB-Cart]", ChatColor.GRAY, "You did not rightclick on a storagecart. Exiting lockmode...");
         } else if(CartHandler.isInUnLockMode(event.getPlayer())) {
            CartHandler.removeFromUnLockMode(event.getPlayer());
            ChatUtils.printInfo(event.getPlayer(), "[FB-Cart]", ChatColor.GRAY, "You did not rightclick on a storagecart. Exiting unlockmode...");
         }

      } else {
         FalseBookMinecart FBCart = CartHandler.getFalseBookMinecart((Minecart)event.getRightClicked());
         if(CartHandler.isInUnLockMode(event.getPlayer())) {
            if(FBCart.getOwner().equalsIgnoreCase("")) {
               ChatUtils.printError(event.getPlayer(), "[FB-Cart]", "This cart is not owned by anyone.");
               CartHandler.removeFromUnLockMode(event.getPlayer());
               event.setCancelled(true);
            } else if(!FBCart.getOwner().equalsIgnoreCase(event.getPlayer().getName())) {
               if(!UtilPermissions.playerCanUseCommand(event.getPlayer(), "falsebook.admin.cart.canlookintoallcarts")) {
                  ChatUtils.printError(event.getPlayer(), "[FB-Cart]", "You are not allowed to unlock this storagecart!");
                  CartHandler.removeFromLockMode(event.getPlayer());
                  event.setCancelled(true);
               } else {
                  FBCart.setOwner("");
                  CartHandler.removeLockedCart(FBCart.getMinecart().getUniqueId());
                  ChatUtils.printSuccess(event.getPlayer(), "[FB-Cart]", "Cart unlocked.");
                  CartHandler.removeFromLockMode(event.getPlayer());
                  event.setCancelled(true);
               }
            } else {
               FBCart.setOwner("");
               CartHandler.removeLockedCart(FBCart.getMinecart().getUniqueId());
               ChatUtils.printSuccess(event.getPlayer(), "[FB-Cart]", "Cart unlocked.");
               CartHandler.removeFromLockMode(event.getPlayer());
               event.setCancelled(true);
            }
         } else {
            if(FalseBookCartCore.getOrCreateSettings(event.getPlayer().getWorld().getName()).isAutoLockStorageCarts() || CartHandler.isInLockMode(event.getPlayer())) {
               if(FBCart.getOwner().equalsIgnoreCase("") && !FBCart.isProgrammed()) {
                  FBCart.setOwner(event.getPlayer().getName());
                  ChatUtils.printSuccess(event.getPlayer(), "[FB-Cart]", "You are now the owner of this storagecart!");
                  CartHandler.addLockedCart(FBCart.getMinecart().getUniqueId(), FBCart.getOwner());
                  CartHandler.removeFromLockMode(event.getPlayer());
                  event.setCancelled(true);
                  return;
               }

               if(!FBCart.getOwner().equalsIgnoreCase(event.getPlayer().getName())) {
                  ChatUtils.printError(event.getPlayer(), "[FB-Cart]", "This cart is already owned or currently programmed!");
                  CartHandler.removeFromLockMode(event.getPlayer());
               }
            }

            if(!FBCart.getOwner().equalsIgnoreCase("")) {
               if(!FBCart.getOwner().equalsIgnoreCase(event.getPlayer().getName())) {
                  if(!UtilPermissions.playerCanUseCommand(event.getPlayer(), "falsebook.admin.cart.canlookintoallcarts")) {
                     ChatUtils.printError(event.getPlayer(), "[FB-Cart]", "You are not the owner of this storagecart!");
                     event.setCancelled(true);
                     return;
                  }

                  ChatUtils.printInfo(event.getPlayer(), "[FB-Cart]", ChatColor.GRAY, "This storagecart is owned by \'" + FBCart.getOwner() + "\'.");
                  return;
               }

               ChatUtils.printInfo(event.getPlayer(), "[FB-Cart]", ChatColor.GRAY, "This storagecart is owned by you.");
            }

         }
      }
   }
}
