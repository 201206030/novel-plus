package com.java2nb.novel.mapper;

import static com.java2nb.novel.mapper.BookDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import com.java2nb.novel.entity.Book;

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
public interface BookMapper {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    BasicColumn[] selectList = BasicColumn.columnList(id, workDirection, catId, catName, picUrl, bookName, authorId, authorName, bookDesc, score, bookStatus, visitCount, wordCount, commentCount, yesterdayBuy, lastIndexId, lastIndexName, lastIndexUpdateTime, isVip, status, updateTime, createTime, crawlSourceId, crawlBookId, crawlLastTime, crawlIsStop);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type = SqlProviderAdapter.class, method = "select")
    long count(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @DeleteProvider(type = SqlProviderAdapter.class, method = "delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @InsertProvider(type = SqlProviderAdapter.class, method = "insert")
    int insert(InsertStatementProvider<Book> insertStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @InsertProvider(type = SqlProviderAdapter.class, method = "insertMultiple")
    int insertMultiple(MultiRowInsertStatementProvider<Book> multipleInsertStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type = SqlProviderAdapter.class, method = "select")
    @ResultMap("BookResult")
    Optional<Book> selectOne(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type = SqlProviderAdapter.class, method = "select")
    @Results(id = "BookResult", value = {
            @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "work_direction", property = "workDirection", jdbcType = JdbcType.TINYINT),
            @Result(column = "cat_id", property = "catId", jdbcType = JdbcType.INTEGER),
            @Result(column = "cat_name", property = "catName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "pic_url", property = "picUrl", jdbcType = JdbcType.VARCHAR),
            @Result(column = "book_name", property = "bookName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "author_id", property = "authorId", jdbcType = JdbcType.BIGINT),
            @Result(column = "author_name", property = "authorName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "book_desc", property = "bookDesc", jdbcType = JdbcType.VARCHAR),
            @Result(column = "score", property = "score", jdbcType = JdbcType.REAL),
            @Result(column = "book_status", property = "bookStatus", jdbcType = JdbcType.TINYINT),
            @Result(column = "visit_count", property = "visitCount", jdbcType = JdbcType.BIGINT),
            @Result(column = "word_count", property = "wordCount", jdbcType = JdbcType.INTEGER),
            @Result(column = "comment_count", property = "commentCount", jdbcType = JdbcType.INTEGER),
            @Result(column="yesterday_buy", property="yesterdayBuy", jdbcType=JdbcType.INTEGER),
            @Result(column = "last_index_id", property = "lastIndexId", jdbcType = JdbcType.BIGINT),
            @Result(column = "last_index_name", property = "lastIndexName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "last_index_update_time", property = "lastIndexUpdateTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "is_vip", property = "isVip", jdbcType = JdbcType.TINYINT),
            @Result(column = "status", property = "status", jdbcType = JdbcType.TINYINT),
            @Result(column = "update_time", property = "updateTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "create_time", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "crawl_source_id", property = "crawlSourceId", jdbcType = JdbcType.INTEGER),
            @Result(column = "crawl_book_id", property = "crawlBookId", jdbcType = JdbcType.VARCHAR),
            @Result(column = "crawl_last_time", property = "crawlLastTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "crawl_is_stop", property = "crawlIsStop", jdbcType = JdbcType.TINYINT)
    })
    List<Book> selectMany(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @UpdateProvider(type = SqlProviderAdapter.class, method = "update")
    int update(UpdateStatementProvider updateStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, book, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, book, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c ->
                c.where(id, isEqualTo(id_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insert(Book record) {
        return MyBatis3Utils.insert(this::insert, record, book, c ->
                c.map(id).toProperty("id")
                        .map(workDirection).toProperty("workDirection")
                        .map(catId).toProperty("catId")
                        .map(catName).toProperty("catName")
                        .map(picUrl).toProperty("picUrl")
                        .map(bookName).toProperty("bookName")
                        .map(authorId).toProperty("authorId")
                        .map(authorName).toProperty("authorName")
                        .map(bookDesc).toProperty("bookDesc")
                        .map(score).toProperty("score")
                        .map(bookStatus).toProperty("bookStatus")
                        .map(visitCount).toProperty("visitCount")
                        .map(wordCount).toProperty("wordCount")
                        .map(commentCount).toProperty("commentCount")
                        .map(yesterdayBuy).toProperty("yesterdayBuy")
                        .map(lastIndexId).toProperty("lastIndexId")
                        .map(lastIndexName).toProperty("lastIndexName")
                        .map(lastIndexUpdateTime).toProperty("lastIndexUpdateTime")
                        .map(isVip).toProperty("isVip")
                        .map(status).toProperty("status")
                        .map(updateTime).toProperty("updateTime")
                        .map(createTime).toProperty("createTime")
                        .map(crawlSourceId).toProperty("crawlSourceId")
                        .map(crawlBookId).toProperty("crawlBookId")
                        .map(crawlLastTime).toProperty("crawlLastTime")
                        .map(crawlIsStop).toProperty("crawlIsStop")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertMultiple(Collection<Book> records) {
        return MyBatis3Utils.insertMultiple(this::insertMultiple, records, book, c ->
                c.map(id).toProperty("id")
                        .map(workDirection).toProperty("workDirection")
                        .map(catId).toProperty("catId")
                        .map(catName).toProperty("catName")
                        .map(picUrl).toProperty("picUrl")
                        .map(bookName).toProperty("bookName")
                        .map(authorId).toProperty("authorId")
                        .map(authorName).toProperty("authorName")
                        .map(bookDesc).toProperty("bookDesc")
                        .map(score).toProperty("score")
                        .map(bookStatus).toProperty("bookStatus")
                        .map(visitCount).toProperty("visitCount")
                        .map(wordCount).toProperty("wordCount")
                        .map(commentCount).toProperty("commentCount")
                        .map(yesterdayBuy).toProperty("yesterdayBuy")
                        .map(lastIndexId).toProperty("lastIndexId")
                        .map(lastIndexName).toProperty("lastIndexName")
                        .map(lastIndexUpdateTime).toProperty("lastIndexUpdateTime")
                        .map(isVip).toProperty("isVip")
                        .map(status).toProperty("status")
                        .map(updateTime).toProperty("updateTime")
                        .map(createTime).toProperty("createTime")
                        .map(crawlSourceId).toProperty("crawlSourceId")
                        .map(crawlBookId).toProperty("crawlBookId")
                        .map(crawlLastTime).toProperty("crawlLastTime")
                        .map(crawlIsStop).toProperty("crawlIsStop")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertSelective(Book record) {
        return MyBatis3Utils.insert(this::insert, record, book, c ->
                c.map(id).toPropertyWhenPresent("id", record::getId)
                        .map(workDirection).toPropertyWhenPresent("workDirection", record::getWorkDirection)
                        .map(catId).toPropertyWhenPresent("catId", record::getCatId)
                        .map(catName).toPropertyWhenPresent("catName", record::getCatName)
                        .map(picUrl).toPropertyWhenPresent("picUrl", record::getPicUrl)
                        .map(bookName).toPropertyWhenPresent("bookName", record::getBookName)
                        .map(authorId).toPropertyWhenPresent("authorId", record::getAuthorId)
                        .map(authorName).toPropertyWhenPresent("authorName", record::getAuthorName)
                        .map(bookDesc).toPropertyWhenPresent("bookDesc", record::getBookDesc)
                        .map(score).toPropertyWhenPresent("score", record::getScore)
                        .map(bookStatus).toPropertyWhenPresent("bookStatus", record::getBookStatus)
                        .map(visitCount).toPropertyWhenPresent("visitCount", record::getVisitCount)
                        .map(wordCount).toPropertyWhenPresent("wordCount", record::getWordCount)
                        .map(commentCount).toPropertyWhenPresent("commentCount", record::getCommentCount)
                        .map(yesterdayBuy).toPropertyWhenPresent("yesterdayBuy", record::getYesterdayBuy)
                        .map(lastIndexId).toPropertyWhenPresent("lastIndexId", record::getLastIndexId)
                        .map(lastIndexName).toPropertyWhenPresent("lastIndexName", record::getLastIndexName)
                        .map(lastIndexUpdateTime).toPropertyWhenPresent("lastIndexUpdateTime", record::getLastIndexUpdateTime)
                        .map(isVip).toPropertyWhenPresent("isVip", record::getIsVip)
                        .map(status).toPropertyWhenPresent("status", record::getStatus)
                        .map(updateTime).toPropertyWhenPresent("updateTime", record::getUpdateTime)
                        .map(createTime).toPropertyWhenPresent("createTime", record::getCreateTime)
                        .map(crawlSourceId).toPropertyWhenPresent("crawlSourceId", record::getCrawlSourceId)
                        .map(crawlBookId).toPropertyWhenPresent("crawlBookId", record::getCrawlBookId)
                        .map(crawlLastTime).toPropertyWhenPresent("crawlLastTime", record::getCrawlLastTime)
                        .map(crawlIsStop).toPropertyWhenPresent("crawlIsStop", record::getCrawlIsStop)
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<Book> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, book, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<Book> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, book, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<Book> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, book, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<Book> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
                c.where(id, isEqualTo(id_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, book, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateAllColumns(Book record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(id).equalTo(record::getId)
                .set(workDirection).equalTo(record::getWorkDirection)
                .set(catId).equalTo(record::getCatId)
                .set(catName).equalTo(record::getCatName)
                .set(picUrl).equalTo(record::getPicUrl)
                .set(bookName).equalTo(record::getBookName)
                .set(authorId).equalTo(record::getAuthorId)
                .set(authorName).equalTo(record::getAuthorName)
                .set(bookDesc).equalTo(record::getBookDesc)
                .set(score).equalTo(record::getScore)
                .set(bookStatus).equalTo(record::getBookStatus)
                .set(visitCount).equalTo(record::getVisitCount)
                .set(wordCount).equalTo(record::getWordCount)
                .set(commentCount).equalTo(record::getCommentCount)
                .set(yesterdayBuy).equalTo(record::getYesterdayBuy)
                .set(lastIndexId).equalTo(record::getLastIndexId)
                .set(lastIndexName).equalTo(record::getLastIndexName)
                .set(lastIndexUpdateTime).equalTo(record::getLastIndexUpdateTime)
                .set(isVip).equalTo(record::getIsVip)
                .set(status).equalTo(record::getStatus)
                .set(updateTime).equalTo(record::getUpdateTime)
                .set(createTime).equalTo(record::getCreateTime)
                .set(crawlSourceId).equalTo(record::getCrawlSourceId)
                .set(crawlBookId).equalTo(record::getCrawlBookId)
                .set(crawlLastTime).equalTo(record::getCrawlLastTime)
                .set(crawlIsStop).equalTo(record::getCrawlIsStop);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(Book record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(id).equalToWhenPresent(record::getId)
                .set(workDirection).equalToWhenPresent(record::getWorkDirection)
                .set(catId).equalToWhenPresent(record::getCatId)
                .set(catName).equalToWhenPresent(record::getCatName)
                .set(picUrl).equalToWhenPresent(record::getPicUrl)
                .set(bookName).equalToWhenPresent(record::getBookName)
                .set(authorId).equalToWhenPresent(record::getAuthorId)
                .set(authorName).equalToWhenPresent(record::getAuthorName)
                .set(bookDesc).equalToWhenPresent(record::getBookDesc)
                .set(score).equalToWhenPresent(record::getScore)
                .set(bookStatus).equalToWhenPresent(record::getBookStatus)
                .set(visitCount).equalToWhenPresent(record::getVisitCount)
                .set(wordCount).equalToWhenPresent(record::getWordCount)
                .set(commentCount).equalToWhenPresent(record::getCommentCount)
                .set(yesterdayBuy).equalToWhenPresent(record::getYesterdayBuy)
                .set(lastIndexId).equalToWhenPresent(record::getLastIndexId)
                .set(lastIndexName).equalToWhenPresent(record::getLastIndexName)
                .set(lastIndexUpdateTime).equalToWhenPresent(record::getLastIndexUpdateTime)
                .set(isVip).equalToWhenPresent(record::getIsVip)
                .set(status).equalToWhenPresent(record::getStatus)
                .set(updateTime).equalToWhenPresent(record::getUpdateTime)
                .set(createTime).equalToWhenPresent(record::getCreateTime)
                .set(crawlSourceId).equalToWhenPresent(record::getCrawlSourceId)
                .set(crawlBookId).equalToWhenPresent(record::getCrawlBookId)
                .set(crawlLastTime).equalToWhenPresent(record::getCrawlLastTime)
                .set(crawlIsStop).equalToWhenPresent(record::getCrawlIsStop);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKey(Book record) {
        return update(c ->
                c.set(workDirection).equalTo(record::getWorkDirection)
                        .set(catId).equalTo(record::getCatId)
                        .set(catName).equalTo(record::getCatName)
                        .set(picUrl).equalTo(record::getPicUrl)
                        .set(bookName).equalTo(record::getBookName)
                        .set(authorId).equalTo(record::getAuthorId)
                        .set(authorName).equalTo(record::getAuthorName)
                        .set(bookDesc).equalTo(record::getBookDesc)
                        .set(score).equalTo(record::getScore)
                        .set(bookStatus).equalTo(record::getBookStatus)
                        .set(visitCount).equalTo(record::getVisitCount)
                        .set(wordCount).equalTo(record::getWordCount)
                        .set(commentCount).equalTo(record::getCommentCount)
                        .set(yesterdayBuy).equalTo(record::getYesterdayBuy)
                        .set(lastIndexId).equalTo(record::getLastIndexId)
                        .set(lastIndexName).equalTo(record::getLastIndexName)
                        .set(lastIndexUpdateTime).equalTo(record::getLastIndexUpdateTime)
                        .set(isVip).equalTo(record::getIsVip)
                        .set(status).equalTo(record::getStatus)
                        .set(updateTime).equalTo(record::getUpdateTime)
                        .set(createTime).equalTo(record::getCreateTime)
                        .set(crawlSourceId).equalTo(record::getCrawlSourceId)
                        .set(crawlBookId).equalTo(record::getCrawlBookId)
                        .set(crawlLastTime).equalTo(record::getCrawlLastTime)
                        .set(crawlIsStop).equalTo(record::getCrawlIsStop)
                        .where(id, isEqualTo(record::getId))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKeySelective(Book record) {
        return update(c ->
                c.set(workDirection).equalToWhenPresent(record::getWorkDirection)
                        .set(catId).equalToWhenPresent(record::getCatId)
                        .set(catName).equalToWhenPresent(record::getCatName)
                        .set(picUrl).equalToWhenPresent(record::getPicUrl)
                        .set(bookName).equalToWhenPresent(record::getBookName)
                        .set(authorId).equalToWhenPresent(record::getAuthorId)
                        .set(authorName).equalToWhenPresent(record::getAuthorName)
                        .set(bookDesc).equalToWhenPresent(record::getBookDesc)
                        .set(score).equalToWhenPresent(record::getScore)
                        .set(bookStatus).equalToWhenPresent(record::getBookStatus)
                        .set(visitCount).equalToWhenPresent(record::getVisitCount)
                        .set(wordCount).equalToWhenPresent(record::getWordCount)
                        .set(commentCount).equalToWhenPresent(record::getCommentCount)
                        .set(yesterdayBuy).equalToWhenPresent(record::getYesterdayBuy)
                        .set(lastIndexId).equalToWhenPresent(record::getLastIndexId)
                        .set(lastIndexName).equalToWhenPresent(record::getLastIndexName)
                        .set(lastIndexUpdateTime).equalToWhenPresent(record::getLastIndexUpdateTime)
                        .set(isVip).equalToWhenPresent(record::getIsVip)
                        .set(status).equalToWhenPresent(record::getStatus)
                        .set(updateTime).equalToWhenPresent(record::getUpdateTime)
                        .set(createTime).equalToWhenPresent(record::getCreateTime)
                        .set(crawlSourceId).equalToWhenPresent(record::getCrawlSourceId)
                        .set(crawlBookId).equalToWhenPresent(record::getCrawlBookId)
                        .set(crawlLastTime).equalToWhenPresent(record::getCrawlLastTime)
                        .set(crawlIsStop).equalToWhenPresent(record::getCrawlIsStop)
                        .where(id, isEqualTo(record::getId))
        );
    }
}