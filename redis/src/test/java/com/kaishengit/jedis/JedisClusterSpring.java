package com.kaishengit.jedis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.JedisCluster;

import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-jedis-cluster.xml")
public class JedisClusterSpring {

    @Autowired
    private JedisCluster jedisCluster;

    @Test
    public void setName() throws IOException {
        jedisCluster.set("hai","你好啊");
        jedisCluster.close();
    }

    @Test
    public void getName() {
        String name = jedisCluster.get("user:1001");
        System.out.println(name);
    }
}
