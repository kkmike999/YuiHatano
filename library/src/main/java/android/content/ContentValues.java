package android.content;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class ContentValues implements Parcelable {

    public static final String TAG = "ContentValues";

    private HashMap<String, Object> mValues;

    public static final Parcelable.Creator<ContentValues> CREATOR = new Parcelable.Creator() {

        public ContentValues createFromParcel(Parcel in) {
            HashMap values = in.readHashMap(null);
//            return new ContentValues(values, null);
            return new ContentValues(values);
        }

        public ContentValues[] newArray(int size) {
            return new ContentValues[size];
        }
    };

    public ContentValues() {
        this.mValues = new HashMap(8);
    }

    public ContentValues(int size) {
        this.mValues = new HashMap(size, 1.0F);
    }

    public ContentValues(ContentValues from) {
        this.mValues = new HashMap(ContentValuesHelper.getValues(from));
    }

    private ContentValues(HashMap<String, Object> values) {
        this.mValues = values;
    }

    public boolean equals(Object object) {
        if (!(object instanceof ContentValues)) {
            return false;
        }
        return this.mValues.equals(((ContentValues) object).mValues);
    }

    public int hashCode() {
        return this.mValues.hashCode();
    }

    public void put(String key, String value) {
        this.mValues.put(key, value);
    }

    public void putAll(ContentValues other) {
        this.mValues.putAll(ContentValuesHelper.getValues(other));
    }

    public void put(String key, Byte value) {
        this.mValues.put(key, value);
    }

    public void put(String key, Short value) {
        this.mValues.put(key, value);
    }

    public void put(String key, Integer value) {
        this.mValues.put(key, value);
    }

    public void put(String key, Long value) {
        this.mValues.put(key, value);
    }

    public void put(String key, Float value) {
        this.mValues.put(key, value);
    }

    public void put(String key, Double value) {
        this.mValues.put(key, value);
    }

    public void put(String key, Boolean value) {
        this.mValues.put(key, value);
    }

    public void put(String key, byte[] value) {
        this.mValues.put(key, value);
    }

    public void putNull(String key) {
        this.mValues.put(key, null);
    }

    public int size() {
        return this.mValues.size();
    }

    public void remove(String key) {
        this.mValues.remove(key);
    }

    public void clear() {
        this.mValues.clear();
    }

    public boolean containsKey(String key) {
        return this.mValues.containsKey(key);
    }

    public Object get(String key) {
        return this.mValues.get(key);
    }

    public String getAsString(String key) {
        Object value = this.mValues.get(key);
        return value != null ? value.toString() : null;
    }

    public Long getAsLong(String key) {
        Object value = this.mValues.get(key);
        try {
            return value != null ? Long.valueOf(((Number) value).longValue()) : null;
        } catch (ClassCastException e) {
            if ((value instanceof CharSequence)) {
                try {
                    return Long.valueOf(value.toString());
                } catch (NumberFormatException e2) {
                    Log.e("ContentValues", new StringBuilder().append("Cannot parse Long value for ").append(value).append(" at key ").append(key).toString());
                    return null;
                }
            }
            Log.e("ContentValues", new StringBuilder().append("Cannot cast value for ").append(key).append(" to a Long: ").append(value).toString(), e);
        }
        return null;
    }

    public Integer getAsInteger(String key) {
        Object value = this.mValues.get(key);
        try {
            return value != null ? Integer.valueOf(((Number) value).intValue()) : null;
        } catch (ClassCastException e) {
            if ((value instanceof CharSequence)) {
                try {
                    return Integer.valueOf(value.toString());
                } catch (NumberFormatException e2) {
                    Log.e("ContentValues", new StringBuilder().append("Cannot parse Integer value for ").append(value).append(" at key ").append(key).toString());
                    return null;
                }
            }
            Log.e("ContentValues", new StringBuilder().append("Cannot cast value for ").append(key).append(" to a Integer: ").append(value).toString(), e);
        }
        return null;
    }

    public Short getAsShort(String key) {
        Object value = this.mValues.get(key);
        try {
            return value != null ? Short.valueOf(((Number) value).shortValue()) : null;
        } catch (ClassCastException e) {
            if ((value instanceof CharSequence)) {
                try {
                    return Short.valueOf(value.toString());
                } catch (NumberFormatException e2) {
                    Log.e("ContentValues", new StringBuilder().append("Cannot parse Short value for ").append(value).append(" at key ").append(key).toString());
                    return null;
                }
            }
            Log.e("ContentValues", new StringBuilder().append("Cannot cast value for ").append(key).append(" to a Short: ").append(value).toString(), e);
        }
        return null;
    }

    public Byte getAsByte(String key) {
        Object value = this.mValues.get(key);
        try {
            return value != null ? Byte.valueOf(((Number) value).byteValue()) : null;
        } catch (ClassCastException e) {
            if ((value instanceof CharSequence)) {
                try {
                    return Byte.valueOf(value.toString());
                } catch (NumberFormatException e2) {
                    Log.e("ContentValues", new StringBuilder().append("Cannot parse Byte value for ").append(value).append(" at key ").append(key).toString());
                    return null;
                }
            }
            Log.e("ContentValues", new StringBuilder().append("Cannot cast value for ").append(key).append(" to a Byte: ").append(value).toString(), e);
        }
        return null;
    }

    public Double getAsDouble(String key) {
        Object value = this.mValues.get(key);
        try {
            return value != null ? Double.valueOf(((Number) value).doubleValue()) : null;
        } catch (ClassCastException e) {
            if ((value instanceof CharSequence)) {
                try {
                    return Double.valueOf(value.toString());
                } catch (NumberFormatException e2) {
                    Log.e("ContentValues", new StringBuilder().append("Cannot parse Double value for ").append(value).append(" at key ").append(key).toString());
                    return null;
                }
            }
            Log.e("ContentValues", new StringBuilder().append("Cannot cast value for ").append(key).append(" to a Double: ").append(value).toString(), e);
        }
        return null;
    }

    public Float getAsFloat(String key) {
        Object value = this.mValues.get(key);
        try {
            return value != null ? Float.valueOf(((Number) value).floatValue()) : null;
        } catch (ClassCastException e) {
            if ((value instanceof CharSequence)) {
                try {
                    return Float.valueOf(value.toString());
                } catch (NumberFormatException e2) {
                    Log.e("ContentValues", new StringBuilder().append("Cannot parse Float value for ").append(value).append(" at key ").append(key).toString());
                    return null;
                }
            }
            Log.e("ContentValues", new StringBuilder().append("Cannot cast value for ").append(key).append(" to a Float: ").append(value).toString(), e);
        }
        return null;
    }

    public Boolean getAsBoolean(String key) {
        Object value = this.mValues.get(key);
        try {
            return (Boolean) value;
        } catch (ClassCastException e) {
            if ((value instanceof CharSequence))
                return Boolean.valueOf(value.toString());
            if ((value instanceof Number)) {
                return Boolean.valueOf(((Number) value).intValue() != 0);
            }
            Log.e("ContentValues", new StringBuilder().append("Cannot cast value for ").append(key).append(" to a Boolean: ").append(value).toString(), e);
        }
        return null;
    }

    public byte[] getAsByteArray(String key) {
        Object value = this.mValues.get(key);
        if ((value instanceof byte[])) {
            return (byte[]) value;
        }
        return null;
    }

    public Set<Map.Entry<String, Object>> valueSet() {
        return this.mValues.entrySet();
    }

    public Set<String> keySet() {
        return this.mValues.keySet();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeMap(this.mValues);
    }

    @Deprecated
    public void putStringArrayList(String key, ArrayList<String> value) {
        this.mValues.put(key, value);
    }

    @Deprecated
    public ArrayList<String> getStringArrayList(String key) {
        return (ArrayList) this.mValues.get(key);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String name : this.mValues.keySet()) {
            String value = getAsString(name);
            if (sb.length() > 0) sb.append(" ");
            sb.append(new StringBuilder().append(name).append("=").append(value).toString());
        }
        return sb.toString();
    }
}