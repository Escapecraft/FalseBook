package com.bukkit.gemo.FalseBook.Block.Mechanics;

import com.bukkit.gemo.utils.BlockUtils;
import java.util.HashMap;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.util.Vector;

public class DoorArea {

   private Sign sign1;
   private Sign sign2;
   private Vector vector1;
   private Vector vector2;
   private HashMap blockList = new HashMap();


   public DoorArea(Sign sign1, Sign sign2, Vector vector1, Vector vector2) {
      this.sign1 = sign1;
      this.sign2 = sign2;
      Vector vectorMin = new Vector(Math.min(vector1.getBlockX(), vector2.getBlockX()), Math.min(vector1.getBlockY(), vector2.getBlockY()), Math.min(vector1.getBlockZ(), vector2.getBlockZ()));
      Vector vectorMax = new Vector(Math.max(vector1.getBlockX(), vector2.getBlockX()), Math.max(vector1.getBlockY(), vector2.getBlockY()), Math.max(vector1.getBlockZ(), vector2.getBlockZ()));
      this.vector1 = vectorMin;
      this.vector2 = vectorMax;
   }

   public void addBlock(Block block) {
      if(!this.blockList.containsKey(block.getLocation().toString())) {
         this.blockList.put(block.getLocation().toString(), block);
      }

   }

   public void removeBlock(Block block) {
      this.blockList.remove(block.getLocation().toString());
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

   public boolean isBlockInArea(Block block) {
      return BlockUtils.LocationEquals(this.sign1.getBlock().getLocation(), block.getLocation())?true:(BlockUtils.LocationEquals(this.sign2.getBlock().getLocation(), block.getLocation())?true:(BlockUtils.LocationEquals(this.sign1.getBlock().getRelative(BlockFace.DOWN).getLocation(), block.getLocation())?true:(BlockUtils.LocationEquals(this.sign2.getBlock().getRelative(BlockFace.DOWN).getLocation(), block.getLocation())?true:(block.getX() >= this.vector1.getBlockX() && block.getX() <= this.vector2.getBlockX()?(block.getZ() >= this.vector1.getBlockZ() && block.getZ() <= this.vector2.getBlockZ()?block.getY() >= this.vector1.getBlockY() && block.getY() <= this.vector2.getBlockY():false):false))));
   }
}
