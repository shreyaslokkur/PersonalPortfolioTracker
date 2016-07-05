package com.lks.models.dao;

import com.lks.models.Equities;
import com.lks.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Created with IntelliJ IDEA.
 * User: shreyaslokkur
 * Date: 24/6/16
 * Time: 5:28 PM
 * To change this template use File | Settings | File Templates.
 */
public interface EquitiesDao extends CrudRepository<Equities, Integer> {

    public Equities findByIsinNumber(String isinNumber);

}
