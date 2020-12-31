package com.hywx.sirs.util;

import java.util.ArrayList;
import java.util.List;

public class CollectionUtil {
	
	/**
	 * list去重
	 * @param list
	 * @return
	 */
	public static List removeDuplicate(List list){  
        List listTemp = new ArrayList();  
        for(int i = 0; i < list.size(); i++) {  
            if(!listTemp.contains(list.get(i))) {  
                listTemp.add(list.get(i));  
            }  
        }  
        
        return listTemp;  
    }

}
