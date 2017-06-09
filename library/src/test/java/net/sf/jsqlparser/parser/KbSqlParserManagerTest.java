package net.sf.jsqlparser.parser;

import net.sf.jsqlparser.statement.insert.Insert;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by kkmike999 on 2017/06/09.
 */
public class KbSqlParserManagerTest {
    @Test
    public void parse() throws Exception {
        String sql = "INSERT or replace INTO person (id, name) VALUES (?, ?)";

        Insert insert = (Insert) new KbSqlParserManager().parse(sql);

        Assert.assertEquals("INSERT OR REPLACE INTO person (id, name) VALUES (?, ?)", insert.toString());
    }

}