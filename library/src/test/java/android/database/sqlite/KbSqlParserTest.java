package android.database.sqlite;

import net.sf.jsqlparser.expression.Expression;

import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

/**
 * Created by kkmike999 on 2017/06/05.
 */
public class KbSqlParserTest {

    @Test
    public void bindSelectArgs() throws Exception {
        Object[] args = new Object[]{1, 10, 20, "test"};
        String   sql  = "select * from person where id=? and (uid BETWEEN ? and ?) and test LIKE ? order by id";

        String boundSql = KbSqlParser.bindArgs(sql, args);

        System.out.println(boundSql);

        Assert.assertEquals("SELECT * FROM person WHERE id = 1 AND (uid BETWEEN 10 AND 20) AND test LIKE 'test' ORDER BY id", boundSql);
    }

    @Test
    public void bindInsertArgs() {
        Object[] args = new Object[]{1, "kk"};
        String   sql  = "INSERT INTO person (id, name) VALUES (?, ?)";

        String boundSql = KbSqlParser.bindArgs(sql, args);

        Assert.assertEquals("INSERT INTO person (id, name) VALUES (1, 'kk')", boundSql);
    }

    @Test
    public void bindDeleteArgs() {
        Object[] args = new Object[]{1};
        String   sql  = "DELETE FROM person WHERE id = ?";

        String boundSql = KbSqlParser.bindArgs(sql, args);

        Assert.assertEquals("DELETE FROM person WHERE id = 1", boundSql);
    }

    @Test
    public void bindUpdateArgs() {
        Object[] args = new Object[]{"kk", 1};
        String   sql  = "UPDATE person SET name = ? WHERE id = ?";

        String boundSql = KbSqlParser.bindArgs(sql, args);

        Assert.assertEquals("UPDATE person SET name = 'kk' WHERE id = 1", boundSql);
    }

    @Test
    public void findJdbcParamExpressions() throws Exception {
//        String sql = "select * from person where (id BETWEEN ? and ?) and test LIKE ? order by id";
//        String sql = "INSERT INTO person (id, name) VALUES (?, ?)";
//        String sql = "UPDATE person SET name = ? WHERE id = ?";
        String sql = "DELETE FROM person WHERE id = ?";

        Set<Expression> expressionSet = KbSqlParser.findBindArgsExpressions(sql);

        System.out.println();
    }
}