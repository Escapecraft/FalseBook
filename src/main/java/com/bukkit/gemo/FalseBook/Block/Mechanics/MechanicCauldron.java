package com.bukkit.gemo.FalseBook.Block.Mechanics;

import com.bukkit.gemo.FalseBook.Block.FalseBookBlockCore;
import com.bukkit.gemo.FalseBook.Block.Cauldrons.CauldronRecipe;
import com.bukkit.gemo.FalseBook.Block.Config.ConfigHandler;
import com.bukkit.gemo.FalseBook.Mechanics.MechanicListener;
import com.bukkit.gemo.utils.BlockUtils;
import com.bukkit.gemo.utils.SignUtils;
import com.bukkit.gemo.utils.UtilPermissions;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class MechanicCauldron extends MechanicListener {

   private ArrayList Recipes = new ArrayList();
   private HashMap cauldronsCooldown = new HashMap();


   public MechanicCauldron(FalseBookBlockCore plugin) {
      plugin.getMechanicHandler().registerEvent(PlayerInteractEvent.class, this);
   }

   public void onLoad() {
      this.loadCauldrons("FalseBook" + System.getProperty("file.separator") + "Cauldrons.txt");
   }

   public void reloadMechanic() {
      this.Recipes = new ArrayList();
      this.loadCauldrons("FalseBook" + System.getProperty("file.separator") + "Cauldrons.txt");
   }

   public void onPlayerInteract(PlayerInteractEvent event, boolean isWallSign, boolean isSignPost) {
      if(!event.isCancelled()) {
         if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            Block block = event.getClickedBlock();
            if(this.isCauldronSpace(event.getClickedBlock())) {
               event.setUseInteractedBlock(Result.DENY);
               event.setUseItemInHand(Result.DENY);
               event.setCancelled(true);
               this.handleCauldron(event.getClickedBlock(), player);
            } else if(ConfigHandler.isAllowMinecraftCauldron(block.getWorld().getName()) && event.getClickedBlock().getTypeId() == Material.CAULDRON.getId()) {
               this.handleCauldron(event.getClickedBlock().getRelative(0, -1, 0), player);
            }

         }
      }
   }

   private boolean loadCauldrons(String FileName) {
      File file = new File("plugins" + System.getProperty("file.separator") + FileName);
      if(file.exists()) {
         try {
            FileInputStream e = new FileInputStream(file);
            DataInputStream in = new DataInputStream(e);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine = "";

            while((strLine = br.readLine()) != null) {
               strLine = strLine.trim();
               String[] split = strLine.split(";");
               if(split.length == 3) {
                  ArrayList iList = SignUtils.parseLineToItemList(split[1], ",", false);
                  ArrayList rList = SignUtils.parseLineToItemList(split[2], ",", false);
                  if(iList != null & rList != null && iList.size() > 0 && rList.size() > 0) {
                     this.Recipes.add(new CauldronRecipe(split[0], iList, rList));
                  }
               }
            }

            FalseBookBlockCore.printInConsole(this.Recipes.size() + " Cauldrons successfully loaded.");
            return true;
         } catch (Exception var10) {
            var10.printStackTrace();
            FalseBookBlockCore.printInConsole("Error while reading file: plugins/" + FileName);
            return false;
         }
      } else {
         FalseBookBlockCore.printInConsole("No Cauldrons loaded!");
         return false;
      }
   }

   private boolean isCauldronSpace(Block block) {
      return block.getType().equals(Material.GLASS) && (block.getRelative(0, -1, 0).getType().equals(Material.LAVA) || block.getRelative(0, -1, 0).getType().equals(Material.STATIONARY_LAVA)) && block.getRelative(1, 0, 0).getType().equals(Material.STONE) && block.getRelative(-1, 0, 0).getType().equals(Material.STONE) && block.getRelative(0, 0, 1).getType().equals(Material.STONE) && block.getRelative(0, 0, -1).getType().equals(Material.STONE) && block.getRelative(1, 1, 0).getType().equals(Material.STONE) && block.getRelative(-1, 1, 0).getType().equals(Material.STONE) && block.getRelative(0, 1, 1).getType().equals(Material.STONE) && block.getRelative(0, 1, -1).getType().equals(Material.STONE) && block.getRelative(1, -1, 0).getType().equals(Material.STONE) && block.getRelative(-1, -1, 0).getType().equals(Material.STONE) && block.getRelative(0, -1, 1).getType().equals(Material.STONE) && block.getRelative(0, -1, -1).getType().equals(Material.STONE);
   }

   private boolean handleCauldron(Block block, Player player) {
      long thisTime = System.currentTimeMillis();
      if(!this.cauldronsCooldown.containsKey(block.getLocation().toString())) {
         this.cauldronsCooldown.put(block.getLocation().toString(), Long.valueOf(thisTime - (long)((ConfigHandler.getCauldronCoolDownTime(block.getWorld().getName()) + 1) * 1000)));
      }

      if(!UtilPermissions.playerCanUseCommand(player, "falsebook.blocks.cauldron.use")) {
         player.sendMessage(ChatColor.RED + "You are not allowed to use Cauldrons!");
         return false;
      } else if(thisTime < ((Long)this.cauldronsCooldown.get(block.getLocation().toString())).longValue() + (long)(ConfigHandler.getCauldronCoolDownTime(block.getWorld().getName()) * 1000)) {
         player.sendMessage(ChatColor.RED + "This cauldron is cooling down.");
         return false;
      } else {
         this.cauldronsCooldown.put(block.getLocation().toString(), Long.valueOf(thisTime));
         ArrayList itemList = (ArrayList)block.getWorld().getEntitiesByClass(Item.class);

         for(int itemStackList = itemList.size() - 1; itemStackList >= 0; --itemStackList) {
            Location loc = ((Item)itemList.get(itemStackList)).getLocation();
            if(!BlockUtils.LocationEquals(loc, block.getRelative(0, 1, 0).getLocation())) {
               itemList.remove(itemStackList);
            }
         }

         if(itemList.size() < 1) {
            return false;
         } else {
            ArrayList var19 = new ArrayList();

            int VALUE;
            for(int possibleRecipes = 0; possibleRecipes < itemList.size(); ++possibleRecipes) {
               ItemStack done = ((Item)itemList.get(possibleRecipes)).getItemStack().clone();
               boolean result = false;

               for(VALUE = 0; VALUE < var19.size(); ++VALUE) {
                  if(done.getTypeId() == ((ItemStack)var19.get(VALUE)).getTypeId() && done.getDurability() == ((ItemStack)var19.get(VALUE)).getDurability()) {
                     ((ItemStack)var19.get(VALUE)).setAmount(((ItemStack)var19.get(VALUE)).getAmount() + done.getAmount());
                     result = true;
                  }
               }

               if(!result) {
                  var19.add(done);
               }
            }

            HashMap var20 = new HashMap();
            boolean var21 = false;
            ArrayList var22 = null;
            VALUE = -1;

            int maximumIndex;
            for(maximumIndex = 0; maximumIndex < this.Recipes.size(); ++maximumIndex) {
               if(((CauldronRecipe)this.Recipes.get(maximumIndex)).verifyCauldron(var19)) {
                  var20.put(Integer.valueOf(maximumIndex), (CauldronRecipe)this.Recipes.get(maximumIndex));
               }
            }

            maximumIndex = -1;
            int maxIngAmount = -1;
            Iterator returnItems = var20.entrySet().iterator();

            while(returnItems.hasNext()) {
               Entry multiplier = (Entry)returnItems.next();
               if(var20.size() == 1) {
                  var21 = true;
                  VALUE = ((Integer)multiplier.getKey()).intValue();
                  break;
               }

               if(maxIngAmount < ((CauldronRecipe)multiplier.getValue()).getIngredientsSize()) {
                  maxIngAmount = ((CauldronRecipe)multiplier.getValue()).getIngredientsSize();
                  maximumIndex = ((Integer)multiplier.getKey()).intValue();
               }
            }

            if(var20.size() > 1) {
               var21 = true;
               VALUE = maximumIndex;
            }

            if(var21) {
               int var24 = ((CauldronRecipe)this.Recipes.get(VALUE)).getMultiplier(var19);
               var22 = ((CauldronRecipe)this.Recipes.get(VALUE)).getResultItems(var19);
               player.sendMessage(ChatColor.GOLD + "*POOOOOOF* You have made \'" + ((CauldronRecipe)this.Recipes.get(VALUE)).getName() + "\'.");
               Iterator item = var22.iterator();

               while(item.hasNext()) {
                  ItemStack var23 = (ItemStack)item.next();
                  player.getWorld().dropItem(((Item)itemList.get(0)).getLocation(), var23.clone());
               }

               HashMap var26 = new HashMap();

               ItemStack thisI;
               int var25;
               for(var25 = itemList.size() - 1; var25 >= 0; --var25) {
                  ItemStack item1 = ((Item)itemList.get(var25)).getItemStack().clone();
                  if(var26.containsKey(item1.getTypeId() + ":" + item1.getDurability())) {
                     thisI = (ItemStack)var26.get(item1.getTypeId() + ":" + item1.getDurability());
                     thisI.setAmount(thisI.getAmount() + item1.getAmount());
                  } else {
                     var26.put(item1.getTypeId() + ":" + item1.getDurability(), item1);
                  }
               }

               Iterator var27 = var26.values().iterator();

               ItemStack var28;
               while(var27.hasNext()) {
                  var28 = (ItemStack)var27.next();
                  thisI = ((CauldronRecipe)this.Recipes.get(VALUE)).getIngredient(var28);
                  if(thisI != null) {
                     var28.setAmount(var28.getAmount() - var24 * thisI.getAmount());
                  }
               }

               for(var25 = itemList.size() - 1; var25 >= 0; --var25) {
                  ((Item)itemList.get(var25)).remove();
               }

               var27 = var26.values().iterator();

               while(var27.hasNext()) {
                  var28 = (ItemStack)var27.next();
                  if(var28.getAmount() > 0) {
                     player.getWorld().dropItem(((Item)itemList.get(0)).getLocation(), var28);
                  }
               }

               var22.clear();
            } else {
               player.sendMessage(ChatColor.RED + "Recipe not found.");
            }

            var19.clear();
            return var21;
         }
      }
   }
}
