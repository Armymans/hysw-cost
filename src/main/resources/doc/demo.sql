drop table if exists demo ;
create table if not exists demo (
  id          varchar(64)   NOT NULL,
  act_id      varchar(36)   DEFAULT NULL comment '账号',
  act_name    varchar(36)   DEFAULT NULL comment '账户名称',
  act_type    char(3)       DEFAULT NULL comment '账户类型',
  total_amt   decimal(18,2) DEFAULT NULL comment '余额',
  remark      varchar(2000) DEFAULT NULL comment '备注说明',
  user_id     varchar(64)   DEFAULT NULL comment '用户id',
  status      char(1)       DEFAULT NULL,
  create_date varchar(20)   DEFAULT NULL comment '创建时间',
  update_date varchar(20)   DEFAULT NULL comment '修改时间',
  PRIMARY KEY (id) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='demo';
