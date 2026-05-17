1. `novel_plus_data.sql`：包含初始化小说数据（小说内容均为无版权风险数据）的完整 SQL 文件。
2. `novel_plus.sql`：不包含初始化小说数据的完整 SQL 文件。
3. `yyyyMMdd.sql`：版本升级所需的增量 SQL 文件。
4. 首次安装时，只需执行 `novel_plus_data.sql` 或 `novel_plus.sql` 中的任意一个即可完成数据库初始化。
5. 后续版本升级时，请根据当前项目所使用的代码版本时间，依次执行该日期之后新增的增量 SQL 文件（即 `sql` 目录中相比旧版本新增的 `.sql` 文件）。
