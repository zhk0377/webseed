/*
 * Copyright (C), 2002-2016, 苏宁易购电子商务有限公司
 * FileName: ZKDemo.java
 * Author:   zhengk
 * Date:     2016年9月7日 上午9:30:22
 * Description: //模块目的、功能描述      
 * History: //修改记录
 * <author>      <time>      <version>    <desc>
 * 修改人姓名             修改时间            版本号                  描述
 */
package com.iteasy.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.ZooDefs.Ids;

import java.io.IOException;

/** 
 * 功能描述: 
 * 
 * @version 2.0.0
 * @author zhengk
 */
public class ZKDemo {

    /**
     * 功能描述: 
     * 
     * @param args void
     * @version 2.0.0
     * @author zhengk
     * @throws IOException 
     * @throws InterruptedException 
     * @throws KeeperException 
     */
    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        //创建一个与服务器的链接
        ZooKeeper zk=new ZooKeeper("127.0.0.1:2181", 6000, new Watcher() {
            //监控所有被触发的事件
            @Override
            public void process(WatchedEvent event) {
                System.out.println("Event:"+event.getType());
            }
        });
        // 查看根节点
        System.out.println("ls / =>"+zk.getChildren("/", true));
        // 创建一个目录节点
        if (zk.exists("/node", true) == null) {
            zk.create("/node", "conan".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println("create /node conan");
            // 查看/node节点数据
            System.out.println("get /node => " + new String(zk.getData("/node", false, null)));
            // 查看根节点
            System.out.println("ls / => " + zk.getChildren("/", true));
        }

        // 创建一个子目录节点
        if (zk.exists("/node/sub1", true) == null) {
            zk.create("/node/sub1", "sub1".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println("create /node/sub1 sub1");
            // 查看node节点
            System.out.println("ls /node => " + zk.getChildren("/node", true));
        }

        // 修改节点数据
        if (zk.exists("/node", true) != null) {
            zk.setData("/node", "changed".getBytes(), -1);
            // 查看/node节点数据
            System.out.println("get /node => " + new String(zk.getData("/node", false, null)));
        }

        // 删除节点
        if (zk.exists("/node/sub1", true) != null) {
            zk.delete("/node/sub1", -1);
            zk.delete("/node", -1);
            // 查看根节点
            System.out.println("ls / => " + zk.getChildren("/", true));
        }

        // 关闭连接
        zk.close();
    }

}
