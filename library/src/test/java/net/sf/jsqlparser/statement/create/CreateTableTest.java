package net.sf.jsqlparser.statement.create;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.statement.Statement;

import org.junit.Test;

import java.io.StringReader;

/**
 * Created by kkmike999 on 2017/6/11.
 */
public class CreateTableTest {


    @Test
    public void testCreateTable() throws JSQLParserException {
        // 第一条语句，会报错。没有声明VARCHAR属性
        // String sql = "CREATE TABLE IF NOT EXISTS net_kb_test_bean_Bean ( \"id\" INTEGER PRIMARY KEY AUTOINCREMENT,\"uid\",\"name\" )";
        String sql = "CREATE TABLE IF NOT EXISTS net_kb_test_bean_Bean ( \"id\" INTEGER PRIMARY KEY AUTOINCREMENT,\"uid\" VARCHAR,\"name\" VARCHAR)";

        CCJSqlParser parser = new CCJSqlParser(new StringReader(sql));
        try {
            Statement statement = parser.Statement();
        } catch (Exception ex) {
            throw new JSQLParserException(ex);
        }
    }
}
