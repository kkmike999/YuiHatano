//package android.os;
//
//import android.support.annotation.RequiresApi;
//
///**
// * Created by kkmike999 on 2018/07/13.
// */
//@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//public class Bundle extends BaseBundle implements Cloneable, Parcelable{
//
//    public Bundle() {
//    }
//
//    public Bundle(ClassLoader loader) {
//    }
//
//    public Bundle(int capacity) {
//    }
//
//    public Bundle(Bundle b) {
//    }
//
//    public Bundle(PersistableBundle b) {
//    }
//
//    protected Bundle(Parcel in) {
//    }
//
//    public static final Creator<Bundle> CREATOR = new Creator<Bundle>() {
//        @Override
//        public Bundle createFromParcel(Parcel in) {
//            return new Bundle(in);
//        }
//
//        @Override
//        public Bundle[] newArray(int size) {
//            return new Bundle[size];
//        }
//    };
//
//    @Override
//    public String toString() {
//        return "Bundle{}";
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel parcel, int i) {}
//}
