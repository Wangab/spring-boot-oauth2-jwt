package com.wab.model.service.inf;

import com.wab.model.entity.AppBaseClientDetaile;

/**
 * @author wanganbang
 * <p>
 * AppClientDetailService Creatd on 2018/1/3
 */
public interface AppClientDetailService {
    AppBaseClientDetaile getBaseClientDetaileByID(String app_id);
}
