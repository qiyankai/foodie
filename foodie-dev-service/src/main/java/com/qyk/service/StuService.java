package com.qyk.service;

import com.qyk.pojo.Stu;

public interface StuService {

    Stu getStuInfo(int id);

    void saveStu();

    void updateStu(int id);

    void deleteStu(int id);

    /**
     * 测试事务
     */
    void saveParent();
    void saveChildren();

}
