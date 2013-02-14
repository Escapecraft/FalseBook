package com.bukkit.gemo.FalseBook.Values;

import com.bukkit.gemo.FalseBook.Exceptions.ValueNotFoundException;
import com.bukkit.gemo.FalseBook.Exceptions.ValueNotParsableException;
import com.bukkit.gemo.FalseBook.Values.ValueString;
import java.util.Iterator;
import java.util.TreeMap;

public class ValueStringList {

   public static String delimiter = "<;>";
   private String name;
   private TreeMap valueList = new TreeMap();


   public ValueStringList(String name) {
      this.name = name;
   }

   public ValueStringList(String name, String importString) {
      this.name = name;
      importString = importString.trim().replace(name + "=", "");
      String[] split = importString.split(delimiter);
      String[] var7 = split;
      int var6 = split.length;

      for(int var5 = 0; var5 < var6; ++var5) {
         String part = var7[var5];

         try {
            this.addValue(part);
         } catch (Exception var9) {
            throw new ValueNotParsableException("VALUE \'" + part + "\' can not be parsed to ValueString.");
         }
      }

   }

   public String getName() {
      return this.name;
   }

   public boolean hasValue(String name) {
      return this.valueList.containsKey(name);
   }

   public ValueString addValue(ValueString value) {
      this.removeValue(value.getName());
      this.valueList.put(value.getName(), value);
      return value;
   }

   public ValueString addValue(String name, String value) {
      return this.addValue(new ValueString(name, value));
   }

   public ValueString addValue(String value) {
      return this.addValue(new ValueString("" + (this.valueList.size() + 1), value));
   }

   public ValueString removeValue(String name) {
      return (ValueString)this.valueList.remove(name);
   }

   public String getValue(String name) {
      try {
         return this.getNativeValue(name).getValue();
      } catch (Exception var3) {
         throw new ValueNotFoundException("VALUE \'" + name + "\' was not found (NULL).");
      }
   }

   public ValueString getNativeValue(String name) {
      return (ValueString)this.valueList.get(name);
   }

   public String exportList() {
      String export = "";

      ValueString val;
      for(Iterator var3 = this.valueList.values().iterator(); var3.hasNext(); export = export + val.getValue() + delimiter) {
         val = (ValueString)var3.next();
      }

      return export;
   }

   public String toString() {
      return this.exportList();
   }
}
