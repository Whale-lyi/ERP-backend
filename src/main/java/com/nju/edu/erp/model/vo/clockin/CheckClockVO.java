package com.nju.edu.erp.model.vo.clockin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckClockVO {
    private boolean alreadyClocked;
}
