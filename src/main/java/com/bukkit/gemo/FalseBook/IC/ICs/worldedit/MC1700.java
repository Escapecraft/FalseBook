package com.bukkit.gemo.FalseBook.IC.ICs.worldedit;

import com.bukkit.gemo.FalseBook.IC.FalseBookICCore;
import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.BaseIC;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.InputState;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import com.bukkit.gemo.FalseBook.IC.ICs.worldedit.TeleporterList;
import com.bukkit.gemo.utils.BlockUtils;
import com.bukkit.gemo.utils.ChatUtils;
import com.bukkit.gemo.utils.LWCProtection;
import com.bukkit.gemo.utils.SignUtils;
import com.bukkit.gemo.utils.UtilPermissions;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;

public class MC1700 extends BaseIC {

   private int maxByPlayer = 5;
   private HashMap playerICList = new HashMap();
   private Random random = new Random();


   public MC1700() {
      this.ICName = "TELEPORTER";
      this.ICNumber = "[MC1700]";
      this.setICGroup(ICGroup.WORLDEDIT);
      this.chipState = new BaseChip(true, false, false, "Clock", "", "");
      this.chipState.setOutputs("Output = Input", "", "");
      this.chipState.setLines("Name of this station", "[ Name of the target station ]");
      this.ICDescription = "The MC1700 will teleport you to another MC1700 when the input (the \"clock\") goes from low to high and you are standing on the ic. The stationnames are stored for each player seperatly (means: 2 players can have the same stationnames).<br />It will automaticly search for the teleportplatform above the ic.<br/><br/><br /><b>LWC:</b><br/>If you secure the IC-Sign with LWC, only users with permissions can teleport with this IC (using /cmodify).<br /><br /><b>Extra Permissionnodes for this IC:</b><br />- falsebook.ic.mc1700.ignoreMaximum : players with this node will ignore the maximum number of teleporters (default: 5)<br/>- falsebook.ic.mc1700.destroyall : players with this node are allowed to destroy all teleporter-ICs (even from other players)";
   }

   public void onImport() {
      this.loadConfig();
      this.loadICs();
   }

   private int getListSize() {
      int count = 0;

      Entry entry;
      for(Iterator var3 = this.playerICList.entrySet().iterator(); var3.hasNext(); count += ((TeleporterList)entry.getValue()).getSize()) {
         entry = (Entry)var3.next();
      }

      return count;
   }

   private int getListSizeForPlayer(String playerName) {
      return this.playerICList.containsKey(playerName)?((TeleporterList)this.playerICList.get(playerName)).getSize():0;
   }

   private void loadConfig() {
      try {
         File e = new File("plugins" + System.getProperty("file.separator") + "FalseBook" + System.getProperty("file.separator") + "ICPlugins" + System.getProperty("file.separator") + "configs");
         e.mkdir();
         e = new File(e, "MC1700.yml");
         if(!e.exists()) {
            this.saveConfig();
            return;
         }

         YamlConfiguration config = new YamlConfiguration();
         config.load(e);
         this.maxByPlayer = config.getInt("settings.maximumICsPerPlayer");
      } catch (Exception var3) {
         FalseBookICCore.printInConsole("Config for Transport-IC [MC1700] could not be loaded!");
         var3.printStackTrace();
         this.saveICs();
      }

   }

   private void saveConfig() {
      try {
         File e = new File("plugins" + System.getProperty("file.separator") + "FalseBook" + System.getProperty("file.separator") + "ICPlugins" + System.getProperty("file.separator") + "configs");
         e.mkdir();
         e = new File(e, "MC1700.yml");
         if(e.exists()) {
            e.delete();
         }

         YamlConfiguration config = new YamlConfiguration();
         config.set("settings.maximumICsPerPlayer", Integer.valueOf(this.maxByPlayer));
         config.save(e);
      } catch (Exception var3) {
         FalseBookICCore.printInConsole("Config for Transport-IC [MC1700] could not be saved!");
         var3.printStackTrace();
      }

   }

   private void loadICs() {
      try {
         File e = new File("plugins" + System.getProperty("file.separator") + "FalseBook" + System.getProperty("file.separator") + "ICPlugins" + System.getProperty("file.separator") + "data");
         e.mkdir();
         e = new File(e, "MC1700.yml");
         if(!e.exists()) {
            return;
         }

         YamlConfiguration config = new YamlConfiguration();
         config.load(e);
         int playercount = config.getInt("playercount");

         for(int i = 1; i <= playercount; ++i) {
            String playerName = config.getString("player." + i + ".name");
            int iccount = config.getInt("player." + i + ".count");

            for(int j = 1; j <= iccount; ++j) {
               Location location = BlockUtils.LocationFromString(config.getString("player." + i + ".ic." + j));
               if(location != null) {
                  location.getChunk().load(true);
                  if(location.getBlock().getTypeId() == Material.WALL_SIGN.getId()) {
                     Sign sign = (Sign)location.getBlock().getState();
                     if(sign.getLine(2).length() >= 1) {
                        this.addTeleporter(playerName, sign.getLine(2), location);
                     }
                  }
               }
            }
         }

         FalseBookICCore.printInConsole(this.getListSize() + " Transport-ICs [MC1700] loaded.");
      } catch (Exception var10) {
         FalseBookICCore.printInConsole("Transport-ICs [MC1700] could not be loaded!");
         var10.printStackTrace();
      }

   }

   private void saveICs() {
      try {
         File e = new File("plugins" + System.getProperty("file.separator") + "FalseBook" + System.getProperty("file.separator") + "ICPlugins" + System.getProperty("file.separator") + "data");
         e.mkdir();
         e = new File(e, "MC1700.yml");
         if(e.exists()) {
            e.delete();
         }

         YamlConfiguration config = new YamlConfiguration();
         int playercount = this.playerICList.size();
         config.set("playercount", Integer.valueOf(playercount));
         int i = 1;
         Iterator var6 = this.playerICList.entrySet().iterator();

         while(var6.hasNext()) {
            Entry entry = (Entry)var6.next();
            int ICCount = ((TeleporterList)entry.getValue()).getSize();
            if(ICCount >= 1) {
               config.set("player." + i + ".count", Integer.valueOf(ICCount));
               config.set("player." + i + ".name", entry.getKey());
               int j = 1;

               for(Iterator var10 = ((TeleporterList)entry.getValue()).getAll().entrySet().iterator(); var10.hasNext(); ++j) {
                  Entry tpEntry = (Entry)var10.next();
                  config.set("player." + i + ".ic." + j, tpEntry.getValue());
               }

               ++i;
            }
         }

         config.save(e);
      } catch (Exception var11) {
         FalseBookICCore.printInConsole("Transport-ICs [MC1700] could not be saved!");
         var11.printStackTrace();
      }

   }

   private String getStationOwner(Location location) {
      Iterator var3 = this.playerICList.entrySet().iterator();

      while(var3.hasNext()) {
         Entry entry = (Entry)var3.next();
         if(((TeleporterList)entry.getValue()).TeleporterExistsByLocation(BlockUtils.LocationToString(location))) {
            return (String)entry.getKey();
         }
      }

      return null;
   }

   private Sign getTargetStation(String playername, String stationName) {
      if(!this.playerICList.containsKey(playername)) {
         return null;
      } else if(!((TeleporterList)this.playerICList.get(playername)).TeleporterExistsByName(stationName)) {
         return null;
      } else {
         Location location = ((TeleporterList)this.playerICList.get(playername)).getLocation(stationName);
         return location.getBlock().getTypeId() != Material.WALL_SIGN.getId()?null:(Sign)location.getBlock().getState();
      }
   }

   private void addTeleporter(String playerName, String stationName, Location location) {
      TeleporterList tList = (TeleporterList)this.playerICList.get(playerName);
      if(tList == null) {
         tList = new TeleporterList();
         this.playerICList.put(playerName, tList);
      }

      if(!tList.TeleporterExistsByName(stationName)) {
         tList.addTeleporter(stationName, location);
      }
   }

   private void removeTeleporter(Location location) {
      boolean removed = false;
      String playerName = null;
      String locString = BlockUtils.LocationToString(location);
      Iterator var6 = this.playerICList.entrySet().iterator();

      while(var6.hasNext()) {
         Entry entry = (Entry)var6.next();
         if(((TeleporterList)entry.getValue()).TeleporterExistsByLocation(locString)) {
            ((TeleporterList)entry.getValue()).removeTeleporterByLocation(locString);
            removed = true;
            playerName = (String)entry.getKey();
            break;
         }
      }

      if(removed) {
         if(((TeleporterList)this.playerICList.get(playerName)).getSize() < 1) {
            this.playerICList.remove(playerName);
         }

         this.saveICs();
      }

   }

   private Location getTargetLocation(HashSet targetBlocks) {
      int randomPos = this.random.nextInt(targetBlocks.size());
      Iterator iterator = targetBlocks.iterator();

      for(int i = 0; iterator.hasNext(); ++i) {
         if(i == randomPos) {
            return ((Block)iterator.next()).getRelative(BlockFace.UP).getLocation().clone();
         }

         iterator.next();
      }

      return null;
   }

   private ArrayList getPlayersInPosition(HashSet blockList) {
      ArrayList playerList = new ArrayList();
      Player[] pList = Bukkit.getOnlinePlayers();
      Player[] var8 = pList;
      int var7 = pList.length;

      for(int var6 = 0; var6 < var7; ++var6) {
         Player player = var8[var6];
         Iterator iterator = blockList.iterator();

         while(iterator.hasNext()) {
            Block thisBlock = (Block)iterator.next();
            if(BlockUtils.LocationEquals(player.getLocation(), thisBlock.getRelative(BlockFace.UP).getLocation())) {
               playerList.add(player);
            }
         }
      }

      return playerList;
   }

   private HashSet getBaseBlocks(Sign signBlock) {
      Block topBlock = getICBlock(signBlock).getBlock().getRelative(BlockFace.UP);
      HashSet transportBlockList = new HashSet();
      ArrayList toCheckList = new ArrayList();
      HashSet checkedBlocksList = new HashSet();
      toCheckList.add(topBlock);
      if(topBlock.getTypeId() == Material.AIR.getId()) {
         return transportBlockList;
      } else {
         if(BlockUtils.canPassThrough(topBlock.getRelative(BlockFace.UP).getTypeId()) && BlockUtils.canPassThrough(topBlock.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getTypeId())) {
            transportBlockList.add(topBlock);
         }

         boolean MAXBLOCKS = true;
         boolean MAXSEARCH = true;
         int k = 0;

         for(int j = 0; j < toCheckList.size(); ++j) {
            Block block = (Block)toCheckList.get(j);
            ArrayList neighbours = BlockUtils.getDirectNeighbours(block, false);
            Iterator var13 = neighbours.iterator();

            while(var13.hasNext()) {
               Block nBlock = (Block)var13.next();
               ++k;
               if(k >= 1000) {
                  break;
               }

               if(!checkedBlocksList.contains(nBlock) && nBlock.getTypeId() == topBlock.getTypeId() && nBlock.getData() == topBlock.getData() && BlockUtils.canPassThrough(nBlock.getRelative(BlockFace.UP).getTypeId()) && BlockUtils.canPassThrough(nBlock.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getTypeId())) {
                  transportBlockList.add(nBlock);
                  toCheckList.add(nBlock);
                  if(transportBlockList.size() >= 40) {
                     break;
                  }
               }
            }

            if(transportBlockList.size() >= 40 || k >= 1000) {
               break;
            }

            checkedBlocksList.add(block);
         }

         return transportBlockList;
      }
   }

   public void checkCreation(SignChangeEvent event) {
      if(event.getLine(2).length() < 1) {
         SignUtils.cancelSignCreation(event, "Please enter a name of this teleport-IC in line 3.");
      } else if(this.getTargetStation(event.getPlayer().getName(), event.getLine(2)) != null) {
         SignUtils.cancelSignCreation(event, "A [MC1700] with this name already exists. Please choose another name.");
      } else if(this.maxByPlayer >= 0 && this.getListSizeForPlayer(event.getPlayer().getName()) >= this.maxByPlayer && !UtilPermissions.playerCanUseCommand(event.getPlayer(), "falsebook.ic.mc1700.ignoreMaximum")) {
         SignUtils.cancelSignCreation(event, "You have reached the maximumnumber of Teleport-ICs ( " + this.maxByPlayer + " ).");
      } else {
         this.addTeleporter(event.getPlayer().getName(), event.getLine(2), event.getBlock().getLocation());
         this.saveICs();
      }
   }

   public void onBreakByExplosion(Sign signBlock) {
      this.removeTeleporter(signBlock.getBlock().getLocation());
   }

   public boolean onBreakByPlayer(Player player, Sign signBlock) {
      String owner = this.getStationOwner(signBlock.getBlock().getLocation());
      if(owner != null) {
         if(!owner.equalsIgnoreCase(player.getName()) && !UtilPermissions.playerCanUseCommand(player, "falsebook.ic.mc1700.destroyall")) {
            return false;
         } else {
            this.removeTeleporter(signBlock.getBlock().getLocation());
            return true;
         }
      } else {
         this.removeTeleporter(signBlock.getBlock().getLocation());
         return true;
      }
   }

   public void Execute(Sign signBlock, InputState currentInputs, InputState previousInputs) {
      if(currentInputs.isInputOneHigh() && previousInputs.isInputOneLow()) {
         if(signBlock.getLine(2).length() < 1) {
            return;
         }

         if(signBlock.getLine(3).length() < 1) {
            return;
         }

         String stationOwner = this.getStationOwner(signBlock.getBlock().getLocation());
         if(stationOwner == null) {
            return;
         }

         Sign targetStation = this.getTargetStation(stationOwner, signBlock.getLine(3));
         if(targetStation == null) {
            return;
         }

         HashSet transportBlockList = this.getBaseBlocks(signBlock);
         if(transportBlockList.size() < 1) {
            return;
         }

         ArrayList playerList = this.getPlayersInPosition(transportBlockList);
         if(playerList.size() < 1) {
            return;
         }

         if(!targetStation.getLine(1).equalsIgnoreCase("[MC1700]")) {
            return;
         }

         if(!targetStation.getLine(2).equalsIgnoreCase(signBlock.getLine(3))) {
            return;
         }

         HashSet transportBlockListTarget = this.getBaseBlocks(targetStation);
         if(transportBlockListTarget.size() < 1) {
            return;
         }

         Iterator var10 = playerList.iterator();

         while(var10.hasNext()) {
            Player player = (Player)var10.next();
            if((player.isOnline() || !player.isDead()) && LWCProtection.canAccessWithCModify(player, signBlock.getBlock())) {
               Location targetLoc = this.getTargetLocation(transportBlockListTarget);
               if(targetLoc == null) {
                  ChatUtils.printError(player, "[FB-IC]", "[MC1700]: Something went wrong... ");
               } else {
                  targetLoc.setYaw(player.getLocation().getYaw());
                  targetLoc.setPitch(player.getLocation().getPitch());
                  targetLoc.setX(targetLoc.getX() + 0.5D);
                  targetLoc.setZ(targetLoc.getZ() + 0.5D);
                  player.teleport(targetLoc);
                  ChatUtils.printInfo(player, "[FB-IC]", ChatColor.GREEN, "You were teleported to \'" + signBlock.getLine(3) + "\'!");
               }
            }
         }

         this.switchLever(Lever.BACK, signBlock, true);
      } else {
         this.switchLever(Lever.BACK, signBlock, false);
      }

   }

   public void onRightClick(Player player, Sign signBlock) {
      String owner = this.getStationOwner(signBlock.getBlock().getLocation());
      if(owner != null) {
         ChatUtils.printInfo(player, "[FB-IC]", ChatColor.GRAY, "Creator: " + ChatColor.WHITE + owner);
         String secText = "";
         if(LWCProtection.canAccessWithCModify(player, signBlock.getBlock())) {
            secText = ChatColor.GREEN + "allowed" + ChatColor.GRAY;
         } else {
            secText = ChatColor.RED + "not allowed" + ChatColor.GRAY;
         }

         ChatUtils.printInfo(player, "[FB-IC]", ChatColor.GRAY, "You are " + secText + " to use this teleporter!");
      }
   }
}
