package com.yuanma.webmvc.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public class BaseController<T> {

    private IService<T> service;

    public BaseController(IService<T> service){
        this.service = service;
    }

    @GetMapping
    public List<T> list(){
        return service.list();
    }



}
