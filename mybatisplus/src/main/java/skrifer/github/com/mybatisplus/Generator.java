package skrifer.github.com.mybatisplus;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import skrifer.github.com.entity.BaseEntity;

import java.sql.Types;
import java.util.Collections;
import java.util.Scanner;

/**
 * doc https://baomidou.com/pages/779a6e/
 */
public class Generator {
    //输出目录(仅限IDE运行时适用！！！！)
    static String OUT_PUT_PATH = System.getProperty("user.dir");

    //dao模块名称
    static String DAO_MODULE_NAME = "mybatisplus";

    //模块下的包名
    static String PACKAGE_NAME = "skrifer.github.com";

    //模块下的代码文件输出路径
    static String CODE_PATH = OUT_PUT_PATH + "/" + DAO_MODULE_NAME + "/src/main/java";

    //模块下的mapper文件输出路径
    static String RESOURCES_PATH = OUT_PUT_PATH + "/" + DAO_MODULE_NAME + "/src/main/resources/mapper";

    //author
    static String AUTHOR = "shenjun";

    //jdbc url(从index配置中拷贝)
    static String JDBC_URL = "jdbc:mysql://10.165.78.7:23311/resmgr?createDatabaseIfNotExist=true&characterEncoding=utf-8&allowMultiQueries=true&serverTimezone=GMT%2B8";

    //jdbc user(从index配置中拷贝)
    static String JDBC_USER = "local_resmgr";

    //jdbc pwd(从index配置中拷贝)
    static String JDBC_PWD = "kedacom@123";

    //表名,多表明逗号分开
    static String TABLE = "resmgr_ppt_slice_result";
//    static String TABLE = "CP_MEMBER_INFO,CP_CASE_INFO,CP_ROOM_INFO,DP_ROOM_INFO,DP_ROOM_ORDER,DP_DEVICE_INFO,DP_DEVICE_REQUEST,DP_VIDEO_INFO,DP_VIDEO_FILE,DP_VIDEO_CONFIG";

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.err.print("请确认是否要生成映射文件(会覆盖当前已存在的,慎重!慎重!!再慎重!!!), 仍然继续请输入:Y");
        String str = scanner.next();
        scanner.close();
        if ("Y".equals(str) == false) {
            System.out.println("恭喜您悬崖勒马!");
            return;
        }

        FastAutoGenerator.create(JDBC_URL,
                JDBC_USER, JDBC_PWD)
                .globalConfig(builder -> {
                    builder.author(AUTHOR) // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir(CODE_PATH); // 指定输出目录
                })
                .dataSourceConfig(builder -> builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                    int typeCode = metaInfo.getJdbcType().TYPE_CODE;
                    if (typeCode == Types.SMALLINT) {
                        // 自定义类型转换
                        return DbColumnType.INTEGER;
                    }
                    return typeRegistry.getColumnType(metaInfo);

                }))
                .packageConfig(builder -> {
                    builder.parent(PACKAGE_NAME) // 所在包路径
                            .moduleName("") // 所属包名(会与上面包路径拼接成完整路劲,同样会加入到controller路径前)
                            .pathInfo(Collections.singletonMap(OutputFile.xml, RESOURCES_PATH)); // 设置mapperXml生成路径(指定到项目或者模块就行，后面路径由上面两个配置指定)
                })
                .strategyConfig(builder -> {
                    builder
                            .addInclude(TABLE) // 设置需要生成的表名
                            .addTablePrefix("t_", "c_")// 设置过滤表前缀
                            .entityBuilder()
                            .enableLombok()// 启用 Lombok
                            .enableTableFieldAnnotation()// 启用字段注解
                            .superClass(BaseEntity.class);

                })
//                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();

        System.out.println("文件生成完毕!");
    }
}
