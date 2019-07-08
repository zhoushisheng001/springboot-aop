package com.zhuguang.zhou.utills;

import java.util.Collection;

/**
 * 工具类
 */
public class CollectionUtils {


    /**
     * 判断集合是否为空
     * @param collection
     * @return
     */
   public static boolean isEmpty (Collection collection) {
       if (collection == null || collection.size() == 0) {
           return true;
       } else {
           return false;
       }
   }

    /**
     * 判断集合不为空
     * @param collection
     * @return
     */
   public static boolean isNotEmpty (Collection collection) {
       return !isEmpty(collection);
   }
}
