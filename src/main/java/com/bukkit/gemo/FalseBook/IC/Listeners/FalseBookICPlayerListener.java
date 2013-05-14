package com.bukkit.gemo.FalseBook.IC.Listeners;

import com.bukkit.gemo.FalseBook.IC.ICFactory;
import com.bukkit.gemo.utils.ChatUtils;
import com.bukkit.gemo.utils.SignUtils;
import com.bukkit.gemo.utils.UtilPermissions;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_5_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_5_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class FalseBookICPlayerListener implements Listener {

   private ICFactory factory = null;


   public void init(ICFactory factory) {
      this.factory = factory;
   }

   public void ExecuteHiddenSwitch(Block block, Player player, BlockFace face) {
      Sign signBlock = (Sign)block.getState();
      int dir = SignUtils.getDirection(signBlock);
      Block[] neighbours = new Block[8];
      neighbours[0] = block.getWorld().getBlockAt(block.getX(), block.getY() + 1, block.getZ());
      neighbours[1] = block.getWorld().getBlockAt(block.getX(), block.getY() - 1, block.getZ());
      if(dir != 2 && dir != 4) {
         if(dir == 1 || dir == 3) {
            neighbours[2] = block.getWorld().getBlockAt(block.getX() + 1, block.getY(), block.getZ());
            neighbours[3] = block.getWorld().getBlockAt(block.getX() - 1, block.getY(), block.getZ());
            neighbours[4] = block.getWorld().getBlockAt(block.getX() + 1, block.getY() + 1, block.getZ());
            neighbours[5] = block.getWorld().getBlockAt(block.getX() - 1, block.getY() + 1, block.getZ());
            neighbours[6] = block.getWorld().getBlockAt(block.getX() + 1, block.getY() - 1, block.getZ());
            neighbours[7] = block.getWorld().getBlockAt(block.getX() - 1, block.getY() - 1, block.getZ());
         }
      } else {
         neighbours[2] = block.getWorld().getBlockAt(block.getX(), block.getY(), block.getZ() + 1);
         neighbours[3] = block.getWorld().getBlockAt(block.getX(), block.getY(), block.getZ() - 1);
         neighbours[4] = block.getWorld().getBlockAt(block.getX(), block.getY() + 1, block.getZ() + 1);
         neighbours[5] = block.getWorld().getBlockAt(block.getX(), block.getY() + 1, block.getZ() - 1);
         neighbours[6] = block.getWorld().getBlockAt(block.getX(), block.getY() - 1, block.getZ() + 1);
         neighbours[7] = block.getWorld().getBlockAt(block.getX(), block.getY() - 1, block.getZ() - 1);
      }

      if(dir >= 1 && dir <= 4) {
         for(int i = 0; i < 8; ++i) {
            if(neighbours[i].getType().equals(Material.LEVER)) {
               int thisPlayer = neighbours[i].getData();
               if((thisPlayer & 8) != 8) {
                  thisPlayer |= 8;
               } else if((thisPlayer & 8) == 8) {
                  thisPlayer ^= 8;
               }

               neighbours[i].setTypeIdAndData(Material.LEVER.getId(), (byte)thisPlayer, true);
            } else if(neighbours[i].getType().equals(Material.STONE_BUTTON)) {
               CraftPlayer var10 = (CraftPlayer)player;
               CraftWorld cWorld = (CraftWorld)block.getWorld();
               // TODO - this needs to be fixed rather than commented out...
               //net.minecraft.server.v1_5_R3.Block.byId[Material.STONE_BUTTON.getId()].interact(cWorld.getHandle(), neighbours[i].getX(), neighbours[i].getY(), neighbours[i].getZ(), var10.getHandle());
            }
         }
      }

   }

   @EventHandler
   public void onPlayerInteract(PlayerInteractEvent event) {
      if(!event.isCancelled()) {
         if(event.getAction() == Action.LEFT_CLICK_BLOCK) {
            this.factory.handleLeftClick(event);
         } else if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if(event.getPlayer() != null) {
               if(event.getClickedBlock() != null) {
                  int i;
                  if(event.getPlayer().getItemInHand().getType().equals(Material.COAL) && event.getClickedBlock().getType().equals(Material.REDSTONE_WIRE)) {
                     byte face = event.getClickedBlock().getData();
                     String txt = ChatColor.YELLOW + "Ammeter: [";

                     for(i = 0; i < 15; ++i) {
                        if(i >= face) {
                           txt = txt + ChatColor.DARK_GRAY;
                        }

                        txt = txt + ":";
                     }

                     txt = txt + ChatColor.YELLOW + "]";
                     txt = txt + ChatColor.WHITE + " " + face + " A";
                     ChatUtils.printLine(event.getPlayer(), ChatColor.YELLOW, txt);
                  }

                  BlockFace[] var5;
                  i = (var5 = BlockFace.values()).length;

                  for(int var8 = 0; var8 < i; ++var8) {
                     BlockFace var7 = var5[var8];
                     if(!var7.equals(BlockFace.SELF) && !var7.equals(BlockFace.DOWN) && !var7.equals(BlockFace.UP) && !var7.toString().contains("_") && event.getClickedBlock().getRelative(var7).getType().equals(Material.WALL_SIGN)) {
                        Player player = event.getPlayer();
                        if(((Sign)event.getClickedBlock().getRelative(var7).getState()).getLine(1).equalsIgnoreCase("[x]") && (UtilPermissions.playerCanUseCommand(player, "falsebook.blocks.hiddenswitch") || UtilPermissions.playerCanUseCommand(player, "falsebook.blocks.*") || UtilPermissions.playerCanUseCommand(player, "falsebook.blocks.hiddenswitch.create") || UtilPermissions.playerCanUseCommand(player, "*"))) {
                           this.ExecuteHiddenSwitch(event.getClickedBlock().getRelative(var7), event.getPlayer(), event.getBlockFace());
                        }
                     }
                  }

                  this.factory.handleRightClick(event);
               }
            }
         }
      }
   }
}
