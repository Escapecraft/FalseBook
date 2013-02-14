package com.bukkit.gemo.FalseBook.Cart.Blocks;

import com.bukkit.gemo.FalseBook.Cart.utils.ItemHandler;
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
import org.bukkit.block.Chest;
import org.bukkit.block.Dispenser;
import org.bukkit.block.Sign;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.StorageMinecart;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;

public class Collect {

   public static boolean checkSignCreation(SignChangeEvent event, Player player, Sign sign) {
      if(!UtilPermissions.playerCanUseCommand(player, "falsebook.cart.collectanddeposit")) {
         SignUtils.cancelSignCreation(event, "You are not allowed to build collectorsigns.");
         return false;
      } else {
         event.setLine(1, "[Collect]");
         ChatUtils.printInfo(player, "[FB-Cart]", ChatColor.GOLD, "Collectsign created.");
         return true;
      }
   }

   public static void Execute(Minecart cart, Sign sign) {
      StorageMinecart storage = (StorageMinecart)cart;
      if(!InventoryUtils.isInventoryEmpty(storage.getInventory())) {
         String[] lines = new String[]{sign.getLine(2), sign.getLine(3)};
         ArrayList list = SignUtils.parseLinesToItemList(lines, "-", false);
         if(list.size() >= 1) {
            Location loc = sign.getBlock().getLocation();
            World w = loc.getWorld();
            int x = loc.getBlockX();
            int y = loc.getBlockY();
            int z = loc.getBlockZ();
            ArrayList chests = BlockUtils.getAdjacentChests(w, x, y + 2, z);
            BlockUtils.addAdjacentChests(chests, w, x, y + 1, z);
            BlockUtils.addAdjacentChests(chests, w, x, y, z);
            ArrayList dispensers = BlockUtils.getAdjacentDispenser(w, x, y + 2, z);
            BlockUtils.addAdjacentDispenser(dispensers, w, x, y + 1, z);
            BlockUtils.addAdjacentDispenser(dispensers, w, x, y, z);
            w = null;
            loc = null;

            int j;
            for(j = 0; j < list.size(); ++j) {
               CollectItemsToChests(chests, storage, (FBItemType)list.get(j), sign);
               if(InventoryUtils.isInventoryEmpty(storage.getInventory())) {
                  return;
               }
            }

            for(j = 0; j < list.size(); ++j) {
               CollectItemsToDispenser(dispensers, storage, (FBItemType)list.get(j), sign);
               if(InventoryUtils.isInventoryEmpty(storage.getInventory())) {
                  return;
               }
            }

            list.clear();
            chests.clear();
            list = null;
            chests = null;
         }
      }
   }

   private static boolean CollectItemsToChests(ArrayList chests, StorageMinecart storage, FBItemType itemType, Sign signBlock) {
      boolean amountInStorage = false;
      Chest dChest = null;

      for(int slot = 0; slot < storage.getInventory().getSize(); ++slot) {
         ItemStack itemStack = storage.getInventory().getItem(slot);
         if(itemStack != null && itemStack.getTypeId() != Material.AIR.getId() && itemStack.getTypeId() == itemType.getItemID() && (itemType.usesWildcart() || itemStack.getDurability() == itemType.getItemData())) {
            int var10 = itemStack.getAmount();
            int restToTransfer = var10;

            for(int i = 0; i < chests.size(); ++i) {
               if(LWCProtection.protectionsAreEqual(((Chest)chests.get(i)).getBlock(), signBlock.getBlock())) {
                  if(InventoryUtils.countGeneralStackedFreeSpace(((Chest)chests.get(i)).getInventory()) > 0) {
                     restToTransfer = ItemHandler.transferItem(itemStack, storage.getInventory(), ((Chest)chests.get(i)).getInventory(), slot);
                     if(restToTransfer < 1) {
                        break;
                     }
                  }

                  dChest = BlockUtils.isDoubleChest(((Chest)chests.get(i)).getBlock());
                  if(dChest != null && InventoryUtils.countGeneralStackedFreeSpace(dChest.getInventory()) > 0) {
                     restToTransfer = ItemHandler.transferItem(itemStack, storage.getInventory(), dChest.getInventory(), slot);
                  }

                  if(restToTransfer < 1) {
                     break;
                  }
               }
            }
         }
      }

      return true;
   }

   private static boolean CollectItemsToDispenser(ArrayList dispensers, StorageMinecart storage, FBItemType itemType, Sign signBlock) {
      boolean amountInStorage = false;

      for(int slot = 0; slot < storage.getInventory().getSize(); ++slot) {
         ItemStack itemStack = storage.getInventory().getItem(slot);
         if(itemStack != null && itemStack.getTypeId() != Material.AIR.getId() && itemStack.getTypeId() == itemType.getItemID() && (itemType.usesWildcart() || itemStack.getDurability() == itemType.getItemData())) {
            int var9 = itemStack.getAmount();

            for(int i = 0; i < dispensers.size(); ++i) {
               if(LWCProtection.protectionsAreEqual(((Dispenser)dispensers.get(i)).getBlock(), signBlock.getBlock()) && InventoryUtils.countGeneralStackedFreeSpace(((Dispenser)dispensers.get(i)).getInventory()) > 0) {
                  int restToTransfer = ItemHandler.transferItem(itemStack, storage.getInventory(), ((Dispenser)dispensers.get(i)).getInventory(), slot);
                  if(restToTransfer < 1) {
                     break;
                  }
               }
            }
         }
      }

      return true;
   }
}
