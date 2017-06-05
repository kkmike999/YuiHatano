package android.database.sqlite;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

/**
 * Created by kkmike999 on 2017/06/05.
 */
public class KbSqlParserTest {
    @Test
    public void bindArgs() throws Exception {
        Object[] args = new Object[]{1, "test"};
        String   sql  = "select * from person where id=? and test LIKE ? order by id";

        String boundSql = KbSqlParser.bindArgs(sql, args);

        System.out.println(boundSql);

        Assert.assertEquals("SELECT * FROM person WHERE id = 1 AND test LIKE 'test' ORDER BY id", boundSql);
    }

    @Test
    public void replaceExpression() throws Exception {

//        Object[] args = new Object[]{1, "test"};
//        String sql = "select * from person where id= ? order by id";
//        String   sql  = "select * from person where id=? and test=? or name='where leo' and age=1 order by id";

//        Object[] args = new Object[]{1, 2, "test"};
//        String   sql  = "select * from person where id between ? and ? and test LIKE ?";

        Object[] args = new Object[]{"test", "test1"};
        String   sql  = "select * from person where test in (?,?)";

        CCJSqlParserManager pm = new CCJSqlParserManager();

        Statement statement = pm.parse(new StringReader(sql));

        if (statement instanceof Select) {
            Select      select = (Select) statement;
            PlainSelect body   = (PlainSelect) select.getSelectBody();
            Expression  where  = body.getWhere();

            KbSqlParser.replaceExpression(where, args, 0);

            System.out.println(body);
            System.out.println(body.getFromItem());
            System.out.println(body.getWhere());
            System.out.println(body.getOrderByElements().toString());
        }

        System.out.println();
    }

}