package com.test.lucene;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by 任春鹏 on 2019-07-02.
 */
public class SortTest {

    private void sort(List<String> lists, int n) {

        int i,j;
        for (i=n-1; i>0; i--)
        {
            // 将a[0...i]中最大的数据放在末尾
            for (j=0; j<i; j++)
            {
                if (!swap(lists.get(j), lists.get(j+1))) {
                    Collections.swap(lists,j,j+1);
                }
            }
        }

        for(String item:lists){
            System.out.println(item);
        }
    }

    private boolean swap(String a, String b){
        boolean flag = true;
        String a2 = a.substring(0,a.indexOf("["));
        String b2 = b.substring(0,b.indexOf("["));

        String a1 = a.substring(a.indexOf("[")+1,a.indexOf("]"));
        String b1 = b.substring(b.indexOf("[")+1,b.indexOf("]"));

        String a3 = a.substring(a.length()-1);
        String b3 = b.substring(b.length()-1);

        String a4 = a.substring(a.indexOf("]")+1, a.indexOf("节选"));
        String b4 = b.substring(b.indexOf("]")+1, b.indexOf("节选"));

        if(a1.compareTo(b1)<0){
            flag = false;
            return flag;
        }else if(a1.compareTo(b1)==0){
            if(a2.compareTo(b2)<0){
                flag = false;
                return flag;
            }else if(a2.compareTo(b2)==0){
                if(a3.compareTo(b3)>0){
                    flag = false;
                    return flag;
                }else if(a3.compareTo(b3)==0){
                    if(a4.compareTo(b4)<0){
                        flag = false;
                        return flag;
                    }else if(a4.compareTo(b4) == 0){
                        System.out.println("不应该出现完全相同的");
                    }
                }
            }
        }

        return flag;
    }

    @Test
    public void rcp(){
        List<String> lists = new ArrayList<>();
        lists.add("甲文[2018]哈哈节选2");
        lists.add("甲文[2016]哈哈节选1");
        lists.add("甲文[2018]哈哈节选1");
        lists.add("甲文[2017]哈哈节选1");
        lists.add("乙文[2018]哈哈节选1");
        lists.add("甲文[2018]尼玛节选1");
        lists.add("乙文[2016]哈哈节选1");
        lists.add("乙文[2018]哈哈节选2");
        sort(lists,lists.size());
    }

    @Test
    public void wq(){
        String a = "甲文[2018]哈哈节选2";
        String a2 = a.substring(a.indexOf("]")+1, a.indexOf("节选"));
        System.out.println(a2);
//        String a = "甲文";
//        String b = "乙文";
//        System.out.println(a.compareTo(b));
    }

}
