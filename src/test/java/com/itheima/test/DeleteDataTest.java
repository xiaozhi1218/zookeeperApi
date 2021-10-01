package com.itheima.test;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
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
public class DeleteDataTest {

    @Test
    public void testDeleteData() throws Exception {
        String connectString = "127.0.0.1:2181";
        int sessionTimeoutMs = 3000; //3 s
        int connectionTimeoutMs = 3000;
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1,3,1);
        CuratorFramework client = CuratorFrameworkFactory.newClient(connectString, sessionTimeoutMs, connectionTimeoutMs, retryPolicy);
        client.start();
        // 删除节点, 单一节点
        //client.delete().forPath("/app2");
        // 删除多级节点
        //client.delete().deletingChildrenIfNeeded().forPath("/app2");
        //guaranteed 保证能完成
        client.delete().guaranteed().deletingChildrenIfNeeded().forPath("/app");
        client.close();
    }
}
