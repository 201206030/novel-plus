package com.java2nb.novel.mapper;

import static com.java2nb.novel.mapper.BookIndexDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import com.java2nb.novel.entity.BookIndex;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import javax.annotation.Generated;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.dynamic.sql.BasicColumn;
import org.mybatis.dynamic.sql.delete.DeleteDSLCompleter;
import org.mybatis.dynamic.sql.delete.render.DeleteStatementProvider;
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider;
import org.mybatis.dynamic.sql.insert.render.MultiRowInsertStatementProvider;
import org.mybatis.dynamic.sql.select.CountDSLCompleter;
import org.mybatis.dynamic.sql.select.SelectDSLCompleter;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.update.UpdateDSL;
import org.mybatis.dynamic.sql.update.UpdateDSLCompleter;
import org.mybatis.dynamic.sql.update.UpdateModel;
import org.mybatis.dynamic.sql.update.render.UpdateStatementProvider;
import org.mybatis.dynamic.sql.util.SqlProviderAdapter;
import org.mybatis.dynamic.sql.util.mybatis3.MyBatis3Utils;

@Mapper
public interface BookIndexMapper {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    BasicColumn[] selectList = BasicColumn.columnList(id, bookId, indexNum, indexName, wordCount, isVip, bookPrice, createTime, updateTime);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type = SqlProviderAdapter.class, method = "select")
    long count(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @DeleteProvider(type = SqlProviderAdapter.class, method = "delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @InsertProvider(type = SqlProviderAdapter.class, method = "insert")
    int insert(InsertStatementProvider<BookIndex> insertStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @InsertProvider(type = SqlProviderAdapter.class, method = "insertMultiple")
    int insertMultiple(MultiRowInsertStatementProvider<BookIndex> multipleInsertStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type = SqlProviderAdapter.class, method = "select")
    @ResultMap("BookIndexResult")
    Optional<BookIndex> selectOne(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type = SqlProviderAdapter.class, method = "select")
    @Results(id = "BookIndexResult", value = {
            @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "book_id", property = "bookId", jdbcType = JdbcType.BIGINT),
            @Result(column = "index_num", property = "indexNum", jdbcType = JdbcType.INTEGER),
            @Result(column = "index_name", property = "indexName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "word_count", property = "wordCount", jdbcType = JdbcType.INTEGER),
            @Result(column = "is_vip", property = "isVip", jdbcType = JdbcType.TINYINT),
            @Result(column = "book_price", property = "bookPrice", jdbcType = JdbcType.INTEGER),
            @Result(column = "create_time", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "update_time", property = "updateTime", jdbcType = JdbcType.TIMESTAMP)
    })
    List<BookIndex> selectMany(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @UpdateProvider(type = SqlProviderAdapter.class, method = "update")
    int update(UpdateStatementProvider updateStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, bookIndex, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, bookIndex, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c ->
                c.where(id, isEqualTo(id_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insert(BookIndex record) {
        return MyBatis3Utils.insert(this::insert, record, bookIndex, c ->
                c.map(id).toProperty("id")
                        .map(bookId).toProperty("bookId")
                        .map(indexNum).toProperty("indexNum")
                        .map(indexName).toProperty("indexName")
                        .map(wordCount).toProperty("wordCount")
                        .map(isVip).toProperty("isVip")
                        .map(bookPrice).toProperty("bookPrice")
                        .map(createTime).toProperty("createTime")
                        .map(updateTime).toProperty("updateTime")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertMultiple(Collection<BookIndex> records) {
        return MyBatis3Utils.insertMultiple(this::insertMultiple, records, bookIndex, c ->
                c.map(id).toProperty("id")
                        .map(bookId).toProperty("bookId")
                        .map(indexNum).toProperty("indexNum")
                        .map(indexName).toProperty("indexName")
                        .map(wordCount).toProperty("wordCount")
                        .map(isVip).toProperty("isVip")
                        .map(bookPrice).toProperty("bookPrice")
                        .map(createTime).toProperty("createTime")
                        .map(updateTime).toProperty("updateTime")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertSelective(BookIndex record) {
        return MyBatis3Utils.insert(this::insert, record, bookIndex, c ->
                c.map(id).toPropertyWhenPresent("id", record::getId)
                        .map(bookId).toPropertyWhenPresent("bookId", record::getBookId)
                        .map(indexNum).toPropertyWhenPresent("indexNum", record::getIndexNum)
                        .map(indexName).toPropertyWhenPresent("indexName", record::getIndexName)
                        .map(wordCount).toPropertyWhenPresent("wordCount", record::getWordCount)
                        .map(isVip).toPropertyWhenPresent("isVip", record::getIsVip)
                        .map(bookPrice).toPropertyWhenPresent("bookPrice", record::getBookPrice)
                        .map(createTime).toPropertyWhenPresent("createTime", record::getCreateTime)
                        .map(updateTime).toPropertyWhenPresent("updateTime", record::getUpdateTime)
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<BookIndex> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, bookIndex, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<BookIndex> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, bookIndex, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<BookIndex> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, bookIndex, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<BookIndex> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
                c.where(id, isEqualTo(id_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, bookIndex, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateAllColumns(BookIndex record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(id).equalTo(record::getId)
                .set(bookId).equalTo(record::getBookId)
                .set(indexNum).equalTo(record::getIndexNum)
                .set(indexName).equalTo(record::getIndexName)
                .set(wordCount).equalTo(record::getWordCount)
                .set(isVip).equalTo(record::getIsVip)
                .set(bookPrice).equalTo(record::getBookPrice)
                .set(createTime).equalTo(record::getCreateTime)
                .set(updateTime).equalTo(record::getUpdateTime);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(BookIndex record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(id).equalToWhenPresent(record::getId)
                .set(bookId).equalToWhenPresent(record::getBookId)
                .set(indexNum).equalToWhenPresent(record::getIndexNum)
                .set(indexName).equalToWhenPresent(record::getIndexName)
                .set(wordCount).equalToWhenPresent(record::getWordCount)
                .set(isVip).equalToWhenPresent(record::getIsVip)
                .set(bookPrice).equalToWhenPresent(record::getBookPrice)
                .set(createTime).equalToWhenPresent(record::getCreateTime)
                .set(updateTime).equalToWhenPresent(record::getUpdateTime);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKey(BookIndex record) {
        return update(c ->
                c.set(bookId).equalTo(record::getBookId)
                        .set(indexNum).equalTo(record::getIndexNum)
                        .set(indexName).equalTo(record::getIndexName)
                        .set(wordCount).equalTo(record::getWordCount)
                        .set(isVip).equalTo(record::getIsVip)
                        .set(bookPrice).equalTo(record::getBookPrice)
                        .set(createTime).equalTo(record::getCreateTime)
                        .set(updateTime).equalTo(record::getUpdateTime)
                        .where(id, isEqualTo(record::getId))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKeySelective(BookIndex record) {
        return update(c ->
                c.set(bookId).equalToWhenPresent(record::getBookId)
                        .set(indexNum).equalToWhenPresent(record::getIndexNum)
                        .set(indexName).equalToWhenPresent(record::getIndexName)
                        .set(wordCount).equalToWhenPresent(record::getWordCount)
                        .set(isVip).equalToWhenPresent(record::getIsVip)
                        .set(bookPrice).equalToWhenPresent(record::getBookPrice)
                        .set(createTime).equalToWhenPresent(record::getCreateTime)
                        .set(updateTime).equalToWhenPresent(record::getUpdateTime)
                        .where(id, isEqualTo(record::getId))
        );
    }
}