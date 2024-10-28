package top.atluofu.stock;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @ClassName: JobApp
 * @description: JobApp
 * @author: 有罗敷的马同学
 * @datetime: 2024Year-10Month-28Day-10:42
 * @Version: 1.0
 */
@SpringBootApplication
@MapperScan("top.atluofu.stock.pojo.mapper")
public class JobApp {
    public static void main(String[] args) {
        SpringApplication.run(JobApp.class, args);
    }
}