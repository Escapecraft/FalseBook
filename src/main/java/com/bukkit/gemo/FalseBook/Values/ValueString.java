package com.bukkit.gemo.FalseBook.Values;


public class ValueString {

   private String name;
   private String value;


   public ValueString(String name, String value) {
      this.name = name;
      this.value = value;
   }

   public String getName() {
      return this.name;
   }

   public String getValue() {
      return this.value;
   }

   public void setValue(String value) {
      this.value = value;
   }

   public String toString() {
      return String.valueOf(this.value);
   }
}
