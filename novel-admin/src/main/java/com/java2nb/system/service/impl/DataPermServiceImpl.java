package com.java2nb.system.service.impl;

import com.java2nb.common.domain.DictDO;
import com.java2nb.common.domain.Tree;
import com.java2nb.common.utils.BuildTree;
import com.java2nb.common.utils.IdWorker;
import com.java2nb.system.dao.RoleDataPermDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.java2nb.system.dao.DataPermDao;
import com.java2nb.system.domain.DataPermDO;
import com.java2nb.system.service.DataPermService;



@Service
public class DataPermServiceImpl implements DataPermService {
	@Autowired
	private DataPermDao dataPermDao;

	@Autowired
	private RoleDataPermDao roleDataPermDao;
	
	@Override
	public DataPermDO get(Long id){
		return dataPermDao.get(id);
	}
	
	@Override
	public List<DataPermDO> list(Map<String, Object> map){
		return dataPermDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return dataPermDao.count(map);
	}
	
	@Override
	public int save(DataPermDO dataPerm){
		return dataPermDao.save(dataPerm);
	}
	
	@Override
	public int update(DataPermDO dataPerm){
		return dataPermDao.update(dataPerm);
	}
	
	@Override
	public int remove(Long id){
		return dataPermDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return dataPermDao.batchRemove(ids);
	}

	@Override
	public List<DataPermDO> listModuleName() {
		return dataPermDao.listModuleName();
	}

	@Override
	public Tree<DataPermDO> getTree() {
		List<Tree<DataPermDO>> trees = new ArrayList<Tree<DataPermDO>>();
		List<DataPermDO> permDOs = dataPermDao.list(new HashMap<>(16));
		Map<String,String> topTree = new HashMap<>();
		for (DataPermDO permDO : permDOs) {
			Tree<DataPermDO> tree = new Tree<>();
			tree.setId(permDO.getId().toString());
			if(!topTree.containsKey(permDO.getModuleName())){
				Tree<DataPermDO> parentTree = new Tree<>();
				String id = new IdWorker().nextId()+"";
				parentTree.setId(id);
				parentTree.setParentId(0+"");
				parentTree.setText(permDO.getModuleName());
				topTree.put(permDO.getModuleName(),id);
				trees.add(parentTree);
			}
			tree.setParentId(topTree.get(permDO.getModuleName()));
			tree.setText(permDO.getName());
			trees.add(tree);
		}
		// 默认顶级菜单为０，根据数据库实际情况调整
		Tree<DataPermDO> t = BuildTree.build(trees);
		return t;
	}

	@Override
	public Tree<DataPermDO> getTree(Long roleId) {
		List<Tree<DataPermDO>> trees = new ArrayList<Tree<DataPermDO>>();
		List<DataPermDO> permDOs = dataPermDao.list(new HashMap<>(16));
		List<Long> permIds = roleDataPermDao.listPermIdByRoleId(roleId);
		Map<String,String> topTree = new HashMap<>();
		for (DataPermDO permDO : permDOs) {
			Tree<DataPermDO> tree = new Tree<>();
			tree.setId(permDO.getId().toString());
			if(!topTree.containsKey(permDO.getModuleName())){
				Tree<DataPermDO> parentTree = new Tree<>();
				String id = new IdWorker().nextId()+"";
				parentTree.setId(id);
				parentTree.setParentId(0+"");
				parentTree.setText(permDO.getModuleName());
				topTree.put(permDO.getModuleName(),id);
				trees.add(parentTree);
			}
			tree.setParentId(topTree.get(permDO.getModuleName()));
			tree.setText(permDO.getName());
			Map<String, Object> state = new HashMap<>(16);
			if (permIds.contains(permDO.getId())) {
				state.put("selected", true);
			} else {
				state.put("selected", false);
			}
			tree.setState(state);
			trees.add(tree);
		}
		// 默认顶级菜单为０，根据数据库实际情况调整
		Tree<DataPermDO> t = BuildTree.build(trees);
		return t;
	}

}
