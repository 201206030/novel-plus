package com.java2nb.common.interceptor;

import com.java2nb.common.utils.ShiroUtils;
import com.java2nb.system.domain.DataPermDO;
import com.java2nb.system.domain.UserDO;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Intercepts({@Signature(
        type = Executor.class,
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
), @Signature(
        type = Executor.class,
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}
)})
@Component
public class DataPermInterceptor implements Interceptor {


    public DataPermInterceptor() {
        super();
    }

    //插件运行的代码，它将代替原有的方法
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) args[0];
        String sqlId = mappedStatement.getId();
        String methodName = sqlId.substring(sqlId.lastIndexOf(".") + 1);
        if (methodName.endsWith("ByPerm")) {
            Object parameter = args[1];
            RowBounds rowBounds = (RowBounds) args[2];
            ResultHandler resultHandler = (ResultHandler) args[3];
            Executor executor = (Executor) invocation.getTarget();
            CacheKey cacheKey;
            BoundSql boundSql;
            if (args.length == 4) {
                boundSql = mappedStatement.getBoundSql(parameter);
                cacheKey = executor.createCacheKey(mappedStatement, parameter, rowBounds, boundSql);
            } else {
                cacheKey = (CacheKey) args[4];
                boundSql = (BoundSql) args[5];
            }
            Class<BoundSql> boundSqlClass = BoundSql.class;
            Field field = boundSqlClass.getDeclaredField("sql");
            field.setAccessible(true);
            String lastSql = boundSql.getSql();
            Pattern tableNamePattern = Pattern.compile("\\s+from\\s+([^\\s]+)\\s*");
            Matcher tableNameMatcher = tableNamePattern.matcher(lastSql);
            if (tableNameMatcher.find()) {
                String tableName = tableNameMatcher.group(1);
                if(!tableName.contains("_")){
                    if(tableNameMatcher.find()) {
                        tableName = tableNameMatcher.group(1);
                    }
                }
                UserDO user = ShiroUtils.getUser();
                List<DataPermDO> perms = user.getDataPerms().get(tableName);
                String pageSql = "";
                int limitIndex = lastSql.indexOf(" limit ");
                if (limitIndex != -1) {
                    pageSql = lastSql.substring(limitIndex);
                    lastSql = lastSql.substring(0, limitIndex);
                }
                String orderSql = "";
                int orderIndex = lastSql.indexOf(" order ");
                if (orderIndex == -1) {
                    orderIndex = lastSql.indexOf(" ORDER ");
                }
                if (orderIndex != -1) {
                    orderSql = lastSql.substring(orderIndex);
                    lastSql = lastSql.substring(0, orderIndex);
                }

                String linkSql = " WHERE ";
                String permSql = "";
                boolean allPerms= false;
                if (perms != null && perms.size() > 0) {

                    Class userClass = UserDO.class;
                    for (DataPermDO perm : perms) {
                        if (allPerms) {
                            break;
                        }

                        String attrName = perm.getCrlAttrName();
                        String columnName = perm.getCrlColumnName();
                        String permCode = perm.getPermCode();

                        switch (permCode.substring(0, permCode.indexOf("_"))) {
                            case "all": {
                                allPerms = true;
                                break;
                            }
                            case "own": {
                                Field attrNameField = userClass.getDeclaredField(attrName);
                                attrNameField.setAccessible(true);
                                String attrValue = attrNameField.get(user) + "";
                                permSql += ("or " + columnName + "=" + attrValue + " ");
                                break;
                            }
                            case "sup": {
                                Field supAttrNameField = userClass.getDeclaredField("sup" + (attrName.substring(0, 1).toUpperCase() + attrName.substring(1)) + "s");
                                supAttrNameField.setAccessible(true);
                                String supAttrValue = (String) supAttrNameField.get(user);
                                permSql += ("or " + columnName + " in(" + supAttrValue + ") ");
                                break;
                            }
                        }

                    }
                }
                if (!allPerms) {
                    if(permSql.length()==0){
                        permSql = "0";
                    }else{
                        permSql = permSql.replaceFirst("or","");
                    }
                    lastSql = lastSql + linkSql + "(" + permSql + ")";
                }
                lastSql += (orderSql + pageSql);

            }

            field.set(boundSql, lastSql);
            return executor.query(mappedStatement, parameter, rowBounds, resultHandler, cacheKey, boundSql);
        }
        return invocation.proceed();
    }

    // 拦截类型StatementHandler
    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }

}
