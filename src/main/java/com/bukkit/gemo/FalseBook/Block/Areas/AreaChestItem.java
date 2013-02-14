package com.bukkit.gemo.FalseBook.Block.Areas;

import java.io.Serializable;

public class AreaChestItem implements Serializable {

   private static final long serialVersionUID = -4259794860749681854L;
   private int TypeID;
   private short Data;
   private int Count;


   public AreaChestItem(int ID, short D, int C) {
      this.setTypeID(ID);
      this.setData(D);
      this.setCount(C);
   }

   public int getTypeID() {
      return this.TypeID;
   }

   public void setTypeID(int typeID) {
      this.TypeID = typeID;
   }

   public short getData() {
      return this.Data;
   }

   public void setData(short data) {
      this.Data = data;
   }

   public int getCount() {
      return this.Count;
   }

   public void setCount(int count) {
      this.Count = count;
   }
}
