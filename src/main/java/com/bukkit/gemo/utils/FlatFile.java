package com.bukkit.gemo.utils;

import com.bukkit.gemo.utils.FlatFileData;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;

public class FlatFile {

   private String FileName;
   private ArrayList Data;
   private BufferedReader reader;
   private Writer writer;
   private boolean isReading = false;
   private boolean isWriting = false;


   public FlatFile(String FileName, boolean read) throws IOException {
      this.FileName = FileName;
      this.Data = new ArrayList();
      if(read) {
         this.readFile();
      }

   }

   public void regenerateFile(String FileName) throws IOException {
      this.writer = new BufferedWriter(new FileWriter(FileName));
      this.writer.flush();
      this.writer.close();
   }

   public boolean readFile() throws IOException {
      boolean FileExisted = true;
      if(!(new File(this.FileName)).exists()) {
         this.regenerateFile(this.FileName);
         FileExisted = false;
      }

      this.reader = new BufferedReader(new InputStreamReader(new FileInputStream(this.FileName), "ISO-8859-1"));
      String line = this.reader.readLine();

      while(line != null) {
         line = line.trim();
         if(!line.startsWith("#")) {
            String[] split = line.split("=");
            if(split.length > 1) {
               this.Data.add(new FlatFileData(split[0], split[1]));
            }

            line = this.reader.readLine();
         }
      }

      this.reader.close();
      return FileExisted;
   }

   public void writeFile() throws IOException {
      this.writer = new BufferedWriter(new FileWriter(this.FileName));

      for(int i = 0; i < this.Data.size(); ++i) {
         this.writer.write(((FlatFileData)this.Data.get(i)).getName() + "=" + ((FlatFileData)this.Data.get(i)).getValue() + "\r\n");
      }

      this.writer.close();
   }

   public void clearAll() {
      this.Data.clear();
   }

   public void closeFile(boolean read) throws IOException {
      if(read) {
         if(this.isReading) {
            this.isReading = false;
         }
      } else if(this.isWriting) {
         this.writer.flush();
         this.writer.close();
         this.isWriting = false;
      }

   }

   public void addBoolean(String name, boolean value) {
      if(!this.propertyExists(name)) {
         this.Data.add(new FlatFileData(name, "" + value));
      }
   }

   public void addInt(String name, int value) {
      if(!this.propertyExists(name)) {
         this.Data.add(new FlatFileData(name, "" + value));
      }
   }

   public void addDouble(String name, double value) {
      if(!this.propertyExists(name)) {
         this.Data.add(new FlatFileData(name, "" + value));
      }
   }

   public void addFloat(String name, float value) {
      if(!this.propertyExists(name)) {
         this.Data.add(new FlatFileData(name, "" + value));
      }
   }

   public void addString(String name, String value) {
      if(!this.propertyExists(name)) {
         this.Data.add(new FlatFileData(name, value));
      }
   }

   public void addBooleanArrayList(String name, ArrayList value, String delimiter) {
      if(!this.propertyExists(name)) {
         String str = "";

         for(int i = 0; i < value.size() - 1; ++i) {
            str = str + value.get(i) + delimiter;
         }

         if(value.size() > 0) {
            str = str + value.get(value.size() - 1);
         }

         this.Data.add(new FlatFileData(name, str));
      }
   }

   public void addIntArrayList(String name, ArrayList value, String delimiter) {
      if(!this.propertyExists(name)) {
         String str = "";

         for(int i = 0; i < value.size() - 1; ++i) {
            str = str + value.get(i) + delimiter;
         }

         if(value.size() > 0) {
            str = str + value.get(value.size() - 1);
         }

         this.Data.add(new FlatFileData(name, str));
      }
   }

   public void addDoubleArrayList(String name, ArrayList value, String delimiter) {
      if(!this.propertyExists(name)) {
         String str = "";

         for(int i = 0; i < value.size() - 1; ++i) {
            str = str + value.get(i) + delimiter;
         }

         if(value.size() > 0) {
            str = str + value.get(value.size() - 1);
         }

         this.Data.add(new FlatFileData(name, str));
      }
   }

   public void addFloatArrayList(String name, ArrayList value, String delimiter) {
      if(!this.propertyExists(name)) {
         String str = "";

         for(int i = 0; i < value.size() - 1; ++i) {
            str = str + value.get(i) + delimiter;
         }

         if(value.size() > 0) {
            str = str + value.get(value.size() - 1);
         }

         this.Data.add(new FlatFileData(name, str));
      }
   }

   public void addStringArrayList(String name, ArrayList value, String delimiter) {
      if(!this.propertyExists(name)) {
         String str = "";

         for(int i = 0; i < value.size() - 1; ++i) {
            str = str + (String)value.get(i) + delimiter;
         }

         if(value.size() > 0) {
            str = str + (String)value.get(value.size() - 1);
         }

         this.Data.add(new FlatFileData(name, str));
      }
   }

   public boolean propertyExists(String name) {
      for(int i = 0; i < this.Data.size(); ++i) {
         if(((FlatFileData)this.Data.get(i)).getName().equalsIgnoreCase(name)) {
            return true;
         }
      }

      return false;
   }

   private int getPropertyID(String name) {
      for(int i = 0; i < this.Data.size(); ++i) {
         if(((FlatFileData)this.Data.get(i)).getName().equalsIgnoreCase(name)) {
            return i;
         }
      }

      return -1;
   }

   public boolean setBoolean(String name, boolean value) {
      if(!this.propertyExists(name)) {
         this.addBoolean(name, value);
      }

      ((FlatFileData)this.Data.get(this.getPropertyID(name))).setValue("" + value);
      return value;
   }

   public int setInt(String name, int value) {
      if(!this.propertyExists(name)) {
         this.addInt(name, value);
      }

      ((FlatFileData)this.Data.get(this.getPropertyID(name))).setValue("" + value);
      return value;
   }

   public double setDouble(String name, double value) {
      if(!this.propertyExists(name)) {
         this.addDouble(name, value);
      }

      ((FlatFileData)this.Data.get(this.getPropertyID(name))).setValue("" + value);
      return value;
   }

   public float setFloat(String name, float value) {
      if(!this.propertyExists(name)) {
         this.addFloat(name, value);
      }

      ((FlatFileData)this.Data.get(this.getPropertyID(name))).setValue("" + value);
      return value;
   }

   public String setString(String name, String value) {
      if(!this.propertyExists(name)) {
         this.addString(name, value);
      }

      ((FlatFileData)this.Data.get(this.getPropertyID(name))).setValue(value);
      return value;
   }

   public ArrayList setBooleanArrayList(String name, ArrayList value, String delimiter) {
      String str = "";

      for(int i = 0; i < value.size() - 1; ++i) {
         str = str + value.get(i) + delimiter;
      }

      if(value.size() > 0) {
         str = str + value.get(value.size() - 1);
      }

      if(!this.propertyExists(name)) {
         this.addBooleanArrayList(name, value, delimiter);
      }

      ((FlatFileData)this.Data.get(this.getPropertyID(name))).setValue(str);
      return value;
   }

   public ArrayList setIntArrayList(String name, ArrayList value, String delimiter) {
      String str = "";

      for(int i = 0; i < value.size() - 1; ++i) {
         str = str + value.get(i) + delimiter;
      }

      if(value.size() > 0) {
         str = str + value.get(value.size() - 1);
      }

      if(!this.propertyExists(name)) {
         this.addIntArrayList(name, value, delimiter);
      }

      ((FlatFileData)this.Data.get(this.getPropertyID(name))).setValue(str);
      return value;
   }

   public ArrayList setDoubleArrayList(String name, ArrayList value, String delimiter) {
      String str = "";

      for(int i = 0; i < value.size() - 1; ++i) {
         str = str + value.get(i) + delimiter;
      }

      if(value.size() > 0) {
         str = str + value.get(value.size() - 1);
      }

      if(!this.propertyExists(name)) {
         this.addDoubleArrayList(name, value, delimiter);
      }

      ((FlatFileData)this.Data.get(this.getPropertyID(name))).setValue(str);
      return value;
   }

   public ArrayList setFloatArrayList(String name, ArrayList value, String delimiter) {
      String str = "";

      for(int i = 0; i < value.size() - 1; ++i) {
         str = str + value.get(i) + delimiter;
      }

      if(value.size() > 0) {
         str = str + value.get(value.size() - 1);
      }

      if(!this.propertyExists(name)) {
         this.addFloatArrayList(name, value, delimiter);
      }

      ((FlatFileData)this.Data.get(this.getPropertyID(name))).setValue(str);
      return value;
   }

   public ArrayList setStringArrayList(String name, ArrayList value, String delimiter) {
      String str = "";

      for(int i = 0; i < value.size() - 1; ++i) {
         str = str + (String)value.get(i) + delimiter;
      }

      if(value.size() > 0) {
         str = str + (String)value.get(value.size() - 1);
      }

      if(!this.propertyExists(name)) {
         this.addStringArrayList(name, value, delimiter);
      }

      ((FlatFileData)this.Data.get(this.getPropertyID(name))).setValue(str);
      return value;
   }

   public boolean getBoolean(String name, boolean standard) {
      return !this.propertyExists(name)?standard:Boolean.valueOf(((FlatFileData)this.Data.get(this.getPropertyID(name))).getValue()).booleanValue();
   }

   public int getInt(String name, int standard) {
      return !this.propertyExists(name)?standard:Integer.valueOf(((FlatFileData)this.Data.get(this.getPropertyID(name))).getValue()).intValue();
   }

   public double getDouble(String name, double standard) {
      return !this.propertyExists(name)?standard:Double.valueOf(((FlatFileData)this.Data.get(this.getPropertyID(name))).getValue()).doubleValue();
   }

   public float getFloat(String name, float standard) {
      return !this.propertyExists(name)?standard:Float.valueOf(((FlatFileData)this.Data.get(this.getPropertyID(name))).getValue()).floatValue();
   }

   public String getString(String name, String standard) {
      return !this.propertyExists(name)?standard:((FlatFileData)this.Data.get(this.getPropertyID(name))).getValue();
   }

   public ArrayList getBooleanArrayList(String name, String delimiter) {
      ArrayList list = new ArrayList();
      if(!this.propertyExists(name)) {
         this.addBooleanArrayList(name, list, delimiter);
         return list;
      } else {
         String[] data = ((FlatFileData)this.Data.get(this.getPropertyID(name))).getValue().trim().split(delimiter);

         for(int i = 0; i < data.length; ++i) {
            data[i] = data[i].trim();
            list.add(Boolean.valueOf(data[i]));
         }

         return list;
      }
   }

   public ArrayList getIntArrayList(String name, String delimiter) {
      ArrayList list = new ArrayList();
      if(!this.propertyExists(name)) {
         this.addIntArrayList(name, list, delimiter);
         return list;
      } else {
         String[] data = ((FlatFileData)this.Data.get(this.getPropertyID(name))).getValue().trim().split(delimiter);

         for(int i = 0; i < data.length; ++i) {
            data[i] = data[i].trim();
            list.add(Integer.valueOf(data[i]));
         }

         return list;
      }
   }

   public ArrayList getDoubleArrayList(String name, String delimiter) {
      ArrayList list = new ArrayList();
      if(!this.propertyExists(name)) {
         this.addDoubleArrayList(name, list, delimiter);
         return list;
      } else {
         String[] data = ((FlatFileData)this.Data.get(this.getPropertyID(name))).getValue().trim().split(delimiter);

         for(int i = 0; i < data.length; ++i) {
            data[i] = data[i].trim();
            list.add(Double.valueOf(data[i]));
         }

         return list;
      }
   }

   public ArrayList getFloatArrayList(String name, String delimiter) {
      ArrayList list = new ArrayList();
      if(!this.propertyExists(name)) {
         this.addFloatArrayList(name, list, delimiter);
         return list;
      } else {
         String[] data = ((FlatFileData)this.Data.get(this.getPropertyID(name))).getValue().trim().split(delimiter);

         for(int i = 0; i < data.length; ++i) {
            data[i] = data[i].trim();
            list.add(Float.valueOf(data[i]));
         }

         return list;
      }
   }

   public ArrayList getStringArrayList(String name, String delimiter) {
      ArrayList list = new ArrayList();
      if(!this.propertyExists(name)) {
         this.addStringArrayList(name, list, delimiter);
         return list;
      } else {
         String[] data = ((FlatFileData)this.Data.get(this.getPropertyID(name))).getValue().trim().split(delimiter);

         for(int i = 0; i < data.length; ++i) {
            list.add(data[i]);
         }

         return list;
      }
   }
}
