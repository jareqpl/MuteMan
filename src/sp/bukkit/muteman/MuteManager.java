/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sp.bukkit.muteman;

import java.io.File;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.config.Configuration;

/**
 *
 * @author sp
 */
public class MuteManager {
    private static Configuration config;
    public static void setupMuteManager(File dataFolder){
        config = new Configuration(new File(dataFolder, "data.yml"));
        config.load();
        
        
    }
    public static boolean isMuted(Player player){
        if (player == null) return false;
        else {
            Double mute = config.getDouble("mute.".concat(player.getName()).concat(".time"), 0);
            
            if (mute == -1){
                return true;
            } else if (mute == 0){
                deleteMute(player.getName());
                return false;
            } else {
                mute *= 1000;
                Long start;
                try {
                    start = (Long) config.getProperty("mute.".concat(player.getName()).concat(".start"));
                } catch (Exception e){
                    return true;
                }
                if (start == null) {
                    return true;
                }
                Long mutems = Long.parseLong(Integer.toString(mute.intValue()));
                if (mutems == null)
                    return false;
                
                mutems += start;
                if (System.currentTimeMillis() < mutems){
                    return true;
                } else {
                    deleteMute(player.getName());
                    return false;
                }
                
            }
        }
    }
    public static Long getTime(String player){       
        if (player == null) return new Long(0);
        else {
            Double mute = config.getDouble("mute.".concat(player).concat(".time"), 0);
            if (mute == -1) {
                return new Long(-1);
            }
            mute *= 1000;
            Long start;
            start = (Long) config.getProperty("mute.".concat(player).concat(".start"));
            
            if (start == null) {
                return new Long(-1);
            }
            Long mutems = Long.parseLong(Integer.toString(mute.intValue()));
            mutems += start;
            return (new Long(mutems - System.currentTimeMillis()) / 1000);
        }
    }
    public static void Mute(CommandSender sender, String mutted, Long time){
       if (config.getProperty("mute.".concat(mutted)) != ""){
           config.setProperty("mute.".concat(mutted).concat(".time"), time);
           config.setProperty("mute.".concat(mutted).concat(".start"), System.currentTimeMillis());
           config.save();
           sender.sendMessage(Msg.$("muted-success").replace("%player%", mutted));
       } else {
           sender.sendMessage(Msg.$("muted-already").replace("%time%", getTime(mutted).toString()));
       }
    }
    
    public static void unMute(CommandSender sender, String mutted){
        if (sender instanceof Player && Access.canMute((Player) sender)){
            deleteMute(mutted);
            sender.sendMessage(Msg.$("unmutted-success").replace("%player%", mutted));
        } else {
            sender.sendMessage(Msg.$("not-permitted"));
        }
    }
    public static void deleteMute(String muted){
        List perms = MuteMan.config.getStringList("disable-permissions.list", null);
        for (int i = 0; i < perms.size(); i++) {
            Access.removeNode(muted, (String) perms.get(i));
        }
        config.removeProperty("mute.".concat(muted));
        config.save();
    }

    public static void getMute(CommandSender sender, String player) {
        if (sender instanceof Player && Access.canGet((Player) sender)) {
            if (config.getProperty("mute.".concat(player)) != "") {
                sender.sendMessage(Msg.$("getmute-already").replace("%time%", getTime(player).toString()).replace("-1", Msg.$("ever")).replace("%player%", player));

            } else {
                sender.sendMessage(Msg.$("getmute-not").replace("%player%", player));
            }

        } else {
            sender.sendMessage(Msg.$("not-permitted"));
        }
    }
}
