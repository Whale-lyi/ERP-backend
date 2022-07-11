package com.nju.edu.erp.service.employeeservice;

import com.nju.edu.erp.model.vo.clockin.CheckClockVO;
import com.nju.edu.erp.model.vo.clockin.ClockInCountVO;
import com.nju.edu.erp.model.vo.clockin.ClockInVO;
import com.nju.edu.erp.model.vo.employee.EmployeeVO;

import java.util.List;

public interface EmployeeService {

    /**
     *注册新员工
     * @param employeeVO
     */
    void register(EmployeeVO employeeVO);

    /**
     * 通过姓名查找员工
     * @param name
     * @return
     */
    EmployeeVO findEmployeeByName(String name);

    /**
     *查找所有员工
     * @return
     */
    List<EmployeeVO> findAll();

    /**
     *员工打卡
     * @param name
     */
    void clockIn(String name);

    /**
     *返回全部员工打卡记录
     * @return
     */
    List<ClockInCountVO> findAllRecord();

    /**
     *返回当前登录用户的打卡信息
     * @param name
     * @return
     */
    List<ClockInVO> findRecordByName(String name);

    /**
     *返回当前登录用户是否成功打卡
     * @param name
     * @return
     */
    CheckClockVO checkRecordByName(String name);
}
