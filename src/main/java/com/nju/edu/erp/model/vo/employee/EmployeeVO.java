package com.nju.edu.erp.model.vo.employee;

import com.nju.edu.erp.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeVO {
    /**
     * 员工姓名
     */
    private String name;

    /**
     * 性别
     * 男/女
     */
    private String sex;

    /**
     * 出生日期,精确到日
     */
    private String birthStr;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 工作岗位（职位）
     */
    private Role role;

    /**
     * 基本工资
     */
    private BigDecimal salary;
}
