package com.paeng.testPlugin.menu;

import com.paeng.testPlugin.util.ItemManager;
import com.paeng.testPlugin.LevelManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class CosmeticMenu {

    LevelManager levelManager = new LevelManager();
    ItemManager item = new ItemManager();

    public void openCosmeticMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 36, "§0치장품");
        for (int i = 0; i < 36; i++) {
            inv.setItem(i, item.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
        }

        inv.setItem(10,item.createItem(Material.NETHER_STAR,"§d상시 파티클",
                "§7닉네임 옆에 표시되는",
                "§7레벨 칭호의 스타일을 설정합니다",
                "",
                "§e클릭해서 변경하기"));

        inv.setItem(12,item.createItem(Material.GOLDEN_SWORD,"§d타격 파티클",
                "§7타격 시 나타낼",
                "§7파티클을 설정합니다",
                "",
                "§e클릭해서 변경하기"));
        inv.setItem(14,item.createItem(Material.ARROW,"§d화살 파티클",
                "§7화살이 날아갈 때의",
                "§7파티클을 설정합니다",
                "",
                "§e클릭해서 변경하기"));

        inv.setItem(31,item.createItem(Material.ARROW,"§a뒤로 가기"));
        player.openInventory(inv);
    }

    public void ParticleEffect(Player player, int page) {
        Inventory inv = Bukkit.createInventory(null, 54, "§0상시 파티클 - "+page);
        for (int i = 0; i < 9; i++) {
            inv.setItem(i, item.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
            inv.setItem(i + 45, item.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
        }
        for (int i = 0; i < 6; i++) {
            inv.setItem(i * 9, item.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
            inv.setItem(i * 9 + 8, item.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
        }

        inv.setItem(49,item.createItem(Material.ARROW,"§a뒤로 가기"));

        if (page == 1) {

            inv.setItem(10, item.createItem(item.createSkull("http://textures.minecraft.net/texture/1919d1594bf809db7b44b3782bf90a69f449a87ce5d18cb40eb653fdec2722"), ChatColor.of("#7F7F7F")+"없음",
                    "",
                    checkTotalLevel(0,player)));

        }
        else if (page == 2) {

        }
        player.openInventory(inv);
    }

    public void openHitEffect(Player player, int page) {
        Inventory inv = Bukkit.createInventory(null, 54, "§0타격 파티클 - "+page);
        for (int i = 0; i < 9; i++) {
            inv.setItem(i, item.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
            inv.setItem(i + 45, item.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
        }
        for (int i = 0; i < 6; i++) {
            inv.setItem(i * 9, item.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
            inv.setItem(i * 9 + 8, item.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
        }

        inv.setItem(49,item.createItem(Material.ARROW,"§a뒤로 가기"));

        if (page == 1) {

            inv.setItem(10, item.createItem(item.createSkull("http://textures.minecraft.net/texture/1919d1594bf809db7b44b3782bf90a69f449a87ce5d18cb40eb653fdec2722"), ChatColor.of("#7F7F7F")+"없음",
                    "",
                    checkTotalLevel(0,player)));

        }
        else if (page == 2) {

        }
        player.openInventory(inv);
    }

    public void arrowTrail(Player player, int page) {
        Inventory inv = Bukkit.createInventory(null, 54, "§0화살 파티클 - "+page);
        for (int i = 0; i < 9; i++) {
            inv.setItem(i, item.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
            inv.setItem(i + 45, item.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
        }
        for (int i = 0; i < 6; i++) {
            inv.setItem(i * 9, item.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
            inv.setItem(i * 9 + 8, item.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
        }

        inv.setItem(49,item.createItem(Material.ARROW,"§a뒤로 가기"));

        if (page == 1) {

            inv.setItem(10, item.createItem(item.createSkull("http://textures.minecraft.net/texture/1919d1594bf809db7b44b3782bf90a69f449a87ce5d18cb40eb653fdec2722"), ChatColor.of("#7F7F7F")+"없음",
                    "",
                    checkTotalLevel(0,player)));
        }
        else if (page == 2) {

        }
        player.openInventory(inv);
    }









    private String checkTotalLevel(int target,Player player) {
        int level = levelManager.getTotalLevel(player.getUniqueId());
        if (level >= target){
            return "§e클릭해서 변경하기";
        }
        else {
            return "§c요구 레벨: " + target;
        }
    }
    private String checkLevel(int target, Player player, LevelManager.ExpCat category) {
        int level = levelManager.getLevel(player.getUniqueId(),category);
        if (level >= target){
            return "§e클릭해서 변경하기";
        }
        else {
            return "§c요구 레벨: " + target;
        }
    }


}