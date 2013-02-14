package com.bukkit.gemo.FalseBook.World;

import com.bukkit.gemo.FalseBook.Settings.WorldSettings;
import com.bukkit.gemo.FalseBook.World.EventList;
import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class FBWorld extends EventList {

   private String worldName;
   private World bukkitWorld;
   private WorldSettings settings;


   public FBWorld(String worldName) {
      this.worldName = worldName;
      this.bukkitWorld = Bukkit.getWorld(worldName);
      this.settings = new WorldSettings();
   }

   public World getBukkitWorld() {
      return this.bukkitWorld;
   }

   public boolean hasBukkitWorld() {
      return this.getBukkitWorld() != null;
   }

   public String getWorldName() {
      return this.worldName;
   }

   public boolean saveSettings(File dataFolder, String fileName) {
      return this.settings.save(dataFolder, fileName);
   }

   public WorldSettings getSettings() {
      return this.settings;
   }
}
