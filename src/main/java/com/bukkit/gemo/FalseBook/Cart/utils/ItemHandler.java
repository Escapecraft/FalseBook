package com.bukkit.gemo.FalseBook.Cart.utils;

import com.bukkit.gemo.utils.BlockUtils;
import com.bukkit.gemo.utils.FBItemType;
import com.bukkit.gemo.utils.InventoryUtils;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ItemHandler {

   public static int transferItem(ItemStack itemStack, Inventory fromInventory, Inventory toInventory, int fromSlot) {
      ItemStack leftover = InventoryUtils.addItemNative(toInventory, itemStack.clone());
      if(leftover != null) {
         itemStack.setAmount(leftover.getAmount());
         return leftover.getAmount();
      } else {
         fromInventory.setItem(fromSlot, (ItemStack)null);
         return 0;
      }
   }

   public static int transferItemWithAmount(ItemStack itemStack, Inventory fromInventory, Inventory toInventory, int fromSlot, int amountToTransfer) {
      ItemStack thisStack = itemStack.clone();
      int transferAmount = Math.min(itemStack.getAmount(), amountToTransfer);
      thisStack.setAmount(transferAmount);
      int restAmount = itemStack.getAmount() - transferAmount;
      ItemStack leftover = InventoryUtils.addItemNative(toInventory, thisStack.clone());
      if(leftover != null) {
         leftover.setAmount(leftover.getAmount() + restAmount);
         amountToTransfer -= transferAmount - leftover.getAmount();
         fromInventory.setItem(fromSlot, leftover);
      } else {
         if(restAmount > 0) {
            itemStack.setAmount(restAmount);
            fromInventory.setItem(fromSlot, itemStack.clone());
         } else {
            fromInventory.setItem(fromSlot, (ItemStack)null);
         }

         amountToTransfer -= transferAmount;
      }

      if(amountToTransfer < 0) {
         amountToTransfer = 0;
      }

      return amountToTransfer;
   }

   public static int transferItemProgrammed(ItemStack itemStack, Inventory fromInventory, Inventory toInventory, int fromSlot) {
      ItemStack leftover = InventoryUtils.addItemNative(toInventory, itemStack.clone());
      if(leftover != null) {
         itemStack.setAmount(leftover.getAmount());
         return itemStack.getAmount() - leftover.getAmount();
      } else {
         fromInventory.setItem(fromSlot, (ItemStack)null);
         return itemStack.getAmount();
      }
   }

   public static boolean transferItem(FBItemType blockType, Inventory fromInventory, Inventory toInventory, int slot, int signCount, int amountInAllChests, boolean isDoubleChest) {
      int itemToTransferAmount = InventoryUtils.countItemInInventory(fromInventory, blockType.getItemID(), blockType.getItemData(), false);
      if(itemToTransferAmount < 1) {
         return false;
      } else {
         int placePerSlot = amountInAllChests / signCount;
         int maxStackSize = BlockUtils.getMaxStackSize(blockType.getItemID());
         placePerSlot = Math.min(placePerSlot, maxStackSize);
         if(placePerSlot < 1) {
            return false;
         } else {
            int oldAmount = 0;
            if(toInventory.getItem(slot) != null && blockType.equals(toInventory.getItem(slot))) {
               oldAmount = toInventory.getItem(slot).getAmount();
            }

            int freeSpace = maxStackSize - oldAmount;
            int canMaximumAdd = Math.min(freeSpace, placePerSlot);
            if(canMaximumAdd < 1) {
               return false;
            } else {
               toInventory.setItem(slot, new ItemStack(blockType.getItemID(), canMaximumAdd + oldAmount, blockType.getItemData()));
               return true;
            }
         }
      }
   }
}
