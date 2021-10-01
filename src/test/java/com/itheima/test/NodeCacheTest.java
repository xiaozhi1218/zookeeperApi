package com.itheima.test;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
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
public class NodeCacheTest {

    @Test
    public void testNodeCacheTest() throws Exception {
        String connectString = "127.0.0.1:2181";
        int sessionTimeoutMs = 3000; //3 s
        int connectionTimeoutMs = 3000;
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1,3,1);
        CuratorFramework client = CuratorFrameworkFactory.newClient(connectString, sessionTimeoutMs, connectionTimeoutMs, retryPolicy);
        client.start();

        // 监听当前节点变化
        NodeCache nodeCache = new NodeCache(client,"/app");
        // 初始化时获取节点数据
        nodeCache.start(true);
        System.out.println(new String(nodeCache.getCurrentData().getData()));
        // 添加监听
        nodeCache.getListenable().addListener(()->{
            // 有变化时就触发回调
            System.out.println("修改后的值=======" + new String(nodeCache.getCurrentData().getData()));
        });
        //client.close();
        // 阻塞它
        System.in.read();
    }
}
