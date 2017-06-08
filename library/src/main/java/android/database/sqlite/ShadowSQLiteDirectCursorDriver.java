package android.database.sqlite;

import android.database.Cursor;
import android.database.ShadowCursor;
import android.os.CancellationSignal;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public final class ShadowSQLiteDirectCursorDriver {
    private final ShadowSQLiteDatabase mDatabase;
    private final String               mEditTable;
    private final String               mSql;
    private final CancellationSignal   mCancellationSignal;
    private       ShadowSQLiteQuery    mQuery;

    public ShadowSQLiteDirectCursorDriver(ShadowSQLiteDatabase db, String sql, String editTable, CancellationSignal cancellationSignal) {
        mDatabase = db;
        mEditTable = editTable;
        mSql = sql;
        mCancellationSignal = cancellationSignal;
    }

    public Cursor query(ShadowSQLiteDatabase.CursorFactory factory, String[] selectionArgs) {
//        ShadowSQLiteQuery query  = new ShadowSQLiteQuery(mDatabase, mSql, selectionArgs, mCancellationSignal);
        try {
            String querySql = KbSqlParser.bindArgs(mSql, selectionArgs);

            mDatabase.debug(querySql);

            Connection connection = mDatabase.getConnection();
            Statement  statement  = connection.createStatement();

            ResultSet         rs          = statement.executeQuery(querySql);
            ResultSetMetaData metaData    = rs.getMetaData();
            int               columnCount = metaData.getColumnCount();
            List<String>      columns     = new ArrayList<>();

            // column name数组, colum从1开始
            for (int i = 1; i < columnCount + 1; i++) {
                String name = metaData.getColumnName(i);

                columns.add(name);
            }

            // 结果集合
            List<List<Object>> datas = new ArrayList<>();

            while (rs.next()) {
                List<Object> data = new ArrayList<>();

                // ResultSet colum从1开始
                for (int i = 1; i < columnCount + 1; i++) {
                    Object value = rs.getObject(i);

                    data.add(value);
                }

                datas.add(data);
            }

            rs.close();
            statement.close();

            ShadowCursor shadowCursor = new ShadowCursor(columns, datas);

            return shadowCursor;
        } catch (RuntimeException ex) {
            throw ex;
        } catch (java.sql.SQLException e) {
            throw new android.database.SQLException(e.getMessage());
        }

//        mQuery = query;
    }

    public void cursorClosed() {
        // Do nothing
    }

    public void setBindArguments(String[] bindArgs) {
//        mQuery.bindAllArgsAsStrings(bindArgs);
    }

    public void cursorDeactivated() {
        // Do nothing
    }

    public void cursorRequeried(Cursor cursor) {
        // Do nothing
    }

    @Override
    public String toString() {
        return "ShadowSQLiteDirectCursorDriver: " + mSql;
    }
}