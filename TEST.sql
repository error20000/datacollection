/*
 Navicat Premium Data Transfer

 Source Server         : 127.0.0.1  test
 Source Server Type    : Oracle
 Source Server Version : 110200
 Source Host           : 127.0.0.1:1521
 Source Schema         : TEST

 Target Server Type    : Oracle
 Target Server Version : 110200
 File Encoding         : 65001

 Date: 17/06/2019 23:45:16
*/


-- ----------------------------
-- Table structure for S_BEACON
-- ----------------------------
DROP TABLE "TEST"."S_BEACON";
CREATE TABLE "TEST"."S_BEACON" (
  "PID" NUMBER NOT NULL ,
  "NAME" VARCHAR2(255 BYTE) ,
  "SN" VARCHAR2(255 BYTE) ,
  "CONNECTED" VARCHAR2(1 BYTE) ,
  "STATUS" VARCHAR2(1 BYTE) ,
  "RESEND" NUMBER ,
  "CREATETIME" VARCHAR2(20 BYTE) ,
  "ALARM" NUMBER 
)
TABLESPACE "USERS"
LOGGING
NOCOMPRESS
PCTFREE 10
INITRANS 1
STORAGE (
  INITIAL 65536 
  NEXT 1048576 
  MINEXTENTS 1
  MAXEXTENTS 2147483645
  BUFFER_POOL DEFAULT
)
PARALLEL 1
NOCACHE
DISABLE ROW MOVEMENT
;
COMMENT ON COLUMN "TEST"."S_BEACON"."NAME" IS '名称';
COMMENT ON COLUMN "TEST"."S_BEACON"."SN" IS '序列号';
COMMENT ON COLUMN "TEST"."S_BEACON"."CONNECTED" IS '曾连接过  Y/N';
COMMENT ON COLUMN "TEST"."S_BEACON"."STATUS" IS '状态  Y/N';
COMMENT ON COLUMN "TEST"."S_BEACON"."RESEND" IS '重连次数';
COMMENT ON COLUMN "TEST"."S_BEACON"."CREATETIME" IS '创建时间';
COMMENT ON COLUMN "TEST"."S_BEACON"."ALARM" IS '报警状态';

-- ----------------------------
-- Records of S_BEACON
-- ----------------------------

-- ----------------------------
-- Table structure for S_DATA
-- ----------------------------
DROP TABLE "TEST"."S_DATA";
CREATE TABLE "TEST"."S_DATA" (
  "PID" NUMBER NOT NULL ,
  "SN" VARCHAR2(255 BYTE) ,
  "S1" NUMBER ,
  "S2" NUMBER ,
  "S3" NUMBER ,
  "S4" NUMBER ,
  "AX" NUMBER ,
  "AY" NUMBER ,
  "TY" NUMBER ,
  "TM" NUMBER ,
  "TD" NUMBER ,
  "TH" NUMBER ,
  "TMM" NUMBER ,
  "TS" NUMBER ,
  "GS" VARCHAR2(1 BYTE) ,
  "DXJ" VARCHAR2(1 BYTE) ,
  "JD" NUMBER ,
  "NBW" VARCHAR2(1 BYTE) ,
  "WD" NUMBER ,
  "ACT" VARCHAR2(1 BYTE) ,
  "CREATETIME" VARCHAR2(20 BYTE) ,
  "AF" NUMBER 
)
TABLESPACE "USERS"
LOGGING
NOCOMPRESS
PCTFREE 10
INITRANS 1
STORAGE (
  INITIAL 65536 
  NEXT 1048576 
  MINEXTENTS 1
  MAXEXTENTS 2147483645
  BUFFER_POOL DEFAULT
)
PARALLEL 1
NOCACHE
DISABLE ROW MOVEMENT
;
COMMENT ON COLUMN "TEST"."S_DATA"."PID" IS '主键';
COMMENT ON COLUMN "TEST"."S_DATA"."SN" IS '序列号';
COMMENT ON COLUMN "TEST"."S_DATA"."AX" IS 'X轴角度';
COMMENT ON COLUMN "TEST"."S_DATA"."AY" IS 'Y轴角度';
COMMENT ON COLUMN "TEST"."S_DATA"."TY" IS '年';
COMMENT ON COLUMN "TEST"."S_DATA"."TM" IS '月';
COMMENT ON COLUMN "TEST"."S_DATA"."TD" IS '日';
COMMENT ON COLUMN "TEST"."S_DATA"."TH" IS '时';
COMMENT ON COLUMN "TEST"."S_DATA"."TMM" IS '分';
COMMENT ON COLUMN "TEST"."S_DATA"."TS" IS '秒';
COMMENT ON COLUMN "TEST"."S_DATA"."GS" IS 'GPS状态  Y/N';
COMMENT ON COLUMN "TEST"."S_DATA"."DXJ" IS '东西经  E/W';
COMMENT ON COLUMN "TEST"."S_DATA"."JD" IS '经度  度分格式';
COMMENT ON COLUMN "TEST"."S_DATA"."NBW" IS '南北纬  S/N';
COMMENT ON COLUMN "TEST"."S_DATA"."WD" IS '纬度  度分格式';
COMMENT ON COLUMN "TEST"."S_DATA"."ACT" IS '自动校时  Y/N';
COMMENT ON COLUMN "TEST"."S_DATA"."CREATETIME" IS '创建时间';
COMMENT ON COLUMN "TEST"."S_DATA"."AF" IS '报警标志';

-- ----------------------------
-- Records of S_DATA
-- ----------------------------

-- ----------------------------
-- Table structure for S_USER
-- ----------------------------
DROP TABLE "TEST"."S_USER";
CREATE TABLE "TEST"."S_USER" (
  "USERNAME" VARCHAR2(255 BYTE) NOT NULL ,
  "PASSWORD" VARCHAR2(32 BYTE) ,
  "PID" NUMBER 
)
TABLESPACE "USERS"
LOGGING
NOCOMPRESS
PCTFREE 10
INITRANS 1
STORAGE (
  INITIAL 65536 
  NEXT 1048576 
  MINEXTENTS 1
  MAXEXTENTS 2147483645
  BUFFER_POOL DEFAULT
)
PARALLEL 1
NOCACHE
DISABLE ROW MOVEMENT
;

-- ----------------------------
-- Records of S_USER
-- ----------------------------
INSERT INTO "TEST"."S_USER" VALUES ('admin', '21232f297a57a5a743894a0e4a801fc3', '1');

-- ----------------------------
-- Primary Key structure for table S_BEACON
-- ----------------------------
ALTER TABLE "TEST"."S_BEACON" ADD CONSTRAINT "SYS_C0011378" PRIMARY KEY ("PID");

-- ----------------------------
-- Primary Key structure for table S_DATA
-- ----------------------------
ALTER TABLE "TEST"."S_DATA" ADD CONSTRAINT "SYS_C0011308" PRIMARY KEY ("PID");

-- ----------------------------
-- Primary Key structure for table S_USER
-- ----------------------------
ALTER TABLE "TEST"."S_USER" ADD CONSTRAINT "SYS_C0011377" PRIMARY KEY ("USERNAME");

-- ----------------------------
-- Checks structure for table S_USER
-- ----------------------------
ALTER TABLE "TEST"."S_USER" ADD CONSTRAINT "SYS_C0011376" CHECK ("USERNAME" IS NOT NULL) NOT DEFERRABLE INITIALLY IMMEDIATE NORELY VALIDATE;
