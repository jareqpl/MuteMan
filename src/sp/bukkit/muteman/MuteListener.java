/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sp.bukkit.muteman;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;

/**
 *
 * @author sp
 */
public class MuteListener extends PlayerListener{
    
    @Override
    public void onPlayerChat(PlayerChatEvent event){
        Player player = event.getPlayer();
        if (MuteManager.isMuted(player)){
            if (MuteMan.config.getBoolean("notify.enabled", true)){
                player.sendMessage(Msg.$("mute-notify"));
                if (MuteMan.config.getBoolean("notify.time", true)){
                    Long time = MuteManager.getTime(player.getName());
                    if (time == -1){
                        player.sendMessage(Msg.$("time-left").replace("%time%", Msg.$("ever")));
                    } else {
                        player.sendMessage(Msg.$("time-left").replace("%time%", MuteManager.getTime(player.getName()).toString().concat(Msg.$("sec"))));
                    }
                }
                int damage = MuteMan.config.getInt("damage", 0);
                if (damage != 0){
                    player.sendMessage(Msg.$("damaged"));
                    player.damage(damage);
                }
            }
            event.setCancelled(true);
        } else {
            
        }
    }
}
