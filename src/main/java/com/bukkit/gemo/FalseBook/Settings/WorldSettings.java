package com.bukkit.gemo.FalseBook.Settings;

import com.bukkit.gemo.FalseBook.Extra.FalseBookExtraCore;
import com.bukkit.gemo.FalseBook.Values.ValueBoolean;
import com.bukkit.gemo.FalseBook.Values.ValueBooleanList;
import com.bukkit.gemo.FalseBook.Values.ValueDouble;
import com.bukkit.gemo.FalseBook.Values.ValueDoubleList;
import com.bukkit.gemo.FalseBook.Values.ValueFloat;
import com.bukkit.gemo.FalseBook.Values.ValueFloatList;
import com.bukkit.gemo.FalseBook.Values.ValueInteger;
import com.bukkit.gemo.FalseBook.Values.ValueIntegerList;
import com.bukkit.gemo.FalseBook.Values.ValueLocation;
import com.bukkit.gemo.FalseBook.Values.ValueLocationList;
import com.bukkit.gemo.FalseBook.Values.ValueString;
import com.bukkit.gemo.FalseBook.Values.ValueStringList;
import com.bukkit.gemo.utils.BlockUtils;
import com.bukkit.gemo.utils.FlatFile;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import org.bukkit.Location;

public class WorldSettings {

   private TreeMap booleanMap = new TreeMap();
   private TreeMap booleanListMap = new TreeMap();
   private TreeMap doubleMap = new TreeMap();
   private TreeMap doubleListMap = new TreeMap();
   private TreeMap floatMap = new TreeMap();
   private TreeMap floatListMap = new TreeMap();
   private TreeMap integerMap = new TreeMap();
   private TreeMap integerListMap = new TreeMap();
   private TreeMap locationMap = new TreeMap();
   private TreeMap locationListMap = new TreeMap();
   private TreeMap stringMap = new TreeMap();
   private TreeMap stringListMap = new TreeMap();


   public boolean getBoolean(String name) {
      return ((ValueBoolean)this.booleanMap.get(name)).getValue();
   }

   public ValueBooleanList getBooleanMap(String name) {
      return (ValueBooleanList)this.booleanListMap.get(name);
   }

   public boolean isKeyInBooleanList(String name) {
      return this.booleanListMap.containsKey(name);
   }

   public ValueBoolean addBoolean(ValueBoolean value) {
      return (ValueBoolean)this.booleanMap.put(value.getName(), value);
   }

   public ValueBoolean addBoolean(String name, boolean value) {
      return this.addBoolean(new ValueBoolean(name, value));
   }

   public ValueBooleanList addBooleanList(ValueBooleanList value) {
      return (ValueBooleanList)this.booleanListMap.put(value.getName(), value);
   }

   public ValueBooleanList addBooleanList(String name, ArrayList valueList) {
      String txt = name + "=";

      boolean val;
      for(Iterator var5 = valueList.iterator(); var5.hasNext(); txt = txt + val + ValueBooleanList.delimiter) {
         val = ((Boolean)var5.next()).booleanValue();
      }

      return (ValueBooleanList)this.booleanListMap.put(name, new ValueBooleanList(name, txt));
   }

   public ValueBooleanList addBooleanList(String name, String importString) {
      return this.addBooleanList(new ValueBooleanList(name, importString));
   }

   public double getDouble(String name) {
      return ((ValueDouble)this.doubleMap.get(name)).getValue();
   }

   public ValueDoubleList getDoubleMap(String name) {
      return (ValueDoubleList)this.doubleListMap.get(name);
   }

   public boolean isKeyInDoubleList(String name) {
      return this.doubleListMap.containsKey(name);
   }

   public ValueDouble addDouble(ValueDouble value) {
      return (ValueDouble)this.doubleMap.put(value.getName(), value);
   }

   public ValueDouble addDouble(String name, double value) {
      return this.addDouble(new ValueDouble(name, value));
   }

   public ValueDoubleList addDoubleList(ValueDoubleList value) {
      return (ValueDoubleList)this.doubleListMap.put(value.getName(), value);
   }

   public ValueDoubleList addDoubleList(String name, ArrayList valueList) {
      String txt = name + "=";

      double val;
      for(Iterator var6 = valueList.iterator(); var6.hasNext(); txt = txt + val + ValueDoubleList.delimiter) {
         val = ((Double)var6.next()).doubleValue();
      }

      return (ValueDoubleList)this.doubleListMap.put(name, new ValueDoubleList(name, txt));
   }

   public ValueDoubleList addDoubleList(String name, String importString) {
      return this.addDoubleList(new ValueDoubleList(name, importString));
   }

   public float getFloat(String name) {
      return ((ValueFloat)this.floatMap.get(name)).getValue();
   }

   public ValueFloatList getFloatMap(String name) {
      return (ValueFloatList)this.floatListMap.get(name);
   }

   public boolean isKeyInFloatList(String name) {
      return this.floatListMap.containsKey(name);
   }

   public ValueFloat addFloat(ValueFloat value) {
      return (ValueFloat)this.floatMap.put(value.getName(), value);
   }

   public ValueFloat addFloat(String name, float value) {
      return this.addFloat(new ValueFloat(name, value));
   }

   public ValueFloatList addFloatList(ValueFloatList value) {
      return (ValueFloatList)this.floatListMap.put(value.getName(), value);
   }

   public ValueFloatList addFloatList(String name, ArrayList valueList) {
      String txt = name + "=";

      float val;
      for(Iterator var5 = valueList.iterator(); var5.hasNext(); txt = txt + val + ValueFloatList.delimiter) {
         val = ((Float)var5.next()).floatValue();
      }

      return (ValueFloatList)this.floatListMap.put(name, new ValueFloatList(name, txt));
   }

   public ValueFloatList addFloatList(String name, String importString) {
      return this.addFloatList(new ValueFloatList(name, importString));
   }

   public int getInteger(String name) {
      return ((ValueInteger)this.integerMap.get(name)).getValue();
   }

   public ValueIntegerList getIntegerMap(String name) {
      return (ValueIntegerList)this.integerListMap.get(name);
   }

   public boolean isKeyInIntegerList(String name) {
      return this.integerListMap.containsKey(name);
   }

   public ValueInteger addInteger(ValueInteger value) {
      return (ValueInteger)this.integerMap.put(value.getName(), value);
   }

   public ValueInteger addInteger(String name, int value) {
      return this.addInteger(new ValueInteger(name, value));
   }

   public ValueIntegerList addIntegerList(ValueIntegerList value) {
      return (ValueIntegerList)this.integerListMap.put(value.getName(), value);
   }

   public ValueIntegerList addIntegerList(String name, ArrayList valueList) {
      String txt = name + "=";

      int val;
      for(Iterator var5 = valueList.iterator(); var5.hasNext(); txt = txt + val + ValueIntegerList.delimiter) {
         val = ((Integer)var5.next()).intValue();
      }

      return (ValueIntegerList)this.integerListMap.put(name, new ValueIntegerList(name, txt));
   }

   public ValueIntegerList addIntegerList(String name, String importString) {
      return this.addIntegerList(new ValueIntegerList(name, importString));
   }

   public Location getLocation(String name) {
      return ((ValueLocation)this.locationMap.get(name)).getValue();
   }

   public ValueLocationList getLocationMap(String name) {
      return (ValueLocationList)this.locationListMap.get(name);
   }

   public boolean isKeyInLocationList(String name) {
      return this.locationListMap.containsKey(name);
   }

   public ValueLocation addLocation(ValueLocation value) {
      return (ValueLocation)this.locationMap.put(value.getName(), value);
   }

   public ValueLocation addLocation(String name, Location value) {
      return this.addLocation(new ValueLocation(name, value));
   }

   public ValueLocationList addLocationList(ValueLocationList value) {
      return (ValueLocationList)this.locationListMap.put(value.getName(), value);
   }

   public ValueLocationList addLocationList(String name, String importString) {
      return this.addLocationList(new ValueLocationList(name, importString));
   }

   public String getString(String name) {
      return ((ValueString)this.stringMap.get(name)).getValue();
   }

   public ValueStringList getStringMap(String name) {
      return (ValueStringList)this.stringListMap.get(name);
   }

   public boolean isKeyInStringList(String name) {
      return this.stringListMap.containsKey(name);
   }

   public ValueString addString(ValueString value) {
      return (ValueString)this.stringMap.put(value.getName(), value);
   }

   public ValueString addString(String name, String value) {
      return this.addString(new ValueString(name, value));
   }

   public ValueStringList addStringList(ValueStringList value) {
      return (ValueStringList)this.stringListMap.put(value.getName(), value);
   }

   public ValueStringList addStringList(String name, String importString) {
      return this.addStringList(new ValueStringList(name, importString));
   }

   public boolean save(File dataFolder, String fileName) {
      dataFolder.mkdirs();
      File file = new File(dataFolder, fileName);
      boolean fileExisted = file.exists();

      try {
         String e = dataFolder.getPath();
         FlatFile config = new FlatFile(e + System.getProperty("file.separator") + fileName, fileExisted);
         Iterator var8 = this.booleanMap.values().iterator();

         while(var8.hasNext()) {
            ValueBoolean value = (ValueBoolean)var8.next();
            config.setBoolean(value.getName(), value.getValue());
         }

         var8 = this.booleanListMap.values().iterator();

         while(var8.hasNext()) {
            ValueBooleanList value1 = (ValueBooleanList)var8.next();
            config.setString(value1.getName(), value1.exportList());
         }

         var8 = this.doubleMap.values().iterator();

         while(var8.hasNext()) {
            ValueDouble value3 = (ValueDouble)var8.next();
            config.setDouble(value3.getName(), value3.getValue());
         }

         var8 = this.doubleListMap.values().iterator();

         while(var8.hasNext()) {
            ValueDoubleList value2 = (ValueDoubleList)var8.next();
            config.setString(value2.getName(), value2.exportList());
         }

         var8 = this.floatMap.values().iterator();

         while(var8.hasNext()) {
            ValueFloat value5 = (ValueFloat)var8.next();
            config.setFloat(value5.getName(), value5.getValue());
         }

         var8 = this.floatListMap.values().iterator();

         while(var8.hasNext()) {
            ValueFloatList value4 = (ValueFloatList)var8.next();
            config.setString(value4.getName(), value4.exportList());
         }

         var8 = this.integerMap.values().iterator();

         while(var8.hasNext()) {
            ValueInteger value7 = (ValueInteger)var8.next();
            config.setInt(value7.getName(), value7.getValue());
         }

         var8 = this.integerListMap.values().iterator();

         while(var8.hasNext()) {
            ValueIntegerList value6 = (ValueIntegerList)var8.next();
            config.setString(value6.getName(), value6.exportList());
         }

         var8 = this.locationMap.values().iterator();

         while(var8.hasNext()) {
            ValueLocation value9 = (ValueLocation)var8.next();
            config.setString(value9.getName(), BlockUtils.LocationToString(value9.getValue()));
         }

         var8 = this.locationListMap.values().iterator();

         while(var8.hasNext()) {
            ValueLocationList value8 = (ValueLocationList)var8.next();
            config.setString(value8.getName(), value8.exportList());
         }

         var8 = this.stringMap.values().iterator();

         while(var8.hasNext()) {
            ValueString value10 = (ValueString)var8.next();
            config.setString(value10.getName(), value10.getValue());
         }

         var8 = this.stringListMap.values().iterator();

         while(var8.hasNext()) {
            ValueStringList value11 = (ValueStringList)var8.next();
            config.setString(value11.getName(), value11.exportList());
         }

         if(fileExisted) {
            file.delete();
         }

         config.writeFile();
         return true;
      } catch (IOException var9) {
         FalseBookExtraCore.printInConsole("Error while saving file: " + file.getName());
         var9.printStackTrace();
         return false;
      }
   }
}
