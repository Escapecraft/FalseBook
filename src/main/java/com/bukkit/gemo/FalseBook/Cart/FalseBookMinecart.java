package com.bukkit.gemo.FalseBook.Cart;

import com.bukkit.gemo.FalseBook.Cart.CartHandler;
import com.bukkit.gemo.FalseBook.Cart.FalseBookCartCore;
import com.bukkit.gemo.utils.BlockUtils;
import com.bukkit.gemo.utils.FBItemType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class FalseBookMinecart {

   private Minecart originalCart;
   private boolean constantSpeedMode = false;
   private boolean wasMovingLastTick = false;
   private boolean wasOnPlates = false;
   private boolean createdLastTick = true;
   private boolean isDead = false;
   private Vector previousMotion;
   private Vector previousPosition;
   private String owner;
   private final ConcurrentHashMap programmedItems;
   private boolean isProgrammed = false;


   public FalseBookMinecart(Minecart cart) {
      this.originalCart = cart;
      this.originalCart.setMaxSpeed(FalseBookCartCore.getOrCreateSettings(cart.getWorld().getName()).getMaxSpeed());
      this.originalCart.setSlowWhenEmpty(FalseBookCartCore.getOrCreateSettings(cart.getWorld().getName()).isSetSlowWhenEmpty());
      this.previousPosition = this.originalCart.getLocation().toVector().clone();
      this.previousMotion = this.originalCart.getVelocity().clone();
      this.programmedItems = new ConcurrentHashMap();
      this.owner = "";
   }

   public void setOwner(String owner) {
      if(owner == null) {
         this.owner = "";
      } else {
         this.owner = owner;
      }

   }

   public String getOwner() {
      return this.owner;
   }

   public void addProgrammedItem(FBItemType item) {
      this.programmedItems.put(item.getString(), item);
      this.isProgrammed = true;
   }

   public void removeProgrammedItem(FBItemType item) {
      this.programmedItems.remove(item.getString());
   }

   public ArrayList getProgrammedItems() {
      ArrayList list = new ArrayList();
      list.addAll(this.programmedItems.values());
      return list;
   }

   public void clearProgrammedItems() {
      this.programmedItems.clear();
      this.isProgrammed = false;
      this.owner = "";
   }

   public boolean isProgrammedItem(FBItemType item) {
      Iterator var3 = this.programmedItems.values().iterator();

      while(var3.hasNext()) {
         FBItemType itemInList = (FBItemType)var3.next();
         if(item.usesWildcart()) {
            if(itemInList.getItemID() == item.getItemID()) {
               return true;
            }
         } else if(itemInList.getItemID() == item.getItemID() && itemInList.getItemData() == item.getItemData()) {
            return true;
         }
      }

      return false;
   }

   public boolean isProgrammedItem(ItemStack item) {
      return this.programmedItems.get(item.getTypeId() + ":" + item.getData()) == null?false:this.programmedItems.get(item.getTypeId() + ":true") != null;
   }

   public boolean isProgrammed() {
      return this.isProgrammed;
   }

   public void update(int ID) {
      this.previousPosition = this.originalCart.getLocation().toVector().clone();
      this.previousMotion = this.originalCart.getVelocity().clone();
      this.setWasOnPlates(this.isOnPressureplates(ID));
      this.setCreatedLastTick(false);
   }

   public void doFrictionFix() {
      Vector nowSpeed = this.getMotion();
      double spd = 0.0039D;
      if(nowSpeed.getX() > 0.0D) {
         nowSpeed.setX(nowSpeed.getX() + spd);
      } else if(nowSpeed.getX() < 0.0D) {
         nowSpeed.setX(nowSpeed.getX() - spd);
      }

      if(nowSpeed.getZ() > 0.0D) {
         nowSpeed.setZ(nowSpeed.getZ() + spd);
      } else if(nowSpeed.getZ() < 0.0D) {
         nowSpeed.setZ(nowSpeed.getZ() - spd);
      }

      this.originalCart.setVelocity(nowSpeed);
   }

   public Minecart getMinecart() {
      return this.originalCart;
   }

   public void setCreatedLastTick(boolean createdLastTick) {
      this.createdLastTick = createdLastTick;
   }

   public boolean wasCreatedLastTick() {
      return this.createdLastTick;
   }

   public void kill() {
      if(!this.isDead) {
         this.isDead = true;
         CartHandler.removeMinecart(this.originalCart);
         this.originalCart.remove();
      }

   }

   public boolean isDead() {
      if(this.originalCart.isDead()) {
         this.isDead = true;
      }

      return this.isDead;
   }

   public Vector getPreviousMotion() {
      return this.previousMotion;
   }

   public Location getPosition() {
      return this.originalCart.getLocation().clone();
   }

   public Vector getMotion() {
      return this.originalCart.getVelocity().clone();
   }

   public Vector getPreviousPosition() {
      return this.previousPosition;
   }

   public boolean isOnRails() {
      int ID = BlockUtils.getRawTypeID(this.originalCart.getLocation());
      return ID == Material.RAILS.getId() || ID == Material.DETECTOR_RAIL.getId() || ID == Material.POWERED_RAIL.getId();
   }

   public boolean isOnTracks(int ID) {
      return ID == Material.RAILS.getId() || ID == Material.STONE_PLATE.getId() || ID == Material.WOOD_PLATE.getId();
   }

   public boolean isOnPressureplates(int ID) {
      return ID == Material.STONE_PLATE.getId() || ID == Material.WOOD_PLATE.getId();
   }

   public boolean hasPassenger() {
      return this.originalCart.getPassenger() != null;
   }

   public Entity getPassenger() {
      return this.originalCart.getPassenger();
   }

   public Player getPlayerPassenger() {
      return this.hasPlayerPassenger()?(Player)this.getPassenger():null;
   }

   public boolean hasPlayerPassenger() {
      return this.hasPassenger() && this.originalCart.getPassenger() instanceof Player;
   }

   public boolean hasMobPassenger() {
      return this.hasPassenger() && this.originalCart.getPassenger() instanceof Monster;
   }

   public boolean hasAnimalPassenger() {
      return this.hasPassenger() && this.originalCart.getPassenger() instanceof Animals;
   }

   public void setWasOnPlates(boolean wasOnPlates) {
      this.wasOnPlates = wasOnPlates;
   }

   public boolean wasOnPlates() {
      return this.wasOnPlates;
   }

   public void setWasMovingLastTick(boolean wasMovingLastTick) {
      this.wasMovingLastTick = wasMovingLastTick;
   }

   public boolean wasMovingLastTick() {
      return this.wasMovingLastTick;
   }

   public boolean hasMoved() {
      return this.getPreviousPosition().getBlockX() != this.originalCart.getLocation().getBlockX()?true:(this.getPreviousPosition().getBlockZ() != this.originalCart.getLocation().getBlockZ()?true:this.getPreviousPosition().getBlockY() != this.originalCart.getLocation().getBlockY());
   }

   public boolean isMoving() {
      return this.getMotionX() != 0.0D || this.getMotionY() != 0.0D || this.getMotionZ() != 0.0D;
   }

   public void setMotion(double x, double y, double z) {
      Vector motion = new Vector();
      motion.setX(x);
      motion.setY(y);
      motion.setZ(z);
      this.originalCart.setVelocity(motion);
   }

   public boolean isInConstantSpeedMode() {
      return this.constantSpeedMode;
   }

   public void setConstantSpeedMode(boolean mode) {
      this.constantSpeedMode = mode;
   }

   public void setMotionX(double x) {
      this.setMotion(x, this.getMotionY(), this.getMotionZ());
   }

   public void setMotionY(double y) {
      this.setMotion(this.getMotionX(), y, this.getMotionZ());
   }

   public void setMotionZ(double z) {
      this.setMotion(this.getMotionX(), this.getMotionY(), z);
   }

   public double getMotionX() {
      return this.originalCart.getVelocity().getX();
   }

   public double getMotionY() {
      return this.originalCart.getVelocity().getY();
   }

   public double getMotionZ() {
      return this.originalCart.getVelocity().getZ();
   }

   public void updateMinecart(Minecart cart) {
      this.originalCart = cart;
   }
}
