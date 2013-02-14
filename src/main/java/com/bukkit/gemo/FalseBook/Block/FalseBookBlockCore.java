package com.bukkit.gemo.FalseBook.Block;

import com.bukkit.gemo.FalseBook.Block.Config.ConfigHandler;
import com.bukkit.gemo.FalseBook.Block.Handler.WorldHandlerBlock;
import com.bukkit.gemo.FalseBook.Block.Listeners.FalseBookBlockListener;
import com.bukkit.gemo.FalseBook.Block.Mechanics.BlockMechanicHandler;
import com.bukkit.gemo.FalseBook.Block.commands.cmdDelFArea;
import com.bukkit.gemo.FalseBook.Block.commands.cmdFArea;
import com.bukkit.gemo.FalseBook.Block.commands.cmdFAreaAllow;
import com.bukkit.gemo.FalseBook.Block.commands.cmdFAreaToggle;
import com.bukkit.gemo.FalseBook.Block.commands.cmdFBBlock;
import com.bukkit.gemo.FalseBook.Block.commands.cmdListFArea;
import com.bukkit.gemo.FalseBook.Block.commands.cmdReload;
import com.bukkit.gemo.FalseBook.Block.commands.cmdReloadSettings;
import com.bukkit.gemo.FalseBook.Core.FalseBookCore;
import com.bukkit.gemo.commands.Command;
import com.bukkit.gemo.commands.CommandList;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class FalseBookBlockCore extends JavaPlugin {

   private FalseBookCore core = null;
   private String pluginName;
   private FalseBookBlockListener listener;
   private CommandList commandList;
   private WorldHandlerBlock worldHandler = null;
   private BlockMechanicHandler mechHandler = null;
   private static FalseBookBlockCore instance = null;


   public static void printInConsole(String str) {
      System.out.println("[ FalseBook Block ] " + str);
   }

   public void onDisable() {
      if(this.core != null) {
         this.mechHandler.onUnload();
         printInConsole(this.pluginName + " disabled");
      }

   }

   public void onEnable() {
      if(!this.searchCore()) {
         printInConsole(">>>>> FalseBookCore.jar not found! FalseBookBlock.jar is being disabled!");
      } else {
         instance = this;
         this.loadVersion();
         new ConfigHandler();
         ConfigHandler.loadWorldSettings();
         this.initCommands();
         this.worldHandler = new WorldHandlerBlock();
         this.loadWorldSettings();
         this.listener = new FalseBookBlockListener(this.worldHandler);
         this.getServer().getPluginManager().registerEvents(this.listener, this);
         this.mechHandler = new BlockMechanicHandler();
         this.mechHandler.registerBlockMechanics(this);
         this.mechHandler.onLoad();
         printInConsole(this.pluginName + " enabled");
      }
   }

   private void initCommands() {
      Command[] commands = new Command[]{new cmdFArea("[FB-Block]", "/farea", "<Areaname>", ""), new cmdDelFArea("[FB-Block]", "/delfarea", "<Areaname>", ""), new cmdListFArea("[FB-Block]", "/listfarea", "", ""), new cmdFAreaAllow("[FB-Block]", "/fareaallow", "<ItemList>", ""), new cmdFAreaAllow("[FB-Block]", "/farealistallow", "<Areaname>", ""), new cmdFAreaToggle("[FB-Block]", "/fareatoggle", "<Autosave|Interact|Protect> <Areaname>", ""), new cmdFBBlock("[FB-Block]", "/fbblock", "", "", false, new Command[]{new cmdReload("[FB-Block]", "reload", "", ""), new cmdReloadSettings("[FB-Block]", "reloadsettings", "", "")})};
      this.commandList = new CommandList("[FB-Chat]", commands);
   }

   public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
      if(this.searchCore()) {
         this.commandList.handleCommand(sender, label, args);
      }

      return true;
   }

   private void loadVersion() {
      PluginDescriptionFile pdfFile = this.getDescription();
      this.pluginName = pdfFile.getName() + " v" + pdfFile.getVersion() + " by GeMo";
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

   public BlockMechanicHandler getMechanicHandler() {
      return this.mechHandler;
   }

   public static FalseBookBlockCore getInstance() {
      return instance;
   }

   public void loadWorldSettings() {
      for(int i = 0; i < Bukkit.getWorlds().size(); ++i) {
         this.getOrCreateSettings(((World)Bukkit.getWorlds().get(i)).getName());
      }

   }

   public void getOrCreateSettings(String worldName) {
      this.worldHandler.getWorld(worldName);
   }
}
