package com.itheima.test;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.junit.Test;

/**
 * <p>
 *
 * </p>
 *
 * @author: Eric
 * @since: 2020/11/18
 */
public class CreateDataTest {

    @Test
    public void testCreateData() throws Exception {
        // 创建连接客户端
        //connectString 连接zookeeper，ip:port
        String connectString = "127.0.0.1:2181";
        //sessionTimeoutMs 会话超时时间 连接上了服务器，但 一直不操作
        int sessionTimeoutMs = 3000; //3 s
        //connectionTimeoutMs 客户端连接服务器，一直连不上，超过时间后就放弃连接
        int connectionTimeoutMs = 3000;
        //retryPolicy 重试策略
        //baseSleepTimeMs  失败时，睡眠多长时间后再重试
        //maxRetries 最多重试的次数
        //maxSleepMs 最长睡眠时间
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1,3,1);
        CuratorFramework client = CuratorFrameworkFactory.newClient(connectString, sessionTimeoutMs, connectionTimeoutMs, retryPolicy);
        // 启动客户端
        client.start();
        // 使用客户端操作
        //1. 创建一个空节点(a)（只能创建一层节点）, 值为ip地址
        //client.create().forPath("/app");
        //2. 创建一个有内容的b节点（只能创建一层节点）
        // -127,127  iso-8859-1 ,乱码：大的存到小。"中文".getBytes() 原始数据没有丢，当再次转utf-8时，能显示出中文
        //client.create().forPath("/app1","中文".getBytes());
        //3. 创建持久节点，同时创建多层节点
        //  withMode创建的模式，CreateMode.PERSISTENT 持久化
        //client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/app2/a","Hello".getBytes());
        //4. 创建带有的序号的持久节点 PERSISTENT_SEQUENTIAL有序的持久节点
        //client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath("/app3","Hello".getBytes());
        //5. 创建临时节点（客户端关闭，节点消失），设置延时5秒关闭（Thread.sleep(5000)）
        //client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath("/tmp1","Hello".getBytes());
        //6. 创建临时带序号节点（客户端关闭，节点消失），设置延时5秒关闭（Thread.sleep(5000)）
        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath("/tmp2","Hello".getBytes());
        //client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath("/jdbc.url","mysql:jdbc://".getBytes());
        Thread.sleep(5000);
        // 关闭客户端
        client.close();
    }
}
