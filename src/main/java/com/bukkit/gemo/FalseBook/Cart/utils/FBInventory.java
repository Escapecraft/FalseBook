package com.bukkit.gemo.FalseBook.Cart.utils;

import net.minecraft.server.v1_6_R3.Container;
import net.minecraft.server.v1_6_R3.EntityHuman;
import net.minecraft.server.v1_6_R3.InventoryCrafting;
import net.minecraft.server.v1_6_R3.ItemStack;

public class FBInventory extends InventoryCrafting {

   private ItemStack[] items = new ItemStack[9];


   public FBInventory(Container container, int i, int j) {
      super(container, i, j);
   }

   public FBInventory() {
      super((Container)null, 3, 3);
   }

   public ItemStack[] getContents() {
      return this.items;
   }

   public int getSize() {
      return 1;
   }

   public ItemStack getItem(int i) {
      return this.items[i];
   }

   public String getName() {
      return "Result";
   }

   public ItemStack splitStack(int i, int j) {
      if(this.items[i] != null) {
         ItemStack itemstack = this.items[i];
         this.items[i] = null;
         return itemstack;
      } else {
         return null;
      }
   }

   public void setItem(int i, ItemStack itemstack) {
      this.items[i] = itemstack;
   }

   public int getMaxStackSize() {
      return 64;
   }

   public void update() {}

   public boolean a(EntityHuman entityhuman) {
      return true;
   }

   public void f() {}

   public void g() {}
}
