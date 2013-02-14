package com.bukkit.gemo.FalseBook.Values;


public class ValueDouble {

   private String name;
   private double value;


   public ValueDouble(String name, double value) {
      this.name = name;
      this.value = value;
   }

   public String getName() {
      return this.name;
   }

   public double getValue() {
      return this.value;
   }

   public void setValue(double value) {
      this.value = value;
   }

   public String toString() {
      return String.valueOf(this.value);
   }
}
