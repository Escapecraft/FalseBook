package com.bukkit.gemo.FalseBook.IC.ICs.worldedit;

import com.bukkit.gemo.utils.BlockUtils;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import org.bukkit.Location;

public class TeleporterList {

   private HashMap ICList = new HashMap();


   public int getSize() {
      return this.ICList.size();
   }

   public HashMap getAll() {
      return this.ICList;
   }

   public boolean TeleporterExistsByName(String name) {
      return this.ICList.containsKey(name);
   }

   public boolean removeTeleporterByName(String name) {
      return name != null?this.ICList.remove(name) != null:false;
   }

   public boolean TeleporterExistsByLocation(String location) {
      Iterator var3 = this.ICList.entrySet().iterator();

      while(var3.hasNext()) {
         Entry entry = (Entry)var3.next();
         if(((String)entry.getValue()).equalsIgnoreCase(location)) {
            return true;
         }
      }

      return false;
   }

   public boolean removeTeleporterByLocation(String location) {
      String name = null;
      Iterator var4 = this.ICList.entrySet().iterator();

      while(var4.hasNext()) {
         Entry entry = (Entry)var4.next();
         if(((String)entry.getValue()).equalsIgnoreCase(location)) {
            name = (String)entry.getKey();
            break;
         }
      }

      return this.removeTeleporterByName(name);
   }

   public Location getLocation(String name) {
      return BlockUtils.LocationFromString((String)this.ICList.get(name));
   }

   public String getLocationString(String name) {
      return (String)this.ICList.get(name);
   }

   public void addTeleporter(String name, Location location) {
      this.ICList.put(name, BlockUtils.LocationToString(location));
   }
}
