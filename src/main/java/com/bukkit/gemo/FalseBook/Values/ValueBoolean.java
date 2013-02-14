package com.bukkit.gemo.FalseBook.Values;


public class ValueBoolean {

   private String name;
   private boolean value;


   public ValueBoolean(String name, boolean value) {
      this.name = name;
      this.value = value;
   }

   public String getName() {
      return this.name;
   }

   public boolean getValue() {
      return this.value;
   }

   public void setValue(boolean value) {
      this.value = value;
   }

   public String toString() {
      return String.valueOf(this.value);
   }
}
