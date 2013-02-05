package com.bukkit.gemo.FalseBook.IC.ICs.selftriggered;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import com.bukkit.gemo.FalseBook.IC.ICs.SelftriggeredBaseIC;
import com.bukkit.gemo.utils.SignUtils;
import org.bukkit.event.block.SignChangeEvent;

public class MC0111 extends SelftriggeredBaseIC {

   private String networkName = "";
   private String mainNetwork = "";


   public String getNetworkName() {
      return this.networkName;
   }

   public MC0111() {
      this.setTypeID(3);
      this.ICName = "RECEIVER";
      this.ICNumber = "[MC0111]";
      this.setICGroup(ICGroup.SELFTRIGGERED);
      this.chipState = new BaseChip(false, false, false, "", "", "");
      this.chipState.setOutputs("Received data", "", "");
      this.chipState.setLines("networkname", "");
      this.ICDescription = "The MC0111 receives the state in a particular band or network.<br /><br />The corresponding transmitter is the MC1110.";
      this.setExecuteOnTick(false);
   }

   public void checkCreation(SignChangeEvent event) {
      if(event.getLine(2) == null) {
         SignUtils.cancelSignCreation(event, "Please define a Networkname!");
      } else if(event.getLine(2).length() < 1) {
         SignUtils.cancelSignCreation(event, "Please define a Networkname!");
      } else {
         this.networkName = event.getLine(2);
         if(event.getLine(3).length() > 0) {
            this.mainNetwork = event.getLine(3);
         }

      }
   }

   public boolean onLoad(String[] lines) {
      this.networkName = "DEFAULT";
      if(lines[2].length() > 0) {
         this.networkName = lines[2];
      } else {
         lines[2] = this.networkName;
      }

      if(lines[3].length() > 0) {
         this.mainNetwork = lines[3];
      } else {
         lines[3] = this.mainNetwork;
      }

      this.setExecuteOnTick(false);
      return true;
   }

   public void setStatus(boolean newStatus) {
      if(newStatus != this.oldStatus) {
         this.oldStatus = newStatus;
         this.switchLever(Lever.BACK, this.signBlock, newStatus);
      }

   }

   public String getMainNetwork() {
      return this.mainNetwork;
   }
}
