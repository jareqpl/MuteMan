/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sp.bukkit.muteman;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author sp
 */
public class Access {
    private static PermissionHandler permissionHandler;
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
        Log.info("Found and will use plugin " + ((Permissions)permissionsPlugin).getDescription().getFullName());
    }
    public static boolean canMute(Player player){
        if (player == null) return true;
        else {
            if (permissionHandler.has(player, "commandbook.mute"))
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
    public static void removeNode(String player, String node){
        if (player == null) return;
        else {
            permissionHandler.removeUserPermission(player, node, node);
        }
    }
    public static void addNode(Player player, String node){
        if (player == null) return;
        else {
            permissionHandler.
            permissionHandler.addUserPermission(player.getName(), node, node);
        }
    }
}
