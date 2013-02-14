package com.bukkit.gemo.FalseBook.Block.Mechanics;

import com.bukkit.gemo.FalseBook.Block.FalseBookBlockCore;
import com.bukkit.gemo.FalseBook.Block.Config.ConfigHandler;
import com.bukkit.gemo.FalseBook.Mechanics.MechanicListener;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class MechanicAppleTreeDrop extends MechanicListener {

   public MechanicAppleTreeDrop(FalseBookBlockCore plugin) {
      plugin.getMechanicHandler().registerEvent(BlockBreakEvent.class, this);
   }

   public void onBlockBreak(BlockBreakEvent event) {
      if(!event.isCancelled()) {
         if(event.getBlock().getType().equals(Material.LEAVES) && ConfigHandler.getAppleDropChance(event.getBlock().getWorld().getName()) > 0.0F) {
            Random random = new Random();
            float f = random.nextFloat() * 100.0F;
            if(f <= ConfigHandler.getAppleDropChance(event.getBlock().getWorld().getName())) {
               ItemStack item = new ItemStack(Material.APPLE, 1);
               event.getBlock().getWorld().dropItem(event.getBlock().getLocation(), item);
            }

            random = null;
         }

      }
   }
}
