package android.database;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kkmike999 on 2017/06/05.
 */
public class ShadowCursor implements Cursor {

    // column name列表，必须按原来的顺序
    List<String> mColums = new ArrayList<>();

    // query结果集合
    List<List<Object>> mDatas = new ArrayList<>();

    int mPosition = -1;

    public ShadowCursor(List<String> colums, List<List<Object>> datas) {
        this.mColums = new ArrayList<>(colums);
        this.mDatas = new ArrayList<>(datas);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public int getPosition() {
        return mPosition;
    }

    @Override
    public boolean move(int offset) {
        return false;
    }

    @Override
    public boolean moveToPosition(int position) {
        mPosition = position;
        return mPosition >= 0 && mPosition < mDatas.size();
    }

    @Override
    public boolean moveToFirst() {
        mPosition = 0;
        return true;
    }

    @Override
    public boolean moveToLast() {
        mPosition = mDatas.size() - 1;
        return true;
    }

    @Override
    public boolean moveToNext() {
        mPosition++;
        return mPosition < mDatas.size();
    }

    @Override
    public boolean moveToPrevious() {
        mPosition--;
        return mPosition >= 0 && mPosition < mDatas.size();
    }

    @Override
    public boolean isFirst() {
        return mPosition == 0;
    }

    @Override
    public boolean isLast() {
        return mPosition == mDatas.size() - 1;
    }

    @Override
    public boolean isBeforeFirst() {
        return mPosition == -1;
    }

    @Override
    public boolean isAfterLast() {
        return mPosition > mDatas.size() + 1;
    }

    @Override
    public int getColumnIndex(String columnName) {
        return mColums.indexOf(columnName);
    }

    @Override
    public int getColumnIndexOrThrow(String columnName) throws IllegalArgumentException {
        if (!mColums.contains(columnName)) {
            throw new IllegalArgumentException("the column \'" + columnName + "\' does not exist");
        }
        return getColumnIndex(columnName);
    }

    @Override
    public String getColumnName(int columnIndex) {
        return mColums.get(columnIndex);
    }

    @Override
    public String[] getColumnNames() {
        return mColums.toArray(new String[0]);
    }

    @Override
    public int getColumnCount() {
        return mColums.size();
    }

    @Override
    public byte[] getBlob(int columnIndex) {
        return new byte[0];
    }

    @Override
    public String getString(int columnIndex) {
        return getObject(columnIndex).toString();
    }

    @Override
    public void copyStringToBuffer(int columnIndex, CharArrayBuffer buffer) {

    }

    @Override
    public short getShort(int columnIndex) {
        return Short.valueOf(getObject(columnIndex).toString());
    }

    @Override
    public int getInt(int columnIndex) {
        return Integer.valueOf(getObject(columnIndex).toString());
    }

    @Override
    public long getLong(int columnIndex) {
        return Long.valueOf(getObject(columnIndex).toString());
    }

    @Override
    public float getFloat(int columnIndex) {
        return Float.valueOf(getObject(columnIndex).toString());
    }

    @Override
    public double getDouble(int columnIndex) {
        return Double.valueOf(getObject(columnIndex).toString());
    }

    @Override
    public int getType(int columnIndex) {
        Object value = getObject(columnIndex);

        if (value instanceof Integer || value instanceof Long) {
            return FIELD_TYPE_INTEGER;
        } else if (value instanceof Float || value instanceof Double) {
            return FIELD_TYPE_FLOAT;
        } else if (value instanceof byte[]) {
            return FIELD_TYPE_BLOB;
        } else if (value == null) {
            return FIELD_TYPE_NULL;
        }
        return FIELD_TYPE_STRING;
    }

    private Object getObject(int columnIndex) {
        return mDatas.get(mPosition).get(columnIndex);
    }

    @Override
    public boolean isNull(int columnIndex) {
        return getObject(columnIndex) == null;
    }

    @Override
    public void deactivate() {

    }

    @Override
    public boolean requery() {
        return false;
    }

    @Override
    public void close() {

    }

    @Override
    public boolean isClosed() {
        return false;
    }

    @Override
    public void registerContentObserver(ContentObserver observer) {

    }

    @Override
    public void unregisterContentObserver(ContentObserver observer) {

    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void setNotificationUri(ContentResolver cr, Uri uri) {

    }

    @Override
    public Uri getNotificationUri() {
        return null;
    }

    @Override
    public boolean getWantsAllOnMoveCalls() {
        return false;
    }

    @Override
    public void setExtras(Bundle extras) {

    }

    @Override
    public Bundle getExtras() {
        return null;
    }

    @Override
    public Bundle respond(Bundle extras) {
        return null;
    }
}
