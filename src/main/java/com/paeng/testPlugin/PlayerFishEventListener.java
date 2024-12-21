package com.paeng.testPlugin;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerFishEventListener implements Listener {

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        if (event.getState() != PlayerFishEvent.State.CAUGHT_FISH) return;
        if (!(event.getCaught() instanceof Item)) return;

        LevelManager manager = new LevelManager();
        TestPlugin pluginInstance = JavaPlugin.getPlugin(TestPlugin.class);

        Player player = event.getPlayer();
        World cur_world = player.getWorld();
        Location hook = event.getHook().getLocation();
        Item stack = (Item) event.getCaught();

        int waterCount = 0;
        for (int x = -2; x < 3; x++) {
            for (int y = -2; y < 3; y++) {
                for (int z = -2; z < 3; z++) {
                    if (cur_world.getBlockAt(hook.getBlockX() + x, hook.getBlockY() + y, hook.getBlockZ() + z).getType() != Material.WATER) continue;
                    waterCount++;
                }
            }
        }

        if (waterCount < 50) {
            int weight = (int) (Math.random() * 4);
            Material[] materials = {Material.STICK, Material.BOWL, Material.ROTTEN_FLESH, Material.GLASS_BOTTLE};
            String[] material_name = {"막대기", "그릇", "썩은 살점", "유리병"};

            stack.setItemStack(new ItemStack(materials[weight]));
            String caught = material_name[weight];

            player.sendMessage("§7§lCOMMON §f" + caught + " 을(를) 낚았다.");
            player.sendMessage("§c[!] 낚시 위치가 좋지 않습니다. 물이 많은 곳으로 이동하세요 §7(위치 점수: " + waterCount * 2 + "/100)");
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS,1.0F,0.5F);
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§a[!] 위치 점수가 100점일 때만 낚시가 가능합니다"));
            return;
        }

        int weight_class = (int) (Math.random() * 10000);
        int weight_item = (int) (Math.random() * 100);

        int item_class_code;
        String[] item_class_names = {"FISH", "COMMON", "RARE", "EPIC", "LEGENDARY","SPECIAL"};
        if (weight_class < 8900) item_class_code = 0; // FISH : 89.0% | 80.1%
        else if (weight_class < 9120) item_class_code = 1; // COMMON : 2.2% | 9.99%
        else if (weight_class < 9970) item_class_code = 2; // RARE : 8.5% | 8.649%
        else if (weight_class < 9980) item_class_code = 3; // EPIC : 0.1% | 0.964%
        else if (weight_class < 9990) item_class_code = 4; // LEGENDARY : 0.1% | 0.1777%
        else item_class_code = 5; // SPECIAL : 0.1% | 0.1195%

        int mainHandLevel = 0, offHandLevel = 0, luckEnchantmentLevel = 0;
        try { mainHandLevel = event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getEnchantLevel(Enchantment.LUCK_OF_THE_SEA); }
        catch (NullPointerException e) {}
        try { offHandLevel = event.getPlayer().getInventory().getItemInOffHand().getItemMeta().getEnchantLevel(Enchantment.LUCK_OF_THE_SEA); }
        catch (NullPointerException e) {}
        luckEnchantmentLevel = Math.max(mainHandLevel, offHandLevel);

        for (; luckEnchantmentLevel > 0; luckEnchantmentLevel--) {
            int upgrade = (int) (Math.random() * 100);
            if (upgrade < 10) item_class_code = Math.min(item_class_code + 1, 4);
        }

        String item_class = item_class_names[item_class_code];

        ConfigurationSection configurationSection = pluginInstance.getConfig().getConfigurationSection("Caught." + item_class);

        List<Integer> item_weights = new ArrayList<>();

        for (String materialName : configurationSection.getKeys(false))
            item_weights.add(pluginInstance.getConfig().getInt("Caught." + item_class + "." + materialName + ".weight"));

        Collections.sort(item_weights);

        int weight_of_desired_item = 100;
        for (Integer itemWeight : item_weights) {
            if (weight_item >= itemWeight) continue;
            weight_of_desired_item = itemWeight;
            break;
        }

        String caught_material_name = "";
        for (String materialName : configurationSection.getKeys(false)) {
            if (pluginInstance.getConfig().getInt("Caught." + item_class + "." + materialName + ".weight") != weight_of_desired_item) continue;
            caught_material_name = materialName;
        }

        // 1. Play Sound
        float[] pitch_data = {0.5F, 0.5F, 2.0F, 0.5F, 1.0F, 1.0F};
        Sound[] sound_data = {Sound.ITEM_BUCKET_FILL_FISH, Sound.BLOCK_NOTE_BLOCK_BASS, Sound.BLOCK_NOTE_BLOCK_BELL, Sound.ENTITY_PLAYER_LEVELUP, Sound.UI_TOAST_CHALLENGE_COMPLETE, Sound.UI_TOAST_CHALLENGE_COMPLETE};
        player.playSound(player.getLocation(), sound_data[item_class_code], 1.0F, pitch_data[item_class_code]);

        // 2. Broadcast Information
        String[] item_class_styles = {"§3", "§7", "§b", "§d", "§e", "§c§l"};
        String name_kor = pluginInstance.getConfig().getString("Caught." + item_class + "." + caught_material_name + ".name");
        String batchim = "를";
        if (pluginInstance.getConfig().getInt("Caught." + item_class + "." + caught_material_name + ".reward") % 2 == 1) {batchim = "을";}
        if (item_class_code < 3) player.sendMessage(item_class_styles[item_class_code] + item_class + " §f" + name_kor + batchim +" 낚았다.");
        else Bukkit.broadcastMessage(item_class_styles[item_class_code] + item_class + "! §f" + player.getName() + "님이 " + name_kor + batchim +" 낚았습니다!");

        // 3. Grant Experience Points
        manager.addExp(player, LevelManager.ExpCat.FISHING, pluginInstance.getConfig().getInt("Caught." + item_class + "." + caught_material_name + ".reward"));

        // In-Game EXP Edge Case
        if (item_class.equals("EPIC") && name_kor.equals("1500 인첸트 경험치")) event.setExpToDrop(1500);

        // 4. Change ItemStack
        if (item_class.equals("RARE") && name_kor.equals("마법이 부여된 낚싯대")) stack.setItemStack(randomEnchantment(new ItemStack(Material.FISHING_ROD)));
        else if (item_class.equals("RARE") && name_kor.equals("마법이 부여된 활")) stack.setItemStack(randomEnchantment(new ItemStack(Material.BOW)));
        else if (item_class.equals("LEGENDARY") && name_kor.equals("마법이 부여된 네더라이트 곡괭이")) stack.setItemStack(randomEnchantment(new ItemStack(Material.NETHERITE_PICKAXE)));
        else if (item_class.equals("LEGENDARY") && name_kor.equals("마법이 부여된 네더라이트 투구")) stack.setItemStack(randomEnchantment(new ItemStack(Material.NETHERITE_HELMET)));
        else if (item_class.equals("SPECIAL") && name_kor.equals("무한 수선 활")) {
            stack.setItemStack(infMendEnchantment(new ItemStack(Material.BOW)));
        }
        else if (item_class.equals("SPECIAL") && name_kor.equals("철 장검")) {
            stack.setItemStack(attributeModifier(new ItemStack(Material.IRON_SWORD),2, EquipmentSlot.HAND,Attribute.ENTITY_INTERACTION_RANGE, AttributeModifier.Operation.ADD_NUMBER));
        }
        /*
        else if (item_class.equals("SPECIAL") && name_kor.equals("저중력 부츠")) {
            ItemStack boots = new ItemStack(Material.CHAINMAIL_BOOTS);
            stack.setItemStack(attributeModifier(boots,-0.5, EquipmentSlot.FEET,Attribute.GENERIC_GRAVITY, AttributeModifier.Operation.MULTIPLY_SCALAR_1));
            stack.setItemStack(attributeModifier(boots,-0.5, EquipmentSlot.FEET,Attribute.GENERIC_JUMP_STRENGTH, AttributeModifier.Operation.ADD_NUMBER));
            stack.setItemStack(attributeModifier(boots,-0.5, EquipmentSlot.FEET,Attribute.GENERIC_GRAVITY, AttributeModifier.Operation.MULTIPLY_SCALAR_1));
        }
         */
        else if (item_class.equals("SPECIAL") && name_kor.equals("신기전")) {
            stack.setItemStack(addEnchantment(new ItemStack(Material.CROSSBOW),Enchantment.MULTISHOT,3));
        }
        else if (item_class.equals("SPECIAL") && name_kor.equals("자수정 검")) {
            ItemStack item = new ItemStack(Material.AMETHYST_SHARD);
            stack.setItemStack(attributeModifier(item,8, EquipmentSlot.HAND,Attribute.ATTACK_DAMAGE, AttributeModifier.Operation.ADD_NUMBER));
            stack.setItemStack(addEnchantment(item,Enchantment.SHARPNESS,6));
        }
        else stack.setItemStack(new ItemStack(Material.valueOf(caught_material_name)));
    }

    public ItemStack randomEnchantment(ItemStack item) {
        List<Enchantment> candidates = new ArrayList<>();

        for (Enchantment enchantment : Registry.ENCHANTMENT) {
            if (enchantment.canEnchantItem(item))
                candidates.add(enchantment);
        }

        candidates.remove(Enchantment.MENDING);

        if (candidates.isEmpty()) return item;

        Collections.shuffle(candidates);
        int max = Math.min(candidates.size(),(int) Math.ceil(Math.random() * 4));

        for (int i = 0; i < max; i++) {
            Enchantment chosen = candidates.get(i);
            item.addEnchantment(chosen, 1 + (int) (Math.random() * ((chosen.getMaxLevel() - 1) + 1)));
        }

        return item;
    }

    public ItemStack infMendEnchantment(ItemStack item) {
        item.addUnsafeEnchantment(Enchantment.INFINITY, 1);
        item.addUnsafeEnchantment(Enchantment.MENDING, 1);
        return item;
    }

    public ItemStack addEnchantment(ItemStack item, Enchantment enchantment, int level) {
        item.addUnsafeEnchantment(enchantment,level);
        return item;
    }

    public ItemStack attributeModifier(ItemStack item, double amount, EquipmentSlot slot, Attribute attribute, AttributeModifier.Operation operation) {

        ItemMeta meta = item.getItemMeta();

        NamespacedKey key = attribute.getKey();
        AttributeModifier modifier = new AttributeModifier(key, amount, operation, slot.getGroup());
        meta.addAttributeModifier(attribute, modifier);

        item.setItemMeta(meta);
        return item;
    }
}