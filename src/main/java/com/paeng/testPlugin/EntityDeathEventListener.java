package com.paeng.testPlugin;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class EntityDeathEventListener implements Listener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Player attacker = event.getEntity().getKiller();
        Entity victim = event.getEntity();

        if (attacker == null) return;

        LevelManager manager = new LevelManager();
        TestPlugin pluginInstance = JavaPlugin.getPlugin(TestPlugin.class);

        CollectionManager.CollectionType mobTypeStore = null;
        double expMultiplier = 1;

        for (CollectionManager.CollectionType blocktype : CollectionManager.CollectionType.values()) {

            List<String> blockList = pluginInstance.getConfig().getStringList("Collection." + blocktype.getCode());

            if (blockList.contains(victim.getName())) {
                mobTypeStore = blocktype;
                //CollectionManager.addExperience(blocktype,player,1);
                expMultiplier = blocktype.getExpWisdom(CollectionManager.getLevel(blocktype,attacker));
            }
        }


        if (victim.getName().equals("Enderman")) {
            if (attacker.getWorld().getName().equals("world_the_end")) manager.addExp(attacker, LevelManager.ExpCat.COMBAT, 1);
            else manager.addExp(attacker, LevelManager.ExpCat.COMBAT, (int) (350*expMultiplier));
        }

        for (LevelManager.ExpCat expcat : LevelManager.ExpCat.values()) {
            manager.addExp(attacker, expcat, (int) (pluginInstance.getConfig().getInt("Killed." + expcat.getName() + "." + victim.getName())*expMultiplier));
        }

        CollectionManager.addExperience(mobTypeStore, attacker, 1);
    }
}
