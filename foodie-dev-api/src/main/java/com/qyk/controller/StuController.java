package com.qyk.controller;

import com.qyk.service.StuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StuController {

    @Autowired
    private StuService stuService;

    /**
     * 使用这个请方式的话，请求路径是： http://localhost:8088/getStu?id=1203
     * @param id
     * @return
     */
    @GetMapping("/getStu")
    public Object getStu(int id) {
        return stuService.getStuInfo(id);
    }

    /**
     * 使用这请求方式的话，请求路径是： http://localhost:8088/getStu2/1203
     * 这是标准的 rest
     * @param id
     * @return
     */
    @GetMapping("/getStu2/{id}")
    public Object getStu2(@PathVariable int id) {
        return stuService.getStuInfo(id);
    }

    @PostMapping("/saveStu")
    public Object saveStu() {
        stuService.saveStu();
        return "OK";
    }


    @PostMapping("/updateStu")
    public Object updateStu(int id) {
        stuService.updateStu(id);
        return "OK";
    }

    @PostMapping("/deleteStu")
    public Object deleteStu(int id) {
        stuService.deleteStu(id);
        return "OK";
    }

}
