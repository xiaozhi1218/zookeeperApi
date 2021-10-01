package com.itheima.test;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
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
public class PathChildrenCacheTest {

    @Test
    public void testPathChildrenCache() throws Exception {
        String connectString = "127.0.0.1:2181";
        int sessionTimeoutMs = 3000; //3 s
        int connectionTimeoutMs = 3000;
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1,3,1);
        CuratorFramework client = CuratorFrameworkFactory.newClient(connectString, sessionTimeoutMs, connectionTimeoutMs, retryPolicy);
        client.start();

        PathChildrenCache pathChildrenCache = new PathChildrenCache(client,"/app/a",true);
        pathChildrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        // 先打印节点的数据
        System.out.println(pathChildrenCache.getCurrentData());
        // 添加监听
        pathChildrenCache.getListenable().addListener((cli,event)->{
            if(event.getType() == PathChildrenCacheEvent.Type.CHILD_UPDATED){
                System.out.println("子节点更新");
                System.out.println("节点:"+event.getData().getPath());
                System.out.println("数据" + new String(event.getData().getData()));
            }else if(event.getType() == PathChildrenCacheEvent.Type.INITIALIZED ){
                System.out.println("初始化操作");
            }else if(event.getType() == PathChildrenCacheEvent.Type.CHILD_REMOVED ){
                System.out.println("删除子节点");
                System.out.println("节点:"+event.getData().getPath());
                System.out.println("数据" + new String(event.getData().getData()));
            }else if(event.getType() == PathChildrenCacheEvent.Type.CHILD_ADDED ){
                System.out.println("添加子节点");
                System.out.println("节点:"+event.getData().getPath());
                System.out.println("数据" + new String(event.getData().getData()));
            }else if(event.getType() == PathChildrenCacheEvent.Type.CONNECTION_SUSPENDED ){
                System.out.println("连接失效");
            }else if(event.getType() == PathChildrenCacheEvent.Type.CONNECTION_RECONNECTED ){
                System.out.println("重新连接");
            }else if(event.getType() == PathChildrenCacheEvent.Type.CONNECTION_LOST ){
                System.out.println("连接失效后稍等一会儿执行");
            }
        });
        // 阻塞它
        System.in.read();
    }
}
