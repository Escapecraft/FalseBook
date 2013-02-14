package com.bukkit.gemo.FalseBook.Block.Areas;

import java.io.Serializable;

public class AreaBlockType implements Serializable {

   private static final long serialVersionUID = 8259446442629731213L;
   private int TypeID;
   private byte Data;


   public AreaBlockType(int ID, byte D) {
      this.setTypeID(ID);
      this.setData(D);
   }

   public int getTypeID() {
      return this.TypeID;
   }

   public void setTypeID(int typeID) {
      this.TypeID = typeID;
   }

   public byte getData() {
      return this.Data;
   }

   public void setData(byte data) {
      this.Data = data;
   }
}
