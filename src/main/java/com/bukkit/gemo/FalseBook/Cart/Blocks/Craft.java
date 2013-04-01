package com.bukkit.gemo.FalseBook.Cart.Blocks;

import com.bukkit.gemo.FalseBook.Cart.utils.FBCraftingManager;
import com.bukkit.gemo.FalseBook.Cart.utils.FBInventory;
import com.bukkit.gemo.utils.ChatUtils;
import com.bukkit.gemo.utils.FBBlockType;
import com.bukkit.gemo.utils.InventoryUtils;
import com.bukkit.gemo.utils.SignUtils;
import com.bukkit.gemo.utils.UtilPermissions;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.server.v1_5_R2.CraftingManager;
import net.minecraft.server.v1_5_R2.IRecipe;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_5_R2.CraftWorld;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.StorageMinecart;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;

public class Craft {

   public static boolean checkSignCreation(SignChangeEvent event, Player player, Sign sign) {
      if(!UtilPermissions.playerCanUseCommand(player, "falsebook.cart.autocraft")) {
         SignUtils.cancelSignCreation(event, "You are not allowed to build Craftsigns.");
         return false;
      } else {
         event.setLine(1, "[Craft]");
         ChatUtils.printInfo(player, "[FB-Cart]", ChatColor.GOLD, "Craftsign created.");
         return true;
      }
   }

   public static void Execute(Minecart cart, Sign sign) {
      StorageMinecart storage = (StorageMinecart)cart;
      if(!InventoryUtils.isInventoryEmpty(storage.getInventory())) {
         ArrayList toCraftMat = SignUtils.parseItems(sign.getLine(2), "####", false);
         if(toCraftMat != null) {
            ArrayList itemList = (ArrayList)CraftingManager.getInstance().getRecipes();
            IRecipe nowCraft = null;
            Iterator stacks = itemList.iterator();

            while(stacks.hasNext()) {
               IRecipe oldContent = (IRecipe)stacks.next();
               if(oldContent != null && oldContent.b() != null && oldContent.b().id == ((FBBlockType)toCraftMat.get(0)).getItemID() && oldContent.b().getData() == ((FBBlockType)toCraftMat.get(0)).getItemData()) {
                  nowCraft = oldContent;
                  break;
               }
            }

            if(nowCraft != null) {
               if(((FBBlockType)toCraftMat.get(0)).getItemData() > 0) {
                  nowCraft.b().setData(((FBBlockType)toCraftMat.get(0)).getItemData());
               } else {
                  nowCraft.b().setData(0);
               }

               ItemStack[] var19 = (ItemStack[])storage.getInventory().getContents().clone();
               ItemStack[] var20 = new ItemStack[9];

               for(int craftInventory = 0; craftInventory < 3; ++craftInventory) {
                  var20[craftInventory] = storage.getInventory().getItem(craftInventory);
                  var20[craftInventory + 3] = storage.getInventory().getItem(craftInventory + 9);
                  var20[craftInventory + 6] = storage.getInventory().getItem(craftInventory + 18);
               }

               CraftWorld var21a = (CraftWorld)storage.getWorld();
               FBInventory var21 = new FBInventory();

               for(int res = 0; res < var20.length; ++res) {
                  if(var20[res] != null && var20[res].getTypeId() != Material.AIR.getId()) {
                     var21.setItem(res, new net.minecraft.server.v1_5_R2.ItemStack(var20[res].getTypeId(), var20[res].getAmount(), var20[res].getDurability()));
                  }
               }

               net.minecraft.server.v1_5_R2.ItemStack var22 = null;
               ItemStack resultStack = new ItemStack(((FBBlockType)toCraftMat.get(0)).getItemID(), 1, ((FBBlockType)toCraftMat.get(0)).getItemData());
               int nowAmount = 0;

               boolean crafted;
               int result;
               for(crafted = false; (var22 = FBCraftingManager.craft(var21, var21a.getHandle())) != null && var22.id == resultStack.getTypeId() && var22.getData() == resultStack.getDurability(); crafted = true) {
                  for(result = 0; result < 9; ++result) {
                     if(var21.getItem(result) != null && var21.getItem(result).id != Material.AIR.getId()) {
                        --var21.getItem(result).count;
                        if(var21.getItem(result).count < 1) {
                           var21.setItem(result, (net.minecraft.server.v1_5_R2.ItemStack)null);
                        }
                     }
                  }

                  nowAmount += nowCraft.b().count;
                  resultStack.setAmount(nowAmount);
               }

               if(crafted) {
                  for(result = 0; result < 3; ++result) {
                     storage.getInventory().setItem(result, (ItemStack)null);
                     storage.getInventory().setItem(result + 9, (ItemStack)null);
                     storage.getInventory().setItem(result + 18, (ItemStack)null);
                  }

                  for(result = 0; result < 3; ++result) {
                     if(var21.getItem(result) != null && var21.getItem(result).id != Material.AIR.getId()) {
                        storage.getInventory().setItem(result, new ItemStack(var21.getItem(result).id, var21.getItem(result).count, Short.valueOf(String.valueOf(var21.getItem(result).getData())).shortValue()));
                     }

                     if(var21.getItem(result + 3) != null && var21.getItem(result + 3).id != Material.AIR.getId()) {
                        storage.getInventory().setItem(result + 9, new ItemStack(var21.getItem(result + 3).id, var21.getItem(result + 3).count, Short.valueOf(String.valueOf(var21.getItem(result + 3).getData())).shortValue()));
                     }

                     if(var21.getItem(result + 6) != null && var21.getItem(result + 6).id != Material.AIR.getId()) {
                        storage.getInventory().setItem(result + 18, new ItemStack(var21.getItem(result + 6).id, var21.getItem(result + 6).count, Short.valueOf(String.valueOf(var21.getItem(result + 6).getData())).shortValue()));
                     }
                  }

                  Object[] var23 = (Object[])null;
                  var23 = getFreeSlot(storage, resultStack);
                  int slot = ((Integer)var23[0]).intValue();
                  boolean found = ((Boolean)var23[1]).booleanValue();

                  int restAmount;
                  for(restAmount = resultStack.getAmount(); restAmount > 0 && found; found = ((Boolean)var23[1]).booleanValue()) {
                     if(found) {
                        ItemStack thisStack = resultStack.clone();
                        if(restAmount > 64) {
                           thisStack.setAmount(64);
                        } else {
                           thisStack.setAmount(restAmount);
                        }

                        restAmount -= 64;
                        if(thisStack.getAmount() > 0) {
                           storage.getInventory().setItem(slot, thisStack.clone());
                        }
                     }

                     var23 = getFreeSlot(storage, resultStack);
                     slot = ((Integer)var23[0]).intValue();
                  }

                  if(restAmount > 0) {
                     storage.getInventory().clear();

                     for(int i = 0; i < var19.length; ++i) {
                        if(var19[i] != null && var19[i].getTypeId() != Material.AIR.getId()) {
                           storage.getInventory().setItem(i, var19[i]);
                        }
                     }
                  }

                  resultStack = null;
                  var21 = null;
                  toCraftMat = null;
               }
            }
         }
      }
   }

   private static Object[] getFreeSlot(StorageMinecart storage, ItemStack resultStack) {
      Object[] result = new Object[2];
      int freeSlot = 3;
      boolean freeFound = false;
      ItemStack[] slots = new ItemStack[3];
      int i = 3;

      while(i < 9) {
         slots[0] = storage.getInventory().getItem(i);
         slots[1] = storage.getInventory().getItem(i + 9);
         slots[2] = storage.getInventory().getItem(i + 18);
         if(slots[0] != null && slots[0].getTypeId() == resultStack.getTypeId() && slots[0].getDurability() == resultStack.getDurability() && slots[0].getAmount() + resultStack.getAmount() <= 64) {
            freeSlot = i;
            freeFound = true;
            resultStack.setAmount(resultStack.getAmount() + slots[0].getAmount());
            break;
         }

         if(slots[1] != null && slots[1].getTypeId() == resultStack.getTypeId() && slots[1].getDurability() == resultStack.getDurability() && slots[1].getAmount() + resultStack.getAmount() <= 64) {
            freeSlot = i + 9;
            freeFound = true;
            resultStack.setAmount(resultStack.getAmount() + slots[1].getAmount());
            break;
         }

         if(slots[2] != null && slots[2].getTypeId() == resultStack.getTypeId() && slots[2].getDurability() == resultStack.getDurability() && slots[2].getAmount() + resultStack.getAmount() <= 64) {
            freeSlot = i + 18;
            freeFound = true;
            resultStack.setAmount(resultStack.getAmount() + slots[2].getAmount());
            break;
         }

         if(slots[0] != null && slots[0].getTypeId() != Material.AIR.getId()) {
            if(slots[1] != null && slots[1].getTypeId() != Material.AIR.getId()) {
               if(slots[2] != null && slots[2].getTypeId() != Material.AIR.getId()) {
                  ++i;
                  continue;
               }

               freeSlot = i + 18;
               freeFound = true;
               break;
            }

            freeSlot = i + 9;
            freeFound = true;
            break;
         }

         freeSlot = i;
         freeFound = true;
         break;
      }

      result[0] = Integer.valueOf(freeSlot);
      result[1] = Boolean.valueOf(freeFound);
      return result;
   }
}
