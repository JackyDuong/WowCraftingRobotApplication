#WowCraftingRobotapplication

This application is using to chain craft on a character.

In [WowCraftingRobotApplication.java](src/main/java/WowCraftingRobotApplication.java), change the value of *CRAFTING_TIME* and *NB_SLOTS_MAX*.

For this, need the addon [Autoseller](https://www.curseforge.com/wow/addons/auto-seller) to sell every junk items and ilvl less than whatever.

##Commande

Go to the package directory
param 1 : crafting time - 3 sec to craft the item
param 2 : nb available slots
param 3 : nb max available slots (when compo slots are done)

`java -jar WowCraftingRobot-1.0.jar 3 11 70`