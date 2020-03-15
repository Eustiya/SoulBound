/*
 * ©2021 August-soft Corporation. All rights reserved.
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

package net.augcloud.boundsoul;

import java.util.logging.Level;

/**
 * @author ：Arisa
 * @date ：Created in 2020/3/12 17:20
 * @description：
 * @version: $
 */
public class Logger {
    
    public static void printlnInfo(String msg){
        BoundSoul.plugin.getServer().getLogger().info(msg);
    }
    
    public static void printlnError(String msg){
        BoundSoul.plugin.getServer().getLogger().log(Level.WARNING,msg);
    }
    
}
