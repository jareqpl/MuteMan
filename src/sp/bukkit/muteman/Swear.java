/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sp.bukkit.muteman;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.util.config.Configuration;

/**
 *
 * @author sp
 */
public class Swear {
    private static Configuration config;
    private static HashMap<String, Integer> data;
        public static void setupSwear(File dataFolder){
        config = new Configuration(new File(dataFolder, "swear.yml"));                 
        data = new HashMap<String, Integer>();
        config.load();
        config.setProperty("settings.damage.from", 3);
        config.setProperty("settings.damage.damage", 2);
        config.setProperty("settings.mute.from", 5);
        config.setProperty("settings.mute.time", 60);
        config.save();
        
    }
    public static void check(PlayerChatEvent event){
        List swears = config.getStringList("swears", null);
        String player = event.getPlayer().getName();
        String string = event.getMessage();
        Boolean sweared = false;
        for (int i = 0; i < swears.size(); i++){
            if (string.toLowerCase().contains(swears.get(i).toString().toLowerCase())) {
                sweared = true;
                if (data.containsKey(player)) {
                    int already = data.get(player);
                    data.remove(player);
                    data.put(player, already + 1);
                } else {
                    data.put(player, 1);                      
                }
                break;
            }
            sweared = false;
        }
        if (!sweared) {
            data.remove(player);
        } else {
            event.getPlayer().sendMessage(Msg.$("dont-swear"));
        }
        if (data.containsKey(player)) {
            if (data.get(player) >= config.getInt("settings.damage.from", 3)) {
                Player p = MuteMan.server.getPlayer(player);
                event.setCancelled(true);
                p.damage(config.getInt("settings.damage.damage", 2));
            }
            if (data.get(player) >= config.getInt("settings.mute.from", 5)) {
                event.setCancelled(true);
                MuteManager.systemMute(player, Long.parseLong(Integer.toString(config.getInt("settings.mute.time", 60))));

            }
       }
    }
}
