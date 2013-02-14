package com.bukkit.gemo.FalseBook.Block.Cauldrons;

import com.bukkit.gemo.utils.FBItemType;
import java.util.ArrayList;
import java.util.Iterator;
import org.bukkit.inventory.ItemStack;

public class CauldronRecipe {

   private ArrayList Ingredients;
   private ArrayList Results;
   private String Name = "";


   public CauldronRecipe(String Name, ArrayList ingredients, ArrayList results) {
      this.Name = Name;
      this.Ingredients = ingredients;
      this.Results = results;
   }

   public boolean verifyCauldron(ArrayList ents) {
      if(this.Results.size() >= 1 && this.Ingredients.size() >= 1) {
         ArrayList ready = new ArrayList();

         int i;
         for(i = 0; i < this.Ingredients.size(); ++i) {
            ready.add(Boolean.valueOf(false));
         }

         for(i = 0; i < ents.size(); ++i) {
            int nowCount = ((ItemStack)ents.get(i)).getAmount();

            for(int j = 0; j < this.Ingredients.size(); ++j) {
               if(((FBItemType)this.Ingredients.get(j)).equals((ItemStack)ents.get(i)) && nowCount >= ((FBItemType)this.Ingredients.get(j)).getAmount()) {
                  ready.set(j, Boolean.valueOf(true));
               }
            }
         }

         for(i = 0; i < ready.size(); ++i) {
            if(!((Boolean)ready.get(i)).booleanValue()) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public int getMultiplier(ArrayList ents) {
      double bestRatio = Double.MAX_VALUE;

      for(int i = 0; i < ents.size(); ++i) {
         int nowCount = ((ItemStack)ents.get(i)).getAmount();

         for(int j = 0; j < this.Ingredients.size(); ++j) {
            if(((FBItemType)this.Ingredients.get(j)).equals((ItemStack)ents.get(i))) {
               double nowRatio = (double)(nowCount / ((FBItemType)this.Ingredients.get(j)).getAmount());
               if(nowRatio < bestRatio) {
                  bestRatio = nowRatio;
               }
            }
         }
      }

      return (int)bestRatio;
   }

   public ArrayList getResultItems(ArrayList ents) {
      ArrayList resultList = new ArrayList();
      Iterator i = this.Results.iterator();

      while(i.hasNext()) {
         FBItemType multiplier = (FBItemType)i.next();
         resultList.add(multiplier.getItemStack());
      }

      int var5 = this.getMultiplier(ents);

      for(int var6 = 0; var6 < resultList.size(); ++var6) {
         ((ItemStack)resultList.get(var6)).setAmount(((ItemStack)resultList.get(var6)).getAmount() * var5);
      }

      return resultList;
   }

   public String getName() {
      return this.Name;
   }

   public int getIngredientsSize() {
      return this.Ingredients.size();
   }

   public ItemStack getIngredient(ItemStack thisItem) {
      Iterator var3 = this.Ingredients.iterator();

      while(var3.hasNext()) {
         FBItemType itemType = (FBItemType)var3.next();
         if(itemType.equals(thisItem)) {
            return itemType.getItemStack();
         }
      }

      return null;
   }
}
