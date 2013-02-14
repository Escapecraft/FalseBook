package com.bukkit.gemo.FalseBook.Cart;

import com.bukkit.gemo.FalseBook.Cart.CartHandler;
import com.bukkit.gemo.FalseBook.Cart.CartMechanic;
import com.bukkit.gemo.FalseBook.Cart.CartWorldSettings;
import com.bukkit.gemo.FalseBook.Cart.FalseBookCartCore;
import com.bukkit.gemo.FalseBook.Cart.Blocks.Launcher;
import com.bukkit.gemo.utils.ChatUtils;
import com.bukkit.gemo.utils.LWCProtection;
import com.bukkit.gemo.utils.SignUtils;
import com.bukkit.gemo.utils.UtilPermissions;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.PoweredMinecart;
import org.bukkit.entity.StorageMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;

public class FalseBookCartBlockListener implements Listener, Runnable {

   private ConcurrentHashMap queuedCartEvents = new ConcurrentHashMap();


   @EventHandler
   public void onBlockPlace(BlockPlaceEvent event) {
      Player player = event.getPlayer();
      Block block = event.getBlock();
      Block under = block.getRelative(0, -1, 0);
      if(block.getTypeId() == Material.RAILS.getId()) {
         CartWorldSettings thisSettings = FalseBookCartCore.getOrCreateSettings(block.getWorld().getName());
         CartMechanic mechanic = thisSettings.getMechanic(under);
         if(mechanic != null) {
            mechanic.checkRailCreation(event, player);
            return;
         }
      }

   }

   public void run() {
      this.queuedCartEvents.clear();
   }

   @EventHandler
   public void onSignChange(SignChangeEvent event) {
      if(event.getBlock().getTypeId() == Material.SIGN_POST.getId()) {
         Player player = event.getPlayer();
         Sign sign = (Sign)event.getBlock().getState();
         CartWorldSettings thisSettings = FalseBookCartCore.getOrCreateSettings(player.getWorld().getName());
         Iterator var6 = thisSettings.getAllMechanics().iterator();

         while(var6.hasNext()) {
            CartMechanic mechanic = (CartMechanic)var6.next();
            if(!mechanic.checkSignCreation(event, player, sign)) {
               return;
            }
         }

         if(event.getLine(1).equalsIgnoreCase("[Cart]") || event.getLine(1).equalsIgnoreCase("[StorageCart]") || event.getLine(1).equalsIgnoreCase("[PoweredCart]")) {
            if(!UtilPermissions.playerCanUseCommand(player, "falsebook.cart.cartdispenser")) {
               SignUtils.cancelSignCreation(event, "You are not allowed to build stationsigns.");
               return;
            }

            if(event.getBlock().getData() != 0 && event.getBlock().getData() != 4 && event.getBlock().getData() != 8 && event.getBlock().getData() != 12) {
               SignUtils.cancelSignCreation(event, "Dispensersigns may only be created at specific angles (90 degrees).");
               return;
            }

            if(event.getLine(1).equalsIgnoreCase("[Cart]")) {
               event.setLine(1, "[Cart]");
            } else if(event.getLine(1).equalsIgnoreCase("[StorageCart]")) {
               event.setLine(1, "[StorageCart]");
            } else if(event.getLine(1).equalsIgnoreCase("[PoweredCart]")) {
               event.setLine(1, "[PoweredCart]");
            }

            ChatUtils.printInfo(player, "[FB-Cart]", ChatColor.GOLD, "Dispensersign created.");
         }

      }
   }

   @EventHandler(
      priority = EventPriority.HIGHEST
   )
   public void onBlockRedstoneChange(BlockRedstoneEvent event) {
      event.getNewCurrent();
      event.getOldCurrent();
      Block block = event.getBlock();
      World world = block.getWorld();
      Location loc = block.getLocation();
      loc.setY(loc.getY() + 1.0D);
      int x = block.getX();
      int y = block.getY();
      int z = block.getZ();
      CartWorldSettings thisSettings = FalseBookCartCore.getOrCreateSettings(block.getWorld().getName());
      int blockXPlus1 = block.getWorld().getBlockAt(x + 1, y, z).getTypeId();
      int blockXMin1 = block.getWorld().getBlockAt(x - 1, y, z).getTypeId();
      int blockZPlus1 = block.getWorld().getBlockAt(x, y, z + 1).getTypeId();
      int blockZMin1 = block.getWorld().getBlockAt(x, y, z - 1).getTypeId();
      int blockYPlus1 = block.getWorld().getBlockAt(x, y + 1, z).getTypeId();
      byte blockXPlus1Data = block.getWorld().getBlockAt(x + 1, y, z).getData();
      byte blockXMin1Data = block.getWorld().getBlockAt(x - 1, y, z).getData();
      byte blockZPlus1Data = block.getWorld().getBlockAt(x, y, z + 1).getData();
      byte blockZMin1Data = block.getWorld().getBlockAt(x, y, z - 1).getData();
      byte blockYPlus1Data = block.getWorld().getBlockAt(x, y + 1, z).getData();
      if(blockXPlus1 == thisSettings.getStationBlock() && blockXPlus1Data == thisSettings.getStationBlockValue() || blockXPlus1 == Material.RAILS.getId()) {
         this.launcherEvent(world, x + 1, y, z, event);
      }

      if(blockXMin1 == thisSettings.getStationBlock() && blockXMin1Data == thisSettings.getStationBlockValue() || blockXMin1 == Material.RAILS.getId()) {
         this.launcherEvent(world, x - 1, y, z, event);
      }

      if(blockZPlus1 == thisSettings.getStationBlock() && blockZPlus1Data == thisSettings.getStationBlockValue() || blockZPlus1 == Material.RAILS.getId()) {
         this.launcherEvent(world, x, y, z + 1, event);
      }

      if(blockZMin1 == thisSettings.getStationBlock() && blockZMin1Data == thisSettings.getStationBlockValue() || blockZMin1 == Material.RAILS.getId()) {
         this.launcherEvent(world, x, y, z - 1, event);
      }

      if(blockYPlus1 == thisSettings.getStationBlock() && blockYPlus1Data == thisSettings.getStationBlockValue() || blockYPlus1 == Material.RAILS.getId()) {
         this.launcherEvent(world, x, y + 1, z, event);
      }

      if(blockXPlus1 == Material.SIGN_POST.getId()) {
         this.launcherEvent(world, x + 1, y + 1, z, event);
      }

      if(blockXMin1 == Material.SIGN_POST.getId()) {
         this.launcherEvent(world, x - 1, y + 1, z, event);
      }

      if(blockZPlus1 == Material.SIGN_POST.getId()) {
         this.launcherEvent(world, x, y + 1, z + 1, event);
      }

      if(blockZMin1 == Material.SIGN_POST.getId()) {
         this.launcherEvent(world, x, y + 1, z - 1, event);
      }

      if(blockXPlus1 == Material.CHEST.getId()) {
         this.cartDepositEvent(world, x + 1, y, z, event);
      }

      if(blockXMin1 == Material.CHEST.getId()) {
         this.cartDepositEvent(world, x - 1, y, z, event);
      }

      if(blockZPlus1 == Material.CHEST.getId()) {
         this.cartDepositEvent(world, x, y, z + 1, event);
      }

      if(blockZMin1 == Material.CHEST.getId()) {
         this.cartDepositEvent(world, x, y, z - 1, event);
      }

   }

   private void cartDepositEvent(World w, int x, int y, int z, BlockRedstoneEvent event) {
      Block block = w.getBlockAt(x, y, z);
      Block underBlock = w.getBlockAt(x, y - 1, z);
      Block signBlock = w.getBlockAt(x, y - 2, z);
      CartWorldSettings thisSettings = FalseBookCartCore.getOrCreateSettings(block.getWorld().getName());
      if(underBlock.getTypeId() == thisSettings.getCollectDepositBlock() && underBlock.getData() == thisSettings.getCollectDepositBlockValue() && signBlock.getType().equals(Material.SIGN_POST)) {
         Sign sign = (Sign)signBlock.getState();
         if(!sign.getLine(1).equalsIgnoreCase("[Cart]") && !sign.getLine(1).equalsIgnoreCase("[PoweredCart]") && !sign.getLine(1).equalsIgnoreCase("[StorageCart]")) {
            return;
         }

         if(this.queuedCartEvents.get(w.getName() + "_" + x + "-" + y + "-" + z) != null) {
            return;
         }

         this.queuedCartEvents.put(w.getName() + "_" + x + "-" + y + "-" + z, sign);
         if(event.getNewCurrent() == 0) {
            return;
         }

         short type = 328;
         if(sign.getLine(1).equalsIgnoreCase("[StorageCart]")) {
            type = 342;
         }

         if(sign.getLine(1).equalsIgnoreCase("[PoweredCart]")) {
            type = 343;
         }

         if(!LWCProtection.protectionsAreEqual(block, signBlock)) {
            return;
         }

         byte newZ = 0;
         byte newX = 0;
         if(sign.getRawData() == 0) {
            newZ = -2;
         } else if(sign.getRawData() == 4) {
            newX = 2;
         } else if(sign.getRawData() == 8) {
            newZ = 2;
         } else {
            if(sign.getRawData() != 12) {
               return;
            }

            newX = -2;
         }

         Location loc = new Location(w, (double)(x + newX) + 0.5D, (double)y + 0.5D, (double)(z + newZ) + 0.5D);
         if(!loc.getBlock().getType().equals(Material.RAILS) && !loc.getBlock().getType().equals(Material.POWERED_RAIL) && !loc.getBlock().getType().equals(Material.DETECTOR_RAIL)) {
            return;
         }

         Chest chest = (Chest)block.getState();
         int cartAmount = 0;
         ArrayList otherItems = new ArrayList();

         int newCarts;
         for(newCarts = 0; newCarts < chest.getInventory().getSize(); ++newCarts) {
            if(chest.getInventory().getItem(newCarts) != null && chest.getInventory().getItem(newCarts).getTypeId() != Material.AIR.getId()) {
               if(chest.getInventory().getItem(newCarts).getTypeId() == type) {
                  cartAmount += chest.getInventory().getItem(newCarts).getAmount();
               } else if(chest.getInventory().getItem(newCarts).getTypeId() > 0) {
                  otherItems.add(chest.getInventory().getItem(newCarts));
               }
            }
         }

         if(cartAmount == 0) {
            return;
         }

         chest.getInventory().clear();

         for(newCarts = 0; newCarts < otherItems.size(); ++newCarts) {
            chest.getInventory().addItem(new ItemStack[]{(ItemStack)otherItems.get(newCarts)});
         }

         ItemStack var20 = null;
         var20 = new ItemStack(Material.MINECART, cartAmount - 1);
         if(type == 342) {
            var20 = new ItemStack(Material.STORAGE_MINECART, cartAmount - 1);
         } else if(type == 343) {
            var20 = new ItemStack(Material.POWERED_MINECART, cartAmount - 1);
         }

         if(cartAmount - 1 > 0) {
            chest.getInventory().addItem(new ItemStack[]{var20});
         }

         Minecart thisCart = null;
         if(type == 328) {
            thisCart = (Minecart)w.spawn(loc, Minecart.class);
         }

         if(type == 342) {
            thisCart = (Minecart)w.spawn(loc, StorageMinecart.class);
         }

         if(type == 343) {
            thisCart = (Minecart)w.spawn(loc, PoweredMinecart.class);
         }

         if(thisCart != null) {
            CartHandler.getFalseBookMinecart(thisCart).setOwner(LWCProtection.getProtectionOwner(chest.getBlock()));
         }
      }

   }

   private void launcherEvent(World w, int x, int y, int z, BlockRedstoneEvent event) {
      if(event.getNewCurrent() != 0) {
         Block block = w.getBlockAt(x, y, z);
         Block underBlock = w.getBlockAt(x, y - 1, z);
         Block railBlock = w.getBlockAt(x, y + 1, z);
         CartWorldSettings thisSettings = FalseBookCartCore.getOrCreateSettings(block.getWorld().getName());
         if(underBlock.getTypeId() == thisSettings.getStationBlock() && underBlock.getData() == thisSettings.getStationBlockValue() && block.getType().equals(Material.RAILS)) {
            this.launcherEvent(w, x, y - 1, z, event);
         } else if(block.getTypeId() == thisSettings.getStationBlock() && block.getData() == thisSettings.getStationBlockValue() && railBlock.getType().equals(Material.RAILS)) {
            Block signBlock = w.getBlockAt(x, y - 1, z);
            if(!signBlock.getType().equals(Material.SIGN_POST)) {
               return;
            }

            if(!((Sign)signBlock.getState()).getLine(1).equalsIgnoreCase("[Station]")) {
               return;
            }

            if(thisSettings.isUseSimpleCartSystem() && !((Boolean)Launcher.isCartOnBlock(signBlock).get(0)).booleanValue() && ((Boolean)Launcher.isPlayerOnBlock(signBlock).get(0)).booleanValue()) {
               Location ent = signBlock.getLocation();
               ent.setY(ent.getY() + 2.0D);
               Minecart i = (Minecart)signBlock.getWorld().spawn(ent, Minecart.class);
               i.setPassenger((Entity)Launcher.isPlayerOnBlock(signBlock).get(1));
               CartHandler.getFalseBookMinecart(i);
            }

            List var13 = w.getEntities();

            for(int var14 = 0; var14 < var13.size(); ++var14) {
               if((var13.get(var14) instanceof Minecart || var13.get(var14) instanceof StorageMinecart || var13.get(var14) instanceof PoweredMinecart) && ((Entity)var13.get(var14)).getLocation().getBlockX() == x && ((Entity)var13.get(var14)).getLocation().getBlockZ() == z && ((Entity)var13.get(var14)).getLocation().getBlockY() == railBlock.getY()) {
                  Launcher.Execute((Minecart)var13.get(var14), signBlock);
                  return;
               }
            }
         }

      }
   }
}
