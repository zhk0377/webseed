/*
 * Copyright (C), 2002-2016, 苏宁易购电子商务有限公司
 * FileName: ZKConnection.java
 * Author:   zhengk
 * Date:     2016年9月7日 上午9:45:39
 * Description: //模块目的、功能描述      
 * History: //修改记录
 * <author>      <time>      <version>    <desc>
 * 修改人姓名             修改时间            版本号                  描述
 */
package com.iteasy.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

/**
 * 功能描述:
 * 
 * @version 2.0.0
 * @author zhengk
 */
public class ZKConnection {
    /**
     * server列表, 以逗号分割
     */
    protected String         hosts           = "localhost:2181,localhost:2182,localhost:2183:";
    /**
     * 连接的超时时间, 毫秒
     */
    private static final int SESSION_TIMEOUT = 5000;
    private CountDownLatch   connectedSignal = new CountDownLatch(1);
    protected ZooKeeper      zk;

    /**
     * 连接zookeeper server
     */
    public void connect() throws Exception {
        zk = new ZooKeeper(hosts, SESSION_TIMEOUT, new ConnWatcher());
        // 等待连接完成
        connectedSignal.await();
    }

    public class ConnWatcher implements Watcher {
        public void process(WatchedEvent event) {
            // 连接建立, 回调process接口时, 其event.getState()为KeeperState.SyncConnected
            if (event.getState() == KeeperState.SyncConnected) {
                // 放开闸门, wait在connect方法上的线程将被唤醒
                connectedSignal.countDown();
            }
        }
    }
}
