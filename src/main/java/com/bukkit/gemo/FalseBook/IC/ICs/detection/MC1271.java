package com.bukkit.gemo.FalseBook.IC.ICs.detection;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.BaseIC;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.InputState;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import com.bukkit.gemo.utils.BlockUtils;
import com.bukkit.gemo.utils.Parser;
import com.bukkit.gemo.utils.SignUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Giant;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.entity.PoweredMinecart;
import org.bukkit.entity.Slime;
import org.bukkit.entity.StorageMinecart;
import org.bukkit.entity.Villager;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.util.Vector;

public class MC1271 extends BaseIC {

   List Types = new ArrayList();


   public MC1271() {
      this.ICName = "DETECTION";
      this.ICNumber = "[MC1271]";
      this.setICGroup(ICGroup.DETECTION);
      this.Types.add("PLAYER");
      this.Types.add("MOBHOSTILE");
      this.Types.add("MOBPEACEFUL");
      this.Types.add("ANYMOB");
      this.Types.add("ANY");
      this.Types.add("CART");
      this.Types.add("STORAGECART");
      this.Types.add("POWEREDCART");
      this.chipState = new BaseChip(true, false, false, "Clock", "", "");
      this.chipState.setOutputs("Output", "", "");
      this.chipState.setLines("Radius[=OffsetX:OffsetY:OffsetZ] (i.e: 0=0:2:0 will check 2 Blocks above the IC-Block)", "detection type");
      this.ICDescription = "The MC1271 outputs high if the specified type is detected in the given distance around the ic, when the input (the \"clock\") goes from low to high.<br /><br /><b>Detection types:</b><ul><li>PLAYER</li><li>MOBHOSTILE</li><li>MOBPEACEFUL</li><li>ANYMOB</li><li>ANY</li><li>CART</li><li>STORAGECART</li><li>POWEREDCART</li></ul><br /><br />The <a href=\"MC0271.html\">MC0271</a> is the selftriggered version.";
   }

   public void checkCreation(SignChangeEvent event) {
      if(!Parser.isIntegerWithOffset(event.getLine(2))) {
         SignUtils.cancelSignCreation(event, "Line 3 must be a number or a number with a vector.");
      } else {
         int radius = Parser.getIntegerFromOffsetLine(event.getLine(2), 0);
         Vector vector = Parser.getVectorFromOffsetLine(event.getLine(2));
         if(radius < 0) {
            radius = 0;
         }

         if(vector.getBlockX() == 0 && vector.getBlockY() == 0 && vector.getBlockZ() == 0) {
            event.setLine(2, "" + radius);
         } else {
            event.setLine(2, radius + "=" + vector.getBlockX() + ":" + vector.getBlockY() + ":" + vector.getBlockZ());
         }

         if(event.getLine(3).length() > 0) {
            boolean f = false;

            for(int i = 0; i < this.Types.size(); ++i) {
               if(((String)this.Types.get(i)).equalsIgnoreCase(event.getLine(3))) {
                  f = true;
                  event.setLine(3, (String)this.Types.get(i));
               }
            }

            if(!f) {
               SignUtils.cancelSignCreation(event, "Type not found.");
            }
         } else {
            SignUtils.cancelSignCreation(event, "Please enter a Type in Line 4.");
         }
      }
   }

   public void Execute(Sign signBlock, InputState currentInputs, InputState previousInputs) {
      if(currentInputs.isInputOneHigh() && previousInputs.isInputOneLow()) {
         if(!Parser.isIntegerWithOffset(signBlock.getLine(2))) {
            return;
         }

         int range = Parser.getIntegerFromOffsetLine(signBlock.getLine(2), 0);
         if(range < 0) {
            range = 0;
         }

         Vector offsetVector = Parser.getVectorFromOffsetLine(signBlock.getLine(2));
         Location blockLoc = getICBlock(signBlock, offsetVector);
         int nowType = -1;

         for(int nowTyp = 0; nowTyp < this.Types.size(); ++nowTyp) {
            if(((String)this.Types.get(nowTyp)).equalsIgnoreCase(signBlock.getLine(3))) {
               nowType = nowTyp;
            }
         }

         boolean result;
         List liste;
         Entity ent;
         Iterator var12;
         switch(nowType) {
         case 0:
            result = false;
            Player[] var17 = Bukkit.getServer().getOnlinePlayers();
            Player[] var14 = var17;
            int var13 = var17.length;

            for(int var16 = 0; var16 < var13; ++var16) {
               Player var15 = var14[var16];
               if(!var15.isDead() && var15.isOnline() && BlockUtils.isInRange(var15.getLocation(), blockLoc, range)) {
                  result = true;
                  break;
               }
            }

            this.switchLever(Lever.BACK, signBlock, result);
            var17 = (Player[])null;
            break;
         case 1:
            result = false;
            liste = signBlock.getWorld().getLivingEntities();
            var12 = liste.iterator();

            while(var12.hasNext()) {
               ent = (Entity)var12.next();
               if(!ent.isDead() && (ent instanceof Monster || ent instanceof Ghast || ent instanceof Giant || ent instanceof Slime) && BlockUtils.isInRange(ent.getLocation(), blockLoc, range)) {
                  result = true;
                  break;
               }
            }

            liste.clear();
            liste = null;
            this.switchLever(Lever.BACK, signBlock, result);
            break;
         case 2:
            result = false;
            liste = signBlock.getWorld().getLivingEntities();
            var12 = liste.iterator();

            while(var12.hasNext()) {
               ent = (Entity)var12.next();
               if(!ent.isDead() && (ent instanceof Animals || ent instanceof Villager || ent instanceof IronGolem || ent instanceof Ocelot) && BlockUtils.isInRange(ent.getLocation(), blockLoc, range)) {
                  result = true;
                  break;
               }
            }

            liste.clear();
            liste = null;
            this.switchLever(Lever.BACK, signBlock, result);
            break;
         case 3:
            result = false;
            liste = signBlock.getWorld().getLivingEntities();
            var12 = liste.iterator();

            while(var12.hasNext()) {
               ent = (Entity)var12.next();
               if(!ent.isDead() && (ent instanceof Animals || ent instanceof Monster || ent instanceof Ghast || ent instanceof Giant || ent instanceof Slime || ent instanceof Villager || ent instanceof IronGolem || ent instanceof Ocelot) && BlockUtils.isInRange(ent.getLocation(), blockLoc, range)) {
                  result = true;
                  break;
               }
            }

            liste.clear();
            liste = null;
            this.switchLever(Lever.BACK, signBlock, result);
            break;
         case 4:
            result = false;
            liste = signBlock.getWorld().getLivingEntities();
            var12 = liste.iterator();

            while(var12.hasNext()) {
               ent = (Entity)var12.next();
               if(!ent.isDead() && (ent instanceof Player || ent instanceof Animals || ent instanceof Monster || ent instanceof Ghast || ent instanceof Giant || ent instanceof Slime || ent instanceof Minecart || ent instanceof StorageMinecart || ent instanceof PoweredMinecart || ent instanceof Villager) && BlockUtils.isInRange(ent.getLocation(), blockLoc, range)) {
                  result = true;
                  break;
               }
            }

            liste.clear();
            liste = null;
            this.switchLever(Lever.BACK, signBlock, result);
            break;
         case 5:
            result = false;
            liste = signBlock.getWorld().getEntities();
            var12 = liste.iterator();

            while(var12.hasNext()) {
               ent = (Entity)var12.next();
               if(!ent.isDead() && ent instanceof Minecart && BlockUtils.isInRange(ent.getLocation(), blockLoc, range)) {
                  result = true;
                  break;
               }
            }

            liste.clear();
            liste = null;
            this.switchLever(Lever.BACK, signBlock, result);
            break;
         case 6:
            result = false;
            liste = signBlock.getWorld().getEntities();
            var12 = liste.iterator();

            while(var12.hasNext()) {
               ent = (Entity)var12.next();
               if(!ent.isDead() && ent instanceof StorageMinecart && BlockUtils.isInRange(ent.getLocation(), blockLoc, range)) {
                  result = true;
                  break;
               }
            }

            liste.clear();
            liste = null;
            this.switchLever(Lever.BACK, signBlock, result);
            break;
         case 7:
            result = false;
            liste = signBlock.getWorld().getEntities();
            var12 = liste.iterator();

            while(var12.hasNext()) {
               ent = (Entity)var12.next();
               if(!ent.isDead() && ent instanceof PoweredMinecart && BlockUtils.isInRange(ent.getLocation(), blockLoc, range)) {
                  result = true;
                  break;
               }
            }

            liste.clear();
            liste = null;
            this.switchLever(Lever.BACK, signBlock, result);
         }

         blockLoc = null;
      }

   }
}
