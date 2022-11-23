package com.service.register;

/**
 * @author hjl
 * @version 1.0
 * @description mybatis-plus反序列化
 * @date 2022/11/16 20:57
 */

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;

import java.util.Collections;

// 演示例子，执行 main 方法控制台输入模块表名回车自动生成对应项目目录中
public class CodeGenerator {
    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/mytestdb", // 这里的路径需要换成你的数据库路径
                "root",
                "root")
                .globalConfig(builder -> {
                    builder.author("hjl") // 设置作者
                            .enableSwagger() // 开启 swagger 模式。如果开启，需要导入 swagger 的pom依赖
                            .fileOverride() // 覆盖已生成文件
                            //这里需要换成你的路径
                            .outputDir("E:\\SpringProject\\JL-Cloud\\Register\\src\\main\\java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.service") // 设置父包名
                            .moduleName("register") // 设置父包模块名，也就是最下一层的文件夹名称，他下边就是 controller，service这些目录了
                            .pathInfo(Collections.singletonMap(OutputFile.xml,
                                    "E:\\SpringProject\\JL-Cloud\\Register\\src\\main\\java\\com\\service\\register\\mapper")); // 设置mapperXml生成路径，这里也需要换成你的路径
                })
                .strategyConfig(builder -> {
                    builder
                            // addInclude 和 addExclude 只能使用一个
                            .addInclude("register","register_personnel","handbook_template_detail","handbook_template","handbook_reference","handbook_permission","handbook_log","handbook_content") // 设置需要生成的表名，如果生成全部，可以使用"all"
                            // .addExclude("m_goods_book", "b_order_status", "e_user_address") // 设置要排除的表名
                            .addTablePrefix("b_", "t_", "e_") // 设置过滤表前缀，也就是生成的类名会去掉这个前缀
                    // .addTableSuffix("s")  // 设置过滤表后缀，也就是生成的类名会去掉这个后缀
                    ;
                })
                .execute();

    }
}
