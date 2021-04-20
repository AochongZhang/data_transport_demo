# 数据轻量迁移工具

## 功能
+ 数据迁移备份
+ 数据恢复
+ 导出数据到文件

## 特性
+ 可配置多个数据源（暂不支持跨数据源迁移）
+ 数据迁移支持配置时间点前数据迁移，每次迁移数据量
+ 数据恢复支持配置开始结束时间，是否覆盖原数据
+ 导出数据到文件支持配置导出文件格式化策略，可实现自定义策略
+ 支持调用mysqldump导出文件，并压缩，上传到oss（暂不支持Windows系统）