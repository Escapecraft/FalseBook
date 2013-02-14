===============================
FalseBook - FalseBookIC
part of: FalseBook by GeMo
===============================

Plugin website: http://www.falsebook.eu

Bukkit website: http://bukkit.org


Note: The jars for these plugins contained a GPLv3 license.  Since the
developer has disappeared, the jars have been decompiled.

This is temporary until we can switch over to the supported version of
CraftBook.

To build, use maven.  Order is critical.  You need core built before the
others and don't clean after core.  (The default goals are as specified
in this list.)
mvn -f pom-core.xml clean install
mvn -f pom-block.xml package
mvn -f pom-cart.xml package
mvn -f pom-extra.xml package
mvn -f pom-ic.xml package

Build scripts created by us.  The following modified to compile:
- FalseBookICPlayerListener.java
- InventoryUtils.java
- SuperPermsPermissions.java
- Craft.java
- FBInventory.java
- FBCraftingManager.java
- BlockUtils.java
- ICUtils.java

Removed the following permission systems (because we couldn't find maven
repos for the jars) and modified UtilPermissions.java:
- AnjoPermissions
- bPermissions
- NijiPermissions
