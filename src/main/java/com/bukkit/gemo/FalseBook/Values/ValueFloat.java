package com.bukkit.gemo.FalseBook.Values;


public class ValueFloat {

   private String name;
   private float value;


   public ValueFloat(String name, float value) {
      this.name = name;
      this.value = value;
   }

   public String getName() {
      return this.name;
   }

   public float getValue() {
      return this.value;
   }

   public void setValue(float value) {
      this.value = value;
   }

   public String toString() {
      return String.valueOf(this.value);
   }
}
