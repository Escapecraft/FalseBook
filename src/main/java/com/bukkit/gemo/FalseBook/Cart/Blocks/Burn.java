package com.bukkit.gemo.FalseBook.Cart.Blocks;

import com.bukkit.gemo.utils.BlockUtils;
import com.bukkit.gemo.utils.ChatUtils;
import com.bukkit.gemo.utils.FBItemType;
import com.bukkit.gemo.utils.InventoryUtils;
import com.bukkit.gemo.utils.LWCProtection;
import com.bukkit.gemo.utils.SignUtils;
import com.bukkit.gemo.utils.UtilPermissions;
import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Furnace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.StorageMinecart;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;

public class Burn {

   public static boolean checkSignCreation(SignChangeEvent event, Player player, Sign sign) {
      if(!UtilPermissions.playerCanUseCommand(player, "falsebook.cart.autoburn")) {
         SignUtils.cancelSignCreation(event, "You are not allowed to build burnsigns.");
         return false;
      } else {
         event.setLine(1, "[Burn]");
         ChatUtils.printInfo(player, "[FB-Cart]", ChatColor.GOLD, "Burnsign created.");
         return true;
      }
   }

   public static void Execute(Minecart cart, Sign sign) {
      StorageMinecart storage = (StorageMinecart)cart;
      ArrayList toBurnMat = SignUtils.parseLineToItemList(sign.getLine(2), "-", false);
      ArrayList burnMat = SignUtils.parseLineToItemList(sign.getLine(3), "-", false);
      if(toBurnMat.size() >= 1 && burnMat.size() >= 1) {
         Location loc = sign.getBlock().getLocation();
         World w = loc.getWorld();
         int x = loc.getBlockX();
         int y = loc.getBlockY();
         int z = loc.getBlockZ();
         ArrayList furnaces = BlockUtils.getAdjacentFurnaces(w, x, y + 2, z);
         BlockUtils.addAdjacentFurnaces(furnaces, w, x, y + 1, z);
         BlockUtils.addAdjacentFurnaces(furnaces, w, x, y, z);
         w = null;
         loc = null;

         for(int i = 0; i < furnaces.size(); ++i) {
            if(LWCProtection.protectionsAreEqual(((Furnace)furnaces.get(i)).getBlock(), sign.getBlock())) {
               getMaterialFromFurnace((Furnace)furnaces.get(i), storage, sign);
               if(!InventoryUtils.isInventoryEmpty(storage.getInventory())) {
                  int j;
                  for(j = 0; j < toBurnMat.size(); ++j) {
                     putMaterialIntoFurnace((Furnace)furnaces.get(i), storage, (FBItemType)toBurnMat.get(j), sign);
                  }

                  for(j = 0; j < burnMat.size(); ++j) {
                     putFuelIntoFurnace((Furnace)furnaces.get(i), storage, (FBItemType)burnMat.get(j), sign);
                  }
               }
            }
         }

         furnaces.clear();
         furnaces = null;
         toBurnMat.clear();
         burnMat.clear();
         burnMat = null;
         toBurnMat = null;
      }
   }

   private static void putFuelIntoFurnace(Furnace furnace, StorageMinecart storage, FBItemType itemType, Sign signBlock) {
      int fuelInStorage = InventoryUtils.countItemInInventory(storage.getInventory(), itemType);
      if(fuelInStorage >= 1) {
         ItemStack item = null;
         ItemStack restItem = null;

         for(int i = 0; i < storage.getInventory().getSize(); ++i) {
            item = storage.getInventory().getItem(i);
            if(item != null && item.getTypeId() != Material.AIR.getId() && itemType.equals(item)) {
               if(itemType.getAmount() == -1) {
                  restItem = InventoryUtils.addItemIntoSlot(furnace.getInventory(), 1, item);
               } else if(itemType.getAmount() > 0) {
                  restItem = InventoryUtils.addItemIntoSlotWithMaxAmount(furnace.getInventory(), 1, item, itemType.getAmount());
               }

               storage.getInventory().setItem(i, restItem);
            }
         }

      }
   }

   private static void putMaterialIntoFurnace(Furnace furnace, StorageMinecart storage, FBItemType itemType, Sign signBlock) {
      int matInStorage = InventoryUtils.countItemInInventory(storage.getInventory(), itemType);
      if(matInStorage >= 1) {
         ItemStack item = null;
         ItemStack restItem = null;

         for(int i = 0; i < storage.getInventory().getSize(); ++i) {
            item = storage.getInventory().getItem(i);
            if(item != null && item.getTypeId() != Material.AIR.getId() && itemType.equals(item)) {
               if(itemType.getAmount() == -1) {
                  restItem = InventoryUtils.addItemIntoSlot(furnace.getInventory(), 0, item);
               } else if(itemType.getAmount() > 0) {
                  restItem = InventoryUtils.addItemIntoSlotWithMaxAmount(furnace.getInventory(), 0, item, itemType.getAmount());
               }

               storage.getInventory().setItem(i, restItem);
            }
         }

      }
   }

   private static void getMaterialFromFurnace(Furnace furnace, StorageMinecart storage, Sign signBlock) {
      ItemStack inFurnace = furnace.getInventory().getItem(2);
      if(inFurnace != null) {
         int matInFurnace = inFurnace.getAmount();
         if(matInFurnace > 0) {
            int transferedItems = InventoryUtils.addItem(storage.getInventory(), inFurnace);
            int restAmount = matInFurnace - transferedItems;
            if(restAmount < 1) {
               furnace.getInventory().setItem(2, (ItemStack)null);
            } else {
               inFurnace.setAmount(restAmount);
            }
         }

      }
   }
}
