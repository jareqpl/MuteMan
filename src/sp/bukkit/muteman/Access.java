/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sp.bukkit.muteman;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
import java.util.List;
import net.minecraft.server.World;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

/**
 *
 * @author sp
 */
public class Access {
    private static PermissionHandler permissionHandler;
    private static PermissionManager pex;
    public static void setupPermissions() {
        if (permissionHandler != null) {
            return;
        }
        Plugin permissionsPlugin = MuteMan.server.getPluginManager().getPlugin("Permissions");
        if (permissionsPlugin == null) {
            Log.info("Permission system not detected, all players can use plugin!");
            return;
        }
        permissionHandler = ((Permissions) permissionsPlugin).getHandler();
        Log.info("Permission system: " + ((Permissions)permissionsPlugin).getDescription().getFullName());
        try {
            pex = PermissionsEx.getPermissionManager();
        } catch(Exception e){
            Log.info("PermissionsEx-support enable error!");
        }
        if (pex == null){
            Log.info("PermissionsEx not found, mute-set-group and mute-add-nodes are not available!");
        }
    }
    public static boolean canMute(Player player){
        if (player == null) return true;
        else {
            if ((permissionHandler.has(player, "commandbook.mute"))||(permissionHandler.has(player, "muteman.mute")))
                return true;
            else
                return false;
        }
    }
    public static boolean canGet(Player player){
        if (player == null) return true;
        else {
            if (permissionHandler.has(player, "commandbook.mute") || permissionHandler.has(player, "muteman.get"))
                return true;
            else
                return false;
        }
    }
    public static boolean canReload(CommandSender sender){
        if (sender instanceof Player){
            Player p = (Player) sender;
            if (!(permissionHandler.has(p, "commandbook.mute"))||(permissionHandler.has(p, "muteman.reload"))){
                return false;
            }
        }
        return true;
    }
    public static boolean canSwear(CommandSender sender){
        if (sender instanceof Player){
            Player p = (Player) sender;
            if (permissionHandler.has(p, "muteman.swear")){
                return true;
            }
        }
        
        return false;
    } 
    public static void removeNode(String player, String node){
        if (player == null) return;
        else {
            if (pex != null)
            pex.getUser(player).removePermission(node);
        }
    }
    public static void removeGroup(String player, String group){
            if (pex != null)
            pex.getUser(player).removeGroup(pex.getGroup(group));           
    }
    public static void addGroup(String player, String group){
            if (pex != null)
            pex.getUser(player).addGroup(pex.getGroup(group));      
    }
    public static void addNode(String player, String node) {
        if (player == null) {
            return;
        } else {
            if (pex != null)
            pex.getUser(player).addPermission(node);
        }
    }
}
