package demo.redis.client.jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.Jedis;

public class RedisTest {

	private Jedis jedis;

	@Before
	public void setUp() {
		// connect redis server 127.0.0.1:6379
		jedis = new Jedis("127.0.0.1", 6379);
		// 权限验证
		// jedis.auth("admin");
	}

	/**
	 * String Test
	 */
	@Test
	public void TestString() {
		jedis.set("name", "wanganlei");
		System.out.println(jedis.get("name"));

		jedis.append("name", " is a man!");
		System.out.println(jedis.get("name"));

		jedis.del("name");
		System.out.println(jedis.get("name"));

		jedis.mset("name", "wanganlei", "age", "26", "qq", "666666");
		jedis.incr("age"); // 年龄+1
		System.out.println(jedis.get("name") + " - " + jedis.get("age") + " - "
				+ jedis.get("qq"));
	}
	
	/**
	 * redis操作map
	 */
	@Test
	public void TestMap(){
		// 添加数据
		Map<String,String> map = new HashMap<String,String>();
		map.put("name", "wanganlei");
		map.put("age", "18");
		map.put("qq", "123679054");
		jedis.hmset("user",map);
		
		// 取出数据，取出user中的name字段值，执行结果:[minxr]-->注意结果是一个泛型的List
		// 第一个参数是存入redis中map对象的key，后面跟的是放入map中的对象的key，后面的key可以跟多个，是可变参数
		List<String> resMap = jedis.hmget("user","age","name","qq");
		System.out.println(resMap);
	}
	
	/**
	 * Test Redis Pool
	 */
	@Test
	public void TestRedisPool(){
		RedisUtil.getJedis().set("name", "中文测试");
		System.out.println(RedisUtil.getJedis().get("name"));
	}

}
