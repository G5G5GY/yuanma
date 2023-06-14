CREATE TABLE "AUTHSYSTEM"."TB_LOG"
(
 "ID" BIGINT IDENTITY(6562,1) NOT NULL,
 "TITLE" VARCHAR(450) NULL,
 "BUSINESS_TYPE" INT NULL,
 "METHOD" VARCHAR(120) NULL,
 "REQUEST_METHOD" VARCHAR(30) NULL,
 "USER_ID" VARCHAR(150) NULL,
 "OPER_URL" VARCHAR(450) NULL,
 "OPER_IP" VARCHAR(150) NULL,
 "OPER_LOCATION" VARCHAR(150) NULL,
 "OPER_PARAM" CLOB NULL,
 "JSON_RESULT" CLOB NULL,
 "ERROR_MSG" CLOB NULL,
 "OPER_TIME" TIMESTAMP(0) NULL,
 "DETAIL_ID" VARCHAR(150) NULL,
 "CREATE_TIME" TIMESTAMP(0) NULL,
 "STATUS" CHAR(1) NULL,
 "MODULE" VARCHAR(150) NULL
);

CREATE TABLE "AUTHSYSTEM"."SYS_USERS_ROLES"
(
 "USER_ID" BIGINT NOT NULL,
 "ROLE_ID" BIGINT NOT NULL
);

CREATE TABLE "AUTHSYSTEM"."SYS_USERS_JOBS"
(
 "USER_ID" BIGINT NOT NULL,
 "JOB_ID" BIGINT NOT NULL
);

CREATE TABLE "AUTHSYSTEM"."SYS_USER"
(
 "USER_ID" BIGINT NOT NULL,
 "DEPT_ID" BIGINT NULL,
 "USERNAME" VARCHAR(765) NULL,
 "NICK_NAME" VARCHAR(765) NULL,
 "GENDER" VARCHAR(150) NULL,
 "PHONE" VARCHAR(765) NULL,
 "EMAIL" VARCHAR(765) NULL,
 "AVATAR_NAME" VARCHAR(765) NULL,
 "AVATAR_PATH" VARCHAR(765) NULL,
 "PASSWORD" VARCHAR(765) NULL,
 "IS_ADMIN" BIT DEFAULT '0'
 NULL,
 "ENABLED" BIGINT NULL,
 "CREATE_BY" VARCHAR(765) NULL,
 "UPDATE_BY" VARCHAR(765) NULL,
 "PWD_RESET_TIME" TIMESTAMP(0) NULL,
 "CREATE_TIME" TIMESTAMP(0) NULL,
 "REL_USER_ID" VARCHAR(192) NULL,
 "UPDATE_TIME" TIMESTAMP(0) NULL,
 "SALT" VARCHAR(765) NULL,
 "POWER_FLAG" CHAR(1) DEFAULT 0
 NULL,
 "UNLOCKED" BIT DEFAULT 1
 NULL,
 "LOCK_DATE" DATETIME(6) NULL,
 "SIGN" TEXT NULL
);

CREATE TABLE "AUTHSYSTEM"."SYS_ROLES_MENUS"
(
 "MENU_ID" BIGINT NOT NULL,
 "ROLE_ID" BIGINT NOT NULL
);

CREATE TABLE "AUTHSYSTEM"."SYS_ROLES_DEPTS"
(
 "ROLE_ID" BIGINT NOT NULL,
 "DEPT_ID" BIGINT NOT NULL
);

CREATE TABLE "AUTHSYSTEM"."SYS_ROLE"
(
 "ROLE_ID" BIGINT IDENTITY(20,1) NOT NULL,
 "NAME" VARCHAR(765) NOT NULL,
 "LEVEL" INT NULL,
 "DESCRIPTION" VARCHAR(765) NULL,
 "DATA_SCOPE" VARCHAR(765) NULL,
 "CREATE_BY" VARCHAR(765) NULL,
 "UPDATE_BY" VARCHAR(765) NULL,
 "CREATE_TIME" TIMESTAMP(0) NULL,
 "UPDATE_TIME" TIMESTAMP(0) NULL,
 "POWER_FLAG" CHAR(1) DEFAULT 0
 NULL
);

CREATE TABLE "AUTHSYSTEM"."SYS_MENU"
(
 "MENU_ID" BIGINT IDENTITY(304,1) NOT NULL,
 "PID" BIGINT NULL,
 "SUB_COUNT" INT DEFAULT 0
 NULL,
 "TYPE" INT NULL,
 "TITLE" VARCHAR(765) NULL,
 "NAME" VARCHAR(765) NULL,
 "COMPONENT" VARCHAR(765) NULL,
 "MENU_SORT" INT NULL,
 "ICON" VARCHAR(765) NULL,
 "PATH" VARCHAR(765) NULL,
 "I_FRAME" INT NULL,
 "CACHE" INT DEFAULT 0
 NULL,
 "HIDDEN" INT DEFAULT 0
 NULL,
 "PERMISSION" VARCHAR(765) NULL,
 "CREATE_BY" VARCHAR(765) NULL,
 "UPDATE_BY" VARCHAR(765) NULL,
 "CREATE_TIME" TIMESTAMP(0) NULL,
 "UPDATE_TIME" TIMESTAMP(0) NULL,
 "POWER_FLAG" CHAR(1) DEFAULT 0
 NULL
);

CREATE TABLE "AUTHSYSTEM"."SYS_LOGIN_LOG"
(
 "ID" BIGINT IDENTITY(1,1) NOT NULL,
 "USER_NAME" VARCHAR(50) NULL,
 "LOGIN_TIME" DATETIME(6) NULL,
 "LOGIN_RESULT" INT NULL,
 "LOGIN_ORGIN_RESULT" INT NULL,
 "USER_IP" VARCHAR(50) NULL,
  CLUSTER PRIMARY KEY("ID") ENABLE 
);

CREATE TABLE "AUTHSYSTEM"."SYS_LOG"
(
 "LOG_ID" BIGINT IDENTITY(1,1) NOT NULL,
 "DESCRIPTION" VARCHAR(765) NULL,
 "LOG_TYPE" VARCHAR(765) NULL,
 "METHOD" VARCHAR(765) NULL,
 "PARAMS" TEXT NULL,
 "REQUEST_IP" VARCHAR(765) NULL,
 "TIME" BIGINT NULL,
 "USERNAME" VARCHAR(765) NULL,
 "ADDRESS" VARCHAR(765) NULL,
 "BROWSER" VARCHAR(765) NULL,
 "EXCEPTION_DETAIL" TEXT NULL,
 "CREATE_TIME" TIMESTAMP(0) NULL
);

CREATE TABLE "AUTHSYSTEM"."SYS_JOB"
(
 "JOB_ID" BIGINT IDENTITY(18,1) NOT NULL,
 "NAME" VARCHAR(765) NOT NULL,
 "ENABLED" INT NOT NULL,
 "JOB_SORT" INT NULL,
 "CREATE_BY" VARCHAR(765) NULL,
 "UPDATE_BY" VARCHAR(765) NULL,
 "CREATE_TIME" TIMESTAMP(0) NULL,
 "UPDATE_TIME" TIMESTAMP(0) NULL
);

CREATE TABLE "AUTHSYSTEM"."SYS_DEPT"
(
 "DEPT_ID" BIGINT IDENTITY(19,1) NOT NULL,
 "PID" BIGINT NULL,
 "SUB_COUNT" INT DEFAULT 0
 NULL,
 "NAME" VARCHAR(765) NOT NULL,
 "DEPT_SORT" INT DEFAULT 999
 NULL,
 "ENABLED" BIT NOT NULL,
 "CREATE_BY" VARCHAR(765) NULL,
 "UPDATE_BY" VARCHAR(765) NULL,
 "CREATE_TIME" TIMESTAMP(0) NULL,
 "UPDATE_TIME" TIMESTAMP(0) NULL
);

CREATE TABLE "AUTHSYSTEM"."SYS_AUTH_POWER_APPROVE_FLOW_LOG"
(
 "ID" BIGINT IDENTITY(1,1) NOT NULL,
 "FLOW_ID" BIGINT DEFAULT NULL
 NULL,
 "OPERATOR" VARCHAR(100) DEFAULT NULL
 NULL,
 "APPROVE_STATUS" CHAR(1) DEFAULT '0'
 NULL,
 "REMARKER" VARCHAR(200) DEFAULT NULL
 NULL,
 "CREATE_BY" VARCHAR(100) DEFAULT NULL
 NULL,
 "CREATE_TIME" DATETIME(6) DEFAULT NULL
 NULL,
 "UPDATE_BY" VARCHAR(100) DEFAULT NULL
 NULL,
 "UPDATE_TIME" DATETIME(6) DEFAULT NULL
 NULL,
  CLUSTER PRIMARY KEY("ID") ENABLE 
);

CREATE TABLE "AUTHSYSTEM"."SYS_AUTH_POWER_APPROVE_FLOW"
(
 "ID" BIGINT IDENTITY(1,1) NOT NULL,
 "FLOW_NO" VARCHAR(100) DEFAULT NULL
 NULL,
 "TITLE" VARCHAR(50) DEFAULT NULL
 NULL,
 "MODULE" VARCHAR(50) DEFAULT NULL
 NULL,
 "STATUS" TINYINT DEFAULT 0
 NULL,
 "REQ_INSTANCE_CLASS" VARCHAR(100) DEFAULT NULL
 NULL,
 "TYPE" TINYINT DEFAULT 1
 NULL,
 "REQ_METHOD" VARCHAR(50) DEFAULT NULL
 NULL,
 "REQ_PARAMS_TYPE" TEXT NULL,
 "REQ_PARAMS" TEXT NULL,
 "CREATE_BY" VARCHAR(32) DEFAULT NULL
 NULL,
 "CREATE_TIME" DATETIME(6) DEFAULT NULL
 NULL,
 "UPDATE_BY" VARCHAR(32) DEFAULT NULL
 NULL,
 "UPDATE_TIME" DATETIME(6) DEFAULT NULL
 NULL,
 "DEL_FLAG" TINYINT DEFAULT NULL
 NULL,
 "REMARKER" VARCHAR(200) DEFAULT NULL
 NULL
);

ALTER TABLE "AUTHSYSTEM"."TB_LOG" ADD CONSTRAINT "PRIMARY" PRIMARY KEY("ID") ;

ALTER TABLE "AUTHSYSTEM"."SYS_USERS_ROLES" ADD CONSTRAINT  PRIMARY KEY("USER_ID","ROLE_ID") ;

ALTER TABLE "AUTHSYSTEM"."SYS_USERS_JOBS" ADD CONSTRAINT  PRIMARY KEY("USER_ID","JOB_ID") ;

ALTER TABLE "AUTHSYSTEM"."SYS_USER" ADD CONSTRAINT  PRIMARY KEY("USER_ID") ;

ALTER TABLE "AUTHSYSTEM"."SYS_USER" ADD CONSTRAINT "UK_KPUBOS9GC2CVTKB0THKTKBKES" UNIQUE("EMAIL") ;

ALTER TABLE "AUTHSYSTEM"."SYS_USER" ADD CONSTRAINT "USERNAME" UNIQUE("USERNAME") ;

ALTER TABLE "AUTHSYSTEM"."SYS_ROLES_MENUS" ADD CONSTRAINT  PRIMARY KEY("MENU_ID","ROLE_ID") ;

ALTER TABLE "AUTHSYSTEM"."SYS_ROLES_DEPTS" ADD CONSTRAINT  PRIMARY KEY("ROLE_ID","DEPT_ID") ;

ALTER TABLE "AUTHSYSTEM"."SYS_ROLE" ADD CONSTRAINT  PRIMARY KEY("ROLE_ID") ;

ALTER TABLE "AUTHSYSTEM"."SYS_ROLE" ADD CONSTRAINT "UNIQ_NAME" UNIQUE("NAME") ;

ALTER TABLE "AUTHSYSTEM"."SYS_MENU" ADD CONSTRAINT  PRIMARY KEY("MENU_ID") ;

ALTER TABLE "AUTHSYSTEM"."SYS_LOG" ADD CONSTRAINT  PRIMARY KEY("LOG_ID") ;

ALTER TABLE "AUTHSYSTEM"."SYS_JOB" ADD CONSTRAINT  PRIMARY KEY("JOB_ID") ;

ALTER TABLE "AUTHSYSTEM"."SYS_JOB" ADD CONSTRAINT "CONS134218857" UNIQUE("NAME") ;

ALTER TABLE "AUTHSYSTEM"."SYS_DEPT" ADD CONSTRAINT  PRIMARY KEY("DEPT_ID") ;

ALTER TABLE "AUTHSYSTEM"."SYS_AUTH_POWER_APPROVE_FLOW" ADD CONSTRAINT  PRIMARY KEY("ID") ;

CREATE INDEX "FKQ4EQ273L04BPU4EFJ0JD0JB98"
ON "AUTHSYSTEM"."SYS_USERS_ROLES"("ROLE_ID");

COMMENT ON TABLE "AUTHSYSTEM"."SYS_USERS_ROLES" IS '用户角色关联';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_USERS_ROLES"."USER_ID" IS '用户ID';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_USERS_ROLES"."ROLE_ID" IS '角色ID';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_USERS_JOBS"."USER_ID" IS '用户ID';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_USERS_JOBS"."JOB_ID" IS '岗位ID';

CREATE INDEX "FK5RWMRYNY6JTHAAXKOGOWNKNQP"
ON "AUTHSYSTEM"."SYS_USER"("DEPT_ID");

CREATE INDEX "INX_ENABLED"
ON "AUTHSYSTEM"."SYS_USER"("ENABLED");

CREATE INDEX "FKPQ2DHYPK2QGT68NAUH2BY22JB"
ON "AUTHSYSTEM"."SYS_USER"("AVATAR_NAME");

COMMENT ON TABLE "AUTHSYSTEM"."SYS_USER" IS '系统用户';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_USER"."USER_ID" IS 'ID';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_USER"."DEPT_ID" IS '部门名称';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_USER"."USERNAME" IS '用户名';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_USER"."NICK_NAME" IS '昵称';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_USER"."GENDER" IS '性别';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_USER"."PHONE" IS '手机号码';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_USER"."EMAIL" IS '邮箱';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_USER"."AVATAR_NAME" IS '头像地址';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_USER"."AVATAR_PATH" IS '头像真实路径';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_USER"."PASSWORD" IS '密码';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_USER"."IS_ADMIN" IS '是否为admin账号';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_USER"."ENABLED" IS '状态：1启用、0禁用';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_USER"."CREATE_BY" IS '创建者';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_USER"."UPDATE_BY" IS '更新着';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_USER"."PWD_RESET_TIME" IS '修改密码的时间';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_USER"."CREATE_TIME" IS '创建日期';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_USER"."UPDATE_TIME" IS '更新时间';

CREATE INDEX "FKCNGG2QADOJHI3A651A5ADKVBQ"
ON "AUTHSYSTEM"."SYS_ROLES_MENUS"("ROLE_ID");

COMMENT ON TABLE "AUTHSYSTEM"."SYS_ROLES_MENUS" IS '角色菜单关联';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_ROLES_MENUS"."MENU_ID" IS '菜单ID';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_ROLES_MENUS"."ROLE_ID" IS '角色ID';

CREATE INDEX "FK7QG6ITN5AJDOA9H9O78V9KSUR"
ON "AUTHSYSTEM"."SYS_ROLES_DEPTS"("DEPT_ID");

COMMENT ON TABLE "AUTHSYSTEM"."SYS_ROLES_DEPTS" IS '角色部门关联';

CREATE INDEX "ROLE_NAME_INDEX"
ON "AUTHSYSTEM"."SYS_ROLE"("NAME");

COMMENT ON TABLE "AUTHSYSTEM"."SYS_ROLE" IS '角色表';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_ROLE"."ROLE_ID" IS 'ID';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_ROLE"."NAME" IS '名称';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_ROLE"."LEVEL" IS '角色级别';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_ROLE"."DESCRIPTION" IS '描述';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_ROLE"."DATA_SCOPE" IS '数据权限';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_ROLE"."CREATE_BY" IS '创建者';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_ROLE"."UPDATE_BY" IS '更新者';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_ROLE"."CREATE_TIME" IS '创建日期';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_ROLE"."UPDATE_TIME" IS '更新时间';

CREATE INDEX "INDEX108625405307900"
ON "AUTHSYSTEM"."SYS_MENU"("PID");

COMMENT ON TABLE "AUTHSYSTEM"."SYS_MENU" IS '系统菜单';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_MENU"."MENU_ID" IS 'ID';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_MENU"."PID" IS '上级菜单ID';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_MENU"."SUB_COUNT" IS '子菜单数目';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_MENU"."TYPE" IS '菜单类型';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_MENU"."TITLE" IS '菜单标题';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_MENU"."NAME" IS '组件名称';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_MENU"."COMPONENT" IS '组件';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_MENU"."MENU_SORT" IS '排序';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_MENU"."ICON" IS '图标';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_MENU"."PATH" IS '链接地址';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_MENU"."I_FRAME" IS '是否外链';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_MENU"."CACHE" IS '缓存';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_MENU"."HIDDEN" IS '隐藏';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_MENU"."PERMISSION" IS '权限';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_MENU"."CREATE_BY" IS '创建者';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_MENU"."UPDATE_BY" IS '更新者';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_MENU"."CREATE_TIME" IS '创建日期';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_MENU"."UPDATE_TIME" IS '更新时间';

CREATE INDEX "LOG_CREATE_TIME_INDEX"
ON "AUTHSYSTEM"."SYS_LOG"("CREATE_TIME");

CREATE INDEX "INX_LOG_TYPE"
ON "AUTHSYSTEM"."SYS_LOG"("LOG_TYPE");

COMMENT ON TABLE "AUTHSYSTEM"."SYS_LOG" IS '系统日志';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_LOG"."LOG_ID" IS 'ID';

CREATE INDEX "INDEX1356601230796300"
ON "AUTHSYSTEM"."SYS_JOB"("ENABLED");

COMMENT ON TABLE "AUTHSYSTEM"."SYS_JOB" IS '岗位';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_JOB"."JOB_ID" IS 'ID';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_JOB"."NAME" IS '岗位名称';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_JOB"."ENABLED" IS '岗位状态';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_JOB"."JOB_SORT" IS '排序';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_JOB"."CREATE_BY" IS '创建者';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_JOB"."UPDATE_BY" IS '更新者';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_JOB"."CREATE_TIME" IS '创建日期';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_JOB"."UPDATE_TIME" IS '更新时间';

CREATE INDEX "INDEX1356601279432700"
ON "AUTHSYSTEM"."SYS_DEPT"("ENABLED");

CREATE INDEX "INDEX1356601291486800"
ON "AUTHSYSTEM"."SYS_DEPT"("PID");

COMMENT ON TABLE "AUTHSYSTEM"."SYS_DEPT" IS '部门';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_DEPT"."DEPT_ID" IS 'ID';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_DEPT"."PID" IS '上级部门';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_DEPT"."SUB_COUNT" IS '子部门数目';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_DEPT"."NAME" IS '名称';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_DEPT"."DEPT_SORT" IS '排序';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_DEPT"."ENABLED" IS '状态';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_DEPT"."CREATE_BY" IS '创建者';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_DEPT"."UPDATE_BY" IS '更新者';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_DEPT"."CREATE_TIME" IS '创建日期';

COMMENT ON COLUMN "AUTHSYSTEM"."SYS_DEPT"."UPDATE_TIME" IS '更新时间';

