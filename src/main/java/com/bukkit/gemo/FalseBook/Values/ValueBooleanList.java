package com.bukkit.gemo.FalseBook.Values;

import com.bukkit.gemo.FalseBook.Exceptions.ValueNotFoundException;
import com.bukkit.gemo.FalseBook.Exceptions.ValueNotParsableException;
import com.bukkit.gemo.FalseBook.Values.ValueBoolean;
import java.util.Iterator;
import java.util.TreeMap;

public class ValueBooleanList {

   public static String delimiter = ",";
   private String name;
   private TreeMap valueList = new TreeMap();


   public ValueBooleanList(String name) {
      this.name = name;
   }

   public ValueBooleanList(String name, String importString) {
      this.name = name;
      importString = importString.trim().replace(name + "=", "").replace(" ", "");
      String[] split = importString.split(delimiter);
      String[] var7 = split;
      int var6 = split.length;

      for(int var5 = 0; var5 < var6; ++var5) {
         String part = var7[var5];

         try {
            this.addValue(Boolean.valueOf(part).booleanValue());
         } catch (Exception var9) {
            throw new ValueNotParsableException("VALUE \'" + part + "\' can not be parsed to ValueBoolean.");
         }
      }

   }

   public String getName() {
      return this.name;
   }

   public boolean hasValue(String name) {
      return this.valueList.containsKey(name);
   }

   public ValueBoolean addValue(ValueBoolean value) {
      this.removeValue(value.getName());
      this.valueList.put(value.getName(), value);
      return value;
   }

   public ValueBoolean addValue(String name, boolean value) {
      return this.addValue(new ValueBoolean(name, value));
   }

   public ValueBoolean addValue(boolean value) {
      return this.addValue(new ValueBoolean("" + (this.valueList.size() + 1), value));
   }

   public ValueBoolean removeValue(String name) {
      return (ValueBoolean)this.valueList.remove(name);
   }

   public boolean getValue(String name) {
      try {
         return this.getNativeValue(name).getValue();
      } catch (Exception var3) {
         throw new ValueNotFoundException("VALUE \'" + name + "\' was not found (NULL).");
      }
   }

   public ValueBoolean getNativeValue(String name) {
      return (ValueBoolean)this.valueList.get(name);
   }

   public String exportList() {
      String export = "";

      ValueBoolean val;
      for(Iterator var3 = this.valueList.values().iterator(); var3.hasNext(); export = export + val.getValue() + delimiter) {
         val = (ValueBoolean)var3.next();
      }

      return export;
   }

   public String toString() {
      return this.exportList();
   }
}
