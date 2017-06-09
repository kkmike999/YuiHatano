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
import java.sql.Statement;

/**
 * Represents a statement that can be executed against a database.  The statement
 * cannot return multiple rows or columns, but single value (1 x 1) result sets
 * are supported.
 * <p>
 * This class is not thread-safe.
 * </p>
 */
public final class ShadowSQLiteStatement extends ShadowSQLiteProgram {
    ShadowSQLiteStatement(ShadowSQLiteDatabase db, String sql, Object[] bindArgs) {
        super(db, sql, bindArgs, null);
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

            getDatabase().execSQL(getSql(), getBindArgs());
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
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public int executeUpdateDelete() throws JSQLParserException {
        acquireReference();
        try {
//            return getSession().executeForChangedRowCount(
//                    getSql(), getBindArgs(), getConnectionFlags(), null);

            String                                sql        = getSql();
            Object[]                              bindArgs   = getBindArgs();
            KbSqlParserManager                    sqlManager = new KbSqlParserManager();
            net.sf.jsqlparser.statement.Statement stm        = sqlManager.parse(sql);

            try {
                sql = KbSqlParser.bindArgs(sql, bindArgs);

                int rows;

                Connection mConnection = getDatabase().getConnection();
                Statement  statement   = mConnection.createStatement();

                if (stm instanceof Update) {
                    rows = statement.executeUpdate(sql);

                    if (!getDatabase().isTransaction) {
                        mConnection.commit();
                    }
                } else {
                    String table = ((Delete) stm).getTable().getName();

                    long beforeCount = getDatabase().getRows(table);

                    statement.execute(getSql());

                    if (!getDatabase().isTransaction) {
                        mConnection.commit();
                    }

                    long afterCount = getDatabase().getRows(table);

                    rows = (int) (afterCount - beforeCount);
                }

                statement.close();

                return rows;
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
            getDatabase().execSQL(getSql(), getBindArgs());

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

            Connection connection = getDatabase().getConnection();
            Statement  statement  = connection.createStatement();
            ResultSet  rs         = statement.executeQuery(sql);

//            int columnIndex  = rs.findColumn("user_version");
//            int user_version = rs.getInt(columnIndex);

            long first = rs.getLong(1);

            rs.close();
            statement.close();

            return first;

//            return getSession().executeForLong(
//                    getSql(), getBindArgs(), getConnectionFlags(), null);
        } catch (SQLiteDatabaseCorruptException ex) {
            onCorruption();
            throw ex;
        } catch (java.sql.SQLException e) {
            throw new android.database.SQLException(e.getMessage());
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
            return getSession().executeForString(
                    getSql(), getBindArgs(), getConnectionFlags(), null);
        } catch (SQLiteDatabaseCorruptException ex) {
            onCorruption();
            throw ex;
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
            return getSession().executeForBlobFileDescriptor(
                    getSql(), getBindArgs(), getConnectionFlags(), null);
        } catch (SQLiteDatabaseCorruptException ex) {
            onCorruption();
            throw ex;
        } finally {
            releaseReference();
        }
    }

    @Override
    public String toString() {
        return "SQLiteProgram: " + getSql();
    }
}
