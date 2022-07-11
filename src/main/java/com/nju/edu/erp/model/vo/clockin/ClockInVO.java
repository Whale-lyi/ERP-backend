package com.nju.edu.erp.model.vo.clockin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClockInVO {
    /**
     * 员工姓名
     */
    private String name;

    /**
     * 打卡时间
     */
    private String timeStr;
}
