package com.bukkit.gemo.FalseBook.Extra.World;

import com.bukkit.gemo.FalseBook.Extra.FalseBookExtraCore;
import com.bukkit.gemo.FalseBook.Extra.World.EnumSettings;
import com.bukkit.gemo.FalseBook.World.FBWorld;
import com.bukkit.gemo.utils.BlockUtils;
import com.bukkit.gemo.utils.ChatUtils;
import com.bukkit.gemo.utils.FlatFile;
import com.bukkit.gemo.utils.UtilPermissions;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class FBWorldExtra extends FBWorld implements Runnable {

   private ConcurrentHashMap protectedBlocks = new ConcurrentHashMap();
   private ArrayList netherQueue = new ArrayList();
   private ArrayList glowstoneQueue = new ArrayList();
   private ArrayList pumpkinQueue = new ArrayList();
   private int TaskID = -1;
   private static final String FileName = "FalseBookExtra.properties";
   private static final String protectedBlocksFileName = "Extra_ProtectedBlocks.dat";


   public FBWorldExtra(String worldName) {
      super(worldName);
      this.loadProtectedBlocks();
      this.TaskID = -1;
   }

   public boolean loadSettings() {
      File baseFolder = new File("plugins" + System.getProperty("file.separator") + "FalseBook");
      baseFolder.mkdirs();
      File worldFolder = new File("plugins" + System.getProperty("file.separator") + "FalseBook" + System.getProperty("file.separator") + this.getWorldName());
      worldFolder.mkdirs();
      File oldFile = new File(baseFolder, "FalseBookExtra.properties");
      if(oldFile.exists()) {
         oldFile.delete();
      }

      try {
         this.initSettings();
         FlatFile e = new FlatFile("plugins" + System.getProperty("file.separator") + "FalseBook" + System.getProperty("file.separator") + this.getWorldName() + System.getProperty("file.separator") + "FalseBookExtra.properties", false);
         if(e.readFile()) {
            this.getSettings().addBoolean(EnumSettings.TOGGLE_PUMPKINS.getName(), e.getBoolean(EnumSettings.TOGGLE_PUMPKINS.getName(), true));
            this.getSettings().addBoolean(EnumSettings.TOGGLE_NETHERRACK.getName(), e.getBoolean(EnumSettings.TOGGLE_NETHERRACK.getName(), true));
            this.getSettings().addBoolean(EnumSettings.TOGGLE_GLOWSTONE.getName(), e.getBoolean(EnumSettings.TOGGLE_GLOWSTONE.getName(), true));
            this.getSettings().addInteger(EnumSettings.GLOWSTONE_OFF_ID.getName(), e.getInt(EnumSettings.GLOWSTONE_OFF_ID.getName(), Material.SOUL_SAND.getId()));
            this.getSettings().addInteger(EnumSettings.GLOWSTONE_OFF_DATA.getName(), e.getInt(EnumSettings.GLOWSTONE_OFF_DATA.getName(), 0));
            this.getSettings().addBoolean(EnumSettings.PROTECT_BLOCKS.getName(), e.getBoolean(EnumSettings.PROTECT_BLOCKS.getName(), true));
            this.saveSettings(new File("plugins/FalseBook" + System.getProperty("file.separator") + this.getWorldName()), "FalseBookExtra.properties");
         } else {
            this.saveSettings(new File("plugins/FalseBook" + System.getProperty("file.separator") + this.getWorldName()), "FalseBookExtra.properties");
         }

         return true;
      } catch (Exception var7) {
         FalseBookExtraCore.printInConsole("Error while reading file: plugins/FalseBook/" + this.getWorldName() + "/" + "FalseBookExtra.properties");

         try {
            FlatFile e1 = new FlatFile("plugins" + System.getProperty("file.separator") + "FalseBook" + System.getProperty("file.separator") + this.getWorldName() + System.getProperty("file.separator") + "FalseBookExtra.properties", false);
            e1.regenerateFile("FalseBook" + System.getProperty("file.separator") + this.getWorldName() + System.getProperty("file.separator") + "FalseBookExtra.properties");
            this.saveSettings(new File("FalseBook" + System.getProperty("file.separator") + this.getWorldName()), this.getWorldName());
         } catch (Exception var6) {
            var6.printStackTrace();
         }

         FalseBookExtraCore.printInConsole("regenerated file: plugins/FalseBook/" + this.getWorldName() + "/" + "FalseBookExtra.properties");
         return true;
      }
   }

   private void initSettings() {
      this.getSettings().addBoolean(EnumSettings.TOGGLE_PUMPKINS.getName(), true);
      this.getSettings().addBoolean(EnumSettings.TOGGLE_NETHERRACK.getName(), true);
      this.getSettings().addBoolean(EnumSettings.TOGGLE_GLOWSTONE.getName(), true);
      this.getSettings().addInteger(EnumSettings.GLOWSTONE_OFF_ID.getName(), Material.SOUL_SAND.getId());
      this.getSettings().addInteger(EnumSettings.GLOWSTONE_OFF_DATA.getName(), 0);
      this.getSettings().addBoolean(EnumSettings.PROTECT_BLOCKS.getName(), true);
   }

   private void loadProtectedBlocks() {
      FalseBookExtraCore.getWorldHandler().convertOldBlocks();

      try {
         File e = new File("plugins" + System.getProperty("file.separator") + "FalseBook" + System.getProperty("file.separator") + this.getWorldName() + System.getProperty("file.separator") + "Extra_ProtectedBlocks.dat");
         if(!e.exists()) {
            return;
         }

         BufferedReader reader = new BufferedReader(new FileReader(e));
         String line = null;

         while((line = reader.readLine()) != null) {
            line = line.trim();
            Block block = BlockUtils.BlockFromLocationString(line);
            if(block != null) {
               this.protectedBlocks.put(line, block);
            }
         }

         reader.close();
         FalseBookExtraCore.printInConsole(this.protectedBlocks.size() + " protected blocks in \'" + this.getWorldName() + "\' loaded.");
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public void saveProtectedBlocks() {
      try {
         File e = new File("plugins" + System.getProperty("file.separator") + "FalseBook" + System.getProperty("file.separator") + this.getWorldName() + System.getProperty("file.separator") + "Extra_ProtectedBlocks.dat");
         if(e.exists()) {
            e.delete();
         }

         FileWriter writer = new FileWriter(e);
         Iterator var4 = this.protectedBlocks.keySet().iterator();

         while(var4.hasNext()) {
            String text = (String)var4.next();
            writer.write(text);
            writer.write(System.getProperty("line.separator"));
         }

         writer.flush();
         writer.close();
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public void run() {
      Iterator var2 = this.netherQueue.iterator();

      Block block;
      while(var2.hasNext()) {
         block = (Block)var2.next();
         this.NetherrackExecute(block, this.isBlockPowered(block));
      }

      this.netherQueue.clear();
      var2 = this.glowstoneQueue.iterator();

      while(var2.hasNext()) {
         block = (Block)var2.next();
         this.GlowstoneExecute(block, this.isBlockPowered(block));
      }

      this.glowstoneQueue.clear();
      var2 = this.pumpkinQueue.iterator();

      while(var2.hasNext()) {
         block = (Block)var2.next();
         this.PumpkinExecute(block, this.isBlockPowered(block));
      }

      this.pumpkinQueue.clear();
      this.TaskID = -1;
   }

   private boolean isBlockPowered(Block block) {
      return !block.getRelative(1, 0, 0).getType().equals(Material.REDSTONE_TORCH_ON) && !block.getRelative(-1, 0, 0).getType().equals(Material.REDSTONE_TORCH_ON) && !block.getRelative(0, 0, 1).getType().equals(Material.REDSTONE_TORCH_ON) && !block.getRelative(0, 0, -1).getType().equals(Material.REDSTONE_TORCH_ON) && !block.getRelative(0, -1, 0).getType().equals(Material.REDSTONE_TORCH_ON) && !block.getRelative(0, 1, 0).getType().equals(Material.REDSTONE_TORCH_ON)?(!block.getRelative(1, 0, 0).getType().equals(Material.DIODE_BLOCK_ON) && !block.getRelative(-1, 0, 0).getType().equals(Material.DIODE_BLOCK_ON) && !block.getRelative(0, 0, 1).getType().equals(Material.DIODE_BLOCK_ON) && !block.getRelative(0, 0, -1).getType().equals(Material.DIODE_BLOCK_ON) && !block.getRelative(0, -1, 0).getType().equals(Material.DIODE_BLOCK_ON) && !block.getRelative(0, 1, 0).getType().equals(Material.DIODE_BLOCK_ON)?(block.getRelative(1, 0, 0).getType().equals(Material.LEVER) && block.getRelative(1, 0, 0).getData() > 7?true:(block.getRelative(-1, 0, 0).getType().equals(Material.LEVER) && block.getRelative(-1, 0, 0).getData() > 7?true:(block.getRelative(0, 0, 1).getType().equals(Material.LEVER) && block.getRelative(0, 0, 1).getData() > 7?true:(block.getRelative(0, 0, -1).getType().equals(Material.LEVER) && block.getRelative(0, 0, -1).getData() > 7?true:(block.getRelative(0, -1, 0).getType().equals(Material.LEVER) && block.getRelative(0, -1, 0).getData() > 7?true:(block.getRelative(0, 1, 0).getType().equals(Material.LEVER) && block.getRelative(0, 1, 0).getData() > 7?true:(block.getRelative(1, 0, 0).getType().equals(Material.REDSTONE_WIRE) && block.getRelative(1, 0, 0).getData() > 0?true:(block.getRelative(-1, 0, 0).getType().equals(Material.REDSTONE_WIRE) && block.getRelative(-1, 0, 0).getData() > 0?true:(block.getRelative(0, 0, 1).getType().equals(Material.REDSTONE_WIRE) && block.getRelative(0, 0, 1).getData() > 0?true:(block.getRelative(0, 0, -1).getType().equals(Material.REDSTONE_WIRE) && block.getRelative(0, 0, -1).getData() > 0?true:(block.getRelative(0, -1, 0).getType().equals(Material.REDSTONE_WIRE) && block.getRelative(0, -1, 0).getData() > 0?true:block.getRelative(0, 1, 0).getType().equals(Material.REDSTONE_WIRE) && block.getRelative(0, 1, 0).getData() > 0))))))))))):true):true;
   }

   private void NetherrackExecute(Block block, boolean on) {
      if(this.getSettings().getBoolean(EnumSettings.TOGGLE_NETHERRACK.getName())) {
         if(block.getType().getId() == Material.NETHERRACK.getId()) {
            Block over = block.getRelative(0, 1, 0);
            if(on && over.getType().equals(Material.AIR)) {
               over.setTypeIdAndData(Material.FIRE.getId(), (byte)0, false);
            } else if(!on && over.getType().equals(Material.FIRE)) {
               over.setTypeIdAndData(Material.AIR.getId(), (byte)0, false);
            }

            over = null;
         }
      }
   }

   private void GlowstoneExecute(Block block, boolean on) {
      if(this.getSettings().getBoolean(EnumSettings.TOGGLE_GLOWSTONE.getName())) {
         if(on || block.getType().getId() == Material.GLOWSTONE.getId()) {
            if((!on || block.getType().getId() == this.getSettings().getInteger(EnumSettings.GLOWSTONE_OFF_ID.getName())) && block.getData() == this.getSettings().getInteger(EnumSettings.GLOWSTONE_OFF_DATA.getName())) {
               if(on) {
                  block.setTypeIdAndData(Material.GLOWSTONE.getId(), (byte)0, false);
               } else {
                  block.setTypeIdAndData(this.getSettings().getInteger(EnumSettings.GLOWSTONE_OFF_ID.getName()), (byte)this.getSettings().getInteger(EnumSettings.GLOWSTONE_OFF_DATA.getName()), false);
               }

               if(this.getSettings().getBoolean(EnumSettings.PROTECT_BLOCKS.getName()) && this.protectedBlocks.remove(BlockUtils.LocationToString(block.getLocation())) == null) {
                  this.protectedBlocks.put(BlockUtils.LocationToString(block.getLocation()), block);
               }

            }
         }
      }
   }

   private void PumpkinExecute(Block block, boolean on) {
      if(this.getSettings().getBoolean(EnumSettings.TOGGLE_PUMPKINS.getName())) {
         if(!on || block.getTypeId() == Material.PUMPKIN.getId()) {
            if(on || block.getTypeId() == Material.JACK_O_LANTERN.getId()) {
               try {
                  byte e = block.getData();
                  if(on) {
                     block.setTypeIdAndData(Material.JACK_O_LANTERN.getId(), e, false);
                  } else {
                     block.setTypeIdAndData(Material.PUMPKIN.getId(), e, false);
                  }
               } catch (Exception var4) {
                  FalseBookExtraCore.printInConsole("ERROR while toggling Pumpkins!");
               }

               if(this.getSettings().getBoolean(EnumSettings.PROTECT_BLOCKS.getName()) && this.protectedBlocks.remove(BlockUtils.LocationToString(block.getLocation())) == null) {
                  this.protectedBlocks.put(BlockUtils.LocationToString(block.getLocation()), block);
               }

            }
         }
      }
   }

   private boolean isBlockProtected(Block block) {
      return this.isBlockProtected(block.getLocation());
   }

   private boolean isBlockProtected(Location location) {
      return this.protectedBlocks.containsKey(BlockUtils.LocationToString(location));
   }

   private boolean isBlockProtected(List blockList) {
      for(int i = 0; i < blockList.size(); ++i) {
         try {
            if(this.isBlockProtected((Block)blockList.get(i))) {
               return true;
            }
         } catch (Exception var4) {
            var4.printStackTrace();
         }
      }

      return false;
   }

   private void signRedstoneEvent(Block block, BlockRedstoneEvent event) {
      if(!block.getType().equals(Material.PUMPKIN) && !block.getType().equals(Material.JACK_O_LANTERN)) {
         if((block.getTypeId() != this.getSettings().getInteger(EnumSettings.GLOWSTONE_OFF_ID.getName()) || block.getData() != this.getSettings().getInteger(EnumSettings.GLOWSTONE_OFF_DATA.getName())) && !block.getType().equals(Material.GLOWSTONE)) {
            if(block.getType().equals(Material.NETHERRACK)) {
               if(!this.netherQueue.contains(block)) {
                  this.netherQueue.add(block);
               }

               if(this.TaskID == -1) {
                  this.TaskID = FalseBookExtraCore.server.getScheduler().scheduleSyncDelayedTask(FalseBookExtraCore.getInstance(), this, 1L);
               }
            }
         } else {
            if(!this.glowstoneQueue.contains(block)) {
               this.glowstoneQueue.add(block);
            }

            if(this.TaskID == -1) {
               this.TaskID = FalseBookExtraCore.server.getScheduler().scheduleSyncDelayedTask(FalseBookExtraCore.getInstance(), this, 1L);
            }
         }
      } else {
         if(!this.pumpkinQueue.contains(block)) {
            this.pumpkinQueue.add(block);
         }

         if(this.TaskID == -1) {
            this.TaskID = FalseBookExtraCore.server.getScheduler().scheduleSyncDelayedTask(FalseBookExtraCore.getInstance(), this, 1L);
         }
      }

   }

   public void onBlockPhysics(BlockPhysicsEvent event) {
      if(event.getBlock().getType().equals(Material.DIODE_BLOCK_ON) || event.getBlock().getType().equals(Material.DIODE_BLOCK_OFF) || event.getBlock().getType().equals(Material.LEVER)) {
         Block block = event.getBlock().getRelative(0, 1, 0);
         if(event.getBlock().getRelative(0, 1, 0).getType().equals(Material.NETHERRACK)) {
            if(!this.netherQueue.contains(block)) {
               this.netherQueue.add(block);
            }

            if(this.TaskID == -1) {
               this.TaskID = FalseBookExtraCore.server.getScheduler().scheduleSyncDelayedTask(FalseBookExtraCore.getInstance(), this, 1L);
            }
         }

         if(event.getBlock().getRelative(0, 1, 0).getTypeId() == this.getSettings().getInteger(EnumSettings.GLOWSTONE_OFF_ID.getName()) && event.getBlock().getRelative(0, 1, 0).getData() == this.getSettings().getInteger(EnumSettings.GLOWSTONE_OFF_DATA.getName())) {
            if(!this.glowstoneQueue.contains(block)) {
               this.glowstoneQueue.add(block);
            }

            if(this.TaskID == -1) {
               this.TaskID = FalseBookExtraCore.server.getScheduler().scheduleSyncDelayedTask(FalseBookExtraCore.getInstance(), this, 1L);
            }
         }

         if(event.getBlock().getRelative(0, 1, 0).getTypeId() == Material.GLOWSTONE.getId()) {
            if(!this.glowstoneQueue.contains(block)) {
               this.glowstoneQueue.add(block);
            }

            if(this.TaskID == -1) {
               this.TaskID = FalseBookExtraCore.server.getScheduler().scheduleSyncDelayedTask(FalseBookExtraCore.getInstance(), this, 1L);
            }
         }

         if(event.getBlock().getRelative(0, 1, 0).getType().equals(Material.PUMPKIN) || event.getBlock().getRelative(0, 1, 0).getType().equals(Material.JACK_O_LANTERN)) {
            if(!this.pumpkinQueue.contains(block)) {
               this.pumpkinQueue.add(block);
            }

            if(this.TaskID == -1) {
               this.TaskID = FalseBookExtraCore.server.getScheduler().scheduleSyncDelayedTask(FalseBookExtraCore.getInstance(), this, 1L);
            }
         }

         block = event.getBlock().getRelative(0, -1, 0);
         if(event.getBlock().getRelative(0, -1, 0).getType().equals(Material.GLOWSTONE) || event.getBlock().getRelative(0, -1, 0).getTypeId() == this.getSettings().getInteger(EnumSettings.GLOWSTONE_OFF_ID.getName()) && event.getBlock().getRelative(0, -1, 0).getData() == this.getSettings().getInteger(EnumSettings.GLOWSTONE_OFF_DATA.getName())) {
            if(!this.glowstoneQueue.contains(block)) {
               this.glowstoneQueue.add(block);
            }

            if(this.TaskID == -1) {
               this.TaskID = FalseBookExtraCore.server.getScheduler().scheduleSyncDelayedTask(FalseBookExtraCore.getInstance(), this, 1L);
            }
         }

         if(event.getBlock().getRelative(0, -1, 0).getType().equals(Material.PUMPKIN) || event.getBlock().getRelative(0, -1, 0).getType().equals(Material.JACK_O_LANTERN)) {
            if(!this.pumpkinQueue.contains(block)) {
               this.pumpkinQueue.add(block);
            }

            if(this.TaskID == -1) {
               this.TaskID = FalseBookExtraCore.server.getScheduler().scheduleSyncDelayedTask(FalseBookExtraCore.getInstance(), this, 1L);
            }
         }

      }
   }

   public void onBlockBreak(BlockBreakEvent event) {
      if(this.getSettings().getBoolean(EnumSettings.PROTECT_BLOCKS.getName())) {
         if(this.isBlockProtected(event.getBlock())) {
            Player player = event.getPlayer();
            if(!UtilPermissions.playerCanUseCommand(player, "falsebook.extra.candestroytoggledblocks")) {
               ChatUtils.printError(player, "[FB-Extra]", "You are not allowed to destroy protected blocks.");
               event.setCancelled(true);
            } else {
               this.protectedBlocks.remove(BlockUtils.LocationToString(event.getBlock().getLocation()));
               ChatUtils.printInfo(player, "[FB-Extra]", ChatColor.GRAY, "Removed protected block.");
            }

            player = null;
         }
      }
   }

   public void onPistonExtend(BlockPistonExtendEvent event) {
      if(this.getSettings().getBoolean(EnumSettings.PROTECT_BLOCKS.getName())) {
         event.setCancelled(this.isBlockProtected(event.getBlocks()));
      }
   }

   public void onPistonRetract(BlockPistonRetractEvent event) {
      if(this.getSettings().getBoolean(EnumSettings.PROTECT_BLOCKS.getName())) {
         event.setCancelled(this.isBlockProtected(event.getRetractLocation()));
      }
   }

   public void onRedstoneChange(BlockRedstoneEvent event) {
      Block block = event.getBlock();
      int ID = block.getTypeId();
      if(ID == Material.LEVER.getId() || ID == Material.REDSTONE_WIRE.getId() || ID == Material.STONE_BUTTON.getId() || ID == Material.REDSTONE_TORCH_ON.getId() || ID == Material.REDSTONE_TORCH_OFF.getId() || ID == Material.DIODE_BLOCK_ON.getId() || ID == Material.DIODE_BLOCK_OFF.getId() || ID == Material.WOOD_PLATE.getId() || ID == Material.STONE_PLATE.getId()) {
         this.signRedstoneEvent(block.getRelative(0, 0, 1), event);
         this.signRedstoneEvent(block.getRelative(0, 0, -1), event);
         this.signRedstoneEvent(block.getRelative(1, 0, 0), event);
         this.signRedstoneEvent(block.getRelative(-1, 0, 0), event);
         this.signRedstoneEvent(block.getRelative(0, 1, 0), event);
         this.signRedstoneEvent(block.getRelative(0, -1, 0), event);
         block = null;
      }
   }

   public void onEntityChangeBlock(EntityChangeBlockEvent event) {
      if(this.getSettings().getBoolean(EnumSettings.PROTECT_BLOCKS.getName())) {
         event.setCancelled(this.isBlockProtected(event.getBlock()));
      }
   }

   public void onEntityExplode(EntityExplodeEvent event) {
      if(this.getSettings().getBoolean(EnumSettings.PROTECT_BLOCKS.getName())) {
         event.setCancelled(this.isBlockProtected(event.blockList()));
      }
   }
}
