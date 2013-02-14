package com.bukkit.gemo.FalseBook.Cart;

import com.bukkit.gemo.FalseBook.Cart.FalseBookMinecart;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;

public class CartHandler {

   private static ConcurrentHashMap minecarts = new ConcurrentHashMap();
   private static HashSet playersInLockMode = new HashSet();
   private static HashSet playersInUnLockMode = new HashSet();
   private static HashMap lockedCarts = new HashMap();


   public static FalseBookMinecart getFalseBookMinecart(Minecart cart) {
      UUID ID = cart.getUniqueId();
      FalseBookMinecart testCart = (FalseBookMinecart)minecarts.get(ID.toString());
      if(testCart != null) {
         testCart.updateMinecart(cart);
         return testCart;
      } else {
         synchronized(cart) {
            FalseBookMinecart newCart = new FalseBookMinecart(cart);
            minecarts.put(ID.toString(), newCart);
            if(lockedCarts.containsKey(ID.toString())) {
               newCart.setOwner((String)lockedCarts.get(ID.toString()));
            }

            return newCart;
         }
      }
   }

   public static void addLockedCart(UUID ID, String owner) {
      lockedCarts.put(ID.toString(), owner);
   }

   public static void addLockedCart(String ID, String owner) {
      lockedCarts.put(ID, owner);
   }

   public static void removeLockedCart(UUID ID) {
      lockedCarts.remove(ID.toString());
   }

   public static void hasLockedCart(UUID ID) {
      lockedCarts.containsKey(ID.toString());
   }

   public static void saveLockedCarts() {
      File file = new File("plugins" + System.getProperty("file.separator") + "FalseBook" + System.getProperty("file.separator") + "lockedCarts.yml");
      if(file.exists()) {
         file.delete();
      }

      try {
         YamlConfiguration e = new YamlConfiguration();
         int count = 0;

         for(Iterator var4 = lockedCarts.entrySet().iterator(); var4.hasNext(); ++count) {
            Entry entry = (Entry)var4.next();
            e.set("Minecart_" + count, (String)entry.getKey() + "--" + (String)entry.getValue());
         }

         e.set("CartCount", Integer.valueOf(count));
         e.save(file);
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public static void loadLockedCarts() {
      File file = new File("plugins" + System.getProperty("file.separator") + "FalseBook" + System.getProperty("file.separator") + "lockedCarts.yml");
      if(file.exists()) {
         try {
            YamlConfiguration e = new YamlConfiguration();
            e.load(file);
            int count = e.getInt("CartCount", 0);

            for(int i = 0; i < count; ++i) {
               String txt = e.getString("Minecart_" + i, "UNKNOWN");
               if(!txt.equalsIgnoreCase("UNKNOWN")) {
                  String[] split = txt.split("--");
                  if(split.length == 2) {
                     addLockedCart(split[0], split[1]);
                  }
               }
            }

            System.out.println(lockedCarts.size() + " locked storagecarts loaded.");
         } catch (Exception var6) {
            var6.printStackTrace();
         }

      }
   }

   public static void removeMinecart(Minecart cart) {
      removeLockedCart(cart.getUniqueId());
      minecarts.remove(cart.getUniqueId().toString());
   }

   public static boolean isInLockMode(Player player) {
      return playersInLockMode.contains(player.getName());
   }

   public static boolean addToLockMode(Player player) {
      removeFromUnLockMode(player);
      return playersInLockMode.add(player.getName());
   }

   public static boolean removeFromLockMode(Player player) {
      return playersInLockMode.remove(player.getName());
   }

   public static boolean isInUnLockMode(Player player) {
      return playersInUnLockMode.contains(player.getName());
   }

   public static boolean addToUnLockMode(Player player) {
      removeFromLockMode(player);
      return playersInUnLockMode.add(player.getName());
   }

   public static boolean removeFromUnLockMode(Player player) {
      return playersInUnLockMode.remove(player.getName());
   }
}
