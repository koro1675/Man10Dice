package koro1675.man10dice;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class Man10Dice extends JavaPlugin implements Listener {

    Man10DiceManager man10DiceManager = null;
    String prefix = "§l[§d§lM§f§la§a§ln§f§l10§5§lDice§f§l]";

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("mdice").setExecutor(new Man10DiceCommands(this));
        Bukkit.getServer().getPluginManager().registerEvents(this,this);
        man10DiceManager = new Man10DiceManager();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }



    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        if (man10DiceManager.flag == 0){
            return;
        }
        try {
            Player p = e.getPlayer();
            Integer s = Integer.valueOf(e.getMessage());
            if (s < 0 || s > 101){
                p.sendMessage(prefix + "§c§l1~100の数字を入力してください!");
                e.setCancelled(true);
                return;
            }
            if (man10DiceManager.who.containsValue(p.getName())){
                p.sendMessage(prefix + "§a§lあなたはもう数字を言いました");
                e.setCancelled(true);
                return;
            }
            if (man10DiceManager.who.containsKey(s)){
                p.sendMessage(prefix + "§c§lすでにその数字は言われています！");
                e.setCancelled(true);
                return;
            }
            if (!man10DiceManager.mes){
                e.setCancelled(true);
                p.sendMessage(prefix + "§e§l" + s + "§a§lと回答しました!");
                man10DiceManager.owner.sendMessage(prefix + p.getName() + "さんが" + "§e§l" + s + "§a§lと回答しました");
                return;
            }
        } catch (NumberFormatException NFE){
            return;
        }
    }

    @EventHandler
    public void onClickInventory(InventoryClickEvent e){
        if (!e.getInventory().getName().equalsIgnoreCase(prefix + "Dice Select Menu")){
            return;
        }
        if (!man10DiceManager.inMenu.contains(e.getWhoClicked())){
            return;
        }
        Player p = (Player) e.getWhoClicked();

        if (!p.isOp()) {
            if (System.currentTimeMillis() - man10DiceManager.lastDiceTime < man10DiceManager.coolTime) {
                p.sendMessage(prefix + "現在クールダウン中です");
                e.setCancelled(true);
                return;
            }
            man10DiceManager.lastDiceTime = System.currentTimeMillis();
        }
        if (man10DiceManager.inMenu.contains(e.getWhoClicked())){
            if (e.getSlot() == 0){
                man10DiceManager.help(p);
                e.setCancelled(true);
            }
            if (e.getSlot() == 1){
                man10DiceManager.i = 6;
                man10DiceManager.roll(p);
                e.setCancelled(true);
            }
            if (e.getSlot() == 4){
                man10DiceManager.i = 12;
                man10DiceManager.roll(p);
                e.setCancelled(true);
            }
            if (e.getSlot() == 7){
                man10DiceManager.i = 100;
                man10DiceManager.roll(p);
                e.setCancelled(true);
            }
            if (e.getSlot() == 8){
                e.setCancelled(true);
                if (!p.hasPermission("man10.mdice.d100")){
                    p.sendMessage(prefix + "§c§lyou do not have permission");
                    return;
                }
                man10DiceManager.d100(p);
                return;
            }
        }
    }

    @EventHandler
    public void onCloseInventory(InventoryCloseEvent e){
        if (!man10DiceManager.someoneInMenu){
            return;
        }
        if (man10DiceManager.inMenu.contains(e.getPlayer())){
            man10DiceManager.inMenu.remove(e.getPlayer());
        }
    }

}
