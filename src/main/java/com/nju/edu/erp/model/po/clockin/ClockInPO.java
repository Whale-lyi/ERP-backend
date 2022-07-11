package com.nju.edu.erp.model.po.clockin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClockInPO {
    /**
     * id
     */
    private Integer id;

    /**
     * 员工姓名
     */
    private String name;

    /**
     * 打卡时间
     */
    private Date time;
}
