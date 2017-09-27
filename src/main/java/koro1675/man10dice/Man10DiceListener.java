package koro1675.man10dice;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class Man10DiceListener implements Listener {

    Man10Dice pl = null;

    public Man10DiceListener(Man10Dice plugin){
        this.pl = null;
    }

    String prefix = "§l[§d§lM§f§la§a§ln§f§l10§5§lDice§f§l]";
    boolean someoneInMenu = false;

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        if (pl.man10DiceManager.flag == 0){
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
            if (pl.man10DiceManager.who.containsValue(p.getName())){
                p.sendMessage(prefix + "§a§lあなたはもう数字を言いました");
                e.setCancelled(true);
                return;
            }
            if (pl.man10DiceManager.who.containsKey(s)){
                p.sendMessage(prefix + "§c§lすでにその数字は言われています！");
                e.setCancelled(true);
                return;
            }
            if (!pl.man10DiceManager.mes){
                e.setCancelled(true);
                p.sendMessage(prefix + "§e§l" + s + "§a§lと回答しました!");
                pl.man10DiceManager.owner.sendMessage(prefix + p.getName() + "さんが" + "§e§l" + s + "§a§lと回答しました");
                return;
            }
        } catch (NumberFormatException NFE){
            return;
        }
    }

    @EventHandler
    public void onClickInventory(InventoryClickEvent e){
        if (someoneInMenu == false){
            return;
        }
        if (!pl.man10DiceManager.inMenu.contains(e.getWhoClicked())){
            return;
        }
        Player p = (Player) e.getWhoClicked();

        if (!p.isOp()) {
            if (System.currentTimeMillis() - pl.man10DiceManager.lastDiceTime < pl.man10DiceManager.coolTime) {
                p.sendMessage(prefix + "現在クールダウン中です");
                e.setCancelled(true);
                return;
            }
            pl.man10DiceManager.lastDiceTime = System.currentTimeMillis();
        }
        if (pl.man10DiceManager.inMenu.contains(e.getWhoClicked())){
            if (e.getSlot() == 0){
                pl.man10DiceManager.help(p);
                e.setCancelled(true);
            }
            if (e.getSlot() == 1){
                pl.man10DiceManager.i = 6;
                pl.man10DiceManager.roll(p);
                e.setCancelled(true);
            }
            if (e.getSlot() == 4){
                pl.man10DiceManager.i = 12;
                pl.man10DiceManager.roll(p);
                e.setCancelled(true);
            }
            if (e.getSlot() == 7){
                pl.man10DiceManager.i = 100;
                pl.man10DiceManager.roll(p);
                e.setCancelled(true);
            }
            if (e.getSlot() == 8){
                e.setCancelled(true);
                if (!p.hasPermission("man10.mdice.d100")){
                    p.sendMessage(prefix + "§c§lyou do not have permission");
                    return;
                }
                pl.man10DiceManager.d100(p);
            }
            return;
        }
    }

    @EventHandler
    public void onCloseInventory(InventoryCloseEvent e){
        if (someoneInMenu == false){
            return;
        }
        if (pl.man10DiceManager.inMenu.contains(e.getPlayer())){
            pl.man10DiceManager.inMenu.remove(e.getPlayer());
        }
    }
}
