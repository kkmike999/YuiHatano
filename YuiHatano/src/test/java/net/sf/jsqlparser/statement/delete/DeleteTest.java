package net.sf.jsqlparser.statement.delete;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.KbSqlParserManager;
import net.sf.jsqlparser.statement.Statement;

import org.junit.Test;

/**
 * Created by kkmike999 on 2017/6/11.
 */
public class DeleteTest {

    @Test
    public void deleteFrom() throws JSQLParserException {
        // 第一条语句报错
//        String sql = "DELETE FROM 'USER'";
//        String sql = "DELETE FROM USER";
        String sql = "DELETE FROM \"USER\"";

        KbSqlParserManager pm = new KbSqlParserManager();

        Statement statement = pm.parse(sql);
    }
}
