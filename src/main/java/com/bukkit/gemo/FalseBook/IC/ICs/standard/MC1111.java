package com.bukkit.gemo.FalseBook.IC.ICs.standard;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.BaseIC;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.InputState;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import com.bukkit.gemo.FalseBook.IC.ICs.SelftriggeredBaseIC;
import com.bukkit.gemo.FalseBook.IC.ICs.selftriggered.MC1110;
import com.bukkit.gemo.utils.LWCProtection;
import com.bukkit.gemo.utils.SignUtils;
import java.util.Iterator;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

public class MC1111 extends BaseIC {

   public MC1111() {
      this.ICName = "RECEIVER";
      this.ICNumber = "[MC1111]";
      this.setICGroup(ICGroup.STANDARD);
      this.chipState = new BaseChip(true, false, false, "Clock", "", "");
      this.chipState.setOutputs("Received data", "", "");
      this.chipState.setLines("networkname", "");
      this.ICDescription = "The MC1111 receives the state in a particular band or network when the clock input goes from low to high.<br /><br />The corresponding transmitter is the MC1110.";
   }

   public void checkCreation(SignChangeEvent event) {
      if(event.getLine(2).length() < 1) {
         SignUtils.cancelSignCreation(event, "Please define a Networkname!");
      }
   }

   public void Execute(Sign signBlock, InputState currentInputs, InputState previousInputs) {
      if(currentInputs.isInputOneHigh() && previousInputs.isInputOneLow()) {
         String networkName = signBlock.getLine(2);
         String mainNetwork = signBlock.getLine(3);
         String thisOwner = LWCProtection.getProtectionOwner(signBlock.getBlock());
         if(networkName.length() > 0) {
            boolean result = false;
            Iterator iterator = this.core.getFactory().getNonTriggeringSensorListIterator();

            while(iterator.hasNext()) {
               SelftriggeredBaseIC IC = (SelftriggeredBaseIC)iterator.next();
               if(IC instanceof MC1110 && networkName.equalsIgnoreCase(((MC1110)IC).getNetworkName()) && ((MC1110)IC).getStatus() && mainNetwork.equalsIgnoreCase(((MC1110)IC).getMainNetwork()) && thisOwner.equalsIgnoreCase(LWCProtection.getProtectionOwner(((MC1110)IC).getSignBlock().getBlock()))) {
                  result = true;
                  break;
               }
            }

            this.switchLever(Lever.BACK, signBlock, result);
         }
      }

   }
}
