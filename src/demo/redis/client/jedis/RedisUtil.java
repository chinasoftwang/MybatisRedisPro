package demo.redis.client.jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public final class RedisUtil {

	// redis pool

	// redis服务器IP
	private static String ADDR = "127.0.0.1";

	// redis port
	private static int PORT = 6379;

	// 访问密码
	private static String AUTH = "";

	// 可用连接实例的最大数目，默认值为8；
	// 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)
	private static int MAX_ACTIVE = 1024;

	// 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8
	private static int MAX_IDLE = 200;

	private static int MAX_WAIT = 10000;

	private static int TIME_OUT = 10000;

	private static boolean TEST_ON_BORROW = true;

	private static JedisPool jedisPool = null;

	static {
		try {
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxTotal(MAX_ACTIVE);
			config.setMaxIdle(MAX_IDLE);
			config.setMaxWaitMillis(MAX_WAIT);
			config.setTestOnBorrow(TEST_ON_BORROW);
			jedisPool = new JedisPool(config, ADDR, PORT, 60000);
			System.out.println(" jedis pool " + jedisPool);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * 获取redis实例
	 */
	public synchronized static Jedis getJedis() {
		try {
			if (jedisPool != null) {
				Jedis jedis = jedisPool.getResource();
				return jedis;
			} else {
				System.out.println("Jedis instance is null");
				return null;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 释放jedis资源
	 */
	@SuppressWarnings("deprecation")
	public static void returnJedis(Jedis jedis){
		if (jedis != null) {
			jedisPool.returnResource(jedis);
		}
	}
}
