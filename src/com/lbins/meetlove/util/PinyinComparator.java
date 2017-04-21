package com.lbins.meetlove.util;


import com.lbins.meetlove.module.Friends;

import java.util.Comparator;

public class PinyinComparator implements Comparator {

	@Override
	public int compare(Object o1, Object o2) {
		Friends empRelateObj1 = (Friends) o1;
		Friends empRelateObj2 = (Friends) o2;
		 String str1 = PingYinUtil.getPingYin(empRelateObj1.getEmpid2Nickname());
	     String str2 = PingYinUtil.getPingYin(empRelateObj2.getEmpid2Nickname());
	     return str1.compareTo(str2);
	}

}
