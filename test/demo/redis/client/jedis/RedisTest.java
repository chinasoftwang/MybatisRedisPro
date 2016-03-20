package demo.redis.client.jedis;

import java.util.HashMap;
import java.util.Iterator;
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
	public void TestMap() {
		// 添加数据
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", "wanganlei");
		map.put("age", "18");
		map.put("qq", "123679054");
		jedis.hmset("user", map);

		// 取出数据，取出user中的name字段值，执行结果:[minxr]-->注意结果是一个泛型的List
		// 第一个参数是存入redis中map对象的key，后面跟的是放入map中的对象的key，后面的key可以跟多个，是可变参数
		List<String> resMap = jedis.hmget("user", "age", "name", "qq");
		System.out.println(resMap);

		// 删除map中的某个键值
		jedis.hdel("user", "age");
		System.out.println(jedis.hmget("user", "age"));
		// 查看返回键值的个数是不是2
		System.out.println(jedis.hlen("user"));

		System.out.println(jedis.exists("user"));
		// 返回map对象中的所有key
		System.out.println(jedis.hkeys("user"));
		// 返回map对象中的所有value
		System.out.println(jedis.hvals("user"));
		// 循环遍历输出user中的所有key和value
		Iterator<String> iter = jedis.hkeys("user").iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			System.out.println(key + ":" + jedis.hmget("user", key));
		}
	}

	@Test
	public void testList() {
		jedis.del("framework");
		// 取出list中所有的数据
		System.out.println(jedis.lrange("framework", 0, -1));
		jedis.lpush("framework", "adf");
		jedis.lpush("framework", "spring");
		jedis.lpush("framework", "mybatis");
		jedis.lpush("framework", "reactJs");
		System.out.println(jedis.lrange("framework", 0, -1));

		jedis.del("framework");
		jedis.rpush("framework", "adf");
		jedis.rpush("framework", "spring");
		jedis.rpush("framework", "mybatis");
		jedis.rpush("framework", "reactJs");
		System.out.println(jedis.lrange("framework", 0, -1));
	}

	@Test
	public void testSet() {
		jedis.sadd("emp", "test");
		jedis.sadd("emp", "chinasoftwang");
		jedis.sadd("emp", "bder");
		jedis.sadd("emp", "developer");
		jedis.sadd("emp", "who");
		System.out.println(jedis.smembers("emp"));

		jedis.srem("emp", "who");
		// 判断“who”是否在集合中
		System.out.println(jedis.sismember("emp", "who"));
		System.out.println(jedis.srandmember("emp"));
		// 返回集合中元素个数
		System.out.println(jedis.scard("emp"));
	}

	@Test
	public void test() {
		// jedis 排序

		jedis.del("a");
		jedis.rpush("a", "1");
		jedis.lpush("a", "3", "6", "9");
		// 取出所有元素
		System.out.println(jedis.lrange("a", 0, -1));
		// 排序
		System.out.println(jedis.sort("a"));
		
		System.out.println(jedis.lrange("a", 0, -1));
	}

	/**
	 * Test Redis Pool
	 */
	@Test
	public void TestRedisPool() {
		RedisUtil.getJedis().set("name", "中文测试");
		System.out.println(RedisUtil.getJedis().get("name"));
	}

}
