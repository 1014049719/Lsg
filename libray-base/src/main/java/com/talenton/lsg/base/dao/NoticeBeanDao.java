package com.talenton.lsg.base.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.talenton.lsg.base.dao.model.NoticeBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table NOTICE_BEAN.
*/
public class NoticeBeanDao extends AbstractDao<NoticeBean, Long> {

    public static final String TABLENAME = "NOTICE_BEAN";

    /**
     * Properties of entity NoticeBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Type = new Property(1, Integer.class, "type", false, "TYPE");
        public final static Property CreateTime = new Property(2, Long.class, "createTime", false, "CREATE_TIME");
        public final static Property Json = new Property(3, String.class, "json", false, "JSON");
    };


    public NoticeBeanDao(DaoConfig config) {
        super(config);
    }
    
    public NoticeBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'NOTICE_BEAN' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'TYPE' INTEGER," + // 1: type
                "'CREATE_TIME' INTEGER," + // 2: createTime
                "'JSON' TEXT);"); // 3: json
        // Add Indexes
        db.execSQL("CREATE INDEX " + constraint + "IDX_NOTICE_BEAN_TYPE ON NOTICE_BEAN" +
                " (TYPE);");
        db.execSQL("CREATE INDEX " + constraint + "IDX_NOTICE_BEAN_TIME ON NOTICE_BEAN" +
                " (CREATE_TIME);");
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'NOTICE_BEAN'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, NoticeBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Integer type = entity.getType();
        if (type != null) {
            stmt.bindLong(2, type);
        }
 
        Long createTime = entity.getCreateTime();
        if (createTime != null) {
            stmt.bindLong(3, createTime);
        }
 
        String json = entity.getJson();
        if (json != null) {
            stmt.bindString(4, json);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public NoticeBean readEntity(Cursor cursor, int offset) {
        NoticeBean entity = new NoticeBean( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1), // type
            cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2), // createTime
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // json
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, NoticeBean entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setType(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
        entity.setCreateTime(cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2));
        entity.setJson(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(NoticeBean entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(NoticeBean entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
