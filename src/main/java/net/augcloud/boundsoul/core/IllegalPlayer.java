package net.augcloud.boundsoul.core;

import org.bukkit.entity.Player;

import java.util.HashMap;


/**
 *  IllegalPlayer class
 *
 * @author Arisa
 * @date 2016/10/31
 * 不知道是干什么的，不管他了
 */
public class IllegalPlayer {
    private HashMap<String, Integer> playerUuid = new HashMap();
    
    public boolean contains(Player player) {
        String uuid = player.getUniqueId().toString();
        if (this.playerUuid.containsKey(uuid)) {
            Integer times = this.playerUuid.get(uuid);
            if (times.equals(10)) {
              return false;
            }
            this.playerUuid.put(uuid, times + 1);
        }
        return true;
    }
    
    public void add(Player player) {
        this.playerUuid.put(player.getUniqueId().toString(), 0);
    }
    
    public void remove(Player player) {
        this.playerUuid.remove(player.getUniqueId().toString());
    }
    
    public void ruin() {
        this.playerUuid.clear();
    }
    
    @Override
    public int hashCode() {
        int prime = 31;
      int result = 1;
        return 31 * result + ((this.playerUuid == null) ? 0 : this.playerUuid.hashCode());
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
          return true;
        }
        if (obj == null) {
          return false;
        }
        if (!(obj instanceof IllegalPlayer)) {
          return false;
        }
        IllegalPlayer other = (IllegalPlayer) obj;
        if (this.playerUuid == null) {
          return other.playerUuid == null;
        } else {
          return this.playerUuid.equals(other.playerUuid);
        }
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("IllegalPlayer [");
        if (this.playerUuid != null) {
            builder.append("Player_uuid=");
            builder.append(this.playerUuid);
        }
        builder.append("]");
        return builder.toString();
    }
    
    public void setPlayer_uuid(HashMap<String, Integer> player_uuid) {
        this.playerUuid = player_uuid;
    }
}
