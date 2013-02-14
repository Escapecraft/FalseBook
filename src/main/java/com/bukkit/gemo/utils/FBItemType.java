package com.bukkit.gemo.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class FBItemType {

   private boolean usesWildcart = false;
   private int ItemID;
   private short ItemData = 0;
   private int amount = -1;


   public String getString() {
      return this.ItemID + ":" + (this.usesWildcart?"true":Short.valueOf(this.ItemData));
   }

   private int getMaterialIDByName(String name) {
      Material[] var5;
      int var4 = (var5 = Material.values()).length;

      for(int var3 = 0; var3 < var4; ++var3) {
         Material m = var5[var3];
         if(m.name().equalsIgnoreCase(name)) {
            return m.getId();
         }
      }

      return 0;
   }

   public FBItemType(String name) {
      this.ItemID = this.getMaterialIDByName(name);
      this.ItemData = 0;
      this.usesWildcart = true;
   }

   public FBItemType(String name, short Data) {
      this.ItemID = this.getMaterialIDByName(name);
      this.ItemData = Data;
      this.usesWildcart = false;
   }

   public FBItemType(int ID) {
      this.ItemID = ID;
      this.ItemData = 0;
      this.usesWildcart = true;
   }

   public FBItemType(int ID, short Data) {
      this.ItemID = ID;
      this.ItemData = Data;
      this.usesWildcart = false;
   }

   public int getItemID() {
      return this.ItemID;
   }

   public boolean usesWildcart() {
      return this.usesWildcart;
   }

   public void setItemID(int itemID) {
      this.ItemID = itemID;
   }

   public void setItemID(String name) {
      this.ItemID = this.getMaterialIDByName(name);
   }

   public short getItemData() {
      return this.ItemData;
   }

   public byte getItemDataAsByte() {
      return Byte.valueOf(String.valueOf(this.ItemData)).byteValue();
   }

   public void setItemData(short itemData) {
      this.ItemData = itemData;
   }

   public int getAmount() {
      return this.amount;
   }

   public void setAmount(int amount) {
      this.amount = amount;
   }

   public boolean equals(FBItemType other) {
      return this.getItemID() == other.getItemID() && (this.usesWildcart || this.getItemData() == other.getItemData());
   }

   public boolean equals(ItemStack other) {
      return other == null?false:this.getItemID() == other.getTypeId() && (this.usesWildcart || this.getItemData() == other.getDurability());
   }

   public ItemStack getItemStack() {
      if(this.amount > 0) {
         ItemStack itemStack = new ItemStack(this.getItemID());
         itemStack.setAmount(this.amount);
         if(!this.usesWildcart) {
            itemStack.setDurability(this.ItemData);
         }

         return itemStack;
      } else {
         return null;
      }
   }
}
