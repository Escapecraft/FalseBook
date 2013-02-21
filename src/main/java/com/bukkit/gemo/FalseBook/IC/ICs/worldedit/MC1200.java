package com.bukkit.gemo.FalseBook.IC.ICs.worldedit;

import com.bukkit.gemo.FalseBook.IC.ICs.BaseChip;
import com.bukkit.gemo.FalseBook.IC.ICs.BaseIC;
import com.bukkit.gemo.FalseBook.IC.ICs.ICGroup;
import com.bukkit.gemo.FalseBook.IC.ICs.InputState;
import com.bukkit.gemo.FalseBook.IC.ICs.Lever;
import com.bukkit.gemo.utils.BlockUtils;
import com.bukkit.gemo.utils.Parser;
import com.bukkit.gemo.utils.SignUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Wolf;
import org.bukkit.event.block.SignChangeEvent;

public class MC1200 extends BaseIC {

   public MC1200() {
      this.ICName = "MOB SPAWNER";
      this.ICNumber = "[MC1200]";
      this.setICGroup(ICGroup.WORLDEDIT);
      this.chipState = new BaseChip(true, false, false, "Clock", "", "");
      this.chipState.setOutputs("Output = Input", "", "");
      this.chipState.setLines("<b>Mobname to spawn:</b><ul><li>AngryWolf</li><li>Blaze</li><li>Chicken</li><li>Cow</li><li>Creeper</li><li>Dog</li><li>EnderDragon</li><li>Ghast</li><li>Giant</li><li>Magmacube</li><li>MushroomCow</li><li>Pig</li><li>PigZombie</li><li>Sheep</li><li>Skeleton</li><li>Slime</li><li>Spider</li><li>Villager</li><li>Zombie</li><li>Wolf</li></ul>If you place \"C:\" in front of the name, it will spawn the mob as a child (if possible).", "The amount of mobs to spawn (default: 1)");
      this.ICDescription = "The MC1200 spawns a mob in the first free space above the block behind the IC sign when the input (the \"clock\") goes from low to high.";
   }

   public void checkCreation(SignChangeEvent event) {
      EntityType[] entityTypes = EntityType.values();
      if(event.getLine(2) == null) {
         SignUtils.cancelSignCreation(event, "Mob \'" + event.getLine(2) + "\' not found.");
      } else {
         boolean found = false;
         String mobLine = event.getLine(2).toLowerCase();
         mobLine = mobLine.replace("monster", "");
         mobLine = mobLine.replace("c:", "");

         for(int name = 0; name < entityTypes.length; ++name) {
            if((entityTypes[name].isAlive() || entityTypes[name].isSpawnable()) && (mobLine.equalsIgnoreCase(entityTypes[name].name()) || mobLine.equalsIgnoreCase("pig_zombie") || mobLine.equalsIgnoreCase("DOG") || mobLine.equalsIgnoreCase("ANGRYWOLF") || mobLine.equalsIgnoreCase("MAGMACUBE") || mobLine.equalsIgnoreCase("BLAZE"))) {
               event.setLine(2, event.getLine(2).toUpperCase());
               found = true;
               break;
            }
         }

         if(!found) {
            String var7 = "";

            for(int i = 0; i < entityTypes.length; ++i) {
               if(entityTypes[i].isAlive() || entityTypes[i].isSpawnable()) {
                  var7 = entityTypes[i].name().replace(" ", "").replace("_", "").replace("-", "");
                  if(mobLine.equalsIgnoreCase(var7)) {
                     event.setLine(2, event.getLine(2).toUpperCase());
                     found = true;
                     break;
                  }
               }
            }
         }

         if(!found) {
            SignUtils.cancelSignCreation(event, "Mob \'" + event.getLine(2) + "\' not found.");
         } else if(!Parser.isIntegerOrEmpty(event.getLine(3))) {
            SignUtils.cancelSignCreation(event, "Line 4 must be an integer.");
         } else if(Parser.isInteger(event.getLine(3)) && Parser.getInteger(event.getLine(3), 1) < 1) {
            SignUtils.cancelSignCreation(event, "Line 4 must be > 0.");
         }
      }
   }

   public void Execute(Sign signBlock, InputState currentInputs, InputState previousInputs) {
      if(currentInputs.isInputOneHigh() && previousInputs.isInputOneLow()) {
         World w = signBlock.getWorld();
         boolean mobCount = false;
         if(!Parser.isIntegerOrEmpty(signBlock.getLine(3))) {
            return;
         }

         int var19 = Parser.getInteger(signBlock.getLine(3), 1);
         if(var19 < 1) {
            return;
         }

         boolean isChild = false;
         boolean isAngryWolf = false;
         boolean isDog = false;
         boolean isSpecial = false;
         Class clazz = null;
         String mobLine = signBlock.getLine(2).toLowerCase();
         mobLine = mobLine.replace("monster", "");
         if(mobLine.startsWith("c:")) {
            isChild = true;
            mobLine = mobLine.replace("c:", "");
         }

         EntityType typeOfMob = null;

         for(int loc = 0; loc < EntityType.values().length; ++loc) {
            if((EntityType.values()[loc].isAlive() || EntityType.values()[loc].isSpawnable()) && EntityType.values()[loc].name().equalsIgnoreCase(mobLine)) {
               typeOfMob = EntityType.values()[loc];
               break;
            }
         }

         int maxY;
         if(typeOfMob == null) {
            String var20 = "";

            for(maxY = 0; maxY < EntityType.values().length; ++maxY) {
               if(EntityType.values()[maxY].isAlive() || EntityType.values()[maxY].isSpawnable()) {
                  var20 = EntityType.values()[maxY].name().replace(" ", "").replace("_", "").replace("-", "");
                  if(mobLine.equalsIgnoreCase(var20)) {
                     typeOfMob = EntityType.values()[maxY];
                     break;
                  }
               }
            }

            if(mobLine.equalsIgnoreCase("pigzombie")) {
               typeOfMob = EntityType.PIG_ZOMBIE;
            } else if(mobLine.equalsIgnoreCase("dog")) {
               isDog = true;
               typeOfMob = EntityType.PIG_ZOMBIE;
            } else if(mobLine.equalsIgnoreCase("angrywolf")) {
               isAngryWolf = true;
               typeOfMob = EntityType.PIG_ZOMBIE;
            } else if(mobLine.equalsIgnoreCase("magmacube")) {
               isSpecial = true;
               isChild = false;
               typeOfMob = null;
               clazz = MagmaCube.class;
            } else if(mobLine.equalsIgnoreCase("blaze")) {
               isSpecial = true;
               isChild = false;
               typeOfMob = null;
               clazz = Blaze.class;
            }
         }

         if(typeOfMob == null && clazz == null) {
            return;
         }

         if(isChild && clazz == null && typeOfMob != EntityType.WOLF && typeOfMob != EntityType.SHEEP && typeOfMob != EntityType.COW && typeOfMob != EntityType.MUSHROOM_COW && typeOfMob != EntityType.PIG && typeOfMob != EntityType.CHICKEN) {
            isChild = false;
         }

         Location var21 = getICBlock(signBlock);
         maxY = Math.min(w.getMaxHeight() - 1, var21.getBlockY() + 10);

         for(int y = var21.getBlockY() + 1; y <= maxY; ++y) {
            if(BlockUtils.canPassThrough(w.getBlockAt(var21.getBlockX(), y, var21.getBlockZ()).getTypeId()) && BlockUtils.canPassThrough(w.getBlockAt(var21.getBlockX(), y + 1, var21.getBlockZ()).getTypeId())) {
               var21.setX(var21.getX() + 0.5D);
               var21.setZ(var21.getZ() + 0.5D);
               var21.setY((double)y);
               this.switchLever(Lever.BACK, signBlock, true);

               for(int c = 0; c < var19; ++c) {
                  if(!isSpecial) {
                     LivingEntity entity = w.spawnCreature(var21, typeOfMob);
                     if(isAngryWolf) {
                        ((Wolf)entity).setAngry(true);
                     } else if(isDog) {
                        ((Wolf)entity).setSitting(true);
                     }

                     if(isChild) {
                        if(typeOfMob == EntityType.WOLF) {
                           ((Wolf)entity).setBaby();
                        }

                        if(typeOfMob == EntityType.COW) {
                           ((Cow)entity).setBaby();
                        }

                        if(typeOfMob == EntityType.MUSHROOM_COW) {
                           ((MushroomCow)entity).setBaby();
                        }

                        if(typeOfMob == EntityType.PIG) {
                           ((Pig)entity).setBaby();
                        }

                        if(typeOfMob == EntityType.CHICKEN) {
                           ((Chicken)entity).setBaby();
                        }

                        if(typeOfMob == EntityType.SHEEP) {
                           ((Sheep)entity).setBaby();
                        }
                     }
                  } else {
                     w.spawn(var21, clazz);
                  }
               }

               return;
            }
         }
      } else {
         this.switchLever(Lever.BACK, signBlock, false);
      }

   }
}
