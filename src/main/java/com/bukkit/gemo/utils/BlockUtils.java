package com.bukkit.gemo.utils;

import com.bukkit.gemo.utils.ChatUtils;
import com.bukkit.gemo.utils.FBBlockType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Dispenser;
import org.bukkit.block.Furnace;
import org.bukkit.craftbukkit.v1_5_R3.CraftWorld;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockUtils {

   private static Map matList = null;
   private static HashSet complexBlockList = new HashSet();


   public static void cancelBlockPlace(BlockPlaceEvent event, String reason) {
      event.setBuild(false);
      event.setCancelled(true);
      ChatUtils.printError(event.getPlayer(), "", reason);
   }

   public static int getRawTypeID(World world, int x, int y, int z) {
      return ((CraftWorld)world).getHandle().getTypeId(x, y, z);
   }

   public static int getRawTypeID(Location location) {
      return ((CraftWorld)location.getWorld()).getHandle().getTypeId(location.getBlockX(), location.getBlockY(), location.getBlockZ());
   }

   public static int getRawSubID(World world, int x, int y, int z) {
      return ((CraftWorld)world).getHandle().getData(x, y, z);
   }

   public static int getRawSubID(Location location) {
      return ((CraftWorld)location.getWorld()).getHandle().getData(location.getBlockX(), location.getBlockY(), location.getBlockZ());
   }

   public static int getMaxStackSize(Material material) {
      return material.getMaxStackSize();
   }

   public static Block getLeverAnchor(Location location, byte data) {
      if(location.getBlock().getTypeId() != Material.LEVER.getId()) {
         return location.getBlock();
      } else {
         byte l = location.getBlock().getData();
         int correctedData = l & 7;
         return correctedData == 1?location.getBlock().getRelative(-1, 0, 0):(correctedData == 2?location.getBlock().getRelative(1, 0, 0):(correctedData == 3?location.getBlock().getRelative(0, 0, -1):(correctedData == 4?location.getBlock().getRelative(0, 0, 1):location.getBlock().getRelative(0, -1, 0))));
      }
   }

   public static int getMaxStackSize(int ID) {
      return Material.getMaterial(ID) == null?0:Material.getMaterial(ID).getMaxStackSize();
   }

   public static boolean isInRange(Location otherLocation, Location baseLocation, int range) {
      return otherLocation.getBlockX() >= baseLocation.getBlockX() - range && otherLocation.getBlockX() <= baseLocation.getBlockX() + range && otherLocation.getBlockZ() >= baseLocation.getBlockZ() - range && otherLocation.getBlockZ() <= baseLocation.getBlockZ() + range && otherLocation.getBlockY() >= baseLocation.getBlockY() - range && otherLocation.getBlockY() <= baseLocation.getBlockY() + range;
   }

   public static Chest isDoubleChest(Block chestBlock) {
      if(chestBlock.getTypeId() != Material.CHEST.getId()) {
         return null;
      } else {
         ArrayList neighbours = getDirectNeighbours(chestBlock, false);

         for(int i = 0; i < neighbours.size(); ++i) {
            if(((Block)neighbours.get(i)).getTypeId() == Material.CHEST.getId()) {
               return (Chest)((Block)neighbours.get(i)).getState();
            }
         }

         return null;
      }
   }

   public static ArrayList getAdjacentChests(Block block) {
      return getAdjacentChests(block.getLocation().getWorld(), block.getLocation().getBlockX(), block.getLocation().getBlockY(), block.getLocation().getBlockZ());
   }

   public static ArrayList getAdjacentChests(Location location) {
      return getAdjacentChests(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
   }

   public static ArrayList getAdjacentChests(World world, int x, int y, int z) {
      ArrayList list = new ArrayList();
      if(getRawTypeID(world, x + 1, y, z) == Material.CHEST.getId()) {
         list.add((Chest)world.getBlockAt(x + 1, y, z).getState());
      }

      if(getRawTypeID(world, x - 1, y, z) == Material.CHEST.getId()) {
         list.add((Chest)world.getBlockAt(x - 1, y, z).getState());
      }

      if(getRawTypeID(world, x, y, z + 1) == Material.CHEST.getId()) {
         list.add((Chest)world.getBlockAt(x, y, z + 1).getState());
      }

      if(getRawTypeID(world, x, y, z - 1) == Material.CHEST.getId()) {
         list.add((Chest)world.getBlockAt(x, y, z - 1).getState());
      }

      return list;
   }

   public static ArrayList addAdjacentChests(ArrayList list, Location location) {
      return addAdjacentChests(list, location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
   }

   public static ArrayList addAdjacentChests(ArrayList list, Block block) {
      return addAdjacentChests(list, block.getLocation().getWorld(), block.getLocation().getBlockX(), block.getLocation().getBlockY(), block.getLocation().getBlockZ());
   }

   public static ArrayList addAdjacentChests(ArrayList list, World world, int x, int y, int z) {
      if(getRawTypeID(world, x + 1, y, z) == Material.CHEST.getId()) {
         list.add((Chest)world.getBlockAt(x + 1, y, z).getState());
      }

      if(getRawTypeID(world, x - 1, y, z) == Material.CHEST.getId()) {
         list.add((Chest)world.getBlockAt(x - 1, y, z).getState());
      }

      if(getRawTypeID(world, x, y, z + 1) == Material.CHEST.getId()) {
         list.add((Chest)world.getBlockAt(x, y, z + 1).getState());
      }

      if(getRawTypeID(world, x, y, z - 1) == Material.CHEST.getId()) {
         list.add((Chest)world.getBlockAt(x, y, z - 1).getState());
      }

      return list;
   }

   public static ArrayList getAdjacentDispenser(World world, int x, int y, int z) {
      ArrayList list = new ArrayList();
      if(getRawTypeID(world, x + 1, y, z) == Material.DISPENSER.getId()) {
         list.add((Dispenser)world.getBlockAt(x + 1, y, z).getState());
      }

      if(getRawTypeID(world, x - 1, y, z) == Material.DISPENSER.getId()) {
         list.add((Dispenser)world.getBlockAt(x - 1, y, z).getState());
      }

      if(getRawTypeID(world, x, y, z + 1) == Material.DISPENSER.getId()) {
         list.add((Dispenser)world.getBlockAt(x, y, z + 1).getState());
      }

      if(getRawTypeID(world, x, y, z - 1) == Material.DISPENSER.getId()) {
         list.add((Dispenser)world.getBlockAt(x, y, z - 1).getState());
      }

      return list;
   }

   public static ArrayList getAdjacentDispenser(ArrayList list, Location location) {
      return addAdjacentDispenser(list, location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
   }

   public static ArrayList getAdjacentDispenser(ArrayList list, Block block) {
      return addAdjacentDispenser(list, block.getLocation().getWorld(), block.getLocation().getBlockX(), block.getLocation().getBlockY(), block.getLocation().getBlockZ());
   }

   public static ArrayList addAdjacentDispenser(ArrayList list, World world, int x, int y, int z) {
      if(getRawTypeID(world, x + 1, y, z) == Material.DISPENSER.getId()) {
         list.add((Dispenser)world.getBlockAt(x + 1, y, z).getState());
      }

      if(getRawTypeID(world, x - 1, y, z) == Material.DISPENSER.getId()) {
         list.add((Dispenser)world.getBlockAt(x - 1, y, z).getState());
      }

      if(getRawTypeID(world, x, y, z + 1) == Material.DISPENSER.getId()) {
         list.add((Dispenser)world.getBlockAt(x, y, z + 1).getState());
      }

      if(getRawTypeID(world, x, y, z - 1) == Material.DISPENSER.getId()) {
         list.add((Dispenser)world.getBlockAt(x, y, z - 1).getState());
      }

      return list;
   }

   public static ArrayList getAdjacentFurnaces(Block block) {
      return getAdjacentFurnaces(block.getLocation().getWorld(), block.getLocation().getBlockX(), block.getLocation().getBlockY(), block.getLocation().getBlockZ());
   }

   public static ArrayList getAdjacentFurnaces(Location location) {
      return getAdjacentFurnaces(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
   }

   public static ArrayList getAdjacentFurnaces(World world, int x, int y, int z) {
      ArrayList list = new ArrayList();
      if(getRawTypeID(world, x + 1, y, z) == Material.FURNACE.getId() || getRawTypeID(world, x + 1, y, z) == Material.BURNING_FURNACE.getId()) {
         list.add((Furnace)world.getBlockAt(x + 1, y, z).getState());
      }

      if(getRawTypeID(world, x - 1, y, z) == Material.FURNACE.getId() || getRawTypeID(world, x - 1, y, z) == Material.BURNING_FURNACE.getId()) {
         list.add((Furnace)world.getBlockAt(x - 1, y, z).getState());
      }

      if(getRawTypeID(world, x, y, z + 1) == Material.FURNACE.getId() || getRawTypeID(world, x, y, z + 1) == Material.BURNING_FURNACE.getId()) {
         list.add((Furnace)world.getBlockAt(x, y, z + 1).getState());
      }

      if(getRawTypeID(world, x, y, z - 1) == Material.FURNACE.getId() || getRawTypeID(world, x, y, z - 1) == Material.BURNING_FURNACE.getId()) {
         list.add((Furnace)world.getBlockAt(x, y, z - 1).getState());
      }

      return list;
   }

   public static ArrayList addAdjacentFurnaces(ArrayList list, World world, int x, int y, int z) {
      if(getRawTypeID(world, x + 1, y, z) == Material.FURNACE.getId() || getRawTypeID(world, x + 1, y, z) == Material.BURNING_FURNACE.getId()) {
         list.add((Furnace)world.getBlockAt(x + 1, y, z).getState());
      }

      if(getRawTypeID(world, x - 1, y, z) == Material.FURNACE.getId() || getRawTypeID(world, x - 1, y, z) == Material.BURNING_FURNACE.getId()) {
         list.add((Furnace)world.getBlockAt(x - 1, y, z).getState());
      }

      if(getRawTypeID(world, x, y, z + 1) == Material.FURNACE.getId() || getRawTypeID(world, x, y, z + 1) == Material.BURNING_FURNACE.getId()) {
         list.add((Furnace)world.getBlockAt(x, y, z + 1).getState());
      }

      if(getRawTypeID(world, x, y, z - 1) == Material.FURNACE.getId() || getRawTypeID(world, x, y, z - 1) == Material.BURNING_FURNACE.getId()) {
         list.add((Furnace)world.getBlockAt(x, y, z - 1).getState());
      }

      return list;
   }

   public static ArrayList addAdjacentFurnaces(ArrayList list, Location location) {
      return addAdjacentFurnaces(list, location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
   }

   public static ArrayList addAdjacentFurnaces(ArrayList list, Block block) {
      return addAdjacentFurnaces(list, block.getLocation().getWorld(), block.getLocation().getBlockX(), block.getLocation().getBlockY(), block.getLocation().getBlockZ());
   }

   public static boolean isValidItemID(String name) {
      String[] split = name.split(":");
      return split.length == 1?isValidItemID(name, (short)0):isValidItemID(split[0], Short.valueOf(split[1]).shortValue());
   }

   public static String getIDPart(String input) {
      return input.split(":").length >= 1?input.split(":")[0]:input;
   }

   public static short getDataPart(String input) {
      try {
         return Short.valueOf(input.split(":")[1]).shortValue();
      } catch (Exception var2) {
         return (short)0;
      }
   }

   private static void initMatList() {
      if(matList == null) {
         matList = new HashMap();
         Material[] var3;
         int var2 = (var3 = Material.values()).length;

         for(int var1 = 0; var1 < var2; ++var1) {
            Material mat = var3[var1];
            String newName = mat.name().replace("_", "").replace(" ", "").toLowerCase();
            matList.put(mat.name().toLowerCase(), Integer.valueOf(mat.getId()));
            if(!mat.name().toLowerCase().equalsIgnoreCase(newName)) {
               matList.put(newName, Integer.valueOf(mat.getId()));
            }
         }
      }

   }

   public static int getIDFromMatList(String itemName) {
      if(!matList.containsKey(itemName)) {
         return 0;
      } else {
         String newName = itemName.toLowerCase().replace("_", "").replace(" ", "");
         return newName.equalsIgnoreCase(itemName)?((Integer)matList.get(itemName.toLowerCase())).intValue():((Integer)matList.get(newName.toLowerCase())).intValue();
      }
   }

   public static boolean isInteger(String input) {
      try {
         Integer.parseInt(getIDPart(input));
         return true;
      } catch (Exception var2) {
         return false;
      }
   }

   public static boolean isValidItemID(int TypeID) {
      return isValidItemID(TypeID, (short)0);
   }

   public static boolean isValidItemID(String name, short Data) {
      if(isInteger(name)) {
         return isValidItemID(Integer.valueOf(name).intValue(), Data);
      } else {
         initMatList();
         return !matList.containsKey(name.toLowerCase())?false:isValidItemID(((Integer)matList.get(name.toLowerCase())).intValue(), Data);
      }
   }

   public static boolean isValidItemID(int TypeID, short Data) {
      initMatList();
      Material mat = Material.getMaterial(TypeID);
      if(mat == null) {
         mat = null;
         return false;
      } else {
         return TypeID == Material.POTION.getId()?true:(Data >= 0 && Data <= 15?(mat.getId() != Material.WOOL.getId() && mat.getId() != Material.INK_SACK.getId()?(mat.getId() != Material.LOG.getId() && mat.getId() != Material.MONSTER_EGGS.getId() && mat.getId() != Material.SMOOTH_BRICK.getId() && mat.getId() != Material.LEAVES.getId() && mat.getId() != Material.JUKEBOX.getId() && mat.getId() != Material.SAPLING.getId() && mat.getId() != Material.LONG_GRASS.getId() && mat.getId() != Material.DEAD_BUSH.getId()?(mat.getId() != Material.DOUBLE_STEP.getId() && mat.getId() != Material.STEP.getId()?(mat.getId() == Material.CROPS.getId()?Data <= 7:(mat.getId() == Material.COAL.getId()?Data <= 1:Data == 0)):Data <= 6):Data <= 2):true):false);
      }
   }

   public static boolean isValidBlock(int ID) {
      Material[] all = Material.values();

      for(int i = 0; i < all.length; ++i) {
         if(all[i].getId() == ID && all[i].isBlock()) {
            return true;
         }
      }

      return false;
   }

   public static boolean isValidBlock(String name) {
      try {
         return isValidBlock(Integer.valueOf(name).intValue());
      } catch (Exception var4) {
         Material[] all = Material.values();

         for(int i = 0; i < all.length; ++i) {
            if(all[i].name().equalsIgnoreCase(name) && all[i].isBlock()) {
               return true;
            }
         }

         return false;
      }
   }

   public static FBBlockType getItemFromString(String line) {
      String[] split = line.split(":");
      int e;
      int ID;
      if(split.length > 1) {
         try {
            e = Integer.valueOf(split[0]).intValue();
            short ID1 = Short.valueOf(split[1]).shortValue();
            return !isValidItemID(e, ID1)?null:(e < 1?null:new FBBlockType(e, ID1));
         } catch (Exception var5) {
            ID = getIDFromMatList(split[0]);
            short SubID = Short.valueOf(split[1]).shortValue();
            return !isValidItemID(ID, SubID)?null:(ID < 1?null:new FBBlockType(ID, SubID));
         }
      } else {
         try {
            e = Integer.valueOf(split[0]).intValue();
            return !isValidItemID(e)?null:(e < 1?null:new FBBlockType(e));
         } catch (Exception var6) {
            ID = getIDFromMatList(line);
            return !isValidItemID(ID)?null:(ID < 1?null:new FBBlockType(ID));
         }
      }
   }

   public static int getItemIDFromName(String name) {
      Material[] all = Material.values();
      if(name == null) {
         return -1;
      } else {
         try {
            String[] e = name.split(":");

            for(int i = 0; i < all.length; ++i) {
               if(all[i].name().equalsIgnoreCase(e[0])) {
                  if(all[i].getId() == Material.WOOL.getId() && e.length > 1 && Integer.valueOf(e[1]).intValue() >= 0 && Integer.valueOf(e[1]).intValue() <= 15) {
                     return all[i].getId();
                  }

                  if(all[i].getId() == Material.INK_SACK.getId() && e.length > 1) {
                     if(Integer.valueOf(e[1]).intValue() >= 0 && Integer.valueOf(e[1]).intValue() <= 15) {
                        return all[i].getId();
                     }
                  } else if(all[i].getId() == Material.STEP.getId() && e.length > 1) {
                     if(Integer.valueOf(e[1]).intValue() >= 0 && Integer.valueOf(e[1]).intValue() <= 3) {
                        return all[i].getId();
                     }
                  } else if(all[i].getId() == Material.LOG.getId() && e.length > 1) {
                     if(Integer.valueOf(e[1]).intValue() >= 0 && Integer.valueOf(e[1]).intValue() <= 2) {
                        return all[i].getId();
                     }
                  } else if(all[i].getId() == Material.SAPLING.getId() && e.length > 1) {
                     if(Integer.valueOf(e[1]).intValue() >= 0 && Integer.valueOf(e[1]).intValue() <= 2) {
                        return all[i].getId();
                     }
                  } else {
                     if(all[i].getId() != Material.COAL.getId() || e.length <= 1) {
                        return all[i].getId();
                     }

                     if(Integer.valueOf(e[1]).intValue() >= 0 && Integer.valueOf(e[1]).intValue() <= 1) {
                        return all[i].getId();
                     }
                  }
               }
            }

            return -1;
         } catch (Exception var4) {
            return -1;
         }
      }
   }

   public static boolean canPassThrough(int id) {
      return id == Material.AIR.getId() || id == Material.SAPLING.getId() || id == Material.YELLOW_FLOWER.getId() || id == Material.RED_ROSE.getId() || id == Material.BROWN_MUSHROOM.getId() || id == Material.RED_MUSHROOM.getId() || id == Material.TORCH.getId() || id == Material.FIRE.getId() || id == Material.REDSTONE_WIRE.getId() || id == Material.CROPS.getId() || id == Material.SIGN_POST.getId() || id == Material.LADDER.getId() || id == Material.RAILS.getId() || id == Material.DETECTOR_RAIL.getId() || id == Material.POWERED_RAIL.getId() || id == Material.WALL_SIGN.getId() || id == Material.LEVER.getId() || id == Material.STONE_PLATE.getId() || id == Material.WOOD_PLATE.getId() || id == Material.REDSTONE_TORCH_OFF.getId() || id == Material.REDSTONE_TORCH_ON.getId() || id == Material.STONE_BUTTON.getId() || id == Material.SNOW.getId() || id == Material.SUGAR_CANE.getId() || id == Material.PORTAL.getId() || id == Material.DIODE.getId() || id == Material.TRAP_DOOR.getId() || id == Material.WEB.getId() || id == Material.LONG_GRASS.getId() || id == Material.DEAD_BUSH.getId() || id == Material.WHEAT.getId();
   }

   public static boolean LocationEquals(Location firstLocation, Location secondLocation) {
      return firstLocation.getBlockX() == secondLocation.getBlockX() && firstLocation.getBlockY() == secondLocation.getBlockY() && firstLocation.getBlockZ() == secondLocation.getBlockZ() && firstLocation.getWorld().getName().equalsIgnoreCase(secondLocation.getWorld().getName());
   }

   public static String LocationToString(Location location) {
      return location.getWorld().getName() + ";" + location.getBlockX() + ";" + location.getBlockY() + ";" + location.getBlockZ();
   }

   public static Location LocationFromString(String text) {
      if(text == null) {
         return null;
      } else {
         text = text.trim();
         String[] split = text.split(";");
         if(split.length != 4) {
            return null;
         } else {
            World world = Bukkit.getServer().getWorld(split[0]);
            if(world == null) {
               return null;
            } else {
               try {
                  return new Location(world, (double)Integer.valueOf(split[1]).intValue(), (double)Integer.valueOf(split[2]).intValue(), (double)Integer.valueOf(split[3]).intValue(), 0.0F, 0.0F);
               } catch (Exception var4) {
                  return null;
               }
            }
         }
      }
   }

   public static Block BlockFromLocationString(String text) {
      Location loc = LocationFromString(text);
      return loc != null?loc.getBlock():null;
   }

   public static boolean isComplexBlock(int id) {
      if(complexBlockList.size() == 0) {
         complexBlockList.add(Integer.valueOf(Material.WALL_SIGN.getId()));
         complexBlockList.add(Integer.valueOf(Material.SIGN_POST.getId()));
         complexBlockList.add(Integer.valueOf(Material.TORCH.getId()));
         complexBlockList.add(Integer.valueOf(Material.REDSTONE_TORCH_ON.getId()));
         complexBlockList.add(Integer.valueOf(Material.REDSTONE_TORCH_OFF.getId()));
         complexBlockList.add(Integer.valueOf(Material.REDSTONE_WIRE.getId()));
         complexBlockList.add(Integer.valueOf(Material.RED_ROSE.getId()));
         complexBlockList.add(Integer.valueOf(Material.YELLOW_FLOWER.getId()));
         complexBlockList.add(Integer.valueOf(Material.BROWN_MUSHROOM.getId()));
         complexBlockList.add(Integer.valueOf(Material.RED_MUSHROOM.getId()));
         complexBlockList.add(Integer.valueOf(Material.LEVER.getId()));
         complexBlockList.add(Integer.valueOf(Material.GRAVEL.getId()));
         complexBlockList.add(Integer.valueOf(Material.SAND.getId()));
         complexBlockList.add(Integer.valueOf(Material.RAILS.getId()));
         complexBlockList.add(Integer.valueOf(Material.POWERED_RAIL.getId()));
         complexBlockList.add(Integer.valueOf(Material.DETECTOR_RAIL.getId()));
         complexBlockList.add(Integer.valueOf(Material.WOODEN_DOOR.getId()));
         complexBlockList.add(Integer.valueOf(Material.IRON_DOOR.getId()));
         complexBlockList.add(Integer.valueOf(Material.WOOD_DOOR.getId()));
         complexBlockList.add(Integer.valueOf(Material.IRON_DOOR_BLOCK.getId()));
         complexBlockList.add(Integer.valueOf(Material.BED_BLOCK.getId()));
         complexBlockList.add(Integer.valueOf(Material.BED.getId()));
         complexBlockList.add(Integer.valueOf(Material.WOODEN_DOOR.getId()));
         complexBlockList.add(Integer.valueOf(Material.SEEDS.getId()));
         complexBlockList.add(Integer.valueOf(Material.CROPS.getId()));
         complexBlockList.add(Integer.valueOf(Material.STONE_PLATE.getId()));
         complexBlockList.add(Integer.valueOf(Material.WOOD_PLATE.getId()));
         complexBlockList.add(Integer.valueOf(Material.CACTUS.getId()));
         complexBlockList.add(Integer.valueOf(Material.STONE_BUTTON.getId()));
         complexBlockList.add(Integer.valueOf(Material.DIODE_BLOCK_OFF.getId()));
         complexBlockList.add(Integer.valueOf(Material.DIODE_BLOCK_ON.getId()));
         complexBlockList.add(Integer.valueOf(Material.CAKE_BLOCK.getId()));
         complexBlockList.add(Integer.valueOf(Material.TRAP_DOOR.getId()));
         complexBlockList.add(Integer.valueOf(Material.DEAD_BUSH.getId()));
         complexBlockList.add(Integer.valueOf(Material.LONG_GRASS.getId()));
         complexBlockList.add(Integer.valueOf(Material.FIRE.getId()));
         complexBlockList.add(Integer.valueOf(Material.LADDER.getId()));
         complexBlockList.add(Integer.valueOf(Material.SUGAR_CANE.getId()));
         complexBlockList.add(Integer.valueOf(Material.SUGAR_CANE_BLOCK.getId()));
         complexBlockList.add(Integer.valueOf(Material.SNOW_BLOCK.getId()));
         complexBlockList.add(Integer.valueOf(Material.PORTAL.getId()));
         complexBlockList.add(Integer.valueOf(Material.IRON_FENCE.getId()));
         complexBlockList.add(Integer.valueOf(Material.THIN_GLASS.getId()));
         complexBlockList.add(Integer.valueOf(Material.TRAP_DOOR.getId()));
         complexBlockList.add(Integer.valueOf(Material.PUMPKIN_STEM.getId()));
         complexBlockList.add(Integer.valueOf(Material.MELON_STEM.getId()));
         complexBlockList.add(Integer.valueOf(Material.FENCE_GATE.getId()));
         complexBlockList.add(Integer.valueOf(Material.NETHER_WARTS.getId()));
         complexBlockList.add(Integer.valueOf(Material.BREWING_STAND.getId()));
         complexBlockList.add(Integer.valueOf(Material.CAULDRON.getId()));
         complexBlockList.add(Integer.valueOf(Material.WATER_LILY.getId()));
      }

      return complexBlockList.contains(Integer.valueOf(id));
   }

   public static boolean isTileEntity(int ID) {
      return ID == Material.CHEST.getId() || ID == Material.SIGN_POST.getId() || ID == Material.WALL_SIGN.getId() || ID == Material.DISPENSER.getId() || ID == Material.MOB_SPAWNER.getId() || ID == Material.FURNACE.getId() || ID == Material.BURNING_FURNACE.getId() || ID == Material.NOTE_BLOCK.getId();
   }

   public static boolean isComplexBlock(Block block) {
      return isComplexBlock(block.getTypeId());
   }

   public static ArrayList ListContainsBlock(ArrayList list, Block block) {
      ArrayList result = new ArrayList();

      for(int i = 0; i < list.size(); ++i) {
         if(((Block)list.get(i)).equals(block)) {
            result.add(Boolean.valueOf(true));
            result.add(block);
            break;
         }
      }

      if(result.size() == 0) {
         result.add(Boolean.valueOf(false));
         result.add((Object)null);
      }

      return result;
   }

   public static ArrayList ListContainsBlockType(ArrayList list, Material material) {
      ArrayList result = new ArrayList();

      for(int i = 0; i < list.size(); ++i) {
         if(((Block)list.get(i)).getType().equals(material)) {
            result.add(Boolean.valueOf(true));
            result.add(list.get(i));
            break;
         }
      }

      if(result.size() == 0) {
         result.add(Boolean.valueOf(false));
         result.add((Object)null);
      }

      return result;
   }

   public static ArrayList ListContainsBlockTypeID(ArrayList list, int blockID) {
      ArrayList result = new ArrayList();

      for(int i = 0; i < list.size(); ++i) {
         if(((Block)list.get(i)).getTypeId() == blockID) {
            result.add(Boolean.valueOf(true));
            result.add(list.get(i));
            break;
         }
      }

      if(result.size() == 0) {
         result.add(Boolean.valueOf(false));
         result.add((Object)null);
      }

      return result;
   }

   public static ArrayList getNeighbourBlocks(Block block) {
      ArrayList list = new ArrayList();
      World w = block.getWorld();
      int x = block.getX();
      int z = block.getZ();
      int y = block.getY();
      list.add(w.getBlockAt(x - 1, y, z));
      list.add(w.getBlockAt(x + 1, y, z));
      list.add(w.getBlockAt(x, y, z - 1));
      list.add(w.getBlockAt(x, y, z + 1));
      list.add(w.getBlockAt(x, y + 1, z));
      list.add(w.getBlockAt(x, y - 1, z));
      list.add(w.getBlockAt(x - 1, y, z - 1));
      list.add(w.getBlockAt(x + 1, y, z + 1));
      list.add(w.getBlockAt(x - 1, y, z + 1));
      list.add(w.getBlockAt(x + 1, y, z - 1));
      list.add(w.getBlockAt(x - 1, y + 1, z));
      list.add(w.getBlockAt(x + 1, y + 1, z));
      list.add(w.getBlockAt(x, y + 1, z - 1));
      list.add(w.getBlockAt(x, y + 1, z + 1));
      list.add(w.getBlockAt(x - 1, y - 1, z));
      list.add(w.getBlockAt(x + 1, y - 1, z));
      list.add(w.getBlockAt(x, y - 1, z - 1));
      list.add(w.getBlockAt(x, y - 1, z + 1));
      list.add(w.getBlockAt(x - 1, y + 1, z - 1));
      list.add(w.getBlockAt(x + 1, y + 1, z + 1));
      list.add(w.getBlockAt(x - 1, y + 1, z + 1));
      list.add(w.getBlockAt(x + 1, y + 1, z - 1));
      list.add(w.getBlockAt(x - 1, y - 1, z - 1));
      list.add(w.getBlockAt(x + 1, y - 1, z + 1));
      list.add(w.getBlockAt(x - 1, y - 1, z + 1));
      list.add(w.getBlockAt(x + 1, y - 1, z - 1));
      return list;
   }

   public static ArrayList getDirectNeighbours(Block block, boolean UpAndDown) {
      ArrayList result = new ArrayList();
      result.add(block.getRelative(1, 0, 0));
      result.add(block.getRelative(-1, 0, 0));
      result.add(block.getRelative(0, 0, 1));
      result.add(block.getRelative(0, 0, -1));
      if(UpAndDown) {
         result.add(block.getRelative(0, 1, 0));
         result.add(block.getRelative(0, -1, 0));
      }

      return result;
   }

   public static boolean isLow(Location location) {
      return !isPowered(location);
   }

   public static boolean isBlockPowered(Block block) {
      return block.isBlockIndirectlyPowered() || block.isBlockPowered();
   }

   public static boolean isPowered(Location location) {
      return getRawTypeID(location) == Material.REDSTONE_WIRE.getId()?getRawSubID(location) > 0:(getRawTypeID(location) == Material.LEVER.getId()?getRawSubID(location) > 8:(getRawTypeID(location) != Material.WOOD_PLATE.getId() && getRawTypeID(location) != Material.STONE_PLATE.getId()?(getRawTypeID(location) == Material.DIODE_BLOCK_ON.getId()?true:getRawTypeID(location) == Material.REDSTONE_TORCH_ON.getId()):getRawSubID(location) > 0));
   }
}
