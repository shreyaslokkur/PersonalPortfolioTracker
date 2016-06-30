package com.lks.models.dao;

import com.lks.models.Investments;
import com.lks.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: shreyaslokkur
 * Date: 24/6/16
 * Time: 5:28 PM
 * To change this template use File | Settings | File Templates.
 */
public interface InvestmentsDao extends CrudRepository<Investments, Integer> {
    List<Investments> findByUserId(int userId);
}
