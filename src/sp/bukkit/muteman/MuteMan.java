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
        config.getBoolean("notify.enabled", true);
        config.getBoolean("notify.time", true);
        config.getString("add-permissions.list", null);
        config.getString("set-group", null);
        config.getBoolean("disable-commands",false);
        
        config.save();
        
        Msg.setupMsg(getDataFolder());
        Swear.setupSwear(getDataFolder());
         
        Access.setupPermissions();
        
        MuteManager.setupMuteManager(getDataFolder());
        
        muteListener = new MuteListener();
        
        pluginman.registerEvent(Type.PLAYER_CHAT, muteListener, Priority.Highest, this);
        pluginman.registerEvent(Type.PLAYER_COMMAND_PREPROCESS, muteListener, Priority.Highest, this);
        config.save();
        Log.info("MuteMan by ShadowPrince enabled successfully!");
    }
    @Override
    public void onDisable() {
        Log.info("MuteMan by ShadowPrince disabled successfully!");
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){ 
        
        if (cmd.getName().equalsIgnoreCase("getmute")){
           if (args.length <= 0) {
                sender.sendMessage(Msg.$("getmute-usage"));
                return true;
            }
           MuteManager.getMute(sender, args[0]);
        } else if (cmd.getName().equalsIgnoreCase("mymute")){
            if (sender instanceof Player){
                Player p = (Player) sender;
                MuteManager.getMute(sender, p.getName());
            }
            else
                sender.sendMessage("Only player can access this command!");
        } else if (cmd.getName().equalsIgnoreCase("reloadmute")){
            if (Access.canReload(sender)) {
                config.load();
                Msg.setupMsg(getDataFolder());
                MuteManager.setupMuteManager(getDataFolder());
                Swear.setupSwear(getDataFolder());
                sender.sendMessage(Msg.$("reloaded"));
            } else {
                sender.sendMessage(Msg.$("not-permitted"));
            }
        }
        
        return true;
    }
    public static void commandUnMute(CommandSender sender, String player){
        if (player == null) {
            sender.sendMessage(Msg.$("unmute-usage"));
            return;
        }
        MuteManager.unMute(sender, player);
    }
    public static void commandMute(CommandSender sender, String player, String duration){
            if (player == null || duration == null) {
                sender.sendMessage(Msg.$("mute-usage"));
                return;
            }

            int mute;
            try {
                mute = Integer.parseInt(duration);
            } catch (Exception e){
                if (!duration.equals("forever")){
                    sender.sendMessage(Msg.$("not-valid-num"));
                    return;
                } else {
                    mute = -1;
                }
            }
            Long mutelong = Long.parseLong(Integer.toString(mute));
            MuteManager.Mute(sender, player, mutelong);
            List perms = config.getStringList("add-permissions.list", null);
            for (int i = 0; i < perms.size(); i++) {
                Access.addNode(player, (String) perms.get(i));
            }
            if (config.getString("set-group", "") != ""){
                String group = config.getString("set-group", "");
                Log.info(group);
                Access.addGroup(player, group);
            }
    }
}
