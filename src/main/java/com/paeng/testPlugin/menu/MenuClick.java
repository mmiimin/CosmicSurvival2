package com.paeng.testPlugin.menu;

import com.paeng.testPlugin.CollectionManager;
import com.paeng.testPlugin.Profile;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

import static java.lang.Integer.parseInt;

public class MenuClick implements Listener
{
    Profile profile = new Profile();

    CollectionMenu collectionMenu = new CollectionMenu();
    CosmeticMenu cosmeticMenu = new CosmeticMenu();

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {

        if (!(e.getView().getTitle().contains("§0"))) return;
        e.setCancelled(true);
        final ItemStack clickedItem = e.getCurrentItem();
        if (clickedItem == null || clickedItem.getType().isAir()) return;
        final Player player = (Player) e.getWhoClicked();
        final int slot = e.getRawSlot();

        if (e.getView().getTitle().equals("§0프로필")){
            switch (slot) {
                case 36 -> collectionMenu.OpenCollectionMenu(player);
                case 8 -> player.closeInventory();
                case 44 -> cosmeticMenu.openCosmeticMenu(player);
            }
        }

        else if (e.getView().getTitle().equals("§0도감")){
            switch (slot) {
                case 9 -> collectionMenu.OpenCollectionDetail(player, CollectionManager.CollectionType.WOOD1);
                case 10 -> collectionMenu.OpenCollectionDetail(player, CollectionManager.CollectionType.WOOD2);
                case 11 -> collectionMenu.OpenCollectionDetail(player, CollectionManager.CollectionType.WOOD3);
                case 12 -> collectionMenu.OpenCollectionDetail(player, CollectionManager.CollectionType.WOOD4);
                case 13 -> collectionMenu.OpenCollectionDetail(player, CollectionManager.CollectionType.STONE);
                case 14 -> collectionMenu.OpenCollectionDetail(player, CollectionManager.CollectionType.STONE2);
                case 15 -> collectionMenu.OpenCollectionDetail(player, CollectionManager.CollectionType.COAL);
                case 16 -> collectionMenu.OpenCollectionDetail(player, CollectionManager.CollectionType.GOLD);
                case 17 -> collectionMenu.OpenCollectionDetail(player, CollectionManager.CollectionType.DIAMOND);
                case 18 -> collectionMenu.OpenCollectionDetail(player, CollectionManager.CollectionType.EMERALD);
                case 19 -> collectionMenu.OpenCollectionDetail(player, CollectionManager.CollectionType.REDSTONE);
                case 20 -> collectionMenu.OpenCollectionDetail(player, CollectionManager.CollectionType.QUARTZ);
                case 21 -> collectionMenu.OpenCollectionDetail(player, CollectionManager.CollectionType.GLOWSTONE);
                case 22 -> collectionMenu.OpenCollectionDetail(player, CollectionManager.CollectionType.NETHERITE);
                case 23 -> collectionMenu.OpenCollectionDetail(player, CollectionManager.CollectionType.ZOMBIE);
                case 24 -> collectionMenu.OpenCollectionDetail(player, CollectionManager.CollectionType.CREEPER);
                case 25 -> collectionMenu.OpenCollectionDetail(player, CollectionManager.CollectionType.PIGLIN);
                case 26 -> collectionMenu.OpenCollectionDetail(player, CollectionManager.CollectionType.BLAZE);
                case 27 -> collectionMenu.OpenCollectionDetail(player, CollectionManager.CollectionType.DRAGON);
                case 28 -> collectionMenu.OpenCollectionDetail(player, CollectionManager.CollectionType.RAVAGER);
                case 29 -> collectionMenu.OpenCollectionDetail(player, CollectionManager.CollectionType.WARDEN);
                case 30 -> collectionMenu.OpenCollectionDetail(player, CollectionManager.CollectionType.GUARDIAN);
                case 31 -> collectionMenu.OpenCollectionDetail(player, CollectionManager.CollectionType.WHEAT);
                case 32 -> collectionMenu.OpenCollectionDetail(player, CollectionManager.CollectionType.CANE);
                case 33 -> collectionMenu.OpenCollectionDetail(player, CollectionManager.CollectionType.CARROT);
                case 34 -> collectionMenu.OpenCollectionDetail(player, CollectionManager.CollectionType.PUMPKIN);



                case 49 -> {
                    player.performCommand("menu");
                }

            }
        }
        else if (e.getView().getTitle().contains("§0도감 정보")){
            switch (slot) {
                case 40 -> collectionMenu.OpenCollectionMenu(player);
            }
        }

        else if (e.getView().getTitle().equals("§0치장품")){
            switch (slot) {
                case 12 -> cosmeticMenu.openHitEffect(player, 1);
                case 31 -> player.performCommand("menu");
            }
        }

        else if (e.getView().getTitle().contains("§0타격 파티클")){
            switch (slot) {
                case 12 -> cosmeticMenu.openHitEffect(player, 1);
                case 49 -> player.performCommand("menu");
            }
        }
    }

    private int getCosmeticNumber(int inv, int page) {
        int result = inv;
        if (result>=37) { result-=6; }
        else if (result>=28) { result-=4; }
        else if (result>=19) { result-=2; }
        result-=10;
        result+=(28*(page-1));
        return result;
    }
}
