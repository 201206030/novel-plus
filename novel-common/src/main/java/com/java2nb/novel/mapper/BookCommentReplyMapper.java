package com.java2nb.novel.mapper;

import static com.java2nb.novel.mapper.BookCommentReplyDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import com.java2nb.novel.entity.BookCommentReply;
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
public interface BookCommentReplyMapper {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    BasicColumn[] selectList = BasicColumn.columnList(id, commentId, replyContent, location, auditStatus, createTime, createUserId);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    int insert(InsertStatementProvider<BookCommentReply> insertStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @InsertProvider(type=SqlProviderAdapter.class, method="insertMultiple")
    int insertMultiple(MultiRowInsertStatementProvider<BookCommentReply> multipleInsertStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("BookCommentReplyResult")
    Optional<BookCommentReply> selectOne(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="BookCommentReplyResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="comment_id", property="commentId", jdbcType=JdbcType.BIGINT),
        @Result(column="reply_content", property="replyContent", jdbcType=JdbcType.VARCHAR),
        @Result(column="location", property="location", jdbcType=JdbcType.VARCHAR),
        @Result(column="audit_status", property="auditStatus", jdbcType=JdbcType.TINYINT),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="create_user_id", property="createUserId", jdbcType=JdbcType.BIGINT)
    })
    List<BookCommentReply> selectMany(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, bookCommentReply, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, bookCommentReply, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insert(BookCommentReply record) {
        return MyBatis3Utils.insert(this::insert, record, bookCommentReply, c ->
            c.map(id).toProperty("id")
            .map(commentId).toProperty("commentId")
            .map(replyContent).toProperty("replyContent")
            .map(location).toProperty("location")
            .map(auditStatus).toProperty("auditStatus")
            .map(createTime).toProperty("createTime")
            .map(createUserId).toProperty("createUserId")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertMultiple(Collection<BookCommentReply> records) {
        return MyBatis3Utils.insertMultiple(this::insertMultiple, records, bookCommentReply, c ->
            c.map(id).toProperty("id")
            .map(commentId).toProperty("commentId")
            .map(replyContent).toProperty("replyContent")
            .map(location).toProperty("location")
            .map(auditStatus).toProperty("auditStatus")
            .map(createTime).toProperty("createTime")
            .map(createUserId).toProperty("createUserId")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertSelective(BookCommentReply record) {
        return MyBatis3Utils.insert(this::insert, record, bookCommentReply, c ->
            c.map(id).toPropertyWhenPresent("id", record::getId)
            .map(commentId).toPropertyWhenPresent("commentId", record::getCommentId)
            .map(replyContent).toPropertyWhenPresent("replyContent", record::getReplyContent)
            .map(location).toPropertyWhenPresent("location", record::getLocation)
            .map(auditStatus).toPropertyWhenPresent("auditStatus", record::getAuditStatus)
            .map(createTime).toPropertyWhenPresent("createTime", record::getCreateTime)
            .map(createUserId).toPropertyWhenPresent("createUserId", record::getCreateUserId)
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<BookCommentReply> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, bookCommentReply, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<BookCommentReply> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, bookCommentReply, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<BookCommentReply> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, bookCommentReply, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<BookCommentReply> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, bookCommentReply, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateAllColumns(BookCommentReply record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(id).equalTo(record::getId)
                .set(commentId).equalTo(record::getCommentId)
                .set(replyContent).equalTo(record::getReplyContent)
                .set(location).equalTo(record::getLocation)
                .set(auditStatus).equalTo(record::getAuditStatus)
                .set(createTime).equalTo(record::getCreateTime)
                .set(createUserId).equalTo(record::getCreateUserId);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(BookCommentReply record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(id).equalToWhenPresent(record::getId)
                .set(commentId).equalToWhenPresent(record::getCommentId)
                .set(replyContent).equalToWhenPresent(record::getReplyContent)
                .set(location).equalToWhenPresent(record::getLocation)
                .set(auditStatus).equalToWhenPresent(record::getAuditStatus)
                .set(createTime).equalToWhenPresent(record::getCreateTime)
                .set(createUserId).equalToWhenPresent(record::getCreateUserId);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKey(BookCommentReply record) {
        return update(c ->
            c.set(commentId).equalTo(record::getCommentId)
            .set(replyContent).equalTo(record::getReplyContent)
            .set(location).equalTo(record::getLocation)
            .set(auditStatus).equalTo(record::getAuditStatus)
            .set(createTime).equalTo(record::getCreateTime)
            .set(createUserId).equalTo(record::getCreateUserId)
            .where(id, isEqualTo(record::getId))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKeySelective(BookCommentReply record) {
        return update(c ->
            c.set(commentId).equalToWhenPresent(record::getCommentId)
            .set(replyContent).equalToWhenPresent(record::getReplyContent)
            .set(location).equalToWhenPresent(record::getLocation)
            .set(auditStatus).equalToWhenPresent(record::getAuditStatus)
            .set(createTime).equalToWhenPresent(record::getCreateTime)
            .set(createUserId).equalToWhenPresent(record::getCreateUserId)
            .where(id, isEqualTo(record::getId))
        );
    }
}