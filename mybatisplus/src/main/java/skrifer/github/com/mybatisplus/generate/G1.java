package skrifer.github.com.mybatisplus.generate;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import io.swagger.models.auth.In;

import java.sql.Types;
import java.util.Collections;

/**
 * doc https://baomidou.com/pages/779a6e/
 */
public class G1 {
    //输出目录
    static String OUT_PUT_PATH = "/Users/junshen/Desktop/WorkSpace/Java/code-research/mybatisplus/src/main/java/";

    //author
    static String AUTHOR = "shenjun";

    //表名,多表明逗号分开
    static String TABLE = "resmgr_ppt_slice_result";
    public static void main(String[] args) {

        FastAutoGenerator.create("jdbc:mysql://10.165.78.7:23311/resmgr?createDatabaseIfNotExist=true&characterEncoding=utf-8&allowMultiQueries=true&serverTimezone=GMT%2B8",
                "local_resmgr", "kedacom@123")
                .globalConfig(builder -> {
                    builder.author(AUTHOR) // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir(OUT_PUT_PATH); // 指定输出目录
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
                    builder.parent("com.kedacom.jy.resmgr.gather") // 所在包路径
                            .moduleName("dao") // 所属包名(会与上面包路径拼接成完整路劲,同样会加入到controller路径前)
                            .pathInfo(Collections.singletonMap(OutputFile.xml, OUT_PUT_PATH)); // 设置mapperXml生成路径(指定到项目或者模块就行，后面路径由上面两个配置指定)
                })
                .strategyConfig(builder -> {
                    builder.addInclude(TABLE) // 设置需要生成的表名
                            .addTablePrefix("t_", "c_"); // 设置过滤表前缀
                })
//                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
