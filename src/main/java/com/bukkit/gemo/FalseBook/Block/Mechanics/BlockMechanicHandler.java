package com.bukkit.gemo.FalseBook.Block.Mechanics;

import com.bukkit.gemo.FalseBook.Block.FalseBookBlockCore;
import com.bukkit.gemo.FalseBook.Block.Mechanics.MechanicAppleTreeDrop;
import com.bukkit.gemo.FalseBook.Block.Mechanics.MechanicArea;
import com.bukkit.gemo.FalseBook.Block.Mechanics.MechanicBookshelf;
import com.bukkit.gemo.FalseBook.Block.Mechanics.MechanicBridge;
import com.bukkit.gemo.FalseBook.Block.Mechanics.MechanicCauldron;
import com.bukkit.gemo.FalseBook.Block.Mechanics.MechanicDoor;
import com.bukkit.gemo.FalseBook.Block.Mechanics.MechanicGate;
import com.bukkit.gemo.FalseBook.Block.Mechanics.MechanicLift;
import com.bukkit.gemo.FalseBook.Block.Mechanics.MechanicLightswitch;
import com.bukkit.gemo.FalseBook.Mechanics.MechanicHandler;
import com.bukkit.gemo.FalseBook.Mechanics.MechanicListener;
import java.util.HashMap;

public class BlockMechanicHandler extends MechanicHandler {

   private HashMap registeredLines = new HashMap();


   public void registerBlockMechanics(FalseBookBlockCore plugin) {
      this.registerMechanic("LIFT", new MechanicLift(plugin));
      this.registerMechanic("APPLE_TREE_DROP", new MechanicAppleTreeDrop(plugin));
      this.registerMechanic("AREA", new MechanicArea(plugin));
      this.registerMechanic("BRIDGE", new MechanicBridge(plugin));
      this.registerMechanic("DOOR", new MechanicDoor(plugin));
      this.registerMechanic("GATE", new MechanicGate(plugin));
      this.registerMechanic("BOOKSHELF", new MechanicBookshelf(plugin));
      this.registerMechanic("CAULDRON", new MechanicCauldron(plugin));
      this.registerMechanic("LIGHTSWITCH", new MechanicLightswitch(plugin));
      this.registerLines();
   }

   private void registerLines() {
      this.registeredLines.put("[lift]", this.getMechanic("LIFT"));
      this.registeredLines.put("[lift up]", this.getMechanic("LIFT"));
      this.registeredLines.put("[lift down]", this.getMechanic("LIFT"));
      this.registeredLines.put("[door up]", this.getMechanic("DOOR"));
      this.registeredLines.put("[door down]", this.getMechanic("DOOR"));
      this.registeredLines.put("[bridge]", this.getMechanic("BRIDGE"));
      this.registeredLines.put("[bridge end]", this.getMechanic("BRIDGE"));
      this.registeredLines.put("[gate]", this.getMechanic("GATE"));
      this.registeredLines.put("[dgate]", this.getMechanic("GATE"));
      this.registeredLines.put("[area]", this.getMechanic("AREA"));
      this.registeredLines.put("[toggle]", this.getMechanic("AREA"));
      this.registeredLines.put("[i]", this.getMechanic("LIGHTSWITCH"));
      this.registeredLines.put("[|]", this.getMechanic("LIGHTSWITCH"));
   }

   public MechanicListener getMechanicByLine(String line) {
      line = line.toLowerCase();
      return (MechanicListener)this.registeredLines.get(line);
   }
}
