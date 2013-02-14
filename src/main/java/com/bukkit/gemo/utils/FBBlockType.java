package com.bukkit.gemo.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class FBBlockType {

   private int ItemID;
   private short ItemData = 0;
   private byte ItemDamage;


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

   public FBBlockType(String name) {
      this.ItemID = this.getMaterialIDByName(name);
      this.ItemData = 0;
      this.ItemDamage = 0;
   }

   public FBBlockType(String name, short Data) {
      this.ItemID = this.getMaterialIDByName(name);
      this.ItemData = Data;
      this.ItemDamage = 0;
   }

   public FBBlockType(int ID) {
      this.ItemID = ID;
      this.ItemData = 0;
      this.ItemDamage = 0;
   }

   public FBBlockType(int ID, short Data) {
      this.ItemID = ID;
      this.ItemData = Data;
      this.ItemDamage = 0;
   }

   public int getItemID() {
      return this.ItemID;
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

   public byte getItemDamage() {
      return this.ItemDamage;
   }

   public boolean equals(FBBlockType other) {
      return this.getItemID() == other.getItemID() && this.getItemData() == other.getItemData();
   }

   public boolean equals(ItemStack other) {
      return other == null?false:this.getItemID() == other.getTypeId() && this.getItemData() == other.getDurability();
   }
}
