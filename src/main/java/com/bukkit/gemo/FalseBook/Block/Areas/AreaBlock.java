package com.bukkit.gemo.FalseBook.Block.Areas;

import com.bukkit.gemo.FalseBook.Block.Areas.AreaComplexBlock;
import java.io.Serializable;

public class AreaBlock implements Serializable {

   private static final long serialVersionUID = 8259446442629731213L;
   private int xPos;
   private int yPos;
   private int zPos;
   private int TypeID;
   private byte Data;
   private AreaComplexBlock inheritedData = null;


   public AreaBlock(int x, int y, int z, int ID, byte D) {
      this.setxPos(x);
      this.setyPos(y);
      this.setzPos(z);
      this.setTypeID(ID);
      this.setData(D);
   }

   public int getxPos() {
      return this.xPos;
   }

   public void setxPos(int xPos) {
      this.xPos = xPos;
   }

   public int getyPos() {
      return this.yPos;
   }

   public void setyPos(int yPos) {
      this.yPos = yPos;
   }

   public int getzPos() {
      return this.zPos;
   }

   public void setzPos(int zPos) {
      this.zPos = zPos;
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

   public void setInheritedData(AreaComplexBlock inheritedData) {
      this.inheritedData = inheritedData;
   }

   public AreaComplexBlock getInheritedData() {
      return this.inheritedData;
   }
}
