package top.atluofu.stock;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import org.junit.jupiter.api.Test;
import top.atluofu.stock.pojo.User;
import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: TestEasyExcel
 * @description: TestEasyExcel
 * @author: 有罗敷的马同学
 * @datetime: 2024Year-10Month-28Day-9:29
 * @Version: 1.0
 */
public class TestEasyExcel {

    public List<User> init() {
        //组装数据
        ArrayList<User> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setAddress("上海" + i);
            user.setUserName("张三" + i);
            user.setBirthday(new Date());
            user.setAge(10 + i);
            users.add(user);
        }
        return users;
    }

    /**
     * 直接导出后，表头名称默认是实体类中的属性名称
     */
    @Test
    public void test02() {
        List<User> users = init();
        //不做任何注解处理时，表头名称与实体类属性名称一致
        EasyExcel.write("C:\\用户.xls", User.class).sheet("用户信息").doWrite(users);
    }

    /**
     * excel数据格式必须与实体类定义一致，否则数据读取不到
     */
    @Test
    public void readExcel() {
        ArrayList<User> users = new ArrayList<>();
        //读取数据
        EasyExcel.read("C:\\用户.xls", User.class, new AnalysisEventListener<User>() {
            @Override
            public void invoke(User o, AnalysisContext analysisContext) {
                System.out.println(o);
                users.add(o);
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                System.out.println("完成。。。。");
            }
        }).sheet().doRead();
        System.out.println(users);
    }
}