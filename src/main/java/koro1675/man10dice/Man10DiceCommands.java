package koro1675.man10dice;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Random;

public class Man10DiceCommands implements CommandExecutor {

    String prefix = "§l[§d§lM§f§la§a§ln§f§l10§5§lDice§f§l]";
    Man10Dice plugin = null;
    public Man10DiceCommands(Man10Dice plugin){
        this.plugin = null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player) sender;
        /////////////////////////////////////
        // openMenu
        /////////////////////////////////////
        if (args.length == 0){
            p.sendMessage(prefix + "ver2.1");
            plugin.man10DiceManager.openDiceMenu(p);
        }

        /////////////////////////////////////
        // help
        /////////////////////////////////////
        if (args[0].equalsIgnoreCase("help")){
            plugin.man10DiceManager.help(p);
            return true;
        }

        /////////////////////////////////////
        // 100D
        /////////////////////////////////////
        if (args[0].equalsIgnoreCase("100d")){
            if (!p.hasPermission("man10.mdice.d100")) {
                p.sendMessage(prefix + "§c§lあなたには権限がありません");
                return true;
            }
            plugin.man10DiceManager.d100(p);
            return true;
        }

        /////////////////////////////////////
        // toggleShowMessage
        /////////////////////////////////////
        if (!p.hasPermission("man10.mdice.d100")) {
            p.sendMessage(prefix + "§c§lあなたには権限がありません");
            return true;
        }
        if (args[0].equalsIgnoreCase("toggleShowMessage") || args[0].equalsIgnoreCase("tmes")){
            plugin.man10DiceManager.toggleShowMessage(p);
            return true;
        }

        /////////////////////////////////////
        // checkShowMessage
        /////////////////////////////////////
        if (!p.hasPermission("man10.mdice.d100")) {
            p.sendMessage(prefix + "§c§lあなたには権限がありません");
            return true;
        }
        if (args[0].equalsIgnoreCase("checkShowMessage") || args[0].equalsIgnoreCase("cmes")){
            plugin.man10DiceManager.checkShowMessage(p);
            return true;
        }

        /////////////////////////////////////
        // normalDice
        /////////////////////////////////////
        try {
            if (!p.hasPermission("man10.mdice.roll")) {
                p.sendMessage(prefix + "§c§lあなたには権限がありません");
                return false;
            }
            if (!p.hasPermission("man10.mdice.nocool")) {
                if (System.currentTimeMillis() - plugin.man10DiceManager.lastDiceTime < plugin.man10DiceManager.coolTime) {
                    p.sendMessage(prefix + "現在クールダウン中です");
                    return false;
                }
                plugin.man10DiceManager.lastDiceTime = System.currentTimeMillis();
            }
            plugin.man10DiceManager.i = Integer.parseInt(args[0]);
            plugin.man10DiceManager.range = Integer.parseInt(args[1]);

            if (args.length == 3) {
                if (plugin.man10DiceManager.range < 1 || plugin.man10DiceManager.range > 20){
                    p.sendMessage(prefix + "§4§l1~20の半径を指定してください");
                    return false;
                }
                try {
                    for (Entity entity : p.getNearbyEntities(plugin.man10DiceManager.range, plugin.man10DiceManager.range, plugin.man10DiceManager.range)) {
                        Player pp = (Player) entity;
                        pp.sendMessage(prefix + "§2§l半径§e§l" + plugin.man10DiceManager.range + "§2§lマスの人に通知しています");
                        Random r = new Random();
                        plugin.man10DiceManager.result = r.nextInt(plugin.man10DiceManager.i) + 1;
                        Bukkit.getServer().broadcastMessage(prefix + "§3§l" + p.getDisplayName() + "§3§lは§l" + ChatColor.YELLOW + "§l" + plugin.man10DiceManager.i + "§3§l面サイコロを振って" + ChatColor.YELLOW + "§l" + plugin.man10DiceManager.result + "§3§lが出た");
                        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                        p.sendMessage("§3§l===============================================================");
                    }
                } catch (NumberFormatException NFE){
                    p.sendMessage(prefix + "§c§l有効な数字を入力してください");
                }
                return false;
            }

            if (plugin.man10DiceManager.i < 1) {
                p.sendMessage(prefix + "§c§l１以上の数を指定してください");
                return false;
            }

            Random r = new Random();
            plugin.man10DiceManager.result = r.nextInt(plugin.man10DiceManager.i) + 1;
            Bukkit.getServer().broadcastMessage(prefix + "§3§l" + p.getDisplayName() + "§3§lは§l" + ChatColor.YELLOW + "§l" + plugin.man10DiceManager.i + "§3§l面サイコロを振って" + ChatColor.YELLOW + "§l" + plugin.man10DiceManager.result + "§3§lが出た");
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
            return false;
        } catch (NumberFormatException NFE) {
            p.sendMessage(prefix + "§c§l/mdice help");
            return false;
        }
    }
}
