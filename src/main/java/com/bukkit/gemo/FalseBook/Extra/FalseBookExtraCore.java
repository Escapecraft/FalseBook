package com.bukkit.gemo.FalseBook.Extra;

import com.bukkit.gemo.FalseBook.Core.FalseBookCore;
import com.bukkit.gemo.FalseBook.Extra.FalseBookExtraBlockListener;
import com.bukkit.gemo.FalseBook.Extra.Handler.WorldHandlerExtra;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class FalseBookExtraCore extends JavaPlugin {

   private FalseBookCore core;
   private static String pluginName;
   public static Server server;
   private static FalseBookExtraCore instance;
   private static WorldHandlerExtra worldHandler;
   private FalseBookExtraBlockListener blockListener;


   public static void printInConsole(String str) {
      System.out.println("[ FalseBook Extra ] " + str);
   }

   public void onDisable() {
      if(this.searchCore()) {
         this.blockListener.saveProtectedBlocks();
         printInConsole(pluginName + " disabled");
      }

   }

   private boolean searchCore() {
      PluginManager pm = this.getServer().getPluginManager();
      if(pm.getPlugin("FalseBookCore") != null) {
         this.core = (FalseBookCore)pm.getPlugin("FalseBookCore");
         PluginDescriptionFile thisPDFFile = this.getDescription();
         PluginDescriptionFile corePDFFile = this.core.getDescription();
         if(!thisPDFFile.getVersion().equalsIgnoreCase(corePDFFile.getVersion())) {
            printInConsole("WRONG CORE-VERSION!");
            return false;
         } else {
            return true;
         }
      } else {
         return false;
      }
   }

   public void onEnable() {
      if(!this.searchCore()) {
         printInConsole(">>>>> FalseBookCore.jar not found! FalseBookExtra.jar is being disabled!");
      } else {
         this.loadVersion();
         server = this.getServer();
         instance = this;
         worldHandler = new WorldHandlerExtra();
         this.blockListener = new FalseBookExtraBlockListener(worldHandler);
         this.getServer().getPluginManager().registerEvents(this.blockListener, this);
         this.loadWorldSettings();
         printInConsole(pluginName + " enabled");
      }
   }

   public FalseBookExtraBlockListener getBlockListener() {
      return this.blockListener;
   }

   public void loadVersion() {
      PluginDescriptionFile pdfFile = this.getDescription();
      pluginName = pdfFile.getName() + " v" + pdfFile.getVersion() + " by GeMo";
   }

   public void loadWorldSettings() {
      for(int i = 0; i < server.getWorlds().size(); ++i) {
         getOrCreateSettings(((World)server.getWorlds().get(i)).getName());
      }

   }

   public static void getOrCreateSettings(String worldName) {
      worldHandler.getWorld(worldName);
   }

   public FalseBookCore getCore() {
      return this.core;
   }

   public static FalseBookExtraCore getInstance() {
      return instance;
   }

   public static WorldHandlerExtra getWorldHandler() {
      return worldHandler;
   }
}
