package com.nju.edu.erp.dao.employeedao;

import com.nju.edu.erp.model.po.clockin.ClockInCountPO;
import com.nju.edu.erp.model.po.clockin.ClockInPO;
import com.nju.edu.erp.model.po.employee.EmployeePO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface EmployeeDao {

    /**
     * 新增员工
     * @param employeePO
     * @return
     */
    Integer insertEmployee(EmployeePO employeePO);

    /**
     * 通过姓名查找
     * @param name
     * @return
     */
    EmployeePO findEmployeeByName(String name);

    /**
     * 查找全部
     * @return
     */
    List<EmployeePO> findAll();

    /**
     * 插入打卡记录
     * @param clockInPO
     * @return
     */
    Integer insertCheckIn(ClockInPO clockInPO);

    /**
     * 通过姓名查找打卡记录
     * @param name
     * @return
     */
    List<ClockInPO> findClockInByName(String name);

    /**
     * 查找全部打卡信息
     * @return
     */
    List<ClockInCountPO> findAllCountRecord();

    /**
     * 获取某人最新打卡记录
     * @param name
     * @return
     */
    ClockInPO getLatestByName(String name);

}
