package com.nju.edu.erp.service.Impl.employeeserviceimpl;

import com.nju.edu.erp.dao.employeedao.EmployeeDao;
import com.nju.edu.erp.exception.MyServiceException;
import com.nju.edu.erp.model.po.clockin.ClockInCountPO;
import com.nju.edu.erp.model.po.clockin.ClockInPO;
import com.nju.edu.erp.model.po.employee.EmployeePO;
import com.nju.edu.erp.model.po.user.User;
import com.nju.edu.erp.model.vo.clockin.CheckClockVO;
import com.nju.edu.erp.model.vo.clockin.ClockInCountVO;
import com.nju.edu.erp.model.vo.clockin.ClockInVO;
import com.nju.edu.erp.model.vo.employee.EmployeeVO;
import com.nju.edu.erp.model.vo.user.UserVO;
import com.nju.edu.erp.service.employeeservice.EmployeeService;
import com.nju.edu.erp.service.employeeservice.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeDao employeeDao;
    private final UserService userService;

    @Autowired
    public EmployeeServiceImpl(EmployeeDao employeeDao, UserService userService) {
        this.employeeDao = employeeDao;
        this.userService = userService;
    }

    @Override
    public void register(EmployeeVO employeeVO) {
        EmployeePO employeePO = new EmployeePO();
        BeanUtils.copyProperties(employeeVO, employeePO);

        EmployeePO employee = employeeDao.findEmployeeByName(employeePO.getName());
        if (employee != null) {
            throw new MyServiceException("A0001", "用户名已存在");
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = dateFormat.parse(employeeVO.getBirthStr());
            employeePO.setBirth(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        employeeDao.insertEmployee(employeePO);

        User user = userService.getUserByName(employeePO.getName());
        if (user == null) {
            UserVO userVO = new UserVO(employeeVO.getName(), employeeVO.getRole(), "123456");
            userService.register(userVO);
        }
    }

    @Override
    public EmployeeVO findEmployeeByName(String name) {
        EmployeePO employeePO = employeeDao.findEmployeeByName(name);
        if (employeePO == null) return null;
        EmployeeVO employeeVO = new EmployeeVO();
        BeanUtils.copyProperties(employeePO, employeeVO);
        return employeeVO;
    }

    @Override
    public List<EmployeeVO> findAll() {
        List<EmployeePO> employeePOS = employeeDao.findAll();
        List<EmployeeVO> employeeVOS = new ArrayList<>();
        for (EmployeePO employeePO : employeePOS) {
            EmployeeVO employeeVO = new EmployeeVO();
            BeanUtils.copyProperties(employeePO, employeeVO);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String birthStr = dateFormat.format(employeePO.getBirth());
            employeeVO.setBirthStr(birthStr);
            employeeVOS.add(employeeVO);
        }
        return employeeVOS;
    }

    @Override
    public void clockIn(String name) {
        if (!checkRecordByName(name).isAlreadyClocked()) {
            ClockInPO clockInPO = new ClockInPO();
            clockInPO.setName(name);
            clockInPO.setTime(new Date());
            employeeDao.insertCheckIn(clockInPO);
        }
    }

    @Override
    public CheckClockVO checkRecordByName(String name) {
        CheckClockVO checkClockVO = new CheckClockVO();
        ClockInPO latest = employeeDao.getLatestByName(name);
        if (latest != null) {
            Date date = latest.getTime();
            Date now = new Date();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dateStr = dateFormat.format(date);
            String nowStr = dateFormat.format(now);
            checkClockVO.setAlreadyClocked(dateStr.equals(nowStr));
        } else {
            checkClockVO.setAlreadyClocked(false);
        }
        return checkClockVO;
    }

    @Override
    public List<ClockInVO> findRecordByName(String name) {
        List<ClockInPO> clockInPOS = employeeDao.findClockInByName(name);
        List<ClockInVO> clockInVOS = new ArrayList<>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (ClockInPO clockInPO : clockInPOS) {
            ClockInVO clockInVO = new ClockInVO();
            BeanUtils.copyProperties(clockInPO, clockInVO);
            String dateStr = dateFormat.format(clockInPO.getTime());
            clockInVO.setTimeStr(dateStr);
            clockInVOS.add(clockInVO);
        }
        return clockInVOS;
    }

    @Override
    public List<ClockInCountVO> findAllRecord() {
        List<ClockInCountPO> clockInCountPOS = employeeDao.findAllCountRecord();
        List<ClockInCountVO> clockInCountVOS = new ArrayList<>();
        for (ClockInCountPO clockInCountPO : clockInCountPOS) {
            ClockInCountVO clockInCountVO = new ClockInCountVO();
            BeanUtils.copyProperties(clockInCountPO, clockInCountVO);
            clockInCountVOS.add(clockInCountVO);
        }
        return clockInCountVOS;
    }
}
