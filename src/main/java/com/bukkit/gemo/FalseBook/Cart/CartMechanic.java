package com.bukkit.gemo.FalseBook.Cart;

import com.bukkit.gemo.FalseBook.Cart.CartWorldSettings;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;

public interface CartMechanic {

   boolean checkRailCreation(BlockPlaceEvent var1, Player var2);

   boolean checkSignCreation(SignChangeEvent var1, Player var2, Sign var3);

   boolean Execute(Minecart var1, Block var2, Block var3, Block var4, CartWorldSettings var5);
}
