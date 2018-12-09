package com.cui.toutiao;

import org.apache.commons.lang.builder.ToStringBuilder;
import redis.clients.jedis.Jedis;

/**
 * 本测试类用于上手Jedis
 * author:CuiWJ
 * Date:2018/11/10
 * Created with IDEA
 */
public class JedisTest {
    private static void print(int index,Object obj){
        System.out.println(index+"、"+obj.toString());
    }
    public static void main(String[] args) {
        Jedis jedis = new Jedis();
        jedis.flushAll();

        //Strings
        jedis.set("jedisKey","This was setted by jedis");
        print(1,jedis.get("jedisKey"));

        //sets
        jedis.sadd("myset","user1","user2","user3","user4");
        print(2,jedis.smembers("myset"));
        print(3,jedis.scard("myset"));
        jedis.sadd("myset2","user1","user3");
        print(4,jedis.sdiff("myset","myset2"));
        print(5,jedis.sinter("myset","myset2"));
        print(6,jedis.sismember("myset","user1"));
        print(7,jedis.smove("myset","myset2","user2"));
        print(8,jedis.srandmember("myset"));
        print(9,jedis.smembers("myset"));
        print(10,jedis.spop("myset"));
        print(11,jedis.smembers("myset"));
        jedis.sadd("myset","user5");
        print(12,jedis.smembers("myset"));
        print(13,jedis.srem("myset","user5"));
        print(14,jedis.smembers("myset"));
        print(15,jedis.smembers("myset2"));
        print(16,jedis.sunion("myset","myset2"));

        //lists


        //hashes


        //sorted sets


    }

}
