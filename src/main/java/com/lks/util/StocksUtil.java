/*************************************************************************
 * ADOBE CONFIDENTIAL
 * ___________________
 *
 *  Copyright 2014 Adobe Systems Incorporated
 *  All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Adobe Systems Incorporated and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Adobe Systems Incorporated and its
 * suppliers and are protected by all applicable intellectual property
 * laws, including trade secret and copyright laws.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Adobe Systems Incorporated.
 **************************************************************************/

package com.lks.util;

import com.lks.models.Equities;
import com.lks.models.dao.EquitiesDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class StocksUtil {

    private static EquitiesDao equitiesDao;

    @Autowired
    public StocksUtil(EquitiesDao equitiesDao) {
        StocksUtil.equitiesDao = equitiesDao;
    }


public static Map<String, Equities> getAllEquities() {
    Iterable<Equities> allEquities = equitiesDao.findAll();
    Map<String, Equities> equitiesMap = new HashMap<String, Equities>();
    for(Equities equities : allEquities) {
        equitiesMap.put(equities.getIsinNumber(), equities);
    }
    return equitiesMap;
}
}
