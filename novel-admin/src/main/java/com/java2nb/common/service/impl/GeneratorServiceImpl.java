package com.java2nb.common.service.impl;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

import com.java2nb.common.dao.GenColumnsDao;
import com.java2nb.common.domain.GenColumnsDO;
import lombok.SneakyThrows;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.java2nb.common.dao.GeneratorMapper;
import com.java2nb.common.service.GeneratorService;
import com.java2nb.common.utils.GenUtils;
import org.springframework.transaction.annotation.Transactional;


@Service
public class GeneratorServiceImpl implements GeneratorService {
    @Autowired
    GeneratorMapper generatorMapper;

    @Autowired
    GenColumnsDao genColumnsDao;

    @Override
    public List<Map<String, Object>> list(String tableName) {
        List<Map<String, Object>> list = generatorMapper.list(tableName);
        return list;
    }

    @Override
    public void generatorCode(String[] tableNames) {
        for (String tableName : tableNames) {
            //查询表信息
            Map<String, String> table = generatorMapper.get(tableName);
            //查询列信息
            List<Map<String, String>> columns = generatorMapper.listColumns(tableName);
            //生成代码
            GenUtils.generatorCode(table, columns, null);
        }
    }

    @Override
    public byte[] downloadCode(String[] tableNames) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        for (String tableName : tableNames) {
            //查询表信息
            Map<String, String> table = generatorMapper.get(tableName);
            //查询列信息
            List<Map<String, String>> columns = generatorMapper.listColumns(tableName);
            //生成代码
            GenUtils.generatorCode(table, columns, zip);
        }
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }


    @Override
    public List<GenColumnsDO> listColumnsByTableName(String tableName) {
        Map<String, Object> query = new HashMap<>();
        query.put("tableName", tableName);
        query.put("sort", "column_sort");
        query.put("order", "asc");
        List<GenColumnsDO> columnList = genColumnsDao.list(query);
        if (columnList.size() == 0) {
            //查询列信息
            List<Map<String, String>> columns = generatorMapper.listColumns(tableName);

            int columnSort = 0;
            for (Map<String, String> column : columns) {
				GenColumnsDO genColumnsDO = transGenColumnDO(tableName,column,++columnSort);

				if(!"PRI".equalsIgnoreCase(column.get("columnKey"))) {
					columnList.add(genColumnsDO);
				}else{
					genColumnsDO.setColumnSort(0);
					genColumnsDO.setPageType(11);
					columnList.add(0,genColumnsDO);
				}


            }

        }else{
			Map<String, String> column = generatorMapper.getPriColumn(tableName);
			GenColumnsDO genColumnsDO = transGenColumnDO(tableName,column,0);
			genColumnsDO.setPageType(11);
			columnList.add(0,genColumnsDO);
		}
        return columnList;
    }

    @Transactional
	@Override
	public boolean genColumnsSave(List<GenColumnsDO> list) {
        GenColumnsDO pkColumn = list.get(0);
        String tableName = pkColumn.getTableName();
        genColumnsDao.deleteByTableName(tableName);
        list.remove(0);
        genColumnsDao.saveBatch(list);
        //查询表信息
        Map<String, String> table = generatorMapper.get(tableName);
        //生成代码
        GenUtils.generatorCode(table,pkColumn, list);



		return true;
	}

	@SneakyThrows
    public GenColumnsDO transGenColumnDO(String tableName,Map<String, String> column,int columnSort){
		GenColumnsDO genColumnsDO = new GenColumnsDO();
		genColumnsDO.setTableName(tableName);
		genColumnsDO.setColumnName(column.get("columnName"));
		genColumnsDO.setColumnType(column.get("dataType"));
		genColumnsDO.setColumnComment(column.get("columnComment"));
		PropertiesConfiguration conf = new PropertiesConfiguration("generator.properties");
		genColumnsDO.setJavaType(conf.getString(column.get("dataType")));
		genColumnsDO.setColumnSort(columnSort);
		genColumnsDO.setExtra(column.get("extra"));
		genColumnsDO.setIsRequired(0);
		if ("Date".equals(conf.getString(column.get("dataType")))) {
			genColumnsDO.setPageType(4);

		} else {
			genColumnsDO.setPageType(1);
		}
		genColumnsDO.setColumnLabel(column.get("columnComment"));
		return genColumnsDO;
	}

}
