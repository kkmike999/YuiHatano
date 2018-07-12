package net.sf.jsqlparser.parser;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.insert.InsertOrReplaceProxy;

import java.io.StringReader;

/**
 * Created by kkmike999 on 2017/06/09.
 * <p>
 * sql解析工具
 */
public class KbSqlParserManager {

    public Statement parse(String sql) throws JSQLParserException {
        sql = sql.trim();

        boolean isInsertOrReplace = false;

        // 校验是否"INSERT OR REPLACE"语法
        if (sql.toUpperCase().startsWith("INSERT OR REPLACE")) {
            isInsertOrReplace = true;

            String[] sqlParts = new String[2];
            sqlParts[0] = sql.substring(0, "INSERT OR REPLACE".length());
            sqlParts[1] = sql.substring("INSERT OR REPLACE".length(), sql.length());

            sqlParts[0] = sqlParts[0].toUpperCase().replace("INSERT OR REPLACE", "INSERT");

            sql = sqlParts[0] + sqlParts[1];
        }

        CCJSqlParser parser = new CCJSqlParser(new StringReader(sql));
        try {
            Statement statement = parser.Statement();

            // 当"INSERT OR REPLACE"语法，生成Insert代理
            if (isInsertOrReplace && statement instanceof Insert) {
                statement = new InsertOrReplaceProxy().getInstance(Insert.class, (Insert) statement);
            }
            return statement;
        } catch (Exception ex) {
            throw new JSQLParserException(ex);
        }
    }
}
