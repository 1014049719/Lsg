package com.talenton.lsg.base.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;


import com.talenton.lsg.base.dao.model.HttpCacheBean;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO for table "HTTP_CACHE".
*/
public class HttpCacheDao extends AbstractDao<HttpCacheBean, Long> {

    public static final String TABLENAME = "HTTP_CACHE";

    /**
     * Properties of entity HttpCacheBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Url = new Property(1, String.class, "url", false, "URL");
        public final static Property Param = new Property(2, String.class, "param", false, "PARAM");
        public final static Property Key = new Property(3, String.class, "key", false, "KEY");
        public final static Property CreateTime = new Property(4, long.class, "createTime", false, "CREATE_TIME");
        public final static Property UpdateTime = new Property(5, long.class, "updateTime", false, "UPDATE_TIME");
        public final static Property Data = new Property(6, String.class, "data", false, "DATA");
    };


    public HttpCacheDao(DaoConfig config) {
        super(config);
    }
    
    public HttpCacheDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"HTTP_CACHE\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"URL\" TEXT NOT NULL ," + // 1: url
                "\"PARAM\" TEXT," + // 2: param
                "\"KEY\" TEXT NOT NULL ," + // 3: key
                "\"CREATE_TIME\" INTEGER NOT NULL ," + // 4: createTime
                "\"UPDATE_TIME\" INTEGER NOT NULL ," + // 5: updateTime
                "\"DATA\" TEXT);"); // 6: data
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"HTTP_CACHE\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, HttpCacheBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getUrl());
 
        String param = entity.getParam();
        if (param != null) {
            stmt.bindString(3, param);
        }
        stmt.bindString(4, entity.getKey());
        stmt.bindLong(5, entity.getCreateTime());
        stmt.bindLong(6, entity.getUpdateTime());
 
        String data = entity.getData();
        if (data != null) {
            stmt.bindString(7, data);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public HttpCacheBean readEntity(Cursor cursor, int offset) {
        HttpCacheBean entity = new HttpCacheBean( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // url
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // param
            cursor.getString(offset + 3), // key
            cursor.getLong(offset + 4), // createTime
            cursor.getLong(offset + 5), // updateTime
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6) // data
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, HttpCacheBean entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUrl(cursor.getString(offset + 1));
        entity.setParam(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setKey(cursor.getString(offset + 3));
        entity.setCreateTime(cursor.getLong(offset + 4));
        entity.setUpdateTime(cursor.getLong(offset + 5));
        entity.setData(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(HttpCacheBean entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(HttpCacheBean entity) {
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
