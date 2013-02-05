package com.bukkit.gemo.FalseBook.IC.ICs;

import com.bukkit.gemo.FalseBook.IC.ICs.InputState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;

public interface IC {

   boolean hasPermission(Player var1);

   void checkCreation(SignChangeEvent var1);

   void onImport();

   void Execute();

   void Execute(Sign var1, InputState var2, InputState var3);
}
