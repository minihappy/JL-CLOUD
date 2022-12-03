package com.service.register.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.service.register.entity.RegisterPersonnel;
import com.service.register.service.impl.RegisterPersonnelServiceImpl;
import com.service.register.service.impl.RegisterServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;

public class BaseController {

    @Autowired
    HttpServletRequest req;

    @Autowired
    RegisterPersonnelServiceImpl registerPersonnelServiceImpl;

    @Autowired
    RegisterServiceImpl registerServiceImpl;

    /**
     * 获取页面
     *
     * @return
     */
    public Page getPage() {
        int current = ServletRequestUtils.getIntParameter(req, "cuurent", 1);
        int size = ServletRequestUtils.getIntParameter(req, "size", 10);

        return new Page(current, size);
    }

}
