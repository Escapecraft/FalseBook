package com.bukkit.gemo.FalseBook.Block.Areas;

import com.bukkit.gemo.FalseBook.Block.Areas.AreaChestItem;
import com.bukkit.gemo.FalseBook.Block.Areas.AreaComplexBlock;
import java.io.Serializable;
import java.util.ArrayList;
import org.bukkit.inventory.Inventory;

public class AreaChest extends AreaComplexBlock implements Serializable {

   private static final long serialVersionUID = -2940465813606518671L;
   private ArrayList ItemList = new ArrayList();


   public AreaChest(Inventory inv) {
      for(int i = 0; i < inv.getSize(); ++i) {
         this.ItemList.add(new AreaChestItem(inv.getItem(i).getTypeId(), inv.getItem(i).getDurability(), inv.getItem(i).getAmount()));
      }

   }

   public ArrayList getItemList() {
      return this.ItemList;
   }

   public void setItemList(ArrayList itemList) {
      this.ItemList = itemList;
   }
}
