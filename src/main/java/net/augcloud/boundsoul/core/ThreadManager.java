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

import net.augcloud.boundsoul.YamlConfig;

/**
 * @author £ºArisa
 * @date £ºCreated in 2020/3/1 18:27
 * @description£º
 * @version: $
 */
public class ThreadManager {
    
    private BoundThread boundThread = null;
    
    public ThreadManager(){
        BoundThread.censor_freq = YamlConfig.getConfig().getInt("Censor-freq");
        
    }
    
    public void instantiation() {
        boundThread = new BoundThread();
        boundThread.init();
    }
    
     public BoundThread getBoundThread(){
        return this.boundThread;
     }
     
    
}
