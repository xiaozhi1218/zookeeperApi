package com.itheima.test;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Test;

/**
 * <p>
 *
 * </p>
 *
 * @author: Eric
 * @since: 2020/11/18
 */
public class TreeCacheTest {

    @Test
    public void testTreeCache() throws Exception {
        String connectString = "127.0.0.1:2181";
        int sessionTimeoutMs = 3000; //3 s
        int connectionTimeoutMs = 3000;
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1,3,1);
        CuratorFramework client = CuratorFrameworkFactory.newClient(connectString, sessionTimeoutMs, connectionTimeoutMs, retryPolicy);
        client.start();

        TreeCache treeCache = new TreeCache(client,"/app");
        treeCache.start();

        // 获取app的值
        System.out.println(treeCache.getCurrentData("/app"));

        treeCache.getListenable().addListener((cli,event)->{
            if(event.getType() == TreeCacheEvent.Type.NODE_ADDED){
                System.out.println(event.getData().getPath() + "节点添加");
            }else if (event.getType() == TreeCacheEvent.Type.NODE_REMOVED){
                System.out.println(event.getData().getPath() + "节点移除");
            }else if(event.getType() == TreeCacheEvent.Type.NODE_UPDATED){
                System.out.println(event.getData().getPath() + "节点修改");
            }else if(event.getType() == TreeCacheEvent.Type.INITIALIZED){
                System.out.println("初始化完成");
            }else if(event.getType() ==TreeCacheEvent.Type.CONNECTION_SUSPENDED){
                System.out.println("连接过时");
            }else if(event.getType() ==TreeCacheEvent.Type.CONNECTION_RECONNECTED){
                System.out.println("重新连接");
            }else if(event.getType() ==TreeCacheEvent.Type.CONNECTION_LOST){
                System.out.println("连接过时一段时间");
            }
        });

        // 阻塞它
        System.in.read();
    }
}
