package com.wab.model.service.impl;

import com.wab.model.dao.AppClientDetaileRepository;
import com.wab.model.entity.AppBaseClientDetaile;
import com.wab.model.service.inf.AppClientDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wanganbang
 * <p>
 * AppClientDetaileServiceImpl Creatd on 2018/1/3
 */
@Service
public class AppClientDetaileServiceImpl implements AppClientDetailService {
    @Autowired
    private AppClientDetaileRepository clientDetaileRepository;

    @Override
    public AppBaseClientDetaile getBaseClientDetaileByID(String app_id) {
        return clientDetaileRepository.findOne(app_id);
    }
}
