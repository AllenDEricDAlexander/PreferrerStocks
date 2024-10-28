package top.atluofu.stock.pojo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: User
 * @description: 测试easy excel
 * @author: 有罗敷的马同学
 * @datetime: 2024Year-10Month-28Day-9:28
 * @Version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements Serializable {
    @ExcelProperty(value = {"用户名"},index = 1)
    private String userName;
    @ExcelProperty(value = {"年龄"},index = 2)
    private Integer age;
    @ExcelProperty(value = {"地址"} ,index = 4)
    private String address;
    @ExcelProperty(value = {"生日"},index = 3)
    //注意：日期格式注解由alibaba.excel提供
    @DateTimeFormat("yyyy/MM/dd HH:mm")
    private Date birthday;
}