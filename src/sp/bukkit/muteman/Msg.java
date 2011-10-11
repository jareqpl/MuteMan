/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sp.bukkit.muteman;

import java.io.File;
import java.util.HashMap;
import org.bukkit.util.config.Configuration;

/**
 *
 * @author sp
 */
public class Msg {
    private static Configuration lang;
    private static HashMap<String, String> map;
    public static void setupMsg(File dataFolder){
        lang = new Configuration(new File(dataFolder, "lang.yml"));
        map = new HashMap<String, String>();
        
        map.put("not-permitted", "&cYou are not permitted to do this!");
        map.put("mute-notify", "&cYou are muted!");
        map.put("time-left", "&aTime left: &f%time%&a.");
        map.put("sec", " second(s)");
        map.put("ever", "forever");
        map.put("not-valid-num", "&eEnter a valid time in seconds (or string \"forever\")!");
        map.put("empty-name", "&eEnter name of the player!");
        map.put("muted-already", "&eThis player is muted already! Time left:&f %time%&e.");
        map.put("muted-success", "&cPlayer &f%player%&c muted!");
        map.put("unmutted-success", "&cPlayer &f%player%&c unmuted!");
        map.put("damaged", "&e*Bam!*");
        map.put("getmute-already", "&ePlayer %player% is muted! Time left: &f%time%&e.");
        map.put("getmute-not", "&aPlayer %player% is'nt mutted.");
        map.put("you-are-muted", "&cYou are muted for &f%time%.");
        
        map.put("reloaded", "&aConfiguration reloaded!");
        map.put("dont-swear", "&cDont swear!");
        
        map.put("mute-usage", "&aUsage: &f/mute (player) (time)&a, where player - player name and time - time in seconds (or string \"forever\").");
        map.put("unmute-usage", "&aUsage: &f/unmute (player)&a, where player - player name.");
        map.put("getmute-usage", "&aUsage: &f/getmutemute (player)&a, where player - player name.");
        
        load();
    }
    public static String $(String id){
        if (map.containsKey(id)){
            return map.get(id).replace("&", "\u00a7");
        } else {
            return "&cString id ".concat(id).concat(" not found!").replace("&", "\u00a7");
        }
    }
    public static String _(String str){
        return str.replace("&", "\u00a7");
    }
    private static void load() {
        lang.load();
        for (String key : map.keySet()) {
            if (lang.getString(key) == null) {
                lang.setProperty(key, map.get(key));
            } else {
                map.put(key, lang.getString(key));
            }
        }
        lang.save();
    }
}
