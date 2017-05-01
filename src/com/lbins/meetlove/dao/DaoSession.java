package com.lbins.meetlove.dao;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import com.lbins.meetlove.dao.Emp;

import com.lbins.meetlove.dao.EmpDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig empDaoConfig;

    private final EmpDao empDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        empDaoConfig = daoConfigMap.get(EmpDao.class).clone();
        empDaoConfig.initIdentityScope(type);

        empDao = new EmpDao(empDaoConfig, this);

        registerDao(Emp.class, empDao);
    }
    
    public void clear() {
        empDaoConfig.getIdentityScope().clear();
    }

    public EmpDao getEmpDao() {
        return empDao;
    }

}
