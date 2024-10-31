package top.atluofu.stock.pojo.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName: TaskThreadPoolInfo
 * @description: 线程池参数配置实体类
 * @author: 有罗敷的马同学
 * @datetime: 2024Year-10Month-31Day-12:31
 * @Version: 1.0
 */
@ConfigurationProperties(prefix = "task.pool")
@Data
public class TaskThreadPoolInfo {
    /**
     *  核心线程数（获取硬件）：线程池创建时候初始化的线程数
     */
    private Integer corePoolSize;
    private Integer maxPoolSize;
    private Integer keepAliveSeconds;
    private Integer queueCapacity;
}