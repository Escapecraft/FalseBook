package com.bukkit.gemo.FalseBook.Cart.utils;

import java.util.ArrayList;
import net.minecraft.server.v1_6_R2.CraftingManager;
import net.minecraft.server.v1_6_R2.IRecipe;
import net.minecraft.server.v1_6_R2.InventoryCrafting;
import net.minecraft.server.v1_6_R2.Item;
import net.minecraft.server.v1_6_R2.ItemStack;
import net.minecraft.server.v1_6_R2.ShapelessRecipes;
import net.minecraft.server.v1_6_R2.World;

public class FBCraftingManager {

   public static ItemStack craft(InventoryCrafting inventorycrafting, World world) {
      int i = 0;
      ItemStack itemstack = null;
      ItemStack itemstack1 = null;

      int j;
      for(j = 0; j < inventorycrafting.getSize(); ++j) {
         ItemStack craftingrecipe = inventorycrafting.getItem(j);
         if(craftingrecipe != null) {
            if(i == 0) {
               itemstack = craftingrecipe;
            }

            if(i == 1) {
               itemstack1 = craftingrecipe;
            }

            ++i;
         }
      }

      if(i == 2 && itemstack.id == itemstack1.id && itemstack.count == 1 && itemstack1.count == 1 && Item.byId[itemstack.id].usesDurability()) {
         Item var12 = Item.byId[itemstack.id];
         int var14 = var12.getMaxDurability() - itemstack.j();
         int var15 = var12.getMaxDurability() - itemstack1.j();
         int i1 = var14 + var15 + var12.getMaxDurability() * 10 / 100;
         int j1 = var12.getMaxDurability() - i1;
         if(j1 < 0) {
            j1 = 0;
         }

         ItemStack result1 = new ItemStack(itemstack.id, 1, j1);
         ArrayList ingredients = new ArrayList();
         ingredients.add(itemstack.cloneItemStack());
         ingredients.add(itemstack1.cloneItemStack());
         ShapelessRecipes recipe = new ShapelessRecipes(result1.cloneItemStack(), ingredients);
         inventorycrafting.currentRecipe = recipe;
         return result1;
      } else {
         for(j = 0; j < CraftingManager.getInstance().getRecipes().size(); ++j) {
            IRecipe var13 = (IRecipe)CraftingManager.getInstance().getRecipes().get(j);
            if(var13.a(inventorycrafting, world)) {
               inventorycrafting.currentRecipe = var13;
               ItemStack result = var13.a(inventorycrafting);
               return result;
            }
         }

         return null;
      }
   }
}
