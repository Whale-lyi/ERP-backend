package com.nju.edu.erp.model.vo.clockin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClockInCountVO {
    /**
     * 员工姓名
     */
    private String name;

    /**
     * 打卡次数
     */
    private Integer count;
}
