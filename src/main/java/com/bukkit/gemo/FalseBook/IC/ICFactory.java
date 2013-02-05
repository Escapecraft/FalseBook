package com.bukkit.gemo.FalseBook.IC;

import com.bukkit.gemo.FalseBook.IC.FalseBookICCore;
import com.bukkit.gemo.FalseBook.IC.PersistenceHandler;
import com.bukkit.gemo.FalseBook.IC.ExecutionEvents.DelayedICExecutionEvent;
import com.bukkit.gemo.FalseBook.IC.ExecutionEvents.ICExecutionEvent;
import com.bukkit.gemo.FalseBook.IC.ExecutionEvents.ICRunningTask;
import com.bukkit.gemo.FalseBook.IC.ICs.BaseIC;
import com.bukkit.gemo.FalseBook.IC.ICs.ExternalICPackage;
import com.bukkit.gemo.FalseBook.IC.ICs.InputState;
import com.bukkit.gemo.FalseBook.IC.ICs.NotLoadedIC;
import com.bukkit.gemo.FalseBook.IC.ICs.SelftriggeredBaseIC;
import com.bukkit.gemo.FalseBook.IC.ICs.selftriggered.MC0111;
import com.bukkit.gemo.FalseBook.IC.ICs.selftriggered.MC1110;
import com.bukkit.gemo.FalseBook.IC.ICs.standard.MC1111;
import com.bukkit.gemo.FalseBook.IC.Plugins.SelfmadeICLoader;
import com.bukkit.gemo.utils.BlockUtils;
import com.bukkit.gemo.utils.ChatUtils;
import com.bukkit.gemo.utils.ICUtils;
import com.bukkit.gemo.utils.MyEventStatistic;
import com.bukkit.gemo.utils.SignUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ICFactory {

   private String API_VERSION = "1.1";
   private SelfmadeICLoader loader = null;
   private FalseBookICCore plugin = null;
   private ICRunningTask TASK = null;
   private PersistenceHandler persistence = null;
   private ArrayList failedICs = new ArrayList();
   private ConcurrentHashMap SensorList = new ConcurrentHashMap();
   private ConcurrentHashMap nonTriggeringSensorList = new ConcurrentHashMap();
   private HashMap registeredTICs = new HashMap();
   private HashMap registeredSTICs = new HashMap();
   public MyEventStatistic statistic = new MyEventStatistic();


   public ICFactory(FalseBookICCore instance) {
      this.plugin = instance;
   }

   public void init(PersistenceHandler persistence) {
      this.registerICs();
      this.TASK = new ICRunningTask(this.plugin);
      this.persistence = persistence;
   }

   public void ExportSTICsToWiki() {
      Iterator iterator = this.registeredSTICs.values().iterator();

      BaseIC IC;
      while(iterator.hasNext()) {
         IC = (BaseIC)iterator.next();
         IC.ExportAsWikiHTML();
      }

      IC = null;
   }

   public void ExportTICsToWiki() {
      Iterator iterator = this.registeredTICs.values().iterator();

      BaseIC IC;
      while(iterator.hasNext()) {
         IC = (BaseIC)iterator.next();
         IC.ExportAsWikiHTML();
      }

      IC = null;
   }

   private void registerSingleTIC(BaseIC thisIC) {
      thisIC.initCore();
      this.registeredTICs.put(thisIC.getICNumber().toUpperCase(), thisIC);
   }

   private void registerSingleSTIC(SelftriggeredBaseIC thisIC) {
      Iterator var3 = this.registeredSTICs.entrySet().iterator();

      while(var3.hasNext()) {
         Entry entry = (Entry)var3.next();
         if(((SelftriggeredBaseIC)entry.getValue()).getTypeID() == thisIC.getTypeID()) {
            FalseBookICCore.printInConsole("WARNING: TypeID of " + ((SelftriggeredBaseIC)entry.getValue()).getICNumber().toUpperCase() + " & " + thisIC.getICNumber().toUpperCase() + " are equal! ( " + thisIC.getTypeID() + " )");
         }
      }

      thisIC.initCore();
      this.registeredSTICs.put(thisIC.getICNumber().toUpperCase(), thisIC);
   }

   private void registerICs() {
      this.registerSingleSTIC(new MC0111());
      this.registerSingleSTIC(new MC1110());
      this.registerSingleTIC(new MC1111());
   }

   private boolean checkICInstance(Object thisIC) {
      return !(thisIC instanceof BaseIC)?false:(((BaseIC)thisIC).getICGroup() == null?false:((BaseIC)thisIC).getICName() != null && ((BaseIC)thisIC).getICNumber() != null);
   }

   public void importSelfmadeICs() {
      File folder = new File("plugins" + System.getProperty("file.separator") + "FalseBook" + System.getProperty("file.separator") + "ICPlugins");
      folder.mkdirs();
      this.loader = new SelfmadeICLoader();
      File[] var5;
      int var4 = (var5 = folder.listFiles()).length;

      for(int var3 = 0; var3 < var4; ++var3) {
         File file = var5[var3];
         if(file.isFile() && file.getName().endsWith(".jar")) {
            ExternalICPackage thisPackage = this.loader.loadPlugin(file);
            if(thisPackage != null) {
               int ICCount = 0;
               int FailedICCount = 0;
               if(this.API_VERSION.equalsIgnoreCase(thisPackage.getAPI_VERSION())) {
                  Iterator var10 = thisPackage.getICList().iterator();

                  while(var10.hasNext()) {
                     Class entry = (Class)var10.next();

                     try {
                        Object e = entry.newInstance();
                        if(!this.checkICInstance(e)) {
                           FalseBookICCore.printInConsole("ERROR: Could not import \'" + entry.getSimpleName() + "\'! (Not initialized correctly.)");
                           ++FailedICCount;
                        } else if(e instanceof SelftriggeredBaseIC) {
                           SelftriggeredBaseIC thisIC = (SelftriggeredBaseIC)e;
                           if(thisIC.getTypeID() != -1) {
                              if(!this.registeredSTICs.containsKey(thisIC.getICNumber().toUpperCase())) {
                                 if(thisPackage.isShowImportMessages()) {
                                    FalseBookICCore.printInConsole("imported STIC: " + thisIC.getICName() + " ( " + thisIC.getICNumber().toUpperCase() + " )");
                                 }

                                 this.registerSingleSTIC(thisIC);
                                 thisIC.onImport();
                                 thisIC.initCore();
                                 ++ICCount;
                              } else {
                                 FalseBookICCore.printInConsole("ERROR: Could not register selfmade " + thisIC.getICNumber().toUpperCase() + "! STIC is already registered!");
                                 ++FailedICCount;
                              }
                           } else {
                              FalseBookICCore.printInConsole("ERROR: Could not import selfmade " + thisIC.getICNumber().toUpperCase() + "! TypeID must me != -1!");
                              ++FailedICCount;
                           }
                        } else if(e instanceof BaseIC) {
                           BaseIC var14 = (BaseIC)e;
                           if(!this.registeredTICs.containsKey(var14.getICNumber().toUpperCase())) {
                              if(thisPackage.isShowImportMessages()) {
                                 FalseBookICCore.printInConsole("imported TIC: " + var14.getICName() + " ( " + var14.getICNumber().toUpperCase() + " )");
                              }

                              this.registerSingleTIC(var14);
                              var14.onImport();
                              var14.initCore();
                              ++ICCount;
                           } else {
                              FalseBookICCore.printInConsole("ERROR: Could not register selfmade " + var14.getICNumber().toUpperCase() + "! IC is already registered!");
                              ++FailedICCount;
                           }
                        } else {
                           FalseBookICCore.printInConsole("ERROR: Could not import \'" + entry.getSimpleName() + "\'!");
                           ++FailedICCount;
                        }
                     } catch (Exception var13) {
                        var13.printStackTrace();
                        FalseBookICCore.printInConsole("ERROR: Could not import \'" + entry.getSimpleName() + "\'!");
                        ++FailedICCount;
                     }
                  }
               } else {
                  FalseBookICCore.printInConsole("ERROR: API-Versions of  \'" + file.getName() + "\' does not match! Current API-Version of FalseBookIC is " + this.API_VERSION);
               }

               if(ICCount > 0) {
                  FalseBookICCore.printInConsole("\'" + thisPackage.getClass().getSimpleName() + "\' imported \'" + ICCount + "\' ICs.");
               }

               if(FailedICCount > 0) {
                  FalseBookICCore.printInConsole("ERROR: \'" + thisPackage.getClass().getSimpleName() + "\' could not import \'" + FailedICCount + "\' ICs.");
               }
            } else {
               FalseBookICCore.printInConsole("ERROR: Could not import \'" + file.getName() + "\'!");
            }
         }
      }

   }

   public boolean STICExists(Location loc) {
      Iterator iterator = this.SensorList.values().iterator();

      SelftriggeredBaseIC IC;
      while(iterator.hasNext()) {
         IC = (SelftriggeredBaseIC)iterator.next();
         if(BlockUtils.LocationEquals(IC.getSignBlock().getBlock().getLocation(), loc)) {
            IC = null;
            return true;
         }
      }

      iterator = this.nonTriggeringSensorList.values().iterator();

      while(iterator.hasNext()) {
         IC = (SelftriggeredBaseIC)iterator.next();
         if(BlockUtils.LocationEquals(IC.getSignBlock().getBlock().getLocation(), loc)) {
            IC = null;
            return true;
         }
      }

      IC = null;
      return false;
   }

   public BaseIC getIC(String line) {
      line = line.toUpperCase();
      return this.registeredTICs.get(line) == null?(BaseIC)this.registeredSTICs.get(line):(BaseIC)this.registeredTICs.get(line);
   }

   public BaseIC getICByName(String line) {
      line = line.toLowerCase().trim();
      Iterator var3;
      if(line.startsWith("*")) {
         line = line.replace("*", "");
         var3 = this.registeredSTICs.values().iterator();

         while(var3.hasNext()) {
            SelftriggeredBaseIC entry = (SelftriggeredBaseIC)var3.next();
            if(line.equalsIgnoreCase(entry.getICName().trim().replace(" ", "").replace("_", "").replace("-", ""))) {
               return entry;
            }
         }
      } else if(line.startsWith("=")) {
         line = line.replace("=", "");
         var3 = this.registeredTICs.values().iterator();

         while(var3.hasNext()) {
            BaseIC entry1 = (BaseIC)var3.next();
            if(line.equalsIgnoreCase(entry1.getICName().trim().replace(" ", "").replace("_", "").replace("-", ""))) {
               return entry1;
            }
         }
      }

      return null;
   }

   public SelftriggeredBaseIC getSTIC(String line) {
      return (SelftriggeredBaseIC)this.registeredSTICs.get(line.toUpperCase());
   }

   public ArrayList getFailedICs() {
      return (ArrayList)this.failedICs.clone();
   }

   public boolean isBlockBreakable(List blockList) {
      for(int i = 0; i < blockList.size(); ++i) {
         try {
            Block block = (Block)blockList.get(i);
            Sign e;
            if(block.getType().equals(Material.WALL_SIGN)) {
               e = (Sign)block.getState();
               BaseIC signData = this.getIC(e.getLine(1).toUpperCase());
               if(signData != null) {
                  return false;
               }
            } else {
               e = null;
               boolean var12 = true;
               ArrayList bList = BlockUtils.getDirectNeighbours(block, true);

               for(int j = 0; j < bList.size(); ++j) {
                  Block var11 = (Block)bList.get(j);
                  if(var11.getType().equals(Material.WALL_SIGN)) {
                     Sign signBlock = (Sign)var11.getState();
                     byte var13 = signBlock.getRawData();
                     if(var13 == 2 && j == 3 || var13 == 4 && j == 1 || var13 == 5 && j == 0 || var13 == 3 && j == 2) {
                        BaseIC thisIC = this.getIC(signBlock.getLine(1).toUpperCase());
                        if(thisIC != null) {
                           return false;
                        }
                     }
                  }
               }
            }
         } catch (Exception var10) {
            var10.printStackTrace();
         }
      }

      return true;
   }

   public boolean isBlockBreakable(Block block) {
      try {
         Sign e;
         if(block.getType().equals(Material.WALL_SIGN)) {
            e = (Sign)block.getState();
            BaseIC signData = this.getIC(e.getLine(1).toUpperCase());
            if(signData != null) {
               return false;
            }
         } else {
            e = null;
            boolean var10 = true;
            ArrayList bList = BlockUtils.getDirectNeighbours(block, true);

            for(int j = 0; j < bList.size(); ++j) {
               Block var9 = (Block)bList.get(j);
               if(var9.getType().equals(Material.WALL_SIGN)) {
                  Sign signBlock = (Sign)var9.getState();
                  byte var11 = signBlock.getRawData();
                  if(var11 == 2 && j == 3 || var11 == 4 && j == 1 || var11 == 5 && j == 0 || var11 == 3 && j == 2) {
                     BaseIC thisIC = this.getIC(signBlock.getLine(1).toUpperCase());
                     if(thisIC != null) {
                        return false;
                     }
                  }
               }
            }
         }
      } catch (Exception var8) {
         var8.printStackTrace();
      }

      return true;
   }

   public void addSelftriggeredIC(Location location, SelftriggeredBaseIC IC) {
      if(IC.isExecuteOnTick()) {
         this.SensorList.put(location.toString(), IC);
      } else {
         this.nonTriggeringSensorList.put(location.toString(), IC);
      }

   }

   public void removeFailedIC(NotLoadedIC IC) {
      this.failedICs.remove(IC);
   }

   public void addFailedIC(NotLoadedIC IC) {
      this.failedICs.add(IC);
   }

   public int getFailedICsSize() {
      return this.failedICs.size();
   }

   public void handleExplodeEvent(EntityExplodeEvent event) {
      if(!this.isBlockBreakable(event.blockList())) {
         if(!FalseBookICCore.getInstance().isAllowExplosionForICs()) {
            event.setYield(0.0F);
            event.setCancelled(true);
         } else {
            Iterator var3 = event.blockList().iterator();

            while(var3.hasNext()) {
               Block block = (Block)var3.next();
               Block b;
               if(block.getType().equals(Material.WALL_SIGN)) {
                  Sign var13 = (Sign)block.getState();
                  BaseIC var15 = this.getIC(var13.getLine(1).toUpperCase());
                  if(var15 != null) {
                     if(var15 instanceof SelftriggeredBaseIC) {
                        Location var16 = block.getLocation();
                        SelftriggeredBaseIC var17;
                        Iterator var18;
                        if(((SelftriggeredBaseIC)var15).isExecuteOnTick()) {
                           var18 = this.SensorList.values().iterator();

                           while(var18.hasNext()) {
                              var17 = (SelftriggeredBaseIC)var18.next();
                              if(BlockUtils.LocationEquals(var16, var17.getSignBlock().getBlock().getLocation())) {
                                 this.SensorList.remove(var17.getSignBlock().getBlock().getLocation().toString());
                                 this.persistence.removeSelftriggeredIC(var17.getSignBlock().getBlock().getLocation());
                                 var17.onBreakByExplosion(var17.getSignBlock());
                              }
                           }
                        } else {
                           var18 = this.nonTriggeringSensorList.values().iterator();

                           while(var18.hasNext()) {
                              var17 = (SelftriggeredBaseIC)var18.next();
                              if(BlockUtils.LocationEquals(var16, var17.getSignBlock().getBlock().getLocation())) {
                                 this.nonTriggeringSensorList.remove(var17.getSignBlock().getBlock().getLocation().toString());
                                 this.persistence.removeSelftriggeredIC(var17.getSignBlock().getBlock().getLocation());
                                 var17.onBreakByExplosion(var17.getSignBlock());
                              }
                           }
                        }

                        var17 = null;
                     } else {
                        var15.onBreakByExplosion(var13);
                     }

                     b = null;
                     var15 = null;
                  }
               } else {
                  b = null;
                  boolean signData = true;
                  ArrayList bList = BlockUtils.getDirectNeighbours(block, false);

                  for(int i = 0; i < bList.size(); ++i) {
                     b = (Block)bList.get(i);
                     if(b.getType().equals(Material.WALL_SIGN)) {
                        Sign signBlock = (Sign)b.getState();
                        byte var14 = signBlock.getRawData();
                        if(var14 == 2 && i == 3 || var14 == 4 && i == 1 || var14 == 5 && i == 0 || var14 == 3 && i == 2) {
                           BaseIC thisIC = this.getIC(signBlock.getLine(1).toUpperCase());
                           if(thisIC != null) {
                              if(!(thisIC instanceof SelftriggeredBaseIC)) {
                                 thisIC.onBreakByExplosion(signBlock);
                              } else {
                                 Location loc2 = b.getLocation();
                                 SelftriggeredBaseIC IC;
                                 Iterator iterator;
                                 if(((SelftriggeredBaseIC)thisIC).isExecuteOnTick()) {
                                    iterator = this.SensorList.values().iterator();

                                    while(iterator.hasNext()) {
                                       IC = (SelftriggeredBaseIC)iterator.next();
                                       if(BlockUtils.LocationEquals(loc2, IC.getSignBlock().getBlock().getLocation())) {
                                          this.SensorList.remove(IC.getSignBlock().getBlock().getLocation().toString());
                                          this.persistence.removeSelftriggeredIC(IC.getSignBlock().getBlock().getLocation());
                                          IC.onBreakByExplosion(IC.getSignBlock());
                                       }
                                    }
                                 } else {
                                    iterator = this.nonTriggeringSensorList.values().iterator();

                                    while(iterator.hasNext()) {
                                       IC = (SelftriggeredBaseIC)iterator.next();
                                       if(BlockUtils.LocationEquals(loc2, IC.getSignBlock().getBlock().getLocation())) {
                                          this.nonTriggeringSensorList.remove(IC.getSignBlock().getBlock().getLocation().toString());
                                          this.persistence.removeSelftriggeredIC(IC.getSignBlock().getBlock().getLocation());
                                          IC.onBreakByExplosion(IC.getSignBlock());
                                       }
                                    }
                                 }

                                 IC = null;
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }

   }

   public void handleEntityChangeBlock(EntityChangeBlockEvent event) {
      if(!this.isBlockBreakable(event.getBlock())) {
         event.setCancelled(true);
      }

   }

   public void handlePistonExtend(BlockPistonExtendEvent event) {
      if(!this.isBlockBreakable(event.getBlocks())) {
         event.setCancelled(true);
      }

   }

   public void handlePistonRetract(BlockPistonRetractEvent event) {
      if(event.isSticky()) {
         try {
            if(!this.isBlockBreakable(event.getRetractLocation().getBlock())) {
               event.setCancelled(true);
            }

         } catch (Exception var3) {
            var3.printStackTrace();
         }
      }
   }

   public void handleBlockBreak(BlockBreakEvent event) {
      Block block = event.getBlock();
      Sign b;
      Iterator signBlock;
      if(block.getType().equals(Material.WALL_SIGN)) {
         b = (Sign)block.getState();
         BaseIC signData = this.getIC(b.getLine(1).toUpperCase());
         if(signData == null) {
            return;
         }

         if(!signData.hasPermission(event.getPlayer())) {
            event.setCancelled(true);
            ChatUtils.printError(event.getPlayer(), "[FB-IC]", "You are not allowed to destroy this IC!");
            signData = null;
            b = null;
            return;
         }

         if(signData instanceof SelftriggeredBaseIC) {
            Location bList = block.getLocation();
            SelftriggeredBaseIC i;
            if(((SelftriggeredBaseIC)signData).isExecuteOnTick()) {
               signBlock = this.SensorList.values().iterator();

               while(signBlock.hasNext()) {
                  i = (SelftriggeredBaseIC)signBlock.next();
                  if(BlockUtils.LocationEquals(bList, i.getSignBlock().getBlock().getLocation())) {
                     if(!i.onBreakByPlayer(event.getPlayer(), i.getSignBlock())) {
                        event.setCancelled(true);
                        ChatUtils.printError(event.getPlayer(), "[FB-IC]", "You are not allowed to destroy this IC!");
                        return;
                     }

                     this.SensorList.remove(i.getSignBlock().getBlock().getLocation().toString());
                     this.persistence.removeSelftriggeredIC(i.getSignBlock().getBlock().getLocation());
                     ChatUtils.printSuccess(event.getPlayer(), "[FB-IC]", i.getICNumber() + " removed.");
                     break;
                  }
               }
            } else {
               signBlock = this.nonTriggeringSensorList.values().iterator();

               while(signBlock.hasNext()) {
                  i = (SelftriggeredBaseIC)signBlock.next();
                  if(BlockUtils.LocationEquals(bList, i.getSignBlock().getBlock().getLocation())) {
                     if(!i.onBreakByPlayer(event.getPlayer(), i.getSignBlock())) {
                        event.setCancelled(true);
                        ChatUtils.printError(event.getPlayer(), "[FB-IC]", "You are not allowed to destroy this IC!");
                        return;
                     }

                     this.nonTriggeringSensorList.remove(i.getSignBlock().getBlock().getLocation().toString());
                     this.persistence.removeSelftriggeredIC(i.getSignBlock().getBlock().getLocation());
                     ChatUtils.printSuccess(event.getPlayer(), "[FB-IC]", i.getICNumber() + " removed.");
                     break;
                  }
               }
            }

            i = null;
         } else {
            if(!signData.onBreakByPlayer(event.getPlayer(), b)) {
               event.setCancelled(true);
               ChatUtils.printError(event.getPlayer(), "[FB-IC]", "You are not allowed to destroy this IC!");
               return;
            }

            ChatUtils.printSuccess(event.getPlayer(), "[FB-IC]", signData.getICNumber() + " removed.");
         }

         b = null;
         signData = null;
      } else {
         b = null;
         boolean var15 = true;
         ArrayList var13 = BlockUtils.getDirectNeighbours(block, false);

         for(int var16 = 0; var16 < var13.size(); ++var16) {
            Block var12 = (Block)var13.get(var16);
            if(var12.getType().equals(Material.WALL_SIGN)) {
               Sign var17 = (Sign)var12.getState();
               byte var14 = var17.getRawData();
               if(var14 == 2 && var16 == 3 || var14 == 4 && var16 == 1 || var14 == 5 && var16 == 0 || var14 == 3 && var16 == 2) {
                  BaseIC thisIC = this.getIC(var17.getLine(1).toUpperCase());
                  if(thisIC != null) {
                     if(!thisIC.hasPermission(event.getPlayer())) {
                        event.setCancelled(true);
                        ChatUtils.printError(event.getPlayer(), "[FB-IC]", "You are not allowed to destroy this IC!");
                        thisIC = null;
                        signBlock = null;
                        return;
                     }

                     if(thisIC instanceof SelftriggeredBaseIC) {
                        Location loc2 = var12.getLocation();
                        SelftriggeredBaseIC IC;
                        Iterator iterator;
                        if(((SelftriggeredBaseIC)thisIC).isExecuteOnTick()) {
                           iterator = this.SensorList.values().iterator();

                           while(iterator.hasNext()) {
                              IC = (SelftriggeredBaseIC)iterator.next();
                              if(BlockUtils.LocationEquals(loc2, IC.getSignBlock().getBlock().getLocation())) {
                                 if(!IC.onBreakByPlayer(event.getPlayer(), IC.getSignBlock())) {
                                    event.setCancelled(true);
                                    ChatUtils.printError(event.getPlayer(), "[FB-IC]", "You are not allowed to destroy this IC!");
                                    return;
                                 }

                                 this.SensorList.remove(IC.getSignBlock().getBlock().getLocation().toString());
                                 this.persistence.removeSelftriggeredIC(IC.getSignBlock().getBlock().getLocation());
                                 ChatUtils.printSuccess(event.getPlayer(), "[FB-IC]", IC.getICNumber() + " removed.");
                              }
                           }
                        } else {
                           iterator = this.nonTriggeringSensorList.values().iterator();

                           while(iterator.hasNext()) {
                              IC = (SelftriggeredBaseIC)iterator.next();
                              if(BlockUtils.LocationEquals(loc2, IC.getSignBlock().getBlock().getLocation())) {
                                 if(!IC.onBreakByPlayer(event.getPlayer(), IC.getSignBlock())) {
                                    event.setCancelled(true);
                                    ChatUtils.printError(event.getPlayer(), "[FB-IC]", "You are not allowed to destroy this IC!");
                                    return;
                                 }

                                 this.nonTriggeringSensorList.remove(IC.getSignBlock().getBlock().getLocation().toString());
                                 this.persistence.removeSelftriggeredIC(IC.getSignBlock().getBlock().getLocation());
                                 ChatUtils.printSuccess(event.getPlayer(), "[FB-IC]", IC.getICNumber() + " removed.");
                              }
                           }
                        }

                        IC = null;
                     } else {
                        if(!thisIC.onBreakByPlayer(event.getPlayer(), var17)) {
                           event.setCancelled(true);
                           ChatUtils.printError(event.getPlayer(), "[FB-IC]", "You are not allowed to destroy this IC!");
                           return;
                        }

                        ChatUtils.printSuccess(event.getPlayer(), "[FB-IC]", thisIC.getICNumber() + " removed.");
                     }
                  }
               }
            }
         }
      }

   }

   public void handleRedstoneEvent(Block block, BlockRedstoneEvent event, int delayTicks, int searchTry) {
      if(block.getType().equals(Material.WALL_SIGN)) {
         Sign delay = (Sign)block.getState();
         if(delay == null) {
            return;
         }

         if(delay.getLine(1) == null) {
            return;
         }

         BaseIC rEvent = this.getIC(delay.getLine(1).toUpperCase());
         if(rEvent == null) {
            return;
         }

         if(rEvent instanceof MC1110) {
            Iterator tBlock = this.getNonTriggeringSensorListIterator();

            while(tBlock.hasNext()) {
               SelftriggeredBaseIC data = (SelftriggeredBaseIC)tBlock.next();
               if(data instanceof MC1110 && BlockUtils.LocationEquals(data.getSignBlock().getBlock().getLocation(), block.getLocation())) {
                  ((MC1110)data).setStatus(event.getNewCurrent() > 0);
                  data = null;
                  return;
               }
            }
         }

         boolean[] data2 = new boolean[]{ICUtils.isInputHigh(delay, 1), ICUtils.isInputHigh(delay, 2), ICUtils.isInputHigh(delay, 3)};
         InputState tBlock2 = new InputState(delay);
         synchronized(this.TASK.getQueuedICs()) {
            if(delayTicks <= 1) {
               ICExecutionEvent thisEvent = new ICExecutionEvent(rEvent, delay, tBlock2);
               if(this.TASK.getQueuedICs().size() + 1 < this.TASK.getMaxExecutions()) {
                  ArrayList positions = ICUtils.getBlockPositions(delay);
                  if(rEvent.getChipState().hasInput(event.getBlock(), positions) && !this.TASK.getQueuedICsPos().containsKey(delay.getBlock().getLocation().toString())) {
                     this.TASK.getQueuedICs().add(thisEvent);
                     this.TASK.getQueuedICsPos().put(delay.getBlock().getLocation().toString(), Integer.valueOf(0));
                  }
               }
            } else {
               DelayedICExecutionEvent thisEvent1 = new DelayedICExecutionEvent(rEvent, delay, tBlock2);
               this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, thisEvent1, (long)(delayTicks + searchTry));
            }
         }

         if(this.TASK.getExeTaskID() == -1 && this.TASK.getQueuedICs().size() > 0) {
            this.TASK.setExeTaskID(this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, this.TASK, 1L));
         }
      } else if(block.getType().equals(Material.DIODE_BLOCK_OFF) || block.getType().equals(Material.DIODE_BLOCK_ON)) {
         if(searchTry > 10) {
            return;
         }

         int delay1 = delayTicks + (int)Math.floor((double)(block.getData() / 4)) + 1;
         BlockRedstoneEvent rEvent1 = new BlockRedstoneEvent(block, event.getOldCurrent(), event.getNewCurrent());
         int data1 = block.getData() % 4;
         Block tBlock1 = block.getRelative(0, 0, 1);
         if(data1 != 2) {
            if(data1 == 0) {
               tBlock1 = block.getRelative(0, 0, -1);
            } else if(data1 == 1) {
               tBlock1 = block.getRelative(1, 0, 0);
            } else if(data1 == 3) {
               tBlock1 = block.getRelative(-1, 0, 0);
            }
         }

         if(tBlock1.getTypeId() != Material.DIODE_BLOCK_ON.getId() && tBlock1.getTypeId() != Material.DIODE_BLOCK_OFF.getId()) {
            this.handleRedstoneEvent(tBlock1, rEvent1, delay1, searchTry + 1);
         } else if(tBlock1.getData() % 4 == data1) {
            this.handleRedstoneEvent(tBlock1, rEvent1, delay1, searchTry + 1);
         }
      }

   }

   public void handleSignChange(SignChangeEvent event) {
      Player player = event.getPlayer();
      BaseIC thisIC = this.getIC(event.getLine(1).toUpperCase());
      if(thisIC == null) {
         String newIC = event.getLine(0).replace(" ", "").replace("_", "").replace("-", "");
         thisIC = this.getICByName(newIC);
         if(thisIC == null) {
            return;
         }

         event.setLine(0, thisIC.getICName());
         event.setLine(1, thisIC.getICNumber());
      }

      if(!event.getBlock().getType().equals(Material.WALL_SIGN)) {
         event.setCancelled(true);
         SignUtils.cancelSignCreation(event, "IC-Signs must be build on a wall.");
      } else if(!thisIC.hasPermission(player)) {
         event.setCancelled(true);
         SignUtils.cancelSignCreation(event, "You are not allowed to build a " + thisIC.getICName());
      } else {
         event.setLine(0, thisIC.getICName());
         event.setLine(1, thisIC.getICNumber());
         if(!(thisIC instanceof SelftriggeredBaseIC)) {
            thisIC.checkCreation(event);
            if(!event.isCancelled()) {
               thisIC.initCore();
               thisIC.notifyCreationSuccess(player);
            }
         } else {
            SelftriggeredBaseIC newIC1 = null;
            SelftriggeredBaseIC nIC = (SelftriggeredBaseIC)this.registeredSTICs.get(event.getLine(1).toUpperCase());
            boolean startUpComplete = false;
            if(nIC != null) {
               try {
                  newIC1 = (SelftriggeredBaseIC)nIC.getClass().newInstance();
                  newIC1.initIC(this.plugin, event.getBlock().getLocation());
                  newIC1.checkCreation(event);
                  startUpComplete = newIC1.onLoad(event.getLines());
               } catch (Exception var8) {
                  var8.printStackTrace();
               }
            } else {
               newIC1 = null;
            }

            if(!event.isCancelled() && newIC1 != null && startUpComplete) {
               thisIC.initCore();
               this.persistence.addSelftriggeredICToDB(((SelftriggeredBaseIC)thisIC).getTypeID(), event.getBlock().getLocation());
               this.addSelftriggeredIC(event.getBlock().getLocation(), newIC1);
               thisIC.notifyCreationSuccess(player);
            }
         }

         thisIC = null;
      }
   }

   public void handleLeftClick(PlayerInteractEvent event) {
      if(event.getClickedBlock().getType().equals(Material.WALL_SIGN)) {
         Sign sign = (Sign)event.getClickedBlock().getState();
         BaseIC IC = this.getIC(sign.getLine(1).toUpperCase());
         if(IC == null) {
            return;
         }

         if(IC instanceof SelftriggeredBaseIC) {
            if(this.STICExists(event.getClickedBlock().getLocation())) {
               IC.onLeftClick(event.getPlayer(), sign);
               return;
            }

            ChatUtils.printInfo(event.getPlayer(), "[FB-IC]", ChatColor.GRAY, "This selftriggered IC is not initialized. Rightlick to initialize it.");
            return;
         }

         IC.onLeftClick(event.getPlayer(), sign);
         IC = null;
      }

   }

   public void handleRightClick(PlayerInteractEvent event) {
      if(event.getClickedBlock().getType().equals(Material.WALL_SIGN)) {
         Sign sign = (Sign)event.getClickedBlock().getState();
         BaseIC IC = this.getIC(sign.getLine(1).toUpperCase());
         if(IC == null) {
            return;
         }

         if(IC instanceof SelftriggeredBaseIC) {
            SelftriggeredBaseIC thisIC = (SelftriggeredBaseIC)IC;
            if(this.STICExists(event.getClickedBlock().getLocation())) {
               IC.onRightClick(event.getPlayer(), sign);
               return;
            }

            event.setUseInteractedBlock(Result.DENY);
            event.setUseItemInHand(Result.DENY);
            event.setCancelled(true);
            boolean startUpComplete = false;
            boolean initComplete = false;

            try {
               thisIC = (SelftriggeredBaseIC)thisIC.getClass().newInstance();
               initComplete = thisIC.initIC(this.plugin, event.getClickedBlock().getLocation());
               startUpComplete = thisIC.onLoad(sign.getLines());
               if(startUpComplete && initComplete) {
                  thisIC.initCore();
                  this.persistence.addSelftriggeredICToDB(thisIC.getTypeID(), event.getClickedBlock().getLocation());
                  if(thisIC.isExecuteOnTick()) {
                     this.SensorList.put(event.getClickedBlock().getLocation().toString(), thisIC);
                  } else {
                     this.nonTriggeringSensorList.put(event.getClickedBlock().getLocation().toString(), thisIC);
                  }

                  thisIC.notifyCreationSuccess(event.getPlayer());
               } else {
                  ChatUtils.printError(event.getPlayer(), "[FB-IC]", "Could not recreate the IC.");
               }
            } catch (Exception var8) {
               var8.printStackTrace();
               ChatUtils.printError(event.getPlayer(), "[FB-IC]", "Error while recreating " + thisIC.getICNumber() + "!");
            }
         } else {
            IC.onRightClick(event.getPlayer(), sign);
         }

         IC = null;
      }

   }

   public HashMap getRegisteredTICs() {
      HashMap result = new HashMap();
      result.putAll(this.registeredTICs);
      return result;
   }

   public int getSensorListSize() {
      return this.SensorList.size() + this.nonTriggeringSensorList.size();
   }

   public int getRegisteredTICsSize() {
      return this.registeredTICs.size();
   }

   public int getRegisteredSTICsSize() {
      return this.registeredSTICs.size();
   }

   public HashMap getRegisteredSTICs() {
      HashMap result = new HashMap();
      result.putAll(this.registeredSTICs);
      return result;
   }

   public Set getRegisteredSTICsEntrys() {
      return this.registeredSTICs.entrySet();
   }

   public Iterator getSensorListIterator() {
      return this.SensorList.values().iterator();
   }

   public Iterator getNonTriggeringSensorListIterator() {
      return this.nonTriggeringSensorList.values().iterator();
   }

   public void executeSTICs() {
      long start = System.nanoTime();
      Iterator iterator = this.getSensorListIterator();

      SelftriggeredBaseIC IC;
      while(iterator.hasNext()) {
         IC = (SelftriggeredBaseIC)iterator.next();
         if(IC.validateIC()) {
            IC.Execute();
         }
      }

      IC = null;
      this.statistic.update(System.nanoTime() - start);
   }

   public void clearSensorList() {
      this.SensorList = new ConcurrentHashMap();
      this.nonTriggeringSensorList = new ConcurrentHashMap();
   }

   public void clearFailedICs() {
      this.failedICs = new ArrayList();
   }
}
