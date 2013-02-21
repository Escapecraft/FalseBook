package com.bukkit.gemo.FalseBook.IC.ICs.standard;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.BaseIC;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.InputState;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import com.bukkit.gemo.utils.Parser;
import com.bukkit.gemo.utils.SignUtils;
import java.util.HashMap;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

public class MC3101 extends BaseIC {

   private HashMap ModeList = new HashMap();
   private HashMap CountList = new HashMap();
   private HashMap StartCountList = new HashMap();


   public MC3101() {
      this.ICName = "COUNTER";
      this.ICNumber = "[MC3101]";
      this.setICGroup(ICGroup.STANDARD);
      this.chipState = new BaseChip(true, true, false, "Clock", "Reset", "");
      this.chipState.setOutputs("Output", "", "");
      this.chipState.setLines("the counter reset value", "ONCE (player resets the signal via resetinput) or INF (the IC resets itself, when reaching the counter reset value)");
      this.ICDescription = "The MC3101 implements a counter that counts down from a given input. The counter counts down each time clock input toggles from low to high, it starts from a predefined value to 0. Output is high when counter reaches 0. If in \'infinite\' mode, it will automatically reset the next time clock is toggled. Otherwise, it only resets when the \'reset\' input toggles from low to high.";
   }

   public void checkCreation(SignChangeEvent event) {
      boolean val = false;
      String mode = "";
      String locStr = event.getBlock().getLocation().toString();
      if(!Parser.isInteger(event.getLine(2))) {
         SignUtils.cancelSignCreation(event, "Enter the resetvalue in line 3.");
      } else {
         event.setLine(2, String.valueOf(Math.abs(Integer.valueOf(event.getLine(2)).intValue())));
         if(event.getLine(3).length() > 0) {
            if(!event.getLine(3).equalsIgnoreCase("ONCE") && !event.getLine(3).equalsIgnoreCase("INF")) {
               SignUtils.cancelSignCreation(event, "Line 4 must be ONCE or INF. (default: INF)");
               return;
            }

            event.setLine(3, event.getLine(3).toUpperCase());
         } else {
            event.setLine(3, "INF");
         }

         int val1 = Integer.valueOf(event.getLine(2)).intValue();
         mode = event.getLine(3);
         this.ModeList.put(locStr, mode);
         this.CountList.put(locStr, Integer.valueOf(val1));
         this.StartCountList.put(locStr, Integer.valueOf(val1));
      }
   }

   public void Execute(Sign signBlock, InputState currentInputs, InputState previousInputs) {
      if(Parser.isInteger(signBlock.getLine(2))) {
         int line2 = Math.abs(Parser.getInteger(signBlock.getLine(2), 1));
         String locStr = signBlock.getBlock().getLocation().toString();
         if(!this.ModeList.containsKey(locStr) || !this.CountList.containsKey(locStr) || !this.StartCountList.containsKey(locStr)) {
            this.ModeList.put(locStr, signBlock.getLine(3));
            this.CountList.put(locStr, Integer.valueOf(line2));
            this.StartCountList.put(locStr, Integer.valueOf(line2));
         }

         if(((String)this.ModeList.get(locStr)).equalsIgnoreCase("INF")) {
            if(currentInputs.isInputOneHigh() && previousInputs.isInputOneLow()) {
               this.CountList.put(locStr, Integer.valueOf(((Integer)this.CountList.get(locStr)).intValue() - 1));
               if(((Integer)this.CountList.get(locStr)).intValue() == 0) {
                  this.switchLever(Lever.BACK, signBlock, true);
               } else {
                  this.switchLever(Lever.BACK, signBlock, false);
                  if(((Integer)this.CountList.get(locStr)).intValue() == -1) {
                     this.CountList.put(locStr, (Integer)this.StartCountList.get(locStr));
                  }
               }
            }
         } else if(((String)this.ModeList.get(locStr)).equalsIgnoreCase("ONCE")) {
            if(currentInputs.isInputTwoHigh() && previousInputs.isInputTwoLow()) {
               this.CountList.put(locStr, (Integer)this.StartCountList.get(locStr));
               this.switchLever(Lever.BACK, signBlock, false);
            } else if(currentInputs.isInputOneHigh() && previousInputs.isInputOneLow()) {
               this.CountList.put(locStr, Integer.valueOf(((Integer)this.CountList.get(locStr)).intValue() - 1));
               if(((Integer)this.CountList.get(locStr)).intValue() < 1) {
                  this.switchLever(Lever.BACK, signBlock, true);
               }
            }
         }

      }
   }
}
