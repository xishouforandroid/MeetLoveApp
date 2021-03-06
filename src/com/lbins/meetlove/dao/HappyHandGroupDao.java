package com.lbins.meetlove.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.lbins.meetlove.dao.HappyHandGroup;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table HAPPY_HAND_GROUP.
*/
public class HappyHandGroupDao extends AbstractDao<HappyHandGroup, String> {

    public static final String TABLENAME = "HAPPY_HAND_GROUP";

    /**
     * Properties of entity HappyHandGroup.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Groupid = new Property(0, String.class, "groupid", true, "GROUPID");
        public final static Property Title = new Property(1, String.class, "title", false, "TITLE");
        public final static Property Content = new Property(2, String.class, "content", false, "CONTENT");
        public final static Property Likeid = new Property(3, String.class, "likeid", false, "LIKEID");
        public final static Property Topnum = new Property(4, String.class, "topnum", false, "TOPNUM");
        public final static Property Pic = new Property(5, String.class, "pic", false, "PIC");
        public final static Property Is_use = new Property(6, String.class, "is_use", false, "IS_USE");
    };

    private DaoSession daoSession;


    public HappyHandGroupDao(DaoConfig config) {
        super(config);
    }
    
    public HappyHandGroupDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'HAPPY_HAND_GROUP' (" + //
                "'GROUPID' TEXT PRIMARY KEY NOT NULL ," + // 0: groupid
                "'TITLE' TEXT," + // 1: title
                "'CONTENT' TEXT," + // 2: content
                "'LIKEID' TEXT," + // 3: likeid
                "'TOPNUM' TEXT," + // 4: topnum
                "'PIC' TEXT," + // 5: pic
                "'IS_USE' TEXT);"); // 6: is_use
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'HAPPY_HAND_GROUP'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, HappyHandGroup entity) {
        stmt.clearBindings();
        stmt.bindString(1, entity.getGroupid());
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(2, title);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(3, content);
        }
 
        String likeid = entity.getLikeid();
        if (likeid != null) {
            stmt.bindString(4, likeid);
        }
 
        String topnum = entity.getTopnum();
        if (topnum != null) {
            stmt.bindString(5, topnum);
        }
 
        String pic = entity.getPic();
        if (pic != null) {
            stmt.bindString(6, pic);
        }
 
        String is_use = entity.getIs_use();
        if (is_use != null) {
            stmt.bindString(7, is_use);
        }
    }

    @Override
    protected void attachEntity(HappyHandGroup entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.getString(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public HappyHandGroup readEntity(Cursor cursor, int offset) {
        HappyHandGroup entity = new HappyHandGroup( //
            cursor.getString(offset + 0), // groupid
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // title
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // content
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // likeid
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // topnum
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // pic
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6) // is_use
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, HappyHandGroup entity, int offset) {
        entity.setGroupid(cursor.getString(offset + 0));
        entity.setTitle(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setContent(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setLikeid(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setTopnum(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setPic(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setIs_use(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
     }
    
    /** @inheritdoc */
    @Override
    protected String updateKeyAfterInsert(HappyHandGroup entity, long rowId) {
        return entity.getGroupid();
    }
    
    /** @inheritdoc */
    @Override
    public String getKey(HappyHandGroup entity) {
        if(entity != null) {
            return entity.getGroupid();
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
