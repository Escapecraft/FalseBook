package com.bukkit.gemo.FalseBook.Block.Handler;

import com.bukkit.gemo.FalseBook.Block.World.FBWorldBlock;
import com.bukkit.gemo.FalseBook.World.FBWorld;
import com.bukkit.gemo.FalseBook.World.WorldHandler;

public class WorldHandlerBlock extends WorldHandler {

   protected FBWorld addWorld(String worldName) {
      this.removeWorld(worldName);
      FBWorldBlock thisWorld = new FBWorldBlock(worldName);
      thisWorld.loadSettings();
      this.worldList.put(worldName, thisWorld);
      return thisWorld;
   }

   public FBWorld getWorld(String worldName) {
      return super.hasWorld(worldName)?(FBWorld)this.worldList.get(worldName):this.addWorld(worldName);
   }
}
