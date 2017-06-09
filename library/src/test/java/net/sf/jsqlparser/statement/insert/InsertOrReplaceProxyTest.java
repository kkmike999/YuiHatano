package net.sf.jsqlparser.statement.insert;

import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.schema.Table;

import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

/**
 * Created by kkmike999 on 2017/06/09.
 */
public class InsertOrReplaceProxyTest {

    @Test
    public void intercept() throws Exception {
        String sql = "INSERT INTO person (id, name) VALUES (1, 'KK')";

        CCJSqlParserManager pm     = new CCJSqlParserManager();
        Insert              insert = (Insert) pm.parse(new StringReader(sql));

        Insert insertProxy = new InsertOrReplaceProxy().getInstance(Insert.class, insert);

        Table table = insertProxy.getTable();

        Assert.assertEquals("person", table.getName());

        String string = insertProxy.toString();

        Assert.assertEquals("INSERT OR REPLACE INTO person (id, name) VALUES (1, 'KK')", string);
    }

}