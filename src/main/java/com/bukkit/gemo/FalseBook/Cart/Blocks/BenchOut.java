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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.StorageMinecart;
import org.bukkit.event.block.SignChangeEvent;

public class BenchOut {

   public static boolean checkSignCreation(SignChangeEvent event, Player player, Sign sign) {
      if(!UtilPermissions.playerCanUseCommand(player, "falsebook.cart.autobenchout")) {
         SignUtils.cancelSignCreation(event, "You are not allowed to build BenchOut-signs.");
         return false;
      } else {
         event.setLine(0, "[BenchOut]");
         ChatUtils.printInfo(player, "[FB-Cart]", ChatColor.GOLD, "BenchOut-sign created.");
         return true;
      }
   }

   private static void countItemsInList(HashMap countList, ArrayList itemList) {
      for(int i = 0; i < 3; ++i) {
         if(((FBItemType)itemList.get(i)).getItemID() != 0) {
            String thisName = ((FBItemType)itemList.get(i)).getString();
            thisName = thisName.replace(":true", ":0");
            int oldCount = 0;
            if(countList.containsKey(thisName)) {
               oldCount = ((Integer)countList.get(thisName)).intValue();
            }

            ++oldCount;
            countList.put(thisName, Integer.valueOf(oldCount));
         }
      }

   }

   public static void Execute(Minecart cart, Sign sign) {
      StorageMinecart storage = (StorageMinecart)cart;
      if(InventoryUtils.countGeneralStackedFreeSpace(storage.getInventory()) >= 0) {
         ArrayList itemList1 = SignUtils.parseLineToItemListWithSize(sign.getLine(1), "-", true, 3, 3);
         ArrayList itemList2 = SignUtils.parseLineToItemListWithSize(sign.getLine(2), "-", true, 3, 3);
         ArrayList itemList3 = SignUtils.parseLineToItemListWithSize(sign.getLine(3), "-", true, 3, 3);
         if(itemList1 != null && itemList2 != null && itemList3 != null) {
            HashMap countList = new HashMap();
            countItemsInList(countList, itemList1);
            countItemsInList(countList, itemList2);
            countItemsInList(countList, itemList3);
            Location loc = sign.getBlock().getLocation();
            World w = loc.getWorld();
            int x = loc.getBlockX();
            int y = loc.getBlockY();
            int z = loc.getBlockZ();
            ArrayList chests = BlockUtils.getAdjacentChests(w, x, y + 2, z);
            BlockUtils.addAdjacentChests(chests, w, x, y + 1, z);
            BlockUtils.addAdjacentChests(chests, w, x, y, z);
            w = null;
            loc = null;
            Chest dChest = null;
            HashMap itemInChestAmount = new HashMap();
            HashMap transferedAmount = new HashMap();
            Iterator entry = countList.entrySet().iterator();

            while(entry.hasNext()) {
               Entry split = (Entry)entry.next();
               int oldCount = 0;
               if(itemInChestAmount.containsKey(split.getKey())) {
                  oldCount = ((Integer)itemInChestAmount.get(split.getKey())).intValue();
               }

               String[] restToRemove = ((String)split.getKey()).split(":");

               for(int removedAll = 0; removedAll < chests.size(); ++removedAll) {
                  try {
                     oldCount += InventoryUtils.countItemInInventory(((Chest)chests.get(removedAll)).getInventory(), Integer.valueOf(restToRemove[0]).intValue(), Short.valueOf(restToRemove[1]).shortValue(), false);
                     dChest = BlockUtils.isDoubleChest(((Chest)chests.get(removedAll)).getBlock());
                     if(dChest != null) {
                        oldCount += InventoryUtils.countItemInInventory(dChest.getInventory(), Integer.valueOf(restToRemove[0]).intValue(), Short.valueOf(restToRemove[1]).shortValue(), false);
                     }
                  } catch (Exception var24) {
                     Location location = sign.getBlock().getLocation();
                     String locString = location.getWorld().getName() + " , " + location.getBlockX() + " / " + location.getBlockY() + " / " + location.getBlockZ();
                     System.out.println("Error in [BenchOut] @ " + locString);
                     System.out.println("[ " + sign.getLine(0) + ", " + sign.getLine(1) + ", " + sign.getLine(2) + ", " + sign.getLine(3) + " ] ");
                  }
               }

               itemInChestAmount.put((String)split.getKey(), Integer.valueOf(oldCount));
               transferedAmount.put((String)split.getKey(), Integer.valueOf(0));
            }

            for(int var25 = 0; var25 < chests.size(); ++var25) {
               for(int var27 = 0; var27 < 3; ++var27) {
                  if(LWCProtection.protectionsAreEqual(((Chest)chests.get(var25)).getBlock(), sign.getBlock())) {
                     DepositItems((Chest)chests.get(var25), storage, (FBItemType)itemList1.get(var27), sign, var27, countList, itemInChestAmount, transferedAmount);
                     DepositItems((Chest)chests.get(var25), storage, (FBItemType)itemList2.get(var27), sign, 9 + var27, countList, itemInChestAmount, transferedAmount);
                     DepositItems((Chest)chests.get(var25), storage, (FBItemType)itemList3.get(var27), sign, 18 + var27, countList, itemInChestAmount, transferedAmount);
                  }
               }
            }

            String[] var28 = (String[])null;
            Iterator var31 = itemInChestAmount.entrySet().iterator();

            while(var31.hasNext()) {
               Entry var26 = (Entry)var31.next();
               var28 = ((String)var26.getKey()).split(":");
               int var29;
               if(((Integer)var26.getValue()).intValue() < 1) {
                  for(var29 = 0; var29 < chests.size(); ++var29) {
                     InventoryUtils.removeItemInInventory(((Chest)chests.get(var29)).getInventory(), Integer.valueOf(var28[0]).intValue(), Short.valueOf(var28[1]).shortValue());
                     dChest = BlockUtils.isDoubleChest(((Chest)chests.get(var29)).getBlock());
                     if(dChest != null) {
                        InventoryUtils.removeItemInInventory(dChest.getInventory(), Integer.valueOf(var28[0]).intValue(), Short.valueOf(var28[1]).shortValue());
                     }
                  }
               } else {
                  var29 = ((Integer)transferedAmount.get(var26.getKey())).intValue();

                  for(boolean var30 = false; var29 > 0 && !var30; var30 = true) {
                     var30 = false;

                     for(int i = 0; i < chests.size(); ++i) {
                        var29 -= InventoryUtils.removeItemAmountInInventory(((Chest)chests.get(i)).getInventory(), Integer.valueOf(var28[0]).intValue(), Short.valueOf(var28[1]).shortValue(), var29);
                        dChest = BlockUtils.isDoubleChest(((Chest)chests.get(i)).getBlock());
                        if(dChest != null) {
                           var29 -= InventoryUtils.removeItemAmountInInventory(dChest.getInventory(), Integer.valueOf(var28[0]).intValue(), Short.valueOf(var28[1]).shortValue(), var29);
                        }
                     }
                  }
               }
            }

            chests.clear();
            chests = null;
            itemList3 = null;
            itemList2 = null;
            itemList1 = null;
         }
      }
   }

   private static boolean DepositItems(Chest chest, StorageMinecart storage, FBItemType BlockType, Sign signBlock, int slot, HashMap countList, HashMap itemInChestAmount, HashMap transferedAmount) {
      if(BlockType.getItemID() == Material.AIR.getId()) {
         return true;
      } else {
         boolean signCount = false;

         int signCount1;
         try {
            signCount1 = ((Integer)countList.get(BlockType.getItemID() + ":" + BlockType.getItemData())).intValue();
         } catch (Exception var15) {
            return false;
         }

         if(signCount1 < 1) {
            return false;
         } else {
            int inAllChestsAmount = ((Integer)itemInChestAmount.get(BlockType.getItemID() + ":" + BlockType.getItemData())).intValue();
            boolean transfered = false;
            int oldCount = InventoryUtils.countItemInSlot(storage.getInventory(), BlockType, slot);
            transfered = ItemHandler.transferItem(BlockType, chest.getInventory(), storage.getInventory(), slot, signCount1, inAllChestsAmount, false);
            Chest dChest = BlockUtils.isDoubleChest(chest.getBlock());
            if(dChest != null) {
               transfered = transfered || ItemHandler.transferItem(BlockType, dChest.getInventory(), storage.getInventory(), slot, signCount1, inAllChestsAmount, true);
            }

            if(transfered) {
               int oldTransfered = 0;
               if(transferedAmount.containsKey(BlockType.getItemID() + ":" + BlockType.getItemData())) {
                  oldTransfered = ((Integer)transferedAmount.get(BlockType.getItemID() + ":" + BlockType.getItemData())).intValue();
               }

               int newCount = InventoryUtils.countItemInSlot(storage.getInventory(), BlockType, slot);
               oldTransfered += newCount - oldCount;
               countList.put(BlockType.getItemID() + ":" + BlockType.getItemData(), Integer.valueOf(signCount1 - 1));
               itemInChestAmount.put(BlockType.getItemID() + ":" + BlockType.getItemData(), Integer.valueOf(inAllChestsAmount - (newCount - oldCount)));
               transferedAmount.put(BlockType.getItemID() + ":" + BlockType.getItemData(), Integer.valueOf(oldTransfered));
            }

            return transfered;
         }
      }
   }
}
