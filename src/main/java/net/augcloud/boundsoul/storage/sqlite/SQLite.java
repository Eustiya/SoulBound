/**
 * All rights Reserved, Designed By www.aug.cloud
 * SQLConnection.java
 *
 * @Package net.augcloud.arisa.akits.sqlite
 * @Description:
 * @author: Arisa
 * @date: 2018年7月23日 上午11:29:05
 * @version V1.0
 * @Copyright: 2018
 */
package net.augcloud.boundsoul.storage.sqlite;


import net.augcloud.boundsoul.BoundSoul;
import net.augcloud.boundsoul.Logger;
import net.augcloud.boundsoul.PluginData;
import org.bukkit.Bukkit;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Arisa
 * @date 2018年7月23日 上午11:29:05
 */
public class SQLite extends PluginData {
    
    //改
    
    private final String path;

    private Connection connection = null;
    private Statement stmt = null;
    private final String TABLE_DATE = "timelimit";
    private final String TABLE_ITEMS = "items";
    private String tableName;
//    private final String TABLE_NAME = "rank";
    
    
    public SQLite(String path) {
        
        this.path = path;
        try {
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + path);
        } catch (Exception e) {
            Logger.printlnInfo(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        try {
            if (!this.connection.isClosed()) {
                Logger.printlnInfo("Opened database successfully");
            }
            this.stmt = this.connection.createStatement();
        } catch (SQLException e) {
            Bukkit.getPluginManager().disablePlugin(BoundSoul.plugin);
            e.printStackTrace();
            Logger.printlnInfo("打开数据库失败!!");
            Logger.printlnInfo("插件即将卸载");
            Logger.printlnInfo("请将报错内容发给作者");
        }
        this.createTable();
    }

    /**
     * 创建SQLite表格
     *
     * @time 2020-03-12 17:58
     * @param tableName 表名
     * @return:
    **/
    public void createTables(String tableName){
    
        this.tableName = tableName;
    }
    
    
    /**
     * 创建SQLite表格
     *
     * @time 2020-03-12 18:03
     * @return:
    **/
    public void createTable() {
        this.openConnection();
        String a = "CREATE TABLE If not exists " + this.TABLE_DATE + " "
                + "(ID INTEGER PRIMARY KEY     NOT NULL," //占位
                + " player_uuid       TEXT    NOT NULL, "//玩家uuid
                + " item            TEXT     NOT NULL, "//物品副本，名字+Lore即可
                + " date            INTEGER     NOT NULL)";//到期日期
        
        if (this.execute(a, false)) {
            Logger.printlnInfo(this.TABLE_DATE +" 成功创建");
        }
    
        String b = "CREATE TABLE If not exists " + this.TABLE_ITEMS + " "
                + "(ID INTEGER PRIMARY KEY     NOT NULL," //占位
                + " player_uuid       TEXT    NOT NULL, "//玩家uuid
                + " item            TEXT     NOT NULL)";//物品副本，名字+Lore+nbt
    
        if (this.execute(b, false)) {
            Logger.printlnInfo(this.TABLE_ITEMS +" 成功创建");
        }
        
        
        this.closeConnection();
    }
    
    private void openConnection() {
        try {
        if (this.connection == null || this.connection.isClosed()) {
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + this.path);
        }
        if(this.stmt == null || this.stmt.isClosed()){
            this.stmt = this.connection.createStatement(); 
        }
        } catch (SQLException e) {
            Logger.printlnInfo("打开SQLite连接失败!!");
            Logger.printlnInfo("请将报错内容发给作者");
            e.printStackTrace();
        }
    }
    
    private boolean execute(String sql, boolean useupdata) {
        this.openConnection();
        boolean success = false;
        try {
            if (useupdata) {
                success = true;
                this.stmt.executeUpdate(sql);
            } else {
                success = this.stmt.execute(sql);
            }
            /*
             * 不同点
             不同1：
             execute可以执行查询语句
             然后通过getResultSet，把结果集取出来
             executeUpdate不能执行查询语句

             不同2:
             execute返回boolean类型，true表示执行的是查询语句，false表示执行的是insert,delete,update等等
             executeUpdate返回的是int，表示有多少条数据受到了影响
             */
        } catch (SQLException e) {
            Logger.printlnInfo("执行sql指令失败");
            Logger.printlnInfo("请将报错内容发给作者");
            e.printStackTrace();
        }
        this.closeConnection();
        return success;
    }
    
    
    public void closeConnection() {
        try {
        if(this.stmt!=null){
            this.stmt.close();
        }
        if(this.connection!=null){
            this.connection.close();
        }
        } catch (SQLException e) {
            
            Logger.printlnInfo("关闭连接失败!!");
            Logger.printlnInfo("请将报错内容发给作者");
            e.printStackTrace();
        }
    }
    
    //改 brokenerdata有改动 plunder_success_date有改动
    public boolean insert(String tableName, Object... args) {
        StringBuilder sb = new StringBuilder();
        switch (tableName) {
            case "blockhealthdata":
                sb.append("INSERT INTO ")
                        .append(this.TABLE_ITEMS)
                        .append(" (ID,player_uuid,item,date) ");
                break;
            default:
                return false;
        }
        sb.append("VALUES (null, ");
        String result;
        for (Object arg : args) {
            StringBuilder value = new StringBuilder();
            if (arg instanceof String) {
                value.append("'" + arg + "'");
            } else if (arg instanceof Integer) {
                value.append(arg);
            } else {
                value.append(arg);
            }
            value.append(", ");
            sb.append(value);
        }
        result = sb.substring(0, sb.length() - 2) + " )";
        return this.execute(result, false);
    }
    
//    public Map<String, Object> selectList(String tableName){
//
//    }
    
}
