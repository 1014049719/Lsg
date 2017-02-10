package com.talenton.lsg.base.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.talenton.lsg.base.dao.model.PhotoPathBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table PHOTO_PATH_BEAN.
*/
public class PhotoPathBeanDao extends AbstractDao<PhotoPathBean, String> {

    public static final String TABLENAME = "PHOTO_PATH_BEAN";

    /**
     * Properties of entity PhotoPathBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property PhotoPath = new Property(0, String.class, "photoPath", true, "PHOTO_PATH");
    };


    public PhotoPathBeanDao(DaoConfig config) {
        super(config);
    }
    
    public PhotoPathBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'PHOTO_PATH_BEAN' (" + //
                "'PHOTO_PATH' TEXT PRIMARY KEY NOT NULL );"); // 0: photoPath
        // Add Indexes
        db.execSQL("CREATE INDEX " + constraint + "IDX_PHOTO_PATH_BEAN_PHOTO_PATH ON PHOTO_PATH_BEAN" +
                " (PHOTO_PATH);");
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'PHOTO_PATH_BEAN'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, PhotoPathBean entity) {
        stmt.clearBindings();
 
        String photoPath = entity.getPhotoPath();
        if (photoPath != null) {
            stmt.bindString(1, photoPath);
        }
    }

    /** @inheritdoc */
    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public PhotoPathBean readEntity(Cursor cursor, int offset) {
        PhotoPathBean entity = new PhotoPathBean( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0) // photoPath
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, PhotoPathBean entity, int offset) {
        entity.setPhotoPath(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
     }
    
    /** @inheritdoc */
    @Override
    protected String updateKeyAfterInsert(PhotoPathBean entity, long rowId) {
        return entity.getPhotoPath();
    }
    
    /** @inheritdoc */
    @Override
    public String getKey(PhotoPathBean entity) {
        if(entity != null) {
            return entity.getPhotoPath();
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