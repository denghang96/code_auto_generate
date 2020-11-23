package com.graduation.project.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.util.Date;

@Data
public class BaseEntity<T extends BaseEntity> extends Model<T> {
    private Date createTime;
    private Date modifyTime;
    private String createName;
    private String modifyName;
}
