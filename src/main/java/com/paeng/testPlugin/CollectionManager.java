package com.paeng.testPlugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class CollectionManager {

    public static YamlConfiguration modifiedFile;
    public static File file;

    public enum CollectionType {
        WOOD1(0, "나무 도감 I", new int[]{64, 128, 256, 512, 1024, 1536, 2048, 3072, 4096}, new double[]{1.05, 1.1, 1.15, 1.2, 1.3, 1.4, 1.5, 1.65, 1.8}, Material.OAK_LOG),
        WOOD2(1, "나무 도감 II", new int[]{64, 128, 256, 512, 1024, 1536, 2048, 3072, 4096}, new double[]{1.05, 1.1, 1.15, 1.2, 1.3, 1.4, 1.5, 1.65, 1.8}, Material.JUNGLE_LOG),
        WOOD3(2, "나무 도감 III", new int[]{64, 128, 256, 512, 1024, 1536, 2048, 3072, 4096}, new double[]{1.05, 1.1, 1.15, 1.2, 1.3, 1.4, 1.5, 1.65, 1.8}, Material.CHERRY_LOG),
        WOOD4(3, "지옥 나무 도감", new int[]{64, 128, 256, 512, 1024, 1536, 2048, 3072, 4096}, new double[]{1.05, 1.1, 1.15, 1.2, 1.3, 1.4, 1.5, 1.65, 1.8}, Material.WARPED_STEM),
        STONE(4, "돌 도감 I", new int[]{100, 250, 1000, 2500, 5000, 10000, 20000, 30000, 50000}, new double[]{1.2, 1.4, 1.6, 1.8, 2.0, 2.2, 2.4, 2.6, 3.0}, Material.STONE),
        STONE2(5, "돌 도감 II", new int[]{100, 250, 1000, 2500, 5000, 10000, 20000, 30000, 50000}, new double[]{1.2, 1.4, 1.6, 1.8, 2.0, 2.2, 2.4, 2.6, 3.0}, Material.GRANITE),
        COAL(6, "광물 도감 I", new int[]{20, 40, 80, 160, 240, 360, 720, 1500, 3000}, new double[]{1.05, 1.1, 1.2, 1.3, 1.4, 1.6, 1.8, 2.0, 2.3}, Material.COAL),
        GOLD(7, "금 도감", new int[]{15, 30, 60, 100, 150, 250, 400, 600, 1000}, new double[]{1.07, 1.14, 1.21, 1.3, 1.47, 1.64, 1.81, 2.0, 2.3}, Material.GOLD_INGOT),
        DIAMOND(8, "다이아몬드 도감", new int[]{5, 15, 25, 40, 60, 80, 100, 150, 250}, new double[]{1.03, 1.06, 1.09, 1.15, 1.21, 1.27, 1.35, 1.45, 1.6}, Material.DIAMOND),
        EMERALD(9, "에메랄드 도감", new int[]{1, 3, 5, 10, 15, 20, 30, 45, 64}, new double[]{1.02, 1.04, 1.06, 1.1, 1.15, 1.2, 1.25, 1.35, 1.5}, Material.EMERALD),
        REDSTONE(10, "광물 도감 II", new int[]{15, 30, 60, 100, 150, 250, 400, 600, 1000}, new double[]{1.05, 1.1, 1.15, 1.2, 1.3, 1.4, 1.5, 1.65, 1.8}, Material.REDSTONE),
        QUARTZ(11, "석영 도감", new int[]{35, 100, 200, 400, 800, 1400, 2000, 3000, 4500}, new double[]{1.05, 1.1, 1.15, 1.2, 1.3, 1.4, 1.5, 1.65, 1.8}, Material.NETHER_QUARTZ_ORE),
        GLOWSTONE(12, "발광석 도감", new int[]{20, 40, 80, 150, 300, 600, 900, 1400, 2000}, new double[]{1.05, 1.1, 1.15, 1.2, 1.3, 1.45, 1.65, 1.9, 2.4}, Material.GLOWSTONE),
        NETHERITE(13, "고대 잔해 도감", new int[]{4, 8, 16, 20, 28, 40, 64, 88, 128}, new double[]{1.04, 1.08, 1.12, 1.16, 1.2, 1.25, 1.3, 1.4, 1.5}, Material.ANCIENT_DEBRIS),
        ZOMBIE(14, "몬스터 도감 I", new int[]{10, 25, 70, 150, 250, 450, 700, 1000, 1500}, new double[]{1.05, 1.1, 1.15, 1.2, 1.3, 1.4, 1.5, 1.65, 1.8}, Material.ZOMBIE_HEAD),
        CREEPER(15, "몬스터 도감 II", new int[]{10, 25, 70, 150, 250, 450, 700, 1000, 1500}, new double[]{1.08, 1.16, 1.24, 1.36, 1.48, 1.6, 1.8, 2.0, 2.25}, Material.CREEPER_HEAD),
        PIGLIN(16, "지옥 몬스터 도감 I", new int[]{10, 20, 40, 80, 150, 250, 350, 480, 660}, new double[]{1.05, 1.1, 1.15, 1.2, 1.3, 1.4, 1.5, 1.65, 1.8}, Material.PIGLIN_HEAD),
        BLAZE(17, "지옥 몬스터 도감 II", new int[]{10, 20, 40, 80, 150, 250, 350, 480, 660}, new double[]{1.05, 1.1, 1.15, 1.2, 1.3, 1.4, 1.5, 1.65, 1.8}, Material.BLAZE_ROD),
        DRAGON(18, "엔더 드래곤 도감", new int[]{1, 2, 3, 4, 5, 6, 7, 8, 10}, new double[]{1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7, 1.8, 2.0}, Material.DRAGON_HEAD),
        RAVAGER(19, "습격자 도감", new int[]{10, 20, 35, 50, 75, 125, 175, 255, 400}, new double[]{1.05, 1.1, 1.15, 1.2, 1.3, 1.4, 1.5, 1.65, 1.8}, Material.OMINOUS_BOTTLE),
        WARDEN(20, "워든 도감", new int[]{1, 2, 4, 6, 10, 14, 18, 24, 30}, new double[]{1.05, 1.1, 1.15, 1.2, 1.3, 1.4, 1.5, 1.65, 1.8}, Material.SCULK_SENSOR),
        GUARDIAN(21, "가디언 도감", new int[]{2, 4, 8, 16, 32, 48, 64, 100, 150}, new double[]{1.05, 1.1, 1.15, 1.2, 1.3, 1.4, 1.5, 1.65, 1.8}, Material.HEART_OF_THE_SEA),
        WHEAT(22, "밀 도감", new int[]{30, 60, 120, 500, 1000, 2000, 3500, 5000, 7500}, new double[]{1.03, 1.06, 1.09, 1.15, 1.21, 1.3, 1.4, 1.5, 1.6}, Material.WHEAT),
        CANE(23, "사탕수수 도감", new int[]{60, 240, 500, 1500, 3000, 6000, 12000, 24000, 50000}, new double[]{1.05, 1.1, 1.2, 1.3, 1.4, 1.55, 1.7, 1.85, 2.0}, Material.SUGAR_CANE),
        CARROT(24, "당근과 감자 도감", new int[]{30, 60, 120, 500, 1000, 2000, 3500, 5000, 7500}, new double[]{1.03, 1.06, 1.09, 1.15, 1.21, 1.3, 1.4, 1.5, 1.6}, Material.CARROT),
        PUMPKIN(25, "호박과 수박 도감", new int[]{30, 120, 250, 750, 1500, 3000, 6000, 12000, 25000}, new double[]{1.05, 1.1, 1.2, 1.3, 1.4, 1.55, 1.7, 1.85, 2.0}, Material.PUMPKIN);

        private final int code;
        private final String nameKor;
        private final int[] requireExp;
        private final double[] expWisdom;
        private final Material displayItem;

        CollectionType(final int code, final String nameKor, final int[] requireExp, final double[] expWisdom, Material displayItem) {
            this.code = code;
            this.nameKor = nameKor;
            this.requireExp = requireExp;
            this.expWisdom = expWisdom;
            this.displayItem = displayItem;
        }

        public int getCode() {
            return code;
        }

        public String getKorName() {
            return nameKor;
        }

        public Material getDisplayItem() {
            return displayItem;
        }

        public int getRequireExp(int level) {
            if (level >= 9) {
                return 0;
            }
            return requireExp[level];
        }

        public int[] getExpTable() {
            return requireExp;
        }

        public double getExpWisdom(int level) {
            if (level < 1 || level > 9) {
                return 1;
            }
            return expWisdom[level - 1];
        }
    }


    private static final HashMap<CollectionType, HashMap<String, Integer>> expData = new HashMap<>();

    static {
        for (CollectionType type : CollectionType.values()) {
            expData.put(type, new HashMap<>());
        }
    }

    public static void setExperience(CollectionType type, Player player, int exp) {
        expData.get(type).put(String.valueOf(player.getUniqueId()), exp);
    }

    public static int getExperience(CollectionType type, Player player) {
        return expData.get(type).getOrDefault(String.valueOf(player.getUniqueId()), 0);


    }

    public static int getLevel(CollectionType type, Player player) {
        int exp = expData.get(type).getOrDefault(String.valueOf(player.getUniqueId()), 0);

        int[] expTable = type.getExpTable();
        for (int i = 0; i < expTable.length; i++) {
            if (exp < expTable[i]) {
                return i;
            }
        }
        return expTable.length;
    }

    public static void addExperience(CollectionType type, Player player, int exp) {
        int currentExp;
        try {
            currentExp = expData.get(type).getOrDefault(String.valueOf(player.getUniqueId()), 0);
        } catch (NullPointerException e) {
            return;
        }
        expData.get(type).put(String.valueOf(player.getUniqueId()), currentExp + exp);

        int[] expTable = type.getExpTable();
        for (int i = 0; i < expTable.length; i++) {
            if ((currentExp < expTable[i]) && (currentExp + exp >= expTable[i])) {
                player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0F, 1.5F);
                Bukkit.broadcastMessage("§b\uD83D\uDCD5 " + player.getName() + "§f님이 " + type.getKorName() + " §b" + getLevel(type, player) + "레벨§f을 달성했습니다!");
            }
        }


    }




    public void saveCollectionData(Player player) {
        for (CollectionManager.CollectionType category : CollectionManager.CollectionType.values()) {
            int experienceData = getExperience(category,player);

            String path = "CollectionData." + player.getUniqueId() + "." + category.getCode() + ".exp";
            modifiedFile.set(path, experienceData);
        }

        try {
            modifiedFile.save(file);
        } catch (IOException exception) {
            Bukkit.getLogger().warning("IO Exception while trying to save file: " + exception.getMessage());
        }
    }

    public void loadCollectionData(Player player) {
        ConfigurationSection dataSection = modifiedFile.getConfigurationSection("CollectionData");

        if (dataSection == null) return;

        String UUID = String.valueOf(player.getUniqueId());
        for (CollectionManager.CollectionType category : CollectionManager.CollectionType.values()) {
            setExperience(category,player,modifiedFile.getInt("CollectionData." + UUID + "." + category.getCode() + ".exp"));
        }
    }

    // When Server loads
    public void startUp() {
        TestPlugin pluginInstance = JavaPlugin.getPlugin(TestPlugin.class);

        file = new File(pluginInstance.getDataFolder(), "collectionData.yml");

        modifiedFile = YamlConfiguration.loadConfiguration(file);
    }

}
