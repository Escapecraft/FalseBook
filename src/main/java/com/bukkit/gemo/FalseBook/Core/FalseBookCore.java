package com.bukkit.gemo.FalseBook.Core;

import com.bukkit.gemo.FalseBook.Core.FalseBookCorePlayerListener;
import com.bukkit.gemo.FalseBook.Core.FalseBookCoreWorldListener;
import com.bukkit.gemo.utils.UtilPermissions;
import com.bukkit.gemo.utils.Permissions.System.Permission;
import java.io.File;
import org.bukkit.Server;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class FalseBookCore extends JavaPlugin {

   private static UtilPermissions Util;
   private static String pluginName;
   private static Server server;
   private static boolean usePermissionCaching = true;
   private static long permissionCacheTime = 15000L;
   private static FalseBookCoreWorldListener worldListener;
   private static FalseBookCorePlayerListener playerListener;


   public static void printInConsole(String str) {
      System.out.println("[ FalseBook Core ] " + str);
   }

   public void onDisable() {
      this.saveSettings();
      printInConsole(pluginName + " disabled!");
   }

   public void loadSettings() {
      try {
         File e = new File("plugins" + System.getProperty("file.separator") + "FalseBook" + System.getProperty("file.separator") + "FalseBookCore.yml");
         if(!e.exists()) {
            this.saveSettings();
            return;
         }

         YamlConfiguration config = new YamlConfiguration();
         config.load(e);
         usePermissionCaching = config.getBoolean("usePermissionCaching", true);
         permissionCacheTime = config.getLong("permissionCacheTime", permissionCacheTime);
         Permission.setPermissionCacheTime(permissionCacheTime);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public void saveSettings() {
      try {
         File e = new File("plugins" + System.getProperty("file.separator") + "FalseBook" + System.getProperty("file.separator") + "FalseBookCore.yml");
         if(e.exists()) {
            e.delete();
         }

         YamlConfiguration config = new YamlConfiguration();
         config.set("usePermissionCaching", Boolean.valueOf(usePermissionCaching));
         config.set("permissionCacheTime", Long.valueOf(permissionCacheTime));
         config.save(e);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public void onEnable() {
      this.loadVersion();
      server = this.getServer();
      Util = new UtilPermissions(this.getServer());
      worldListener = new FalseBookCoreWorldListener(this);
      playerListener = new FalseBookCorePlayerListener();
      this.getServer().getPluginManager().registerEvents(worldListener, this);
      this.getServer().getPluginManager().registerEvents(playerListener, this);
      this.loadSettings();
      printInConsole(pluginName + " enabled!");
   }

   private void loadVersion() {
      PluginDescriptionFile pdfFile = this.getDescription();
      pluginName = pdfFile.getName() + " v" + pdfFile.getVersion() + " by GeMo";
   }

   public static UtilPermissions getUtil() {
      return Util;
   }

   public static String getPluginName() {
      return pluginName;
   }

   public static void setPluginName(String pluginName) {
      pluginName = pluginName;
   }

   public static Server getMCServer() {
      return server;
   }

   public static void setMCServer(Server server) {
      server = server;
   }

   public static Player getPlayer(String name) {
      Player[] var4;
      int var3 = (var4 = server.getOnlinePlayers()).length;

      for(int var2 = 0; var2 < var3; ++var2) {
         Player player = var4[var2];
         if(player.getName().equalsIgnoreCase(name)) {
            return player;
         }
      }

      return null;
   }

   public static boolean usePermissionCaching() {
      return usePermissionCaching;
   }

   public static long getPermissionCacheTime() {
      return permissionCacheTime;
   }
}
