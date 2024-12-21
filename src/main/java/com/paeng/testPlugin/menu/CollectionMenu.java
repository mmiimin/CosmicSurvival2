package com.paeng.testPlugin.menu;

import com.paeng.testPlugin.CollectionManager;
import com.paeng.testPlugin.util.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class CollectionMenu {

    ItemManager item = new ItemManager();

    public void OpenCollectionMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, "§0도감");
        for (int i = 0; i < 9; i++) {
            inv.setItem(i, item.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
        }
        for (int i = 45; i < 54; i++) {
            inv.setItem(i, item.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
        }

        int k = 9;
        for (CollectionManager.CollectionType blocktype : CollectionManager.CollectionType.values()) {
            String nextLevelDescription;
            String barDescription;

            if (CollectionManager.getLevel(blocktype,player) == 9) {
                nextLevelDescription = "§6도감 완성! §7현재 도감 점수: §a§l" + CollectionManager.getExperience(blocktype, player);
                barDescription = "§7현재 도감 레벨: " + "§69레벨 "+"§e§m━━━━━━━━━━━━━━━━━━━━§e 100%" ;
            }
            else {
                String percentToNextLevel = String.format("%.1f",(double) CollectionManager.getExperience(blocktype, player) / blocktype.getRequireExp(CollectionManager.getLevel(blocktype, player))*100) + "%";
                int blockLeft = blocktype.getRequireExp(CollectionManager.getLevel(blocktype, player)) - CollectionManager.getExperience(blocktype, player);
                int barLength = Math.min(20,(int) Math.round((double) CollectionManager.getExperience(blocktype, player) / blocktype.getRequireExp(CollectionManager.getLevel(blocktype, player))*20));
                nextLevelDescription = "§7다음 레벨까지: §6" + blockLeft + "§7 남음 (§a" + CollectionManager.getExperience(blocktype, player) + "§7/" + blocktype.getRequireExp(CollectionManager.getLevel(blocktype, player)) + ")";
                barDescription =  "§7현재 도감 레벨: " + "§6" + CollectionManager.getLevel(blocktype,player) + "레벨 "+ "§a§m━".repeat(barLength) + "§7§m━".repeat(20-barLength) + "§e " + percentToNextLevel;
            }


            String[] description = mergeArrays(new String[]{" ", barDescription, nextLevelDescription," "}, collectionList(blocktype));

            inv.setItem(k, item.createItemArrayLore(blocktype.getDisplayItem(), "§e" + blocktype.getKorName(),
                    description
            ));
            k+=1;
        }

        inv.setItem(4,item.createItem(Material.BOOK,"§b도감"));
        inv.setItem(49,item.createItem(Material.ARROW,"§a뒤로 가기"));
        player.openInventory(inv);
    }

    public void OpenCollectionDetail(Player player, CollectionManager.CollectionType type) {
        Inventory inv = Bukkit.createInventory(null, 45, "§0도감 정보 - " + type.getKorName());
        for (int i = 0; i < 45; i++) {
            inv.setItem(i, item.createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
        }

        for (int i = 0; i < 9; i++) {

            String nextLevelDescription;
            String barDescription;
            if (CollectionManager.getLevel(type,player) > i) {
                nextLevelDescription = "§e"+(i+1)+"레벨 달성 §a§l" + "§7(§a" + CollectionManager.getExperience(type, player) + "§7/" + type.getRequireExp(i) + ")";
                barDescription = "§e§m━━━━━━━━━━━━━━━━━━━━§e 100%" ;
            }
            else {
                String percentToNextLevel = String.format("%.1f",(double) CollectionManager.getExperience(type, player) / type.getRequireExp(i)*100) + "%";
                int blockLeft = type.getRequireExp(i) - CollectionManager.getExperience(type, player);
                int barLength = Math.min(20,(int) Math.round((double) CollectionManager.getExperience(type, player) / type.getRequireExp(i)*20));
                nextLevelDescription = "§7"+(i+1)+"레벨까지 §6" + blockLeft + "§7 남음 (§a" + CollectionManager.getExperience(type, player) + "§7/" + type.getRequireExp(i) + ")";
                barDescription =  "§a§m━".repeat(barLength) + "§7§m━".repeat(20-barLength) + "§e " + percentToNextLevel;
            }
            String[] description = mergeArrays(new String[]{" ", nextLevelDescription, barDescription," ","§f"+(i+1)+"레벨 보상:"}, rewardList(type, i+1));


            inv.setItem(18+i, item.createItemArrayLore(glassColor(player, type, i+1), "§b" + type.getKorName() +" - "+ (i+1) +  " 레벨", description ));
        }




        inv.setItem(4,item.createItem(type.getDisplayItem(),"§b" + type.getKorName()));
        inv.setItem(40,item.createItem(Material.ARROW,"§a뒤로 가기"));
        player.openInventory(inv);
    }

    public static String[] mergeArrays(String[] array1, String[] array2) {
        String[] mergedArray = new String[array1.length + array2.length];
        System.arraycopy(array1, 0, mergedArray, 0, array1.length);
        System.arraycopy(array2, 0, mergedArray, array1.length, array2.length);
        return mergedArray;
    }

    private Material glassColor(Player player, CollectionManager.CollectionType type, int targetLevel) {
        if (CollectionManager.getLevel(type,player) > targetLevel-1) {return Material.LIME_STAINED_GLASS_PANE;}
        else if (CollectionManager.getLevel(type,player) == targetLevel-1) {return Material.ORANGE_STAINED_GLASS_PANE;}
        else {return Material.RED_STAINED_GLASS_PANE;}
        }

    private String[] rewardList(CollectionManager.CollectionType type, int level) {
        switch (type.getCode()) {
            case 0 -> { // 나무 도감 1
                switch (level){
                    case 1 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 5% 증가)", "보상"}; }
                    case 2 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 10% 증가)", "보상"}; }
                    case 3 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 15% 증가)", "보상"}; }
                    case 4 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 20% 증가)", "보상"}; }
                    case 5 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 30% 증가)", "보상"}; }
                    case 6 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 40% 증가)", "보상"}; }
                    case 7 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 50% 증가)", "보상"}; }
                    case 8 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +15%§8p §7(총 65% 증가)", "보상"}; }
                    case 9 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +15%§8p §7(총 80% 증가)", "보상"}; }

                }
            }
            case 1 -> { // 나무 도감 2
                switch (level){
                    case 1 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 5% 증가)", "보상"}; }
                    case 2 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 10% 증가)", "보상"}; }
                    case 3 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 15% 증가)", "보상"}; }
                    case 4 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 20% 증가)", "보상"}; }
                    case 5 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 30% 증가)", "보상"}; }
                    case 6 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 40% 증가)", "보상"}; }
                    case 7 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 50% 증가)", "보상"}; }
                    case 8 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +15%§8p §7(총 65% 증가)", "보상"}; }
                    case 9 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +15%§8p §7(총 80% 증가)", "보상"}; }

                }
            }
            case 2 -> { // 나무 도감 3
                switch (level){
                    case 1 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 5% 증가)", "보상"}; }
                    case 2 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 10% 증가)", "보상"}; }
                    case 3 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 15% 증가)", "보상"}; }
                    case 4 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 20% 증가)", "보상"}; }
                    case 5 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 30% 증가)", "보상"}; }
                    case 6 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 40% 증가)", "보상"}; }
                    case 7 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 50% 증가)", "보상"}; }
                    case 8 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +15%§8p §7(총 65% 증가)", "보상"}; }
                    case 9 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +15%§8p §7(총 80% 증가)", "보상"}; }

                }
            }
            case 3 -> { // 지옥 나무 도감
                switch (level){
                    case 1 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 5% 증가)", "보상"}; }
                    case 2 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 10% 증가)", "보상"}; }
                    case 3 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 15% 증가)", "보상"}; }
                    case 4 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 20% 증가)", "보상"}; }
                    case 5 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 30% 증가)", "보상"}; }
                    case 6 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 40% 증가)", "보상"}; }
                    case 7 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 50% 증가)", "보상"}; }
                    case 8 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +15%§8p §7(총 65% 증가)", "보상"}; }
                    case 9 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +15%§8p §7(총 80% 증가)", "보상"}; }

                }
            }
            case 4 -> { // 돌 도감 1
                switch (level){
                    case 1 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +20%§8p §7(총 20% 증가)", "보상"}; }
                    case 2 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +20%§8p §7(총 40% 증가)", "보상"}; }
                    case 3 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +20%§8p §7(총 60% 증가)", "보상"}; }
                    case 4 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +20%§8p §7(총 80% 증가)", "보상"}; }
                    case 5 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +20%§8p §7(총 100% 증가)", "보상"}; }
                    case 6 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +20%§8p §7(총 120% 증가)", "보상"}; }
                    case 7 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +20%§8p §7(총 140% 증가)", "보상"}; }
                    case 8 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +20%§8p §7(총 160% 증가)", "보상"}; }
                    case 9 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +40%§8p §7(총 200% 증가)", "보상"}; }

                }
            }
            case 5 -> { // 돌 도감 2
                switch (level){
                    case 1 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +20%§8p §7(총 20% 증가)", "보상"}; }
                    case 2 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +20%§8p §7(총 40% 증가)", "보상"}; }
                    case 3 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +20%§8p §7(총 60% 증가)", "보상"}; }
                    case 4 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +20%§8p §7(총 80% 증가)", "보상"}; }
                    case 5 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +20%§8p §7(총 100% 증가)", "보상"}; }
                    case 6 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +20%§8p §7(총 120% 증가)", "보상"}; }
                    case 7 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +20%§8p §7(총 140% 증가)", "보상"}; }
                    case 8 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +20%§8p §7(총 160% 증가)", "보상"}; }
                    case 9 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +40%§8p §7(총 200% 증가)", "보상"}; }

                }
            }
            case 6 -> { // 광물 도감 1 (철 석탄 구리)
                switch (level){
                    case 1 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 5% 증가)", "보상"}; }
                    case 2 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 10% 증가)", "보상"}; }
                    case 3 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 20% 증가)", "보상"}; }
                    case 4 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 30% 증가)", "보상"}; }
                    case 5 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 40% 증가)", "보상"}; }
                    case 6 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +20%§8p §7(총 60% 증가)", "보상"}; }
                    case 7 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +20%§8p §7(총 80% 증가)", "보상"}; }
                    case 8 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +20%§8p §7(총 100% 증가)", "보상"}; }
                    case 9 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +30%§8p §7(총 130% 증가)", "보상"}; }

                }
            }
            case 7 -> { // 금 도감
                switch (level){
                    case 1 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +7%§8p §7(총 7% 증가)", "보상"}; }
                    case 2 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +7%§8p §7(총 14% 증가)", "보상"}; }
                    case 3 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +7%§8p §7(총 21% 증가)", "보상"}; }
                    case 4 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +9%§8p §7(총 30% 증가)", "보상"}; }
                    case 5 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +17%§8p §7(총 47% 증가)", "보상"}; }
                    case 6 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +17%§8p §7(총 64% 증가)", "보상"}; }
                    case 7 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +17%§8p §7(총 81% 증가)", "보상"}; }
                    case 8 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +19%§8p §7(총 100% 증가)", "보상"}; }
                    case 9 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +30%§8p §7(총 130% 증가)", "보상"}; }

                }
            }
            case 8 -> { // 다이아 도감
                switch (level){
                    case 1 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +3%§8p §7(총 3% 증가)", "보상"}; }
                    case 2 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +3%§8p §7(총 6% 증가)", "보상"}; }
                    case 3 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +3%§8p §7(총 9% 증가)", "보상"}; }
                    case 4 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +6%§8p §7(총 15% 증가)", "보상"}; }
                    case 5 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +6%§8p §7(총 21% 증가)", "보상"}; }
                    case 6 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +6%§8p §7(총 27% 증가)", "보상"}; }
                    case 7 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +8%§8p §7(총 35% 증가)", "보상"}; }
                    case 8 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 45% 증가)", "보상"}; }
                    case 9 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +15%§8p §7(총 60% 증가)", "보상"}; }

                }
            }
            case 9 -> { // 에메랄드
                switch (level){
                    case 1 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +2%§8p §7(총 2% 증가)", "보상"}; }
                    case 2 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +2%§8p §7(총 4% 증가)", "보상"}; }
                    case 3 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +2%§8p §7(총 6% 증가)", "보상"}; }
                    case 4 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +4%§8p §7(총 10% 증가)", "보상"}; }
                    case 5 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 15% 증가)", "보상"}; }
                    case 6 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 20% 증가)", "보상"}; }
                    case 7 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 25% 증가)", "보상"}; }
                    case 8 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 35% 증가)", "보상"}; }
                    case 9 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +15%§8p §7(총 50% 증가)", "보상"}; }
                }
            }
            case 10 -> { // 청금 레드스톤 광물2
                switch (level){
                    case 1 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 5% 증가)", "보상"}; }
                    case 2 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 10% 증가)", "보상"}; }
                    case 3 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 15% 증가)", "보상"}; }
                    case 4 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 20% 증가)", "보상"}; }
                    case 5 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 30% 증가)", "보상"}; }
                    case 6 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 40% 증가)", "보상"}; }
                    case 7 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 50% 증가)", "보상"}; }
                    case 8 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +15%§8p §7(총 65% 증가)", "보상"}; }
                    case 9 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +15%§8p §7(총 80% 증가)", "보상"}; }


                }
            }
            case 11 -> { // 석영 도감
                switch (level){
                    case 1 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 5% 증가)", "보상"}; }
                    case 2 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 10% 증가)", "보상"}; }
                    case 3 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 15% 증가)", "보상"}; }
                    case 4 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 20% 증가)", "보상"}; }
                    case 5 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 30% 증가)", "보상"}; }
                    case 6 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 40% 증가)", "보상"}; }
                    case 7 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 50% 증가)", "보상"}; }
                    case 8 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +15%§8p §7(총 65% 증가)", "보상"}; }
                    case 9 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +15%§8p §7(총 80% 증가)", "보상"}; }


                }
            }
            case 12 -> { // 발광석 도감
                switch (level){
                    case 1 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 5% 증가)", "보상"}; }
                    case 2 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 10% 증가)", "보상"}; }
                    case 3 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 15% 증가)", "보상"}; }
                    case 4 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 20% 증가)", "보상"}; }
                    case 5 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 30% 증가)", "보상"}; }
                    case 6 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +15%§8p §7(총 45% 증가)", "보상"}; }
                    case 7 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +20%§8p §7(총 65% 증가)", "보상"}; }
                    case 8 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +25%§8p §7(총 90% 증가)", "보상"}; }
                    case 9 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +50%§8p §7(총 140% 증가)", "보상"}; }

                }
            }
            case 13 -> { // 고대잔해
                switch (level){
                    case 1 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +4%§8p §7(총 4% 증가)", "보상"}; }
                    case 2 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +4%§8p §7(총 8% 증가)", "보상"}; }
                    case 3 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +4%§8p §7(총 12% 증가)", "보상"}; }
                    case 4 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +4%§8p §7(총 16% 증가)", "보상"}; }
                    case 5 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +4%§8p §7(총 20% 증가)", "보상"}; }
                    case 6 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 25% 증가)", "보상"}; }
                    case 7 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 30% 증가)", "보상"}; }
                    case 8 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 40% 증가)", "보상"}; }
                    case 9 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 50% 증가)", "보상"}; }


                }
            }
            case 14 -> { // 좀비 몬스터1
                switch (level){
                    case 1 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 5% 증가)", "보상"}; }
                    case 2 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 10% 증가)", "보상"}; }
                    case 3 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 15% 증가)", "보상"}; }
                    case 4 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 20% 증가)", "보상"}; }
                    case 5 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 30% 증가)", "보상"}; }
                    case 6 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 40% 증가)", "보상"}; }
                    case 7 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 50% 증가)", "보상"}; }
                    case 8 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +15%§8p §7(총 65% 증가)", "보상"}; }
                    case 9 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +15%§8p §7(총 80% 증가)", "보상"}; }

                }
            }
            case 15 -> { // 몬스터2
                switch (level){
                    case 1 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +8%§8p §7(총 8% 증가)", "보상"}; }
                    case 2 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +8%§8p §7(총 16% 증가)", "보상"}; }
                    case 3 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +8%§8p §7(총 24% 증가)", "보상"}; }
                    case 4 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +12%§8p §7(총 36% 증가)", "보상"}; }
                    case 5 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +12%§8p §7(총 48% 증가)", "보상"}; }
                    case 6 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +12%§8p §7(총 60% 증가)", "보상"}; }
                    case 7 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +20%§8p §7(총 80% 증가)", "보상"}; }
                    case 8 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +20%§8p §7(총 100% 증가)", "보상"}; }
                    case 9 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +25%§8p §7(총 125% 증가)", "보상"}; }

                }
            }
            case 16 -> { // 지옥몹1
                switch (level){
                    case 1 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 5% 증가)", "보상"}; }
                    case 2 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 10% 증가)", "보상"}; }
                    case 3 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 15% 증가)", "보상"}; }
                    case 4 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 20% 증가)", "보상"}; }
                    case 5 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 30% 증가)", "보상"}; }
                    case 6 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 40% 증가)", "보상"}; }
                    case 7 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 50% 증가)", "보상"}; }
                    case 8 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +15%§8p §7(총 65% 증가)", "보상"}; }
                    case 9 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +15%§8p §7(총 80% 증가)", "보상"}; }

                }
            }
            case 17 -> { // 지옥몹2
                switch (level){
                    case 1 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 5% 증가)", "보상"}; }
                    case 2 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 10% 증가)", "보상"}; }
                    case 3 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 15% 증가)", "보상"}; }
                    case 4 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 20% 증가)", "보상"}; }
                    case 5 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 30% 증가)", "보상"}; }
                    case 6 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 40% 증가)", "보상"}; }
                    case 7 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 50% 증가)", "보상"}; }
                    case 8 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +15%§8p §7(총 65% 증가)", "보상"}; }
                    case 9 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +15%§8p §7(총 80% 증가)", "보상"}; }

                }
            }
            case 18 -> { // 드래곤
                switch (level){
                    case 1 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 10% 증가)", "보상"}; }
                    case 2 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 20% 증가)", "보상"}; }
                    case 3 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 30% 증가)", "보상"}; }
                    case 4 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 40% 증가)", "보상"}; }
                    case 5 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 50% 증가)", "보상"}; }
                    case 6 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 60% 증가)", "보상"}; }
                    case 7 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 70% 증가)", "보상"}; }
                    case 8 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 80% 증가)", "보상"}; }
                    case 9 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +20%§8p §7(총 100% 증가)", "보상"}; }

                }
            }
            case 19 -> { // 습격자
                switch (level){
                    case 1 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 5% 증가)", "보상"}; }
                    case 2 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 10% 증가)", "보상"}; }
                    case 3 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 15% 증가)", "보상"}; }
                    case 4 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 20% 증가)", "보상"}; }
                    case 5 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 30% 증가)", "보상"}; }
                    case 6 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 40% 증가)", "보상"}; }
                    case 7 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 50% 증가)", "보상"}; }
                    case 8 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +15%§8p §7(총 65% 증가)", "보상"}; }
                    case 9 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +15%§8p §7(총 80% 증가)", "보상"}; }

                }
            }
            case 20 -> { // 워든
                switch (level){
                    case 1 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 5% 증가)", "보상"}; }
                    case 2 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 10% 증가)", "보상"}; }
                    case 3 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 15% 증가)", "보상"}; }
                    case 4 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 20% 증가)", "보상"}; }
                    case 5 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 30% 증가)", "보상"}; }
                    case 6 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 40% 증가)", "보상"}; }
                    case 7 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 50% 증가)", "보상"}; }
                    case 8 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +15%§8p §7(총 65% 증가)", "보상"}; }
                    case 9 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +15%§8p §7(총 80% 증가)", "보상"}; }

                }
            }
            case 21 -> { // 가디언
                switch (level){
                    case 1 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 5% 증가)", "보상"}; }
                    case 2 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 10% 증가)", "보상"}; }
                    case 3 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 15% 증가)", "보상"}; }
                    case 4 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 20% 증가)", "보상"}; }
                    case 5 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 30% 증가)", "보상"}; }
                    case 6 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 40% 증가)", "보상"}; }
                    case 7 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 50% 증가)", "보상"}; }
                    case 8 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +15%§8p §7(총 65% 증가)", "보상"}; }
                    case 9 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +15%§8p §7(총 80% 증가)", "보상"}; }

                }
            }
            case 22 -> { // 밀
                switch (level){
                    case 1 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +3%§8p §7(총 3% 증가)", "보상"}; }
                    case 2 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +3%§8p §7(총 6% 증가)", "보상"}; }
                    case 3 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +3%§8p §7(총 9% 증가)", "보상"}; }
                    case 4 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +6%§8p §7(총 15% 증가)", "보상"}; }
                    case 5 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +6%§8p §7(총 21% 증가)", "보상"}; }
                    case 6 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +9%§8p §7(총 30% 증가)", "보상"}; }
                    case 7 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 40% 증가)", "보상"}; }
                    case 8 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 50% 증가)", "보상"}; }
                    case 9 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 60% 증가)", "보상"}; }

                }
            }
            case 23 -> { // 사탕수수
                switch (level){
                    case 1 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 5% 증가)", "보상"}; }
                    case 2 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 10% 증가)", "보상"}; }
                    case 3 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 20% 증가)", "보상"}; }
                    case 4 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 30% 증가)", "보상"}; }
                    case 5 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 40% 증가)", "보상"}; }
                    case 6 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +15%§8p §7(총 55% 증가)", "보상"}; }
                    case 7 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +15%§8p §7(총 70% 증가)", "보상"}; }
                    case 8 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +15%§8p §7(총 85% 증가)", "보상"}; }
                    case 9 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +15%§8p §7(총 100% 증가)", "보상"}; }


                }
            }
            case 24 -> { // 감자당근
                switch (level){
                    case 1 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +3%§8p §7(총 3% 증가)", "보상"}; }
                    case 2 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +3%§8p §7(총 6% 증가)", "보상"}; }
                    case 3 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +3%§8p §7(총 9% 증가)", "보상"}; }
                    case 4 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +6%§8p §7(총 15% 증가)", "보상"}; }
                    case 5 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +6%§8p §7(총 21% 증가)", "보상"}; }
                    case 6 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +9%§8p §7(총 30% 증가)", "보상"}; }
                    case 7 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 40% 증가)", "보상"}; }
                    case 8 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 50% 증가)", "보상"}; }
                    case 9 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 60% 증가)", "보상"}; }


                }
            }
            case 25 -> { // 수박호박
                switch (level){
                    case 1 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 5% 증가)", "보상"}; }
                    case 2 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +5%§8p §7(총 10% 증가)", "보상"}; }
                    case 3 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 20% 증가)", "보상"}; }
                    case 4 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 30% 증가)", "보상"}; }
                    case 5 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +10%§8p §7(총 40% 증가)", "보상"}; }
                    case 6 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +15%§8p §7(총 55% 증가)", "보상"}; }
                    case 7 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +15%§8p §7(총 70% 증가)", "보상"}; }
                    case 8 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +15%§8p §7(총 85% 증가)", "보상"}; }
                    case 9 -> { return new String[]{"§f- §e도감 내 항목 경험치 획득량 +15%§8p §7(총 100% 증가)", "보상"}; }

                }
            }
        }
        return new String[]{"§crewardList"+type.getCode()+"번을 찾을 수 없습니다"};

    }


    private String[] collectionList(CollectionManager.CollectionType type) {
        switch (type.getCode()) {
            case 0 -> { return new String[]{"§7- 참나무", "§7- 자작나무", "§7- 가문비나무"}; }
            case 1 -> { return new String[]{"§7- 아카시아나무", "§7- 짙은 참나무", "§7- 정글나무"}; }
            case 2 -> { return new String[]{"§7- 창백한 참나무", "§7- 벚나무", "§7- 맹그로브나무"}; }
            case 3 -> { return new String[]{"§7- 진홍빛 균사", "§7- 뒤틀린 균사"}; }
            case 4 -> { return new String[]{"§7- 돌", "§7- 조약돌", "§7- 심층암", "§7- 심층암 조약돌", "§7- 방해석", "§7- 응회암", "§7- 흑요석"}; }
            case 5 -> { return new String[]{"§7- 화강암", "§7- 섬록암", "§7- 안산암", "§7- 모래", "§7- 자갈"}; }
            case 6 -> { return new String[]{"§7- 철 원석", "§7- 석탄 원석", "§7- 구리 원석"}; }
            case 7 -> { return new String[]{"§7- 금 원석"}; }
            case 8 -> { return new String[]{"§7- 다이아몬드 원석"}; }
            case 9 -> { return new String[]{"§7- 에메랄드 원석"}; }
            case 10 -> { return new String[]{"§7- 청금석 원석", "§7- 레드스톤 원석"}; }
            case 11 -> { return new String[]{"§7- 네더 석영"}; }
            case 12 -> { return new String[]{"§7- 발광석"}; }
            case 13 -> { return new String[]{"§7- 고대 잔해"}; }
            case 14 -> { return new String[]{"§7- 좀비", "§7- 주민 좀비", "§7- 스켈레톤", "§7- 거미", "§7- 동굴 거미", "§7- 드라운드"}; }
            case 15 -> { return new String[]{"§7- 크리퍼", "§7- 마녀", "§7- 슬라임", "§7- 허스크", "§7- 스트레이"}; }
            case 16 -> { return new String[]{"§7- 호글린", "§7- 피글린", "§7- 조글린", "§7- 난폭한 피글린"}; }
            case 17 -> { return new String[]{"§7- 블레이즈", "§7- 위더 스켈레톤", "§7- 마그마 큐브", "§7- 가스트"}; }
            case 18 -> { return new String[]{"§7- 엔더 드래곤"}; }
            case 19 -> { return new String[]{"§7- 변명자", "§7- 소환사", "§7- 벡스", "§7- 약탈자", "§7- 파괴수"}; }
            case 20 -> { return new String[]{"§7- 워든"}; }
            case 21 -> { return new String[]{"§7- 가디언", "§7- 엘더 가디언"}; }
            case 22 -> { return new String[]{"§7- 밀"}; }
            case 23 -> { return new String[]{"§7- 사탕수수"}; }
            case 24 -> { return new String[]{"§7- 감자", "§7- 당근"}; }
            case 25 -> { return new String[]{"§7- 호박", "§7- 수박"}; }






        }
        return new String[]{"§ccollectionList"+type.getCode()+"번을 찾을 수 없습니다"};

    }
}
