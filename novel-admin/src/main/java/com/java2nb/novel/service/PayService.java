package com.java2nb.novel.service;

import com.java2nb.novel.domain.PayDO;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 充值订单
 * 
 * @author xiongxy
 * @email 1179705413@qq.com
 * @date 2020-12-01 03:49:57
 */
public interface PayService {
	
	PayDO get(Long id);
	
	List<PayDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(PayDO pay);
	
	int update(PayDO pay);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

    Map<Object, Object> tableSta(Date minDate);
}
