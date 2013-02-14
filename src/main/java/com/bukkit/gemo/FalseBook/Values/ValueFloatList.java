package com.bukkit.gemo.FalseBook.Values;

import com.bukkit.gemo.FalseBook.Exceptions.ValueNotFoundException;
import com.bukkit.gemo.FalseBook.Exceptions.ValueNotParsableException;
import com.bukkit.gemo.FalseBook.Values.ValueFloat;
import java.util.ArrayList;
import java.util.Iterator;

public class ValueFloatList {

   public static String delimiter = ",";
   private String name;
   private ArrayList valueList = new ArrayList();


   public ValueFloatList(String name) {
      this.name = name;
   }

   public ValueFloatList(String name, String importString) {
      this.name = name;
      importString = importString.trim().replace(name + "=", "").replace(" ", "");
      String[] split = importString.split(delimiter);
      String[] var7 = split;
      int var6 = split.length;

      for(int var5 = 0; var5 < var6; ++var5) {
         String part = var7[var5];

         try {
            this.addValue((float)Integer.valueOf(part).intValue());
         } catch (Exception var9) {
            throw new ValueNotParsableException("VALUE \'" + part + "\' can not be parsed to ValueFloat.");
         }
      }

   }

   public String getName() {
      return this.name;
   }

   public boolean hasValue(float value) {
      Iterator var3 = this.valueList.iterator();

      while(var3.hasNext()) {
         ValueFloat thisValue = (ValueFloat)var3.next();
         if(thisValue.getValue() == value) {
            return true;
         }
      }

      return false;
   }

   public ValueFloat addValue(ValueFloat value) {
      this.removeValue(value.getValue());
      this.valueList.add(value);
      return value;
   }

   public ValueFloat addValue(String name, float value) {
      return this.addValue(new ValueFloat(name, value));
   }

   public ValueFloat addValue(float value) {
      return this.addValue(new ValueFloat("" + (this.valueList.size() + 1), value));
   }

   public boolean removeValue(float value) {
      boolean found = false;

      for(int index = this.valueList.size() - 1; index >= 0; --index) {
         ValueFloat thisVal = (ValueFloat)this.valueList.get(index);
         if(thisVal.getValue() == value) {
            this.valueList.remove(index);
            found = true;
         }
      }

      return found;
   }

   public float getValue(int index) {
      try {
         return ((ValueFloat)this.valueList.get(index)).getValue();
      } catch (Exception var3) {
         throw new ValueNotFoundException("VALUE with index " + index + " was not found (SIZE: " + this.valueList.size() + ").");
      }
   }

   public ArrayList getAll() {
      return this.valueList;
   }

   public String exportList() {
      String export = "";

      ValueFloat val;
      for(Iterator var3 = this.valueList.iterator(); var3.hasNext(); export = export + val.getValue() + delimiter) {
         val = (ValueFloat)var3.next();
      }

      return export;
   }

   public String toString() {
      return this.exportList();
   }
}
