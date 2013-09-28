package com.bukkit.gemo.utils;

import com.bukkit.gemo.utils.BlockUtils;
import com.bukkit.gemo.utils.FBBlockType;
import com.bukkit.gemo.utils.FBItemType;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_6_R3.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_6_R3.inventory.CraftItemStack;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryUtils {

   public static boolean isInventoryEmpty(Inventory inventory) {
      ItemStack item = null;

      for(int i = 0; i < inventory.getSize(); ++i) {
         item = inventory.getItem(i);
         if(item != null && item.getTypeId() != Material.AIR.getId()) {
            return false;
         }
      }

      return true;
   }

   public static int countItemInInventory(Inventory inventory, ItemStack itemStack) {
      int count = 0;
      ItemStack item = null;

      for(int i = 0; i < inventory.getSize(); ++i) {
         item = inventory.getItem(i);
         if(item != null && item.getTypeId() != Material.AIR.getId() && item.getTypeId() == itemStack.getTypeId() && item.getDurability() == itemStack.getDurability() && item.getEnchantments().equals(itemStack.getEnchantments())) {
            count += item.getAmount();
         }
      }

      return count;
   }

   public static int countItemInInventory(Inventory inventory, FBItemType itemStack) {
      return countItemInInventory(inventory, itemStack.getItemID(), itemStack.getItemData(), itemStack.usesWildcart());
   }

   public static int countItemInInventory(Inventory inventory, int ID, short SubID, boolean useWildcart) {
      int count = 0;
      ItemStack item = null;

      for(int i = 0; i < inventory.getSize(); ++i) {
         item = inventory.getItem(i);
         if(item != null && item.getTypeId() != Material.AIR.getId() && item.getTypeId() == ID && (useWildcart || item.getDurability() == SubID)) {
            count += item.getAmount();
         }
      }

      return count;
   }

   public static int countItemInSlot(Inventory inventory, ItemStack itemStack, int slot) {
      return countItemInSlot(inventory, itemStack.getTypeId(), itemStack.getDurability(), slot);
   }

   public static int countItemInSlot(Inventory inventory, FBItemType itemStack, int slot) {
      return countItemInSlot(inventory, itemStack.getItemID(), itemStack.getItemData(), slot);
   }

   public static int countItemInSlot(Inventory inventory, int ID, short SubID, int slot) {
      ItemStack item = null;
      item = inventory.getItem(slot);
      return item == null?0:(item.getTypeId() == ID && item.getDurability() == SubID?item.getAmount():0);
   }

   public static int removeItemInInventory(Inventory inventory, ItemStack itemStack) {
      return removeItemInInventory(inventory, itemStack.getTypeId(), itemStack.getDurability());
   }

   public static int removeItemInInventory(Inventory inventory, FBBlockType itemStack) {
      return removeItemInInventory(inventory, itemStack.getItemID(), itemStack.getItemData());
   }

   public static int removeItemInInventory(Inventory inventory, int ID, short SubID) {
      int removed = 0;
      ItemStack item = null;

      for(int i = 0; i < inventory.getSize(); ++i) {
         item = inventory.getItem(i);
         if(item != null && item.getTypeId() != Material.AIR.getId() && item.getTypeId() == ID && item.getDurability() == SubID) {
            removed += item.getAmount();
            inventory.setItem(i, (ItemStack)null);
         }
      }

      return removed;
   }

   public static int removeItemAmountInInventory(Inventory inventory, FBBlockType itemStack, int amount) {
      return removeItemAmountInInventory(inventory, itemStack.getItemID(), itemStack.getItemData(), amount);
   }

   public static int removeItemAmountInInventory(Inventory inventory, int ID, short SubID, int amount) {
      if(amount < 1) {
         return 0;
      } else {
         int removed = 0;
         int restAmount = amount;
         ItemStack item = null;

         for(int i = 0; i < inventory.getSize(); ++i) {
            item = inventory.getItem(i);
            if(item != null && item.getTypeId() != Material.AIR.getId() && item.getTypeId() == ID && item.getDurability() == SubID) {
               if(restAmount >= item.getAmount()) {
                  removed += item.getAmount();
                  restAmount -= item.getAmount();
                  inventory.setItem(i, (ItemStack)null);
               } else {
                  int itemAmount = item.getAmount();
                  itemAmount -= restAmount;
                  removed += restAmount;
                  item.setAmount(itemAmount);
                  restAmount = 0;
               }

               if(restAmount < 1) {
                  break;
               }
            }
         }

         return removed;
      }
   }

   public static ItemStack addItemIntoSlot(Inventory inventory, int slot, ItemStack item) {
      ItemStack itemInSlot = inventory.getItem(slot);
      int maxStackSize = BlockUtils.getMaxStackSize(item.getTypeId());
      int amountToTransfer;
      int restAmount;
      if(itemInSlot != null && itemInSlot.getTypeId() == item.getTypeId() && itemInSlot.getDurability() == item.getDurability()) {
         maxStackSize -= itemInSlot.getAmount();
         amountToTransfer = Math.min(maxStackSize, item.getAmount());
         restAmount = item.getAmount() - amountToTransfer;
         itemInSlot.setAmount(itemInSlot.getAmount() + amountToTransfer);
         if(restAmount < 1) {
            return null;
         } else {
            item = item.clone();
            item.setAmount(restAmount);
            return item;
         }
      } else if(itemInSlot != null && itemInSlot.getTypeId() != Material.AIR.getId()) {
         return item.clone();
      } else {
         amountToTransfer = Math.min(maxStackSize, item.getAmount());
         restAmount = item.getAmount() - amountToTransfer;
         inventory.setItem(slot, new ItemStack(item.getTypeId(), amountToTransfer, item.getDurability()));
         if(restAmount < 1) {
            return null;
         } else {
            item = item.clone();
            item.setAmount(restAmount);
            return item;
         }
      }
   }

   public static ItemStack addItemIntoSlotWithMaxAmount(Inventory inventory, int slot, ItemStack item, int maxAmount) {
      ItemStack itemInSlot = inventory.getItem(slot);
      int maxStackSize = Math.min(maxAmount, BlockUtils.getMaxStackSize(item.getTypeId()));
      int amountToTransfer;
      int restAmount;
      if(itemInSlot != null && itemInSlot.getTypeId() == item.getTypeId() && itemInSlot.getDurability() == item.getDurability()) {
         maxStackSize -= itemInSlot.getAmount();
         amountToTransfer = Math.min(maxStackSize, item.getAmount());
         restAmount = item.getAmount() - amountToTransfer;
         if(amountToTransfer < 1) {
            return item.clone();
         } else {
            itemInSlot.setAmount(itemInSlot.getAmount() + amountToTransfer);
            if(restAmount < 1) {
               return null;
            } else {
               item = item.clone();
               item.setAmount(restAmount);
               return item;
            }
         }
      } else if(itemInSlot != null && itemInSlot.getTypeId() != Material.AIR.getId()) {
         return item.clone();
      } else {
         amountToTransfer = Math.min(maxStackSize, item.getAmount());
         restAmount = item.getAmount() - amountToTransfer;
         if(amountToTransfer < 1) {
            return item.clone();
         } else {
            inventory.setItem(slot, new ItemStack(item.getTypeId(), amountToTransfer, item.getDurability()));
            if(restAmount < 1) {
               return null;
            } else {
               item = item.clone();
               item.setAmount(restAmount);
               return item;
            }
         }
      }
   }

   public static ItemStack addItemNative(Inventory inventory, ItemStack item) {
      CraftInventory cInv = (CraftInventory)inventory;

      while(true) {
         int firstPartial = cInv.firstPartial(item.getType());
         int amount;
         if(firstPartial == -1) {
            int partialItem1 = cInv.firstEmpty();
            if(partialItem1 == -1) {
               return item;
            }

            amount = BlockUtils.getMaxStackSize(item.getTypeId());
            if(item.getAmount() <= amount) {
               cInv.setItem(partialItem1, item);
               break;
            }

            CraftItemStack partialAmount1 = CraftItemStack.asCraftCopy(item);
            partialAmount1.setAmount(amount);
            partialAmount1.setDurability(item.getDurability());
            partialAmount1.addUnsafeEnchantments(item.getEnchantments());
            cInv.setItem(partialItem1, partialAmount1);
            item.setAmount(item.getAmount() - amount);
         } else {
            ItemStack partialItem = cInv.getItem(firstPartial);
            amount = item.getAmount();
            int partialAmount = partialItem.getAmount();
            int maxAmount = partialItem.getMaxStackSize();
            if(amount + partialAmount <= maxAmount) {
               partialItem.setAmount(amount + partialAmount);
               break;
            }

            partialItem.setAmount(maxAmount);
            item.setAmount(amount + partialAmount - maxAmount);
         }
      }

      return null;
   }

   public static int addItem(Inventory inventory, ItemStack itemStack) {
      CraftInventory cInv = (CraftInventory)inventory;
      int itemAmount = itemStack.getAmount();

      while(true) {
         int firstPartial = cInv.firstPartial(itemStack.getType());
         int amount;
         if(firstPartial != -1) {
            ItemStack partialItem1 = cInv.getItem(firstPartial);
            amount = itemStack.getAmount();
            int partialAmount1 = partialItem1.getAmount();
            int maxAmount = Material.getMaterial(itemStack.getTypeId()).getMaxStackSize();
            if(amount + partialAmount1 <= maxAmount) {
               partialItem1.setAmount(amount + partialAmount1);
               return itemAmount;
            }

            partialItem1.setAmount(maxAmount);
            itemStack.setAmount(amount + partialAmount1 - maxAmount);
            return itemAmount - itemStack.getAmount();
         }

         int partialItem = inventory.firstEmpty();
         if(partialItem == -1) {
            return 0;
         }

         amount = Material.getMaterial(itemStack.getTypeId()).getMaxStackSize();
         if(itemStack.getAmount() <= amount) {
            cInv.setItem(partialItem, itemStack);
            return itemAmount;
         }

         CraftItemStack partialAmount = CraftItemStack.asCraftCopy(itemStack);
         partialAmount.setAmount(amount);
         partialAmount.setDurability(itemStack.getDurability());
         partialAmount.addUnsafeEnchantments(itemStack.getEnchantments());
         cInv.setItem(partialItem, partialAmount);
         itemStack.setAmount(itemStack.getAmount() - amount);
         addItem(inventory, itemStack);
      }
   }

   public static int countFreeSpace(Inventory inventory, ItemStack itemStack) {
      int amount = 0;
      int maxStackSize = BlockUtils.getMaxStackSize(itemStack.getType());
      ItemStack item = null;

      for(int i = 0; i < inventory.getSize(); ++i) {
         item = inventory.getItem(i);
         if(item != null && item.getTypeId() != Material.AIR.getId()) {
            if(inventory.getItem(i).getTypeId() == itemStack.getAmount() && inventory.getItem(i).getDurability() == itemStack.getDurability() && inventory.getItem(i).getEnchantments().equals(itemStack.getEnchantments()) && inventory.getItem(i).getAmount() <= maxStackSize) {
               amount += maxStackSize - inventory.getItem(i).getAmount();
            }
         } else {
            amount += maxStackSize;
         }
      }

      return amount;
   }

   public static int countGeneralItemAmount(Inventory inventory) {
      int amount = 0;
      ItemStack item = null;

      for(int i = 0; i < inventory.getSize(); ++i) {
         item = inventory.getItem(i);
         if(item != null && item.getTypeId() != Material.AIR.getId()) {
            amount += inventory.getItem(i).getAmount();
         }
      }

      return amount;
   }

   public static int countGeneralStackedFreeSpace(Inventory inventory) {
      int amount = 0;
      ItemStack item = null;

      for(int i = 0; i < inventory.getSize(); ++i) {
         item = inventory.getItem(i);
         if(item != null && item.getTypeId() != Material.AIR.getId()) {
            amount += BlockUtils.getMaxStackSize(item.getTypeId()) - item.getAmount();
         } else {
            amount += 64;
         }
      }

      return amount;
   }
}
