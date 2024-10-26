package top.atluofu.stock;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @ClassName: StockApp
 * @description: 定义main启动类
 * @author: 有罗敷的马同学
 * @datetime: 2024Year-10Month-26Day-12:48
 * @Version: 1.0
 */
@SpringBootApplication
@MapperScan("top.atluofu.stock.pojo.mapper")
public class StockApp {
    public static void main(String[] args) {
        SpringApplication.run(StockApp.class, args);
    }
}