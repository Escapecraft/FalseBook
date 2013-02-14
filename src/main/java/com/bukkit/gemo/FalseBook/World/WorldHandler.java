package com.bukkit.gemo.FalseBook.World;

import com.bukkit.gemo.FalseBook.World.FBWorld;
import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.World;

public class WorldHandler {

   protected HashMap worldList = new HashMap();


   protected FBWorld addWorld(String worldName) {
      this.removeWorld(worldName);
      FBWorld thisWorld = new FBWorld(worldName);
      this.worldList.put(worldName, thisWorld);
      return thisWorld;
   }

   protected FBWorld removeWorld(String worldName) {
      return (FBWorld)this.worldList.remove(worldName);
   }

   public boolean hasWorld(String worldName) {
      return this.worldList.containsKey(worldName);
   }

   public boolean hasBukkitWorld(String worldName) {
      return this.getWorld(worldName).hasBukkitWorld();
   }

   public World getBukkitWorld(String worldName) {
      return this.getWorld(worldName).getBukkitWorld();
   }

   public FBWorld getWorld(String worldName) {
      return this.hasWorld(worldName)?(FBWorld)this.worldList.get(worldName):this.addWorld(worldName);
   }

   public ArrayList getAllWorlds() {
      ArrayList list = new ArrayList();
      list.addAll(this.worldList.values());
      return list;
   }
}
