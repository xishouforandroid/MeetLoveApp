package com.lbins.meetlove.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import de.greenrobot.dao.query.QueryBuilder;

import java.util.List;

/**
 * Created by liuzwei on 2015/3/13.
 */
public class DBHelper {
    private static Context mContext;
    private static DBHelper instance;
    private static DaoMaster.DevOpenHelper helper;
    private static SQLiteDatabase db;
    private static DaoMaster daoMaster;

    private EmpDao empDao;

    private DBHelper() {
    }

    public static DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper();
            if (mContext == null) {
                mContext = context;
            }
            helper = new DaoMaster.DevOpenHelper(context, "meet_love_hm_db_t_20170501", null);
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            instance.empDao = daoMaster.newSession().getEmpDao();
        }
        return instance;
    }

    /**
     * 插入或是更新数据
     *
     * @return
     */
    public long saveEmp(Emp emp) {
        return empDao.insertOrReplace(emp);
    }

    //查询是否存在该会员信息
    public boolean isSaved(String ID) {
        QueryBuilder<Emp> qb = empDao.queryBuilder();
        qb.where(EmpDao.Properties.Empid.eq(ID));
        qb.buildCount().count();
        return qb.buildCount().count() > 0 ? true : false;
    }


    /**
     * 更新数据
     *
     * @param emp
     */
    public void updateEmp(Emp emp) {
        empDao.update(emp);
    }

    /**
     * 删除所有数据--会员信息
     */

    public void deleteAllEmp() {
        empDao.deleteAll();
    }

    /**
     * 删除数据根据
     */

    public void deleteEmpByHxusername(String empid) {
        QueryBuilder qb = empDao.queryBuilder();
        qb.where(EmpDao.Properties.Empid.eq(empid));
        empDao.deleteByKey(empid);//删除
    }

    /**
     *
     * @return
     */
    public List<Emp> getEmpList() {
        return empDao.loadAll();
    }


    //查询会员信息
    public Emp getEmpById(String id) {
        Emp emp = empDao.load(id);
        return emp;
    }

}
