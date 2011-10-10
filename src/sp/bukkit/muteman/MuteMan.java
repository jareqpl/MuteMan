/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sp.bukkit.muteman;

import com.nijiko.permissions.PermissionHandler;
import java.io.File;

import java.util.List;
import java.util.Vector;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

/**
 *
 * @author sp
 */
public class MuteMan extends JavaPlugin{
    public static Server server;
    public static PluginManager pluginman;
    private static PermissionHandler permissionHandler;
    private MuteListener muteListener;
    public static Configuration config;
    
    @Override
    public void onEnable() {
        server = getServer();
        pluginman = server.getPluginManager();
        config = new Configuration(new File(getDataFolder(), "config.yml"));
        
        config.load();
        
        config.getInt("damage", 0);
        config.getBoolean("notify.enable", true);
        config.getBoolean("notify.time", true);
        config.getBoolean("disable-permissions.enabled", false);
        config.getString("disable-permissions.list", null);
        
        config.save();
        
        Msg.setupMsg(getDataFolder());
        Access.setupPermissions();
        
        MuteManager.setupMuteManager(getDataFolder());
        
        muteListener = new MuteListener();
        
        pluginman.registerEvent(Type.PLAYER_CHAT, muteListener, Priority.Highest, this);
        config.save();
    }
    @Override
    public void onDisable() {
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){ 
        if (sender instanceof Player){
            List perms = config.getStringList("disable-permissions.list", null);
            Player p = (Player) sender;
            Log.info("add " + perms.size() + " perms: ");
            for (int i = 0; i < perms.size(); i++){
                Log.info("add " + perms.get(i));
                Access.addNode(p, (String) perms.get(i));
            }
        }
        if (cmd.getName().equalsIgnoreCase("addmute")){
            if (args.length <= 0) {
                sender.sendMessage(Msg.$("mute-usage"));
                return true;
            }
            if (args.length <= 1) {
                sender.sendMessage(Msg.$("not-valid-num"));
                return true;
            }
            int mute;
            try {
                mute = Integer.parseInt(args[1]);
            } catch (Exception e){
                if (!args[1].equals("forever")){
                    sender.sendMessage(Msg.$("not-valid-num"));
                    return true;
                } else {
                    mute = -1;
                }
            }
            Long mutelong = Long.parseLong(Integer.toString(mute));
            MuteManager.Mute(sender, args[0], mutelong);
        }
        if (cmd.getName().equalsIgnoreCase("delmute")){
            if (args.length <= 0) {
                sender.sendMessage(Msg.$("delmute"));
                return true;
            }
            MuteManager.unMute(sender, args[0]);
        }
        if (cmd.getName().equalsIgnoreCase("getmute")){
           if (args.length <= 0) {
                sender.sendMessage(Msg.$("getmute-usage"));
                return true;
            }
           MuteManager.getMute(sender, args[0]);
        }
        
        return true;
    }

}
