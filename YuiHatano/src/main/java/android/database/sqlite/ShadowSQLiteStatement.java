/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.database.sqlite;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.ParcelFileDescriptor;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.KbSqlParserManager;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.update.Update;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Represents a statement that can be executed against a database.  The statement
 * cannot return multiple rows or columns, but single value (1 x 1) result sets
 * are supported.
 * <p>
 * This class is not thread-safe.
 * </p>
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public final class ShadowSQLiteStatement extends ShadowSQLiteProgram {

    public static boolean DEBUG        = true;
    public static boolean DEBUG_PRAGMA = false;

    Connection mConnection;
    Statement  mStatement;

    ShadowSQLiteStatement(ShadowSQLiteDatabase db, String sql, Object[] bindArgs) {
        super(db, sql, bindArgs, null);

        mConnection = db.getConnection();
        try {
            mStatement = mConnection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Execute this SQL statement, if it is not a SELECT / INSERT / DELETE / UPDATE, for example
     * CREATE / DROP table, view, trigger, index etc.
     *
     * @throws android.database.SQLException If the SQL string is invalid for
     *                                       some reason
     */
    public void execute() {
        acquireReference();
        try {
            //            getSession().execute(getSql(), getBindArgs(), getConnectionFlags(), null);
            //            getDatabase().execSQL(getSql(), getBindArgs());

            String   sql      = getSql();
            Object[] bindArgs = getBindArgs();

            String afterSql = (bindArgs == null || bindArgs.length == 0) ? sql : KbSqlBuilder.bindArgs(sql, bindArgs);

            debug(afterSql);

            try {
                mStatement.execute(afterSql);

                if (!getDatabase().isTransaction) {
                    mConnection.commit();
                }
            } catch (java.sql.SQLException e) {
                throw new android.database.SQLException("", e);
            }
        } catch (SQLiteDatabaseCorruptException ex) {
            onCorruption();
            throw ex;
        } finally {
            releaseReference();
        }
    }

    /**
     * Execute this SQL statement, if the the number of rows affected by execution of this SQL
     * statement is of any importance to the caller - for example, UPDATE / DELETE SQL statements.
     *
     * @return the number of rows affected by this SQL statement execution.
     * @throws android.database.SQLException If the SQL string is invalid for
     *                                       some reason
     */
    public int executeUpdateDelete() {
        acquireReference();
        try {
            String   sql      = getSql();
            Object[] bindArgs = getBindArgs();

            // 以下语句报错：
            // CREATE TABLE IF NOT EXISTS net_kb_test_bean_Bean ( "id"    INTEGER PRIMARY KEY AUTOINCREMENT,"uid","name" )
            // 正确：
            // CREATE TABLE IF NOT EXISTS net_kb_test_bean_Bean ( "id"    INTEGER PRIMARY KEY AUTOINCREMENT,"uid" VARCHAR,"name" VARCHAR)

            // 特殊语句处理:
            if (sql.trim().toUpperCase().startsWith("CREATE TABLE")) {
                debug(sql);

                try {
                    mStatement.execute(sql);

                    if (!getDatabase().isTransaction) {
                        mConnection.commit();
                    }
                    return 0;
                } catch (SQLException e) {
                    throw new android.database.SQLException("", e);
                }
            }

            // 防止"DELETE FROM 'USER'"
            if (sql.trim().toUpperCase().startsWith("DELETE FROM")) {
                StringBuilder sb = new StringBuilder(sql);

                boolean hasWhere = sql.toUpperCase().contains("WHERE");

                if (!hasWhere) {
                    // 没有where才执行替换
                    int len = (hasWhere ? sql.toUpperCase().indexOf("WHERE") : sb.length());

                    int quotesCount = 0;

                    for (int i = "DELETE FROM".length(); i < len; i++) {
                        if (sb.charAt(i) == '\'') {
                            if (quotesCount == 0) {
                                quotesCount++;
                                sb.setCharAt(i, '\"');
                                continue;
                            } else {
                                quotesCount--;
                                sb.setCharAt(i, '\"');
                                break;
                            }
                        }
                    }

                    sql = sb.toString();
                }
            }

            try {
                KbSqlParserManager                    sqlManager = new KbSqlParserManager();
                net.sf.jsqlparser.statement.Statement stm        = sqlManager.parse(sql);

                // Android SQLite会将类型为NUMERIC转成INTEGER https://www.runoob.com/sqlite/sqlite-data-types.html
                sql = KbSqlParser.bindArgs(sql, bindArgs);

                debug(sql);

                int rows;

                if (stm instanceof Update) {
                    rows = mStatement.executeUpdate(sql);

                    if (!getDatabase().isTransaction) {
                        mConnection.commit();
                    }
                } else if (stm instanceof Delete) {
                    // delete
                    String table = ((Delete) stm).getTable().getName();

                    long beforeCount = getDatabase().getRows(table);

                    mStatement.execute(sql);

                    if (!getDatabase().isTransaction) {
                        mConnection.commit();
                    }

                    long afterCount = getDatabase().getRows(table);

                    rows = (int) (afterCount - beforeCount);
                } else {
                    // TODO: 2017/6/11 不知道是否正确
                    rows = mStatement.executeUpdate(sql);

                    if (!getDatabase().isTransaction) {
                        mConnection.commit();
                    }
                }

                return rows;
            } catch (java.sql.SQLException e) {
                debug(sql);

                throw new android.database.SQLException("", e);
            }
        } catch (SQLiteDatabaseCorruptException ex) {
            onCorruption();
            throw ex;
        } catch (JSQLParserException e) {
            throw new RuntimeException(e);
        } finally {
            releaseReference();
        }
    }

    /**
     * Execute this SQL statement and return the ID of the row inserted due to this call.
     * The SQL statement should be an INSERT for this to be a useful call.
     *
     * @return the row ID of the last row inserted, if this insert is successful. -1 otherwise.
     * @throws android.database.SQLException If the SQL string is invalid for
     *                                       some reason
     */
    public long executeInsert() {
        acquireReference();
        try {
            //            getDatabase().execSQL(getSql(), getBindArgs());

            execute();

            return 0;
            //            return getSession().executeForLastInsertedRowId(getSql(), getBindArgs(), getConnectionFlags(), null);
        } catch (SQLiteDatabaseCorruptException ex) {
            onCorruption();
            throw ex;
        } finally {
            releaseReference();
        }
    }

    /**
     * Execute a statement that returns a 1 by 1 table with a numeric value.
     * For example, SELECT COUNT(*) FROM table;
     * <p>
     * 貌似是查询第一行第一个元素
     *
     * @return The result of the query.
     * @throws SQLiteDoneException if the query returns zero rows
     */
    public long simpleQueryForLong() {
        acquireReference();
        try {
            String   sql  = getSql();
            Object[] args = getBindArgs();

            sql = KbSqlParser.bindArgs(sql, args);

            debug(sql);

            ResultSet rs = mStatement.executeQuery(sql);

            long first = rs.getLong(1);

            rs.close();

            return first;

            //            return getSession().executeForLong(
            //                    getSql(), getBindArgs(), getConnectionFlags(), null);
        } catch (SQLiteDatabaseCorruptException ex) {
            onCorruption();
            throw ex;
        } catch (java.sql.SQLException e) {
            throw new android.database.SQLException("", e);
        } finally {
            releaseReference();
        }
    }

    /**
     * Execute a statement that returns a 1 by 1 table with a text value.
     * For example, SELECT COUNT(*) FROM table;
     *
     * @return The result of the query.
     * @throws SQLiteDoneException if the query returns zero rows
     */
    public String simpleQueryForString() {
        acquireReference();
        try {
            //            return getSession().executeForString(getSql(), getBindArgs(), getConnectionFlags(), null);

            String   sql  = getSql();
            Object[] args = getBindArgs();

            sql = KbSqlParser.bindArgs(sql, args);

            debug(sql);

            ResultSet rs = mStatement.executeQuery(sql);

            String first = rs.getString(1);

            rs.close();

            return first;
        } catch (SQLiteDatabaseCorruptException ex) {
            onCorruption();
            throw ex;
        } catch (SQLException e) {
            throw new android.database.SQLException("", e);
        } finally {
            releaseReference();
        }
    }

    /**
     * Executes a statement that returns a 1 by 1 table with a blob value.
     *
     * @return A read-only file descriptor for a copy of the blob value, or {@code null}
     * if the value is null or could not be read for some reason.
     * @throws SQLiteDoneException if the query returns zero rows
     */
    public ParcelFileDescriptor simpleQueryForBlobFileDescriptor() {
        acquireReference();
        try {
            return getSession().executeForBlobFileDescriptor(getSql(), getBindArgs(), getConnectionFlags(), null);
        } catch (SQLiteDatabaseCorruptException ex) {
            onCorruption();
            throw ex;
        } finally {
            releaseReference();
        }
    }

    protected void debug(String sql) {
        if (DEBUG) {
            if (sql.startsWith("PRAGMA")) {
                if (DEBUG_PRAGMA) {
                    System.out.println(sql);
                }
                return;
            }

            System.out.println(sql);
        }
    }

    @Override
    public void close() {
        super.close();

        try {
            mStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "SQLiteProgram: " + getSql();
    }
}
