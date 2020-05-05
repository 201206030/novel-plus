/**
 * 
 */
package com.java2nb.common.service;

import java.util.List;
import java.util.Map;

import com.java2nb.common.domain.GenColumnsDO;
import org.springframework.stereotype.Service;

/**
 * @author xiongxy
 * @Time 2019-10-20 11:23:09
 * @description
 * 
 */
@Service
public interface GeneratorService {
	List<Map<String, Object>> list(String tableName);

	void generatorCode(String[] tableNames);

	byte[] downloadCode(String[] tableNames);


    List<GenColumnsDO> listColumnsByTableName(String tableName);

    boolean genColumnsSave(List<GenColumnsDO> list);
}
