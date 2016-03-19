package demo.redis.mybatis.mapper;

import java.util.Map;

public interface DeptMapper {

	// 执行function获取deptID
	public Integer getDeptIdByEmpId(Map map);

	// 执行procedure获取deptId
	public Integer getDeptIdForEmp(Map map);
}
