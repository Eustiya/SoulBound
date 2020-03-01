/*
 * ?2021 August-soft Corporation. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.augcloud.boundsoul.utils;

import net.augcloud.boundsoul.Main;
import net.augcloud.boundsoul.YamlConfig;
import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author £ºArisa
 * @date £ºCreated in 2020/3/1 18:11
 * @description£º
 * @version: $
 */
public class UtilsOfEffect {
    
    public static void effect(Player player){
        crateEffect(player.getLocation(), player.getWorld());
        (new BukkitRunnable() {
            @Override
            public void run() {
                fwtest(player);
            }
        }).runTask(Main.plugin);
        sendSound(player);
        
    }
    
    private static void sendSound(Player player){
        if (!YamlConfig.getConfig().getBoolean("useSound")) {
            if(Sound.valueOf("LEVEL_UP")!=null){
                player.playSound(player.getLocation(),
                        Sound.valueOf("LEVEL_UP"), 1.0F, 1.0F);
            }else if(Sound.valueOf("ENTITY_PLAYER_LEVELUP")!=null ){
                player.playSound(player.getLocation(),
                        Sound.valueOf("ENTITY_PLAYER_LEVELUP"), 1.0F, 1.0F);
            }
        
        }
    }
    
    private static void crateEffect(final Location location, final World world) {
        if (!YamlConfig.getConfig().getBoolean("useEffect")) {
            return;
        }
        location.setX(location.getX() + 0.5D);
        location.setY(location.getY() + 0.5D);
        location.setZ(location.getZ() + 0.5D);
        if (50 > 1) {
            for (int i = 0; 50 > i; i++) {
                (new BukkitRunnable() {
                    int a = 0;
                    
                    @Override
                    public void run() {
                        if (this.a > 1) {
                            cancel();
                            return;
                        }
                        this.a++;
                        world.playEffect(location, Effect.FIREWORKS_SPARK, 1);
                    }
                }).runTaskTimer(Main.plugin, 0L, 10L);
            }
        } else {
            (new BukkitRunnable() {
                int a = 0;
                
                @Override
                public void run() {
                    if (this.a > 1) {
                        cancel();
                        return;
                    }
                    this.a++;
                    world.playEffect(location, Effect.FIREWORKS_SPARK, 1);
                }
            }).runTaskTimer(Main.plugin, 0L, 10L);
        }
    }
    
    private static void fwtest(Player player) {
        if (!YamlConfig.getConfig().getBoolean("useFireworks")) {
            return;
        }
        Firework fw = player.getWorld().spawn(player.getLocation(), Firework.class);
        FireworkMeta fm = fw.getFireworkMeta();
        fm.addEffect(FireworkEffect.builder().flicker(false).with(FireworkEffect.Type.BURST)
                .with(FireworkEffect.Type.BALL).with(FireworkEffect.Type.STAR).withColor(Color.YELLOW)
                .withColor(Color.ORANGE).withColor(Color.PURPLE).withColor(Color.GREEN).build());
        fm.setPower(1);
        fw.setFireworkMeta(fm);
    }
}
