package com.bukkit.gemo.FalseBook.IC.ICs.selftriggered;

import com.bukkit.gemo.FalseBook.IC.FalseBookICCore;
import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.InputState;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import com.bukkit.gemo.FalseBook.IC.ICs.SelftriggeredBaseIC;
import com.bukkit.gemo.FalseBook.IC.ICs.selftriggered.MC0111;
import com.bukkit.gemo.utils.LWCProtection;
import com.bukkit.gemo.utils.SignUtils;
import java.util.Iterator;
import org.bukkit.Location;
import org.bukkit.event.block.SignChangeEvent;

public class MC1110 extends SelftriggeredBaseIC {

   private String networkName = "";
   private String mainNetwork = "";
   boolean curStatus;


   public MC1110() {
      this.setTypeID(2);
      this.ICName = "TRANSMITTER";
      this.ICNumber = "[MC1110]";
      this.setICGroup(ICGroup.SELFTRIGGERED);
      this.chipState = new BaseChip(false, false, false, "Data", "", "");
      this.chipState.setOutputs("Output = Input", "", "");
      this.chipState.setLines("networkname", "");
      this.ICDescription = "The MC1110 transmits the input value to a particular named band or network.";
      this.setExecuteOnTick(false);
   }

   public void checkCreation(SignChangeEvent event) {
      if(event.getLine(2) == null) {
         SignUtils.cancelSignCreation(event, "Please define a Networkname!");
      } else if(event.getLine(2).length() < 1) {
         SignUtils.cancelSignCreation(event, "Please define a Networkname!");
      }
   }

   public boolean onLoad(String[] lines) {
      this.networkName = "DEFAULT";
      if(lines[2].length() > 0) {
         this.networkName = lines[2];
      }

      if(lines[3].length() > 0) {
         this.mainNetwork = lines[3];
      }

      lines[2] = this.networkName;
      lines[3] = this.mainNetwork;
      this.setExecuteOnTick(false);
      return true;
   }

   public void setStatus(boolean newStatus) {
      if(super.validateIC()) {
         if(newStatus != this.oldStatus) {
            this.oldStatus = newStatus;
            String thisOwner = LWCProtection.getProtectionOwner(this.signBlock.getBlock());
            Iterator iterator = this.core.getFactory().getNonTriggeringSensorListIterator();

            SelftriggeredBaseIC IC;
            while(iterator.hasNext()) {
               IC = (SelftriggeredBaseIC)iterator.next();
               if(IC instanceof MC0111 && this.networkName.equalsIgnoreCase(((MC0111)IC).getNetworkName()) && this.mainNetwork.equalsIgnoreCase(((MC0111)IC).getMainNetwork()) && thisOwner.equalsIgnoreCase(LWCProtection.getProtectionOwner(((MC0111)IC).getSignBlock().getBlock()))) {
                  ((MC0111)IC).setStatus(newStatus);
               }
            }

            IC = null;
            this.switchLever(Lever.BACK, this.signBlock, newStatus);
         }

      }
   }

   public boolean initIC(FalseBookICCore plugin, Location location) {
      boolean result = super.initIC(plugin, location);
      if(!result) {
         return false;
      } else {
         this.oldStatus = this.oldStatus || (new InputState(this.signBlock)).isInputOneHigh();
         return result;
      }
   }

   public boolean getStatus() {
      return this.oldStatus;
   }

   public String getNetworkName() {
      return this.networkName;
   }

   public String getMainNetwork() {
      return this.mainNetwork;
   }
}
