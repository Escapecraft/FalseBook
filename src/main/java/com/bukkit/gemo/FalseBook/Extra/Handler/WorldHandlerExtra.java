package com.bukkit.gemo.FalseBook.Extra.Handler;

import com.bukkit.gemo.FalseBook.Extra.FalseBookExtraCore;
import com.bukkit.gemo.FalseBook.Extra.World.FBWorldExtra;
import com.bukkit.gemo.FalseBook.World.FBWorld;
import com.bukkit.gemo.FalseBook.World.WorldHandler;
import com.bukkit.gemo.utils.BlockUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import org.bukkit.block.Block;

public class WorldHandlerExtra extends WorldHandler {

   public void convertOldBlocks() {
      File baseFolder = new File("plugins" + System.getProperty("file.separator") + "FalseBook");
      baseFolder.mkdirs();
      File file = new File(baseFolder, "Extra_ProtectedBlocks.dat");
      if(file.exists()) {
         try {
            FalseBookExtraCore.printInConsole("Converting old protected blocks...");
            HashMap e = new HashMap();
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = null;
            int count = 0;

            while((line = reader.readLine()) != null) {
               line = line.trim();
               Block entry = BlockUtils.BlockFromLocationString(line);
               if(entry != null) {
                  String worldName = entry.getWorld().getName();
                  HashMap thisMap = (HashMap)e.get(worldName);
                  if(thisMap == null) {
                     thisMap = new HashMap();
                     e.put(worldName, thisMap);
                  }

                  thisMap.put(line, entry);
                  ++count;
               }
            }

            reader.close();
            Iterator var10 = e.entrySet().iterator();

            while(var10.hasNext()) {
               Entry var12 = (Entry)var10.next();
               this.saveProtectedBlocks((String)var12.getKey(), (HashMap)var12.getValue());
            }

            file.delete();
            FalseBookExtraCore.printInConsole("Done! ( " + count + " )");
         } catch (Exception var11) {
            var11.printStackTrace();
         }

      }
   }

   public void saveProtectedBlocks(String worldName, HashMap blockList) {
      try {
         File e = new File("plugins" + System.getProperty("file.separator") + "FalseBook" + System.getProperty("file.separator") + worldName + System.getProperty("file.separator"));
         e.mkdirs();
         File file = new File(e, "Extra_ProtectedBlocks.dat");
         if(file.exists()) {
            return;
         }

         FileWriter writer = new FileWriter(file);
         Iterator var7 = blockList.keySet().iterator();

         while(var7.hasNext()) {
            String text = (String)var7.next();
            writer.write(text);
            writer.write(System.getProperty("line.separator"));
         }

         writer.flush();
         writer.close();
      } catch (Exception var8) {
         var8.printStackTrace();
      }

   }

   protected FBWorld addWorld(String worldName) {
      this.removeWorld(worldName);
      FBWorldExtra thisWorld = new FBWorldExtra(worldName);
      thisWorld.loadSettings();
      this.worldList.put(worldName, thisWorld);
      return thisWorld;
   }

   public FBWorld getWorld(String worldName) {
      return super.hasWorld(worldName)?(FBWorld)this.worldList.get(worldName):this.addWorld(worldName);
   }
}
