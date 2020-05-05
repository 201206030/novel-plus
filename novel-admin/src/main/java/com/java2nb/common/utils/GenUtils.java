package com.java2nb.common.utils;


import com.java2nb.common.config.Constant;
import com.java2nb.common.domain.ColumnDO;
import com.java2nb.common.domain.GenColumnsDO;
import com.java2nb.common.domain.TableDO;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成器   工具类
 */
public class GenUtils {


    public static List<String> getTemplates() {
        List<String> templates = new ArrayList<String>();
        templates.add("templates/common/generator/domain.java.vm");
        templates.add("templates/common/generator/Dao.java.vm");
        //templates.add("templates/common/generator/Mapper.java.vm");
        templates.add("templates/common/generator/Mapper.xml.vm");
        templates.add("templates/common/generator/Service.java.vm");
        templates.add("templates/common/generator/ServiceImpl.java.vm");
        templates.add("templates/common/generator/Controller.java.vm");
        templates.add("templates/common/generator/list.html.vm");
        templates.add("templates/common/generator/add.html.vm");
        templates.add("templates/common/generator/edit.html.vm");
        templates.add("templates/common/generator/detail.html.vm");
        templates.add("templates/common/generator/list.js.vm");
        templates.add("templates/common/generator/add.js.vm");
        templates.add("templates/common/generator/edit.js.vm");
        templates.add("templates/common/generator/menu.sql.vm");

        //templates.add("templates/common/generator/menu.sql.vm");
        return templates;
    }


    /**
     * 生成代码
     */


    public static void generatorCode(Map<String, String> table,
                                     List<Map<String, String>> columns, ZipOutputStream zip) {
        /*//封装模板数据
        Map<String, Object> map = new HashMap<>(16);


        //配置信息
        Configuration config = getConfig();
        //表信息
        TableDO tableDO = new TableDO();
        tableDO.setTableName(table.get("tableName"));
        tableDO.setComments(table.get("tableComment"));
        //表名转换成Java类名
        String className = tableToJava(tableDO.getTableName(), config.getString("tablePrefix"), config.getString("autoRemovePre"));
        tableDO.setClassName(className);
        tableDO.setClassname(StringUtils.uncapitalize(className));

        //列信息
        List<ColumnDO> columsList = new ArrayList<>();
        for (Map<String, String> column : columns) {
            ColumnDO columnDO = new ColumnDO();
            columnDO.setColumnName(column.get("columnName"));
            columnDO.setDataType(column.get("dataType"));
            columnDO.setComments(column.get("columnComment"));
            columnDO.setExtra(column.get("extra"));

            //列名转换成Java属性名
            String attrName = columnToJava(columnDO.getColumnName());
            columnDO.setAttrName(attrName);
            columnDO.setAttrname(StringUtils.uncapitalize(attrName));

            //列的数据类型，转换成Java类型
            String attrType = config.getString(columnDO.getDataType(), "unknowType");
            switch (attrType) {
                case "BigDecimal": {
                    map.put("hasBigDecimal", true);
                    break;
                }
                case "Date": {
                    map.put("hasDate", true);
                    break;
                }
                case "Long": {
                    map.put("hasLong", true);
                    break;
                }
            }

            columnDO.setAttrType(attrType);

            //是否主键
            if ("PRI".equalsIgnoreCase(column.get("columnKey")) && tableDO.getPk() == null) {
                tableDO.setPk(columnDO);
            }

            columsList.add(columnDO);
        }
        tableDO.setColumns(columsList);

        //没主键，则第一个字段为主键
        if (tableDO.getPk() == null) {
            tableDO.setPk(tableDO.getColumns().get(0));
        }

        //设置velocity资源加载器
        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(prop);


        map.put("tableName", tableDO.getTableName());
        map.put("comments", tableDO.getComments());
        map.put("pk", tableDO.getPk());
        map.put("className", tableDO.getClassName());
        map.put("classname", tableDO.getClassname());
        map.put("pathName", config.getString("package").substring(config.getString("package").lastIndexOf(".") + 1));
        map.put("columns", tableDO.getColumns());
        map.put("package", config.getString("package"));
        map.put("author", config.getString("author"));
        map.put("email", config.getString("email"));
        map.put("datetime", DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
        VelocityContext context = new VelocityContext(map);

        //获取模板列表
        List<String> templates = getTemplates();
        for (String template : templates) {
            //渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, "UTF-8");
            tpl.merge(context, sw);

            try {
                String fileName = getFileName(template, tableDO.getClassname(), tableDO.getClassName(), config.getString("package"));
                if (zip != null) {
                    //添加到zip
                    zip.putNextEntry(new ZipEntry(fileName));
                    IOUtils.write(sw.toString(), zip, "UTF-8");
                    IOUtils.closeQuietly(sw);
                    zip.closeEntry();
                } else {
                    String srcPath = config.getString("srcPath");
                    fileName = srcPath + File.separator + fileName;
                    File file = new File(fileName);
                    if (file.exists()) {
                        file = new File(fileName + 1);
                    }
                    File parentCatelog = file.getParentFile();
                    if (!parentCatelog.exists()) {
                        parentCatelog.mkdirs();
                    }
                    OutputStream fos = new FileOutputStream(file);
                    IOUtils.write(sw.toString(), fos, "UTF-8");
                    fos.close();
                }
            } catch (IOException e) {
                throw new RuntimeException("渲染模板失败，表名：" + tableDO.getTableName(), e);
            }
        }*/
    }


    /**
     * 列名转换成Java属性名
     */
    public static String columnToJava(String columnName) {
        return WordUtils.capitalizeFully(columnName, new char[]{'_'}).replace("_", "");
    }

    /**
     * 表名转换成Java类名
     */
    public static String tableToJava(String tableName, String tablePrefix, String autoRemovePre) {
        if (Constant.AUTO_REOMVE_PRE.equals(autoRemovePre)) {
            tableName = tableName.substring(tableName.indexOf("_") + 1);
        }
        if (StringUtils.isNotBlank(tablePrefix)) {
            tableName = tableName.replace(tablePrefix, "");
        }

        return columnToJava(tableName);
    }

    /**
     * 获取配置信息
     */
    public static Configuration getConfig() {
        try {
            return new PropertiesConfiguration("generator.properties");
        } catch (ConfigurationException e) {
            throw new RuntimeException("获取配置文件失败，", e);
        }
    }

    /**
     * 获取文件名
     */
    public static String getFileName(String template, String classname, String className, String packageName) {
        String moduleName = packageName.substring(packageName.lastIndexOf(".") + 1);

        String javaPackagePath = "main" + File.separator + "java" + File.separator;
        //String modulesname=config.getString("packageName");
        if (StringUtils.isNotBlank(packageName)) {
            javaPackagePath += packageName.replace(".", File.separator) + File.separator;
        }

        if (template.contains("domain.java.vm")) {
            return javaPackagePath + "domain" + File.separator + className + "DO.java";
        }

        if (template.contains("Dao.java.vm")) {
            return javaPackagePath + "dao" + File.separator + className + "Dao.java";
        }

//		if(template.contains("Mapper.java.vm")){
//			return packagePath + "dao" + File.separator + className + "Mapper.java";
//		}

        if (template.contains("Service.java.vm")) {
            return javaPackagePath + "service" + File.separator + className + "Service.java";
        }

        if (template.contains("ServiceImpl.java.vm")) {
            return javaPackagePath + "service" + File.separator + "impl" + File.separator + className + "ServiceImpl.java";
        }

        if (template.contains("Controller.java.vm")) {
            return javaPackagePath + "controller" + File.separator + className + "Controller.java";
        }

        if (template.contains("Mapper.xml.vm")) {
            return "main" + File.separator + "resources" + File.separator + "mybatis" + File.separator + moduleName + File.separator + className + "Mapper.xml";
        }

        if (template.contains("list.html.vm")) {
            return "main" + File.separator + "resources" + File.separator + "templates" + File.separator
                    + moduleName + File.separator + classname + File.separator + classname + ".html";
            //				+ "modules" + File.separator + "generator" + File.separator + className.toLowerCase() + ".html";
        }
        if (template.contains("add.html.vm")) {
            return "main" + File.separator + "resources" + File.separator + "templates" + File.separator
                    + moduleName + File.separator + classname + File.separator + "add.html";
        }
        if (template.contains("edit.html.vm")) {
            return "main" + File.separator + "resources" + File.separator + "templates" + File.separator
                    + moduleName + File.separator + classname + File.separator + "edit.html";
        }
        if (template.contains("detail.html.vm")) {
            return "main" + File.separator + "resources" + File.separator + "templates" + File.separator
                    + moduleName + File.separator + classname + File.separator + "detail.html";
        }

        if (template.contains("list.js.vm")) {
            return "main" + File.separator + "resources" + File.separator + "static" + File.separator + "js" + File.separator
                    + "appjs" + File.separator + moduleName + File.separator + classname + File.separator + classname + ".js";
            //		+ "modules" + File.separator + "generator" + File.separator + className.toLowerCase() + ".js";
        }
        if (template.contains("add.js.vm")) {
            return "main" + File.separator + "resources" + File.separator + "static" + File.separator + "js" + File.separator
                    + "appjs" + File.separator + moduleName + File.separator + classname + File.separator + "add.js";
        }
        if (template.contains("edit.js.vm")) {
            return "main" + File.separator + "resources" + File.separator + "static" + File.separator + "js" + File.separator
                    + "appjs" + File.separator + moduleName + File.separator + classname + File.separator + "edit.js";
        }
        if (template.contains("menu.sql.vm")) {
            return "main" + File.separator + "resources" + File.separator + "static" + File.separator + "sql"
                    + File.separator + moduleName + File.separator + classname + File.separator + "menu.js";
        }

//		if(template.contains("menu.sql.vm")){
//			return className.toLowerCase() + "_menu.sql";
//		}

        return null;
    }

    public static void generatorCode(Map<String, String> table, GenColumnsDO pkColumn, List<GenColumnsDO> list) {
        //封装模板数据
        Map<String, Object> map = new HashMap<>(16);


        //配置信息
        Configuration config = getConfig();
        //表信息
        TableDO tableDO = new TableDO();
        tableDO.setTableName(table.get("tableName"));
        tableDO.setComments(table.get("tableComment"));
        //表名转换成Java类名
        String className = tableToJava(tableDO.getTableName(), config.getString("tablePrefix"), config.getString("autoRemovePre"));
        tableDO.setClassName(className);
        tableDO.setClassname(StringUtils.uncapitalize(className));



        Collections.sort(list, Comparator.comparingInt(GenColumnsDO::getColumnSort));

        for(GenColumnsDO genColumnsDO : list){
            String attrName = columnToJava(genColumnsDO.getColumnName());
            genColumnsDO.setAttrName(attrName);
            genColumnsDO.setAttrname(StringUtils.uncapitalize(attrName));
        }

        String attrName = columnToJava(pkColumn.getColumnName());
        pkColumn.setAttrName(attrName);
        pkColumn.setAttrname(StringUtils.uncapitalize(attrName));

        tableDO.setPk(pkColumn);

        list.add(0,pkColumn);
        tableDO.setColumns(list);

        //设置velocity资源加载器
        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(prop);


        map.put("tableName", tableDO.getTableName());
        map.put("comments", tableDO.getComments());
        map.put("pk", tableDO.getPk());
        map.put("className", tableDO.getClassName());
        map.put("classname", tableDO.getClassname());
        map.put("pathName", config.getString("package").substring(config.getString("package").lastIndexOf(".") + 1));
        map.put("columns", tableDO.getColumns());
        map.put("package", config.getString("package"));
        map.put("author", config.getString("author"));
        map.put("email", config.getString("email"));
        map.put("datetime", DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
        VelocityContext context = new VelocityContext(map);

        //获取模板列表
        List<String> templates = getTemplates();
        for (String template : templates) {
            //渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, "UTF-8");
            tpl.merge(context, sw);

            try {
                String fileName = getFileName(template, tableDO.getClassname(), tableDO.getClassName(), config.getString("package"));

                String srcPath = config.getString("srcPath");
                fileName = srcPath + File.separator + fileName;
                File file = new File(fileName);
                if (file.exists()) {
                    file = new File(fileName + 1);
                }
                File parentCatelog = file.getParentFile();
                if (!parentCatelog.exists()) {
                    parentCatelog.mkdirs();
                }
                OutputStream fos = new FileOutputStream(file);
                IOUtils.write(sw.toString(), fos, "UTF-8");
                fos.close();
            } catch (IOException e) {
                throw new RuntimeException("渲染模板失败，表名：" + tableDO.getTableName(), e);
            }
        }
    }
}
