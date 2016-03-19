package demo.redis.mybatis.mapper;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import demo.redis.client.jedis.RedisUtil;
import demo.redis.mybatis.mapper.DeptMapper;

public class DeptMapperTest {

	private SqlSessionFactory sqlSessionFactory;

	@Before
	public void setUp() throws Exception {
		String resource = "SqlMapConfig.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
	}

	@Test
	public void testGetDeptIdByEmpId() {
		Map paraMap = new HashMap();
		SqlSession sqlSession = sqlSessionFactory.openSession();
		DeptMapper deptMapper = sqlSession.getMapper(DeptMapper.class);
		paraMap.put("empId", 100);
		deptMapper.getDeptIdByEmpId(paraMap);
		Integer deptId = (Integer) paraMap.get("deptId");
		System.out.println("result is :  " + deptId);
	}

	@Test
	public void testGetDeptIdForEmp() {
		Map paraMap = new HashMap();
		SqlSession sqlSession = sqlSessionFactory.openSession();
		DeptMapper deptMapper = sqlSession.getMapper(DeptMapper.class);
		paraMap.put("empId", 113);
		deptMapper.getDeptIdForEmp(paraMap);
		Integer deptId = (Integer) paraMap.get("deptId");
		System.out.println("deptId is :  " + deptId);
	}

	@Test
	public void testMybatisAndRedis() {
		Integer deptId = 0;

		Integer[] empIds = { 100, 113, 129, 120, 121, 106 };

		for (int i = 0; i < empIds.length; i++) {
			Integer empId = empIds[i];
			String key = "deptId" + empId;
			String str = RedisUtil.getJedis().get(key);
			if (str != null) {
				deptId = Integer.valueOf(str);
				System.out.println("key: " + key + "   get from redis  " + deptId);
			} else {
				Map paraMap = new HashMap();
				SqlSession sqlSession = sqlSessionFactory.openSession();
				DeptMapper deptMapper = sqlSession.getMapper(DeptMapper.class);
				paraMap.put("empId", empId);
				deptMapper.getDeptIdForEmp(paraMap);
				deptId = (Integer) paraMap.get("deptId");
				// 向redis中存储数据
				RedisUtil.getJedis().set(key, deptId.toString());
				System.out.println("key: " + key + "   get from oracle database "
						+ deptId);
			}
		}
	}
}
