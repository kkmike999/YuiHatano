package net.kb.test.dbflow;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Method;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Select;

import net.kb.test.bean.UserModel;
import net.kb.test.bean.UserModel_Table;
import net.yui.testCase.DbFlowCase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

/**
 * Created by kkmike999 on 2017/06/12.
 */
public class DbFlowTest extends DbFlowCase {

    @Before
    public void setUp() throws Exception {
        FlowManager.init(new FlowConfig.Builder(getApplication()).build());

        Assert.assertEquals(0, new Select(Method.count()).from(UserModel.class).count());
    }

    @Test
    public void onInsert() {
        UserModel people = new UserModel();

        people.name = "张三";
        people.sex = 1;
        people.save();// 添加对象，一条一条保存

        Assert.assertEquals(1, new Select(Method.count()).from(UserModel.class).count());
    }

    @Test
    public void onDelete() {
        save("张三");

        new Delete().from(UserModel.class)
                    .where(UserModel_Table.name.eq("张三"))
                    .execute();

        Assert.assertEquals(0, new Select(Method.count()).from(UserModel.class).count());
    }

    @Test
    public void onUpdtea() {
        save("张三");

        // 张三->李四
        SQLite.update(UserModel.class).set(UserModel_Table.name.eq("李四"))
              .where(UserModel_Table.name.eq("张三"))
              .execute();

        UserModel user = SQLite.select()
                               .from(UserModel.class)
                               .querySingle();

        Assert.assertEquals("李四", user.getName());
    }

    private void save(String name) {
        UserModel people = new UserModel();

        people.name = name;
        people.sex = new Random().nextInt(2);
        people.save();
    }
}
