package net.zlw.cloud.settleAccounts.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @Classname 其他信息表
 * @Description TODO
 * @Date 2020/11/25 18:15
 * @Created by sjf
 */
@Data
@Table(name = "other_info")
public class OtherInfo {
        /*    `id`               '唯一标识',
            `num`              '编码',
            `serial_number`    '序号',
            `foreign_key`       '基本信息表外键',
            `found_id`          '创建人id',
            `founder_company`   '创建人公司',
            `create_time`       '创建时间',
            `update_time`       '修改时间',
            `status`            '0删除1正常',*/
    @Column(name = "id")
    private String id;
    @Column(name = "num")
    private String num;
    @Column(name = "serial_number")
    private String serialNumber;
    @Column(name = "foreign_key")
    private String foreignKey;
    @Column(name = "id")
    private String foundId;
    @Column(name = "founder_company")
    private String founderCompany;
    @Column(name = "create_time")
    private String createTime;
    @Column(name = "update_time")
    private String updateTime;
    @Column(name = "status")
    private String status;

}
