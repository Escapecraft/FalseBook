package com.bukkit.gemo.FalseBook.IC.ICs;

import com.bukkit.gemo.utils.BlockUtils;
import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.block.Block;

public class BaseChip {

   private boolean hasInput1 = false;
   private boolean hasInput2 = false;
   private boolean hasInput3 = false;
   private String NameInput1 = "";
   private String NameInput2 = "";
   private String NameInput3 = "";
   private String NameOutput1 = "";
   private String NameOutput2 = "";
   private String NameOutput3 = "";
   private String signLine3 = "";
   private String signLine4 = "";


   public BaseChip(boolean has1, boolean has2, boolean has3, String nameInput1, String nameInput2, String nameInput3) {
      this.hasInput1 = has1;
      this.hasInput2 = has2;
      this.hasInput3 = has3;
      this.NameInput1 = nameInput1;
      this.NameInput2 = nameInput2;
      this.NameInput3 = nameInput3;
   }

   public void setOutputs(String nameOutput1, String nameOutput2, String nameOutput3) {
      this.NameOutput1 = nameOutput1;
      this.NameOutput2 = nameOutput2;
      this.NameOutput3 = nameOutput3;
   }

   public void setLines(String line3, String line4) {
      this.signLine3 = line3;
      this.signLine4 = line4;
   }

   public boolean hasInput(Block eventBlock, ArrayList positions) {
      return this.hasInput1() && BlockUtils.LocationEquals(eventBlock.getLocation(), (Location)positions.get(0))?true:(this.hasInput2() && BlockUtils.LocationEquals(eventBlock.getLocation(), (Location)positions.get(1))?true:this.hasInput3() && BlockUtils.LocationEquals(eventBlock.getLocation(), (Location)positions.get(2)));
   }

   public int getInputCount() {
      int c = 0;
      if(this.NameInput1.length() > 0) {
         ++c;
      }

      if(this.NameInput2.length() > 0) {
         ++c;
      }

      if(this.NameInput3.length() > 0) {
         ++c;
      }

      return c;
   }

   public int getOutputCount() {
      int c = 0;
      if(this.NameOutput1.length() > 0) {
         ++c;
      }

      if(this.NameOutput2.length() > 0) {
         ++c;
      }

      if(this.NameOutput3.length() > 0) {
         ++c;
      }

      return c;
   }

   public boolean hasInput1() {
      return this.hasInput1;
   }

   public boolean hasInput2() {
      return this.hasInput2;
   }

   public boolean hasInput3() {
      return this.hasInput3;
   }

   public String getNameInput1() {
      return this.NameInput1;
   }

   public String getNameInput2() {
      return this.NameInput2;
   }

   public String getNameInput3() {
      return this.NameInput3;
   }

   public String getNameOutput1() {
      return this.NameOutput1;
   }

   public String getNameOutput2() {
      return this.NameOutput2;
   }

   public String getNameOutput3() {
      return this.NameOutput3;
   }

   public String getSignLine3() {
      return this.signLine3;
   }

   public String getSignLine4() {
      return this.signLine4;
   }
}
