package com.bukkit.gemo.FalseBook.Values;

import com.bukkit.gemo.FalseBook.Exceptions.ValueNotFoundException;
import com.bukkit.gemo.FalseBook.Exceptions.ValueNotParsableException;
import com.bukkit.gemo.FalseBook.Values.ValueLocation;
import com.bukkit.gemo.utils.BlockUtils;
import java.util.Iterator;
import java.util.TreeMap;
import org.bukkit.Location;

public class ValueLocationList {

   public static String delimiter = "<;>";
   private String name;
   private TreeMap valueList = new TreeMap();


   public ValueLocationList(String name) {
      this.name = name;
   }

   public ValueLocationList(String name, String importString) {
      this.name = name;
      importString = importString.trim().replace(name + "=", "");
      String[] split = importString.split(delimiter);
      String[] var7 = split;
      int var6 = split.length;

      for(int var5 = 0; var5 < var6; ++var5) {
         String part = var7[var5];

         try {
            if(this.addValue(BlockUtils.LocationFromString(part)) == null) {
               throw new ValueNotParsableException("VALUE \'" + part + "\' can not be parsed to ValueLocation.");
            }
         } catch (Exception var9) {
            throw new ValueNotParsableException("VALUE \'" + part + "\' can not be parsed to ValueLocation.");
         }
      }

   }

   public String getName() {
      return this.name;
   }

   public boolean hasValue(String name) {
      return this.valueList.containsKey(name);
   }

   public ValueLocation addValue(ValueLocation value) {
      this.removeValue(value.getName());
      this.valueList.put(value.getName(), value);
      return value;
   }

   public ValueLocation addValue(String name, Location value) {
      return this.addValue(new ValueLocation(name, value));
   }

   public ValueLocation addValue(Location value) {
      return this.addValue(new ValueLocation("" + (this.valueList.size() + 1), value));
   }

   public ValueLocation removeValue(String name) {
      return (ValueLocation)this.valueList.remove(name);
   }

   public Location getValue(String name) {
      try {
         return this.getNativeValue(name).getValue();
      } catch (Exception var3) {
         throw new ValueNotFoundException("VALUE \'" + name + "\' was not found (NULL).");
      }
   }

   public ValueLocation getNativeValue(String name) {
      return (ValueLocation)this.valueList.get(name);
   }

   public String exportList() {
      String export = "";

      ValueLocation val;
      for(Iterator var3 = this.valueList.values().iterator(); var3.hasNext(); export = export + BlockUtils.LocationToString(val.getValue()) + delimiter) {
         val = (ValueLocation)var3.next();
      }

      return export;
   }

   public String toString() {
      return this.exportList();
   }
}
