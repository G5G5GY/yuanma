package com.yuanma.module.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_users_jobs")
public class UserAndJobRelEntity {
    private Long userId;
    private Long jobId;
}
