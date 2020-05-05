package com.java2nb.test.dao;

import com.java2nb.test.domain.OrderDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 付呗-订单信息表
 * @author xiongxy
 * @email 1179705413@qq.com
 * @date 2019-11-25 11:57:16
 */
@Mapper
public interface OrderDao {

	OrderDO get(Long id);
	
	List<OrderDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(OrderDO order);
	
	int update(OrderDO order);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}
