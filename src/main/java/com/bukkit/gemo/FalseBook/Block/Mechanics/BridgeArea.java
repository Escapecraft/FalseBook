package com.bukkit.gemo.FalseBook.Block.Mechanics;

import com.bukkit.gemo.utils.BlockUtils;
import java.awt.Point;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;

public class BridgeArea {

   private Sign sign1;
   private Sign sign2;
   private Point corner1;
   private Point corner2;
   private Boolean up = Boolean.valueOf(false);


   public BridgeArea(Sign sign1, Sign sign2, Point corner1, Point corner2) {
      this.sign1 = sign1;
      this.sign2 = sign2;
      Point cornerMin = new Point(Math.min(corner1.x, corner2.x), Math.min(corner1.y, corner2.y));
      Point cornerMax = new Point(Math.max(corner1.x, corner2.x), Math.max(corner1.y, corner2.y));
      this.corner1 = cornerMin;
      this.corner2 = cornerMax;
   }

   public Sign getSign1() {
      return this.sign1;
   }

   public void setSign1(Sign sign1) {
      this.sign1 = sign1;
   }

   public Sign getSign2() {
      return this.sign2;
   }

   public void setSign2(Sign sign2) {
      this.sign2 = sign2;
   }

   public Boolean getUp() {
      return this.up;
   }

   public void setUp(Boolean up) {
      this.up = up;
   }

   public boolean isBlockInArea(Block block) {
      if(BlockUtils.LocationEquals(this.sign1.getBlock().getLocation(), block.getLocation())) {
         return true;
      } else if(BlockUtils.LocationEquals(this.sign2.getBlock().getLocation(), block.getLocation())) {
         return true;
      } else if(BlockUtils.LocationEquals(this.sign1.getBlock().getRelative(BlockFace.DOWN).getLocation(), block.getLocation())) {
         return true;
      } else if(BlockUtils.LocationEquals(this.sign2.getBlock().getRelative(BlockFace.DOWN).getLocation(), block.getLocation())) {
         return true;
      } else if(block.getX() >= this.corner1.x && block.getX() <= this.corner2.x && block.getZ() >= this.corner1.y && block.getZ() <= this.corner2.y) {
         if(this.getUp().booleanValue()) {
            if(block.getY() != this.sign1.getY() + 1) {
               return false;
            }
         } else if(block.getY() != this.sign1.getY() - 1) {
            return false;
         }

         return true;
      } else {
         return false;
      }
   }
}
