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

package net.augcloud.boundsoul.core;

import net.augcloud.boundsoul.PluginData;
import net.augcloud.boundsoul.Main;
import net.augcloud.boundsoul.events.ToolOfEvents;

/**
 * @author ：Arisa
 * @date ：Created in 2020/3/1 18:25
 * @description：
 * @version: $
 */
public class BoundThread {
    
    static int censor_freq = 500;
    private Thread updataInvThread;
    private Thread checkThread;
    private boolean threadRunning = false;
    
    void init() {
        if (!this.threadRunning) {
            this.threadRunning = true;
            startThread();
            checkThread();
            return;
        }
        allStart();
    }
    
    private void allStart() {
        this.updataInvThread.start();
        this.checkThread.start();
    }
    
    public void allStop() {
        this.threadRunning = false;
        this.updataInvThread.stop();
        this.checkThread.stop();
    }
    
    public void startThread() {
        Runnable runnable = new Runnable() {
            boolean running = true;
            
            @Override
            public void run() {
                while (BoundThread.this.threadRunning && this.running) {
                    if (!PluginData.bs.isEmpty()) {
                        PluginData.bs.clear();
                    }
                    ToolOfEvents.updataPlayerinv();
                    ToolOfEvents.updataInventory(PluginData.bs);
                    try {
                        Thread.sleep(censor_freq);
                    } catch (InterruptedException e) {

                        this.running = false;
                        BoundThread.this.updataInvThread.stop();
                        BoundThread.this.startThread();
                        Main.plugin.getLogger()
                                .info("警告!插件异步线程运行中发生错误崩溃，检测绑定功能已经失效!!请截图完整错误报告内容并申报至QQ1131271403Arisa进行修复，正在重启线程");
                        e.printStackTrace();
                    }
                }
            }
        };
        this.updataInvThread = new Thread(runnable, "Updata-Thread");
        this.updataInvThread.start();
        Main.plugin.getLogger().info("线程运行中");
    }
    
    private void checkThread() {
        Runnable runnable = new Runnable() {
            boolean running = true;
            
            @Override
            public void run() {
                while (BoundThread.this.threadRunning && this.running) {
                    try {
                        if (!BoundThread.this.updataInvThread.isAlive()) {
                            BoundThread.this.updataInvThread.stop();
                            BoundThread.this.startThread();
                            Main.plugin.getLogger().info("发现检测线程不运行，自动激活了它");
                        }
                        Main.illegalPlayer.ruin();
                        Thread.sleep(20000L);
                    } catch (InterruptedException e) {
                        Main.plugin.getLogger().info("检测线程崩溃");
                        this.running = false;
                        BoundThread.this.checkThread.stop();
                        BoundThread.this.checkThread();
                        e.printStackTrace();
                    }
                }
            }
        };
        this.checkThread = new Thread(runnable, "Check-Thread");
        this.checkThread.start();
        Main.plugin.getLogger().info("检测线程运行，此线程会每10秒确认一次主线程存活，如果停止会自动重启");
    }
}