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
    private FriendsDao friendsDao;
    private HappyHandJwDao happyHandJwDao;
    private HappyHandMessageDao happyHandMessageDao;
    private HappyHandNewsDao happyHandNewsDao;
    private HappyHandNoticeDao happyHandNoticeDao;

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
            instance.friendsDao = daoMaster.newSession().getFriendsDao();
            instance.happyHandJwDao = daoMaster.newSession().getHappyHandJwDao();
            instance.happyHandMessageDao = daoMaster.newSession().getHappyHandMessageDao();
            instance.happyHandNewsDao = daoMaster.newSession().getHappyHandNewsDao();
            instance.happyHandNoticeDao = daoMaster.newSession().getHappyHandNoticeDao();
        }
        return instance;
    }


    //------emp---------
    public long saveEmp(Emp emp) {
        return empDao.insertOrReplace(emp);
    }

    public List<Emp> getEmpList() {
        return empDao.loadAll();
    }

    public Emp getEmpById(String id) {
        Emp emp = empDao.load(id);
        return emp;
    }

    public void updateEmp(Emp emp) {
        empDao.update(emp);
    }
    //------friends------
    public long saveFriends(Friends friends) {
        return friendsDao.insertOrReplace(friends);
    }

    public List<Friends> getFriendsList() {
        return friendsDao.loadAll();
    }

    public Friends getFriendsById(String id) {
        Friends friends = friendsDao.load(id);
        return friends;
    }

    public void updateFriends(Friends friends) {
        friendsDao.update(friends);
    }
    //------HappyHandJw------
    public long saveHappyHandJw(HappyHandJw friends) {
        return happyHandJwDao.insertOrReplace(friends);
    }

    public List<HappyHandJw> getHappyHandJwList() {
        return happyHandJwDao.loadAll();
    }

    public HappyHandJw getHappyHandJwById(String id) {
        HappyHandJw friends = happyHandJwDao.load(id);
        return friends;
    }

    public void updateHappyHandJw(HappyHandJw friends) {
        happyHandJwDao.update(friends);
    }
    //------HappyHandMessage------
    public long saveHappyHandMessage(HappyHandMessage friends) {
        return happyHandMessageDao.insertOrReplace(friends);
    }

    public List<HappyHandMessage> getHappyHandMessageList() {
        return happyHandMessageDao.loadAll();
    }

    public HappyHandMessage getHappyHandMessageById(String id) {
        HappyHandMessage friends = happyHandMessageDao.load(id);
        return friends;
    }

    public void updateHappyHandMessage(HappyHandMessage friends) {
        happyHandMessageDao.update(friends);
    }

    public List<HappyHandMessage> getHappyHandMessageQuery(String is_read) {
        QueryBuilder qb = happyHandMessageDao.queryBuilder();
        qb.where(HappyHandMessageDao.Properties.Is_read.eq(is_read));
        List<HappyHandMessage> lists = qb.list();
        return lists;
    }

    //------HappyHandNews------
    public long saveHappyHandNews(HappyHandNews friends) {
        return happyHandNewsDao.insertOrReplace(friends);
    }

    public List<HappyHandNews> getHappyHandNewsList() {
        return happyHandNewsDao.loadAll();
    }

    public HappyHandNews getHappyHandNewsById(String id) {
        HappyHandNews friends = happyHandNewsDao.load(id);
        return friends;
    }

    public void updateHappyHandNews(HappyHandNews friends) {
        happyHandNewsDao.update(friends);
    }
    public List<HappyHandNews> getHappyHandNewsQuery(String is_read) {
        QueryBuilder qb = happyHandNewsDao.queryBuilder();
        qb.where(HappyHandNewsDao.Properties.Is_read.eq(is_read));
        List<HappyHandNews> lists = qb.list();
        return lists;
    }
    //------HappyHandNotice------
    public long saveHappyHandNotice(HappyHandNotice friends) {
        return happyHandNoticeDao.insertOrReplace(friends);
    }

    public List<HappyHandNotice> getHappyHandNoticeList() {
        return happyHandNoticeDao.loadAll();
    }

    public HappyHandNotice getHappyHandNoticeById(String id) {
        HappyHandNotice friends = happyHandNoticeDao.load(id);
        return friends;
    }

    public void updateHappyHandNotice(HappyHandNotice friends) {
        happyHandNoticeDao.update(friends);
    }

    public List<HappyHandNotice> getHappyHandNoticeQuery(String is_read) {
        QueryBuilder qb = happyHandNoticeDao.queryBuilder();
        qb.where(HappyHandNoticeDao.Properties.Is_read.eq(is_read));
        List<HappyHandNotice> lists = qb.list();
        return lists;
    }

//    //查询是否存在该会员信息
//    public boolean isSaved(String ID) {
//        QueryBuilder<Emp> qb = empDao.queryBuilder();
//        qb.where(EmpDao.Properties.Empid.eq(ID));
//        qb.buildCount().count();
//        return qb.buildCount().count() > 0 ? true : false;
//    }
//    /**
//     * 更新数据
//     *
//     * @param emp
//     */


//    /**
//     * 删除所有数据--会员信息
//     */
//
//    public void deleteAllEmp() {
//        empDao.deleteAll();
//    }
//
//    /**
//     * 删除数据根据
//     */
//
//    public void deleteEmpByHxusername(String empid) {
//        QueryBuilder qb = empDao.queryBuilder();
//        qb.where(EmpDao.Properties.Empid.eq(empid));
//        empDao.deleteByKey(empid);//删除
//    }

}
