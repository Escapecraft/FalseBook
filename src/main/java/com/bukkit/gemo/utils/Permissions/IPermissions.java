package com.bukkit.gemo.utils.Permissions;

import java.util.List;
import org.bukkit.entity.Player;

public interface IPermissions {

   boolean isActive();

   boolean permission(Player var1, String var2);

   List getGroups(Player var1);

   List getGroups(String var1, String var2);
}
