package koro1675.man10dice;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class Man10DiceManager {

    /////////////////////////////////////////////////////////////////
    boolean mes = false;
    boolean someoneInMenu = false;
    long lastDiceTime = System.currentTimeMillis();
    long coolTime = 5000;
    long range = 0;
    int i = 0;
    int result = 0;
    int flag = 0;
    HashMap<Integer, String> who = new HashMap<>();
    HashMap <Player, Player> now100d = new HashMap<>();
    ArrayList<Player> inMenu = new ArrayList<>();
    Player owner;
    String prefix = "§l[§d§lM§f§la§a§ln§f§l10§5§lDice§f§l]";
    /////////////////////////////////////////////////////////////////


    int openDiceMenu(Player p){
        Inventory inv = Bukkit.getServer().createInventory(null, 9, prefix + "Dice Select Menu");
        //gui help
        ItemStack dicehelp = new ItemStack(Material.PAPER);
        ItemMeta dicehelpMeta = dicehelp.getItemMeta();
        dicehelpMeta.setDisplayName("§7§lDiceHelp");
        dicehelp.setItemMeta(dicehelpMeta);
        inv.setItem(0, dicehelp);
        //mdice 6
        ItemStack dice6 = new ItemStack(Material.CHEST);
        ItemMeta dice6Meta = dice6.getItemMeta();
        dice6Meta.setDisplayName("§e§l6§3§l面Dice");
        dice6.setItemMeta(dice6Meta);
        inv.setItem(1, dice6);
        //mdice 12
        ItemStack dice12 = new ItemStack(Material.CHEST);
        ItemMeta dice12Meta = dice12.getItemMeta();
        dice12Meta.setDisplayName("§e§l12§3§l面Dice");
        dice12.setItemMeta(dice12Meta);
        inv.setItem(4, dice12);
        //mdice 100
        ItemStack dice100 = new ItemStack(Material.CHEST);
        ItemMeta dice100Meta = dice100.getItemMeta();
        dice100Meta.setDisplayName("§e§l100§3§l面Dice");
        dice100.setItemMeta(dice100Meta);
        inv.setItem(7, dice100);
        //100Dstart~stop自動
        ItemStack d100 = new ItemStack(Material.ARROW);
        ItemMeta d100Meta = d100.getItemMeta();
        d100Meta.setDisplayName("§e§l100D メッセージ表示:" + mes);
        d100.setItemMeta(d100Meta);
        inv.setItem(8, d100);
        //開いた時にlistに追加
        inMenu.add(p);
        someoneInMenu = true;
        p.openInventory(inv);
        /*
        Inventory diceMenu = Bukkit.getServer().createInventory(null, 9, prefix + "Dice Select Menu");
        //gui help
        placeItem(0, diceMenu, Material.BOOK, 1, "§d§lHelp", null, null, null);
        //mdice 6
        placeItem(1, diceMenu, Material.CHEST, 1, "§e§l6§3§l面Dice", null, null, null);
        //mdice 12
        placeItem(3, diceMenu, Material.CHEST, 1, "§e§l12§3§l面Dice", null, null, null);
        //mdice 100
        placeItem(7, diceMenu, Material.CHEST, 1, "§e§l100§3§l面Dice", null, null, null);
        //100Dstart~stop自動
        if (p.hasPermission("man10.mdice.d100")) {
            placeItem(1, diceMenu, Material.ARROW, 1, "§6§l100D§3§lShowMessage:" + mes, null, null, null);
        }
        //開いた時にlistに追加
        inMenu.add(p);
        p.openInventory(diceMenu);
         */
        return 0;
    }

    int toggleShowMessage(Player p) {
        if (!mes) {
            mes = true;
            p.sendMessage(prefix + "§b§lメッセージ表示を§f§l§nfalse§b§lから§f§l§ntrue§b§lにしました");
            return 0;
        }
        mes = false;
        p.sendMessage(prefix + "§b§lメッセージ表示を§f§l§ntrue§b§lから§f§l§nfalse§b§lにしました");
        return 0;
    }

    int checkShowMessage(Player p){
        p.sendMessage(prefix + "§b§lメッセージ表示" + mes);
        return 0;
    }

    int roll(Player p){
        Random r = new Random();
        result = r.nextInt(i) + 1;
        Bukkit.getServer().broadcastMessage(prefix + "§3§l" + p.getDisplayName() + "§3§lは§l" + ChatColor.YELLOW + "§l" + i + "§3§l面サイコロを振って" + ChatColor.YELLOW + "§l" + result + "§3§lが出た");
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
        return 0;
    }

    int d100(Player p){
        if (flag == 0) {
            flag = 1;
            now100d.put(p.getPlayer(), p.getPlayer());
            owner = p.getPlayer();
            Bukkit.broadcastMessage(prefix + p.getDisplayName() + "§d§lさんが§e§l100D§d§lをスタートしました！§a§l(半角数字のみだけ入力してください！)");
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    Bukkit.broadcastMessage(prefix + p.getDisplayName() + "§d§lさんが§e§l100D§d§lをストップしました！");
                    i = 100;
                    roll(p);

                    Integer ss = Integer.valueOf(result);
                    if (who.containsKey(ss)) {
                        Bukkit.broadcastMessage(prefix + "§5§l§n" + who.get(ss) + "§a§lさんが§6§l当てました！！！");
                    }
                    if (who.containsKey(ss - 1)) {
                        Bukkit.broadcastMessage(prefix + "§b§l" + who.get(ss - 1) + "§6§Lさんが１少ない前後で当てました！");
                    }
                    if (who.containsKey(ss + 1)) {
                        Bukkit.broadcastMessage(prefix + "§b§l" + who.get(ss + 1) + "§6§Lさんが１多い前後で当てました！");
                    }
                    flag = 0;
                    who.clear();
                    now100d.clear();
                    if (p.isOp()) {
                        return;
                    }
                }
            };
            Timer timer = new Timer();
            timer.schedule(task, 20000);
        }
        p.sendMessage(prefix + "§c§l100Dが実施されています！");
        return 0;
    }

    int placeItem(Integer slots, Inventory guiName, Material material, Integer amount, String itemName, List<String> loreList, Enchantment enchantment, Integer enchLevel){
        ItemStack GUIitemStack = new ItemStack(material, amount);
        ItemMeta GUIitemMeta = GUIitemStack.getItemMeta();
        GUIitemMeta.setDisplayName(itemName);
        GUIitemMeta.setLore(loreList);
        GUIitemMeta.addEnchant(enchantment,enchLevel,true);
        guiName.setItem(slots, GUIitemStack);
        return 0;
    }

    int help(Player p) {
        String prefix = "§f§l[§d§lM§f§la§a§ln10§b§lParty§f§l]";
        p.sendMessage("§7§l===============================================================");
        p.sendMessage(prefix);
        p.sendMessage("§3§l/mdice: §2GUI形式のダイスメニューを開きます");
        p.sendMessage("§3§l/mdice <数字> <半径>:§2指定した面のサイコロを回します(半径はなくても可)");
        p.sendMessage("§3§l/mdice help:§2あなたが今見ているものです");
        if (p.isOp()){
            p.sendMessage("§c§l/mdice 100d:§2100Dを開始します。");
            p.sendMessage("§c§l/mdice checkShowMessage§7(cmes)§c§l:§2100Dの回答の表示非表示を確認します。");
            p.sendMessage("§c§l/mdice toggleShowMessage§7(tmes)§c§l:§2100Dの回答の表示を切り替えます。");
        }
        p.sendMessage("§6§lcreated by koro1675");
        p.sendMessage("§7§l===============================================================");
        return 0;
    }
}
