package com.bukkit.gemo.FalseBook.Cart;

import com.bukkit.gemo.FalseBook.Cart.CartMechanic;
import com.bukkit.gemo.FalseBook.Cart.FalseBookCartCore;
import com.bukkit.gemo.FalseBook.Cart.Blocks.Booster2x;
import com.bukkit.gemo.FalseBook.Cart.Blocks.Booster8x;
import com.bukkit.gemo.FalseBook.Cart.Blocks.Break25;
import com.bukkit.gemo.FalseBook.Cart.Blocks.Break50;
import com.bukkit.gemo.FalseBook.Cart.Blocks.Catcher;
import com.bukkit.gemo.FalseBook.Cart.Blocks.ConstantSpeed;
import com.bukkit.gemo.FalseBook.Cart.Blocks.Eject;
import com.bukkit.gemo.FalseBook.Cart.Blocks.Emitter;
import com.bukkit.gemo.FalseBook.Cart.Blocks.MultiBlock;
import com.bukkit.gemo.FalseBook.Cart.Blocks.ProgramCart;
import com.bukkit.gemo.FalseBook.Cart.Blocks.ProgramCartDeposit;
import com.bukkit.gemo.FalseBook.Cart.Blocks.Reverse;
import com.bukkit.gemo.FalseBook.Cart.Blocks.Sort;
import com.bukkit.gemo.FalseBook.Cart.Blocks.Teleport;
import com.bukkit.gemo.utils.FlatFile;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import org.bukkit.block.Block;
import org.bukkit.entity.Minecart;

public class CartWorldSettings {

   private FlatFile config;
   private HashMap mechList;
   private double maxSpeed = 0.5D;
   private double launchSpeed = 3.0D;
   private double constantSpeed = 0.2D;
   private boolean doFrictionFix = true;
   private boolean setSlowWhenEmpty = false;
   private int BoosterBlock2x = 14;
   private int BoosterBlock8x = 41;
   private int ConstantSpeedBlock = 42;
   private int StationBlock = 49;
   private int EjectBlock = 16;
   private int BrakeBlock25x = 13;
   private int BrakeBlock50x = 88;
   private int ReverseBlock = 35;
   private int CollectDepositBlock = 15;
   private int SortBlock = 87;
   private int EmitterBlock = 21;
   private int ProgramCartBlock = 22;
   private int ProgramCartDepositBlock = 35;
   private int TeleportBlock = 35;
   private int BoosterBlock2xValue = 0;
   private int BoosterBlock8xValue = 0;
   private int ConstantSpeedBlockValue = 0;
   private int StationBlockValue = 0;
   private int EjectBlockValue = 0;
   private int BrakeBlock25xValue = 0;
   private int BrakeBlock50xValue = 0;
   private int ReverseBlockValue = 0;
   private int CollectDepositBlockValue = 0;
   private int SortBlockValue = 0;
   private int EmitterBlockValue = 0;
   private int ProgramCartBlockValue = 0;
   private int ProgramCartDepositBlockValue = 11;
   private int TeleportBlockValue = 5;
   private boolean useSimpleCartSystem = false;
   private boolean removeCartOnExit = false;
   private boolean killCartsOnCollision = false;
   private boolean autoLockStorageCarts = true;


   public CartWorldSettings(String FileName, String worldName) {
      this.loadSettings(FileName, worldName);
   }

   public FlatFile getConfig() {
      return this.config;
   }

   public void setConfig(FlatFile config) {
      this.config = config;
   }

   private void registerMechanics() {
      this.mechList = new HashMap();
      this.mechList.put(this.BoosterBlock2x + ":" + this.BoosterBlock2xValue, new Booster2x());
      this.mechList.put(this.BoosterBlock8x + ":" + this.BoosterBlock8xValue, new Booster8x());
      this.mechList.put(this.BrakeBlock25x + ":" + this.BrakeBlock25xValue, new Break25());
      this.mechList.put(this.BrakeBlock50x + ":" + this.BrakeBlock50xValue, new Break50());
      this.mechList.put(this.ReverseBlock + ":" + this.ReverseBlockValue, new Reverse());
      this.mechList.put(this.EmitterBlock + ":" + this.EmitterBlockValue, new Emitter());
      this.mechList.put(this.EjectBlock + ":" + this.EjectBlockValue, new Eject());
      this.mechList.put(this.ConstantSpeedBlock + ":" + this.ConstantSpeedBlockValue, new ConstantSpeed());
      this.mechList.put(this.StationBlock + ":" + this.StationBlockValue, new Catcher());
      this.mechList.put(this.SortBlock + ":" + this.SortBlockValue, new Sort());
      this.mechList.put(this.CollectDepositBlock + ":" + this.CollectDepositBlockValue, new MultiBlock());
      this.mechList.put(this.ProgramCartBlock + ":" + this.ProgramCartBlockValue, new ProgramCart());
      this.mechList.put(this.ProgramCartDepositBlock + ":" + this.ProgramCartDepositBlockValue, new ProgramCartDeposit());
      this.mechList.put(this.TeleportBlock + ":" + this.TeleportBlockValue, new Teleport());
   }

   public boolean isBlockRegistered(Block block) {
      return this.mechList.containsKey(block.getTypeId() + ":" + block.getData());
   }

   public Collection getAllMechanics() {
      return this.mechList.values();
   }

   public CartMechanic getMechanic(Block block) {
      return (CartMechanic)this.mechList.get(block.getTypeId() + ":" + block.getData());
   }

   public CartMechanic getMechanic(int TypeID, int SubID) {
      return (CartMechanic)this.mechList.get(TypeID + ":" + SubID);
   }

   public boolean executeMechanic(Minecart cart, Block railBlock, Block block, Block signBlock) {
      return ((CartMechanic)this.mechList.get(block.getTypeId() + ":" + block.getData())).Execute(cart, railBlock, block, signBlock, this);
   }

   public boolean saveSettings(String FileName, String worldName) {
      File folder = new File("plugins" + System.getProperty("file.separator") + "FalseBook");
      folder.mkdirs();
      folder = new File("plugins" + System.getProperty("file.separator") + "FalseBook" + System.getProperty("file.separator") + worldName);
      folder.mkdirs();

      try {
         this.config = new FlatFile("plugins" + System.getProperty("file.separator") + "FalseBook" + System.getProperty("file.separator") + worldName + System.getProperty("file.separator") + FileName, false);
         this.config.setInt("BoosterBlock2x", this.BoosterBlock2x);
         this.config.setInt("BoosterBlock2xValue", this.BoosterBlock2xValue);
         this.config.setInt("BoosterBlock8x", this.BoosterBlock8x);
         this.config.setInt("BoosterBlock8xValue", this.BoosterBlock8xValue);
         this.config.setInt("ConstantSpeedBlock", this.ConstantSpeedBlock);
         this.config.setInt("ConstantSpeedBlockValue", this.ConstantSpeedBlockValue);
         this.config.setInt("StationBlock", this.StationBlock);
         this.config.setInt("StationBlockValue", this.StationBlockValue);
         this.config.setInt("EjectBlock", this.EjectBlock);
         this.config.setInt("EjectBlockValue", this.EjectBlockValue);
         this.config.setInt("BrakeBlock25x", this.BrakeBlock25x);
         this.config.setInt("BrakeBlock25xValue", this.BrakeBlock25xValue);
         this.config.setInt("BrakeBlock50x", this.BrakeBlock50x);
         this.config.setInt("BrakeBlock50xValue", this.BrakeBlock50xValue);
         this.config.setInt("ReverseBlock", this.ReverseBlock);
         this.config.setInt("ReverseBlockValue", this.ReverseBlockValue);
         this.config.setInt("CollectDepositBlock", this.CollectDepositBlock);
         this.config.setInt("CollectDepositBlockValue", this.CollectDepositBlockValue);
         this.config.setInt("SortBlock", this.SortBlock);
         this.config.setInt("SortBlockValue", this.SortBlockValue);
         this.config.setInt("EmitterBlock", this.EmitterBlock);
         this.config.setInt("EmitterBlockValue", this.EmitterBlockValue);
         this.config.setInt("ProgramCartBlock", this.ProgramCartBlock);
         this.config.setInt("ProgramCartBlockValue", this.ProgramCartBlockValue);
         this.config.setInt("ProgramCartDepositBlock", this.ProgramCartDepositBlock);
         this.config.setInt("ProgramCartDepositBlockValue", this.ProgramCartDepositBlockValue);
         this.config.setInt("TeleportBlock", this.TeleportBlock);
         this.config.setInt("TeleportBlockValue", this.TeleportBlockValue);
         this.config.setDouble("maxSpeed", this.maxSpeed);
         this.config.setDouble("launchSpeed", this.launchSpeed);
         this.config.setDouble("constantSpeed", this.constantSpeed);
         this.config.setBoolean("doFrictionFix", this.doFrictionFix);
         this.config.setBoolean("setSlowWhenEmpty", this.setSlowWhenEmpty);
         this.config.setBoolean("useSimpleCartSystem", this.useSimpleCartSystem);
         this.config.setBoolean("removeCartOnExit", this.removeCartOnExit);
         this.config.setBoolean("killCartsOnCollision", this.killCartsOnCollision);
         this.config.setBoolean("autoLockStorageCarts", this.autoLockStorageCarts);
         this.config.writeFile();
         return true;
      } catch (IOException var5) {
         FalseBookCartCore.printInConsole("Error while saving file: plugins/FalseBook/" + worldName + "/" + FileName);
         var5.printStackTrace();
         return false;
      }
   }

   public boolean loadSettings(String FileName, String worldName) {
      File folder = new File("plugins" + System.getProperty("file.separator") + "FalseBook");
      folder.mkdirs();
      folder = new File("plugins" + System.getProperty("file.separator") + "FalseBook" + System.getProperty("file.separator") + worldName);
      folder.mkdirs();
      File oldFile = new File("plugins" + System.getProperty("file.separator") + "FalseBook" + System.getProperty("file.separator") + FileName);
      if(oldFile.exists()) {
         oldFile.delete();
      }

      try {
         this.config = new FlatFile("plugins" + System.getProperty("file.separator") + "FalseBook" + System.getProperty("file.separator") + worldName + System.getProperty("file.separator") + FileName, false);
         if(this.config.readFile()) {
            this.BoosterBlock2x = this.config.getInt("BoosterBlock2x", this.BoosterBlock2x);
            this.BoosterBlock8x = this.config.getInt("BoosterBlock8x", this.BoosterBlock8x);
            this.ConstantSpeedBlock = this.config.getInt("ConstantSpeedBlock", this.ConstantSpeedBlock);
            this.StationBlock = this.config.getInt("StationBlock", this.StationBlock);
            this.EjectBlock = this.config.getInt("EjectBlock", this.EjectBlock);
            this.BrakeBlock25x = this.config.getInt("BrakeBlock25x", this.BrakeBlock25x);
            this.BrakeBlock50x = this.config.getInt("BrakeBlock50x", this.BrakeBlock50x);
            this.ReverseBlock = this.config.getInt("ReverseBlock", this.ReverseBlock);
            this.CollectDepositBlock = this.config.getInt("CollectDepositBlock", this.CollectDepositBlock);
            this.SortBlock = this.config.getInt("SortBlock", this.SortBlock);
            this.EmitterBlock = this.config.getInt("EmitterBlock", this.EmitterBlock);
            this.ProgramCartBlock = this.config.getInt("ProgramCartBlock", this.ProgramCartBlock);
            this.ProgramCartDepositBlock = this.config.getInt("ProgramCartDepositBlock", this.ProgramCartDepositBlock);
            this.TeleportBlock = this.config.getInt("TeleportBlock", this.TeleportBlock);
            this.BoosterBlock2xValue = this.config.getInt("BoosterBlock2xValue", this.BoosterBlock2xValue);
            this.BoosterBlock8xValue = this.config.getInt("BoosterBlock8xValue", this.BoosterBlock8xValue);
            this.ConstantSpeedBlockValue = this.config.getInt("ConstantSpeedBlockValue", this.ConstantSpeedBlockValue);
            this.StationBlockValue = this.config.getInt("StationBlockValue", this.StationBlockValue);
            this.EjectBlockValue = this.config.getInt("EjectBlockValue", this.EjectBlockValue);
            this.BrakeBlock25xValue = this.config.getInt("BrakeBlock25xValue", this.BrakeBlock25xValue);
            this.BrakeBlock50xValue = this.config.getInt("BrakeBlock50xValue", this.BrakeBlock50xValue);
            this.ReverseBlockValue = this.config.getInt("ReverseBlockValue", this.ReverseBlockValue);
            this.CollectDepositBlockValue = this.config.getInt("CollectDepositBlockValue", this.CollectDepositBlockValue);
            this.SortBlockValue = this.config.getInt("SortBlockValue", this.SortBlockValue);
            this.EmitterBlockValue = this.config.getInt("EmitterBlockValue", this.EmitterBlockValue);
            this.ProgramCartBlockValue = this.config.getInt("ProgramCartBlockValue", this.ProgramCartBlockValue);
            this.ProgramCartDepositBlockValue = this.config.getInt("ProgramCartDepositBlockValue", this.ProgramCartDepositBlockValue);
            this.TeleportBlockValue = this.config.getInt("TeleportBlockValue", this.TeleportBlockValue);
            this.autoLockStorageCarts = this.config.getBoolean("autoLockStorageCarts", this.autoLockStorageCarts);
            this.maxSpeed = this.config.getDouble("maxSpeed", this.maxSpeed);
            this.launchSpeed = this.config.getDouble("launchSpeed", this.launchSpeed);
            this.constantSpeed = this.config.getDouble("constantSpeed", this.constantSpeed);
            this.doFrictionFix = this.config.getBoolean("doFrictionFix", this.doFrictionFix);
            this.setSlowWhenEmpty = this.config.getBoolean("setSlowWhenEmpty", this.setSlowWhenEmpty);
            this.useSimpleCartSystem = this.config.getBoolean("useSimpleCartSystem", this.useSimpleCartSystem);
            this.removeCartOnExit = this.config.getBoolean("removeCartOnExit", this.removeCartOnExit);
            this.killCartsOnCollision = this.config.getBoolean("killCartsOnCollision", this.killCartsOnCollision);
            this.saveSettings(FileName, worldName);
         } else {
            this.saveSettings(FileName, worldName);
         }

         this.registerMechanics();
         return true;
      } catch (Exception var8) {
         FalseBookCartCore.printInConsole("Error while reading file: plugins/FalseBook/" + worldName + "/" + FileName);

         try {
            this.config.regenerateFile("FalseBook" + System.getProperty("file.separator") + worldName + System.getProperty("file.separator") + FileName);
            this.saveSettings(FileName, worldName);
         } catch (Exception var7) {
            var7.printStackTrace();
         }

         FalseBookCartCore.printInConsole("recreated file: plugins/FalseBook/" + worldName + "/" + FileName);
         this.registerMechanics();
         return true;
      }
   }

   public double getMaxSpeed() {
      return this.maxSpeed;
   }

   public void setMaxSpeed(double maxSpeed) {
      this.maxSpeed = maxSpeed;
   }

   public double getLaunchSpeed() {
      return this.launchSpeed;
   }

   public void setLaunchSpeed(double launchSpeed) {
      this.launchSpeed = launchSpeed;
   }

   public double getConstantSpeed() {
      return this.constantSpeed;
   }

   public void setConstantSpeed(double constantSpeed) {
      this.constantSpeed = constantSpeed;
   }

   public boolean isDoFrictionFix() {
      return this.doFrictionFix;
   }

   public void setDoFrictionFix(boolean doFrictionFix) {
      this.doFrictionFix = doFrictionFix;
   }

   public boolean isSetSlowWhenEmpty() {
      return this.setSlowWhenEmpty;
   }

   public void setSetSlowWhenEmpty(boolean setSlowWhenEmpty) {
      this.setSlowWhenEmpty = setSlowWhenEmpty;
   }

   public int getBoosterBlock2x() {
      return this.BoosterBlock2x;
   }

   public void setBoosterBlock2x(int boosterBlock2x) {
      this.BoosterBlock2x = boosterBlock2x;
   }

   public int getBoosterBlock8x() {
      return this.BoosterBlock8x;
   }

   public void setBoosterBlock8x(int boosterBlock8x) {
      this.BoosterBlock8x = boosterBlock8x;
   }

   public int getConstantSpeedBlock() {
      return this.ConstantSpeedBlock;
   }

   public void setConstantSpeedBlock(int constantSpeedBlock) {
      this.ConstantSpeedBlock = constantSpeedBlock;
   }

   public int getStationBlock() {
      return this.StationBlock;
   }

   public void setStationBlock(int stationBlock) {
      this.StationBlock = stationBlock;
   }

   public int getEjectBlock() {
      return this.EjectBlock;
   }

   public void setEjectBlock(int ejectBlock) {
      this.EjectBlock = ejectBlock;
   }

   public int getBrakeBlock25x() {
      return this.BrakeBlock25x;
   }

   public void setBrakeBlock25x(int brakeBlock25x) {
      this.BrakeBlock25x = brakeBlock25x;
   }

   public int getBrakeBlock50x() {
      return this.BrakeBlock50x;
   }

   public void setBrakeBlock50x(int brakeBlock50x) {
      this.BrakeBlock50x = brakeBlock50x;
   }

   public int getReverseBlock() {
      return this.ReverseBlock;
   }

   public void setReverseBlock(int reverseBlock) {
      this.ReverseBlock = reverseBlock;
   }

   public int getCollectDepositBlock() {
      return this.CollectDepositBlock;
   }

   public void setCollectDepositBlock(int collectDepositBlock) {
      this.CollectDepositBlock = collectDepositBlock;
   }

   public int getSortBlock() {
      return this.SortBlock;
   }

   public void setSortBlock(int sortBlock) {
      this.SortBlock = sortBlock;
   }

   public int getEmitterBlock() {
      return this.EmitterBlock;
   }

   public void setEmitterBlock(int emitterBlock) {
      this.EmitterBlock = emitterBlock;
   }

   public int getBoosterBlock2xValue() {
      return this.BoosterBlock2xValue;
   }

   public void setBoosterBlock2xValue(int boosterBlock2xValue) {
      this.BoosterBlock2xValue = boosterBlock2xValue;
   }

   public int getBoosterBlock8xValue() {
      return this.BoosterBlock8xValue;
   }

   public void setBoosterBlock8xValue(int boosterBlock8xValue) {
      this.BoosterBlock8xValue = boosterBlock8xValue;
   }

   public int getConstantSpeedBlockValue() {
      return this.ConstantSpeedBlockValue;
   }

   public void setConstantSpeedBlockValue(int constantSpeedBlockValue) {
      this.ConstantSpeedBlockValue = constantSpeedBlockValue;
   }

   public int getStationBlockValue() {
      return this.StationBlockValue;
   }

   public void setStationBlockValue(int stationBlockValue) {
      this.StationBlockValue = stationBlockValue;
   }

   public int getEjectBlockValue() {
      return this.EjectBlockValue;
   }

   public void setEjectBlockValue(int ejectBlockValue) {
      this.EjectBlockValue = ejectBlockValue;
   }

   public int getBrakeBlock25xValue() {
      return this.BrakeBlock25xValue;
   }

   public void setBrakeBlock25xValue(int brakeBlock25xValue) {
      this.BrakeBlock25xValue = brakeBlock25xValue;
   }

   public int getBrakeBlock50xValue() {
      return this.BrakeBlock50xValue;
   }

   public void setBrakeBlock50xValue(int brakeBlock50xValue) {
      this.BrakeBlock50xValue = brakeBlock50xValue;
   }

   public int getReverseBlockValue() {
      return this.ReverseBlockValue;
   }

   public void setReverseBlockValue(int reverseBlockValue) {
      this.ReverseBlockValue = reverseBlockValue;
   }

   public int getCollectDepositBlockValue() {
      return this.CollectDepositBlockValue;
   }

   public void setCollectDepositBlockValue(int collectDepositBlockValue) {
      this.CollectDepositBlockValue = collectDepositBlockValue;
   }

   public int getSortBlockValue() {
      return this.SortBlockValue;
   }

   public void setSortBlockValue(int sortBlockValue) {
      this.SortBlockValue = sortBlockValue;
   }

   public int getEmitterBlockValue() {
      return this.EmitterBlockValue;
   }

   public void setEmitterBlockValue(int emitterBlockValue) {
      this.EmitterBlockValue = emitterBlockValue;
   }

   public int getProgramCartBlock() {
      return this.ProgramCartBlock;
   }

   public void setProgramCartBlock(int programCartBlock) {
      this.ProgramCartBlock = programCartBlock;
   }

   public int getProgramCartBlockValue() {
      return this.ProgramCartBlockValue;
   }

   public void setProgramCartBlockValue(int programCartBlockValue) {
      this.ProgramCartBlockValue = programCartBlockValue;
   }

   public int getProgramCartDepositBlock() {
      return this.ProgramCartDepositBlock;
   }

   public void setProgramCartDepositBlock(int programCartDepositBlock) {
      this.ProgramCartDepositBlock = programCartDepositBlock;
   }

   public int getProgramCartDepositBlockValue() {
      return this.ProgramCartDepositBlockValue;
   }

   public void setProgramCartDepositBlockValue(int programCartDepositBlockValue) {
      this.ProgramCartDepositBlockValue = programCartDepositBlockValue;
   }

   public int getTeleportBlock() {
      return this.TeleportBlock;
   }

   public void setTeleportBlock(int teleportBlock) {
      this.TeleportBlock = teleportBlock;
   }

   public int getTeleportBlockValue() {
      return this.TeleportBlockValue;
   }

   public void setTeleportBlockValue(int teleportBlockValue) {
      this.TeleportBlockValue = teleportBlockValue;
   }

   public boolean isUseSimpleCartSystem() {
      return this.useSimpleCartSystem;
   }

   public void setUseSimpleCartSystem(boolean useSimpleCartSystem) {
      this.useSimpleCartSystem = useSimpleCartSystem;
   }

   public boolean isRemoveCartOnExit() {
      return this.removeCartOnExit;
   }

   public void setRemoveCartOnExit(boolean removeCartOnExit) {
      this.removeCartOnExit = removeCartOnExit;
   }

   public void setKillCartsOnCollision(boolean killCartsOnCollision) {
      this.killCartsOnCollision = killCartsOnCollision;
   }

   public boolean isKillCartsOnCollision() {
      return this.killCartsOnCollision;
   }

   public boolean isAutoLockStorageCarts() {
      return this.autoLockStorageCarts;
   }
}
