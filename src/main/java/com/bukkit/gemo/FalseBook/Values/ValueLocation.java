package com.bukkit.gemo.FalseBook.Values;

import com.bukkit.gemo.utils.BlockUtils;
import org.bukkit.Location;

public class ValueLocation {

   private String name;
   private Location value;


   public ValueLocation(String name, Location value) {
      this.name = name;
      this.value = value;
   }

   public String getName() {
      return this.name;
   }

   public Location getValue() {
      return this.value;
   }

   public void setValue(Location value) {
      this.value = value;
   }

   public String toString() {
      return BlockUtils.LocationToString(this.value);
   }
}
