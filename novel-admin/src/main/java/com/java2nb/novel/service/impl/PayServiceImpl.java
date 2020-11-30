package com.java2nb.novel.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.java2nb.novel.dao.PayDao;
import com.java2nb.novel.domain.PayDO;
import com.java2nb.novel.service.PayService;



@Service
public class PayServiceImpl implements PayService {
	@Autowired
	private PayDao payDao;
	
	@Override
	public PayDO get(Long id){
		return payDao.get(id);
	}
	
	@Override
	public List<PayDO> list(Map<String, Object> map){
		return payDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return payDao.count(map);
	}
	
	@Override
	public int save(PayDO pay){
		return payDao.save(pay);
	}
	
	@Override
	public int update(PayDO pay){
		return payDao.update(pay);
	}
	
	@Override
	public int remove(Long id){
		return payDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return payDao.batchRemove(ids);
	}

	@Override
	public Map<Object, Object> tableSta(Date minDate) {
		List<Map<Object, Object>> maps = payDao.tableSta(minDate);

		return maps.stream().collect(Collectors.toMap(x -> x.get("staDate"), x -> x.get("orderCount")));


	}

}
