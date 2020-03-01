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
 * @author ��Arisa
 * @date ��Created in 2020/3/1 18:25
 * @description��
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
                                .info("����!����첽�߳������з���������������󶨹����Ѿ�ʧЧ!!���ͼ�������󱨸����ݲ��걨��QQ1131271403Arisa�����޸������������߳�");
                        e.printStackTrace();
                    }
                }
            }
        };
        this.updataInvThread = new Thread(runnable, "Updata-Thread");
        this.updataInvThread.start();
        Main.plugin.getLogger().info("�߳�������");
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
                            Main.plugin.getLogger().info("���ּ���̲߳����У��Զ���������");
                        }
                        Main.illegalPlayer.ruin();
                        Thread.sleep(20000L);
                    } catch (InterruptedException e) {
                        Main.plugin.getLogger().info("����̱߳���");
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
        Main.plugin.getLogger().info("����߳����У����̻߳�ÿ10��ȷ��һ�����̴߳����ֹͣ���Զ�����");
    }
}