package com.example.qjm3662.newproject.Tool;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.widget.EditText;
import android.widget.Toast;

import com.example.qjm3662.newproject.App;
import com.example.qjm3662.newproject.Data.User;
import com.example.qjm3662.newproject.Data.UserBase;
import com.example.qjm3662.newproject.myself.MyDialog;
import com.example.qjm3662.newproject.NetWorkOperator;
import com.example.qjm3662.newproject.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by qjm3662 on 2016/5/31 0031.
 */
public class Tool {

//    public static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
//    public static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
//    public static final int PHOTO_REQUEST_CUT = 3;// 结果
//
//    // 使用系统当前日期加以调整作为照片的名称
//    public static String getPhotoFileName() {
//        Date date = new Date(System.currentTimeMillis());
//        SimpleDateFormat dateFormat = new SimpleDateFormat(
//                "'IMG'_yyyyMMdd_HHmmss");
//        return dateFormat.format(date) + ".jpg";
//    }
//
//    public static void getCamera(Context context, File file) {
//        Intent cameraintent = new Intent(
//                MediaStore.ACTION_IMAGE_CAPTURE);
//        // 指定调用相机拍照后照片的储存路径
//        cameraintent.putExtra(MediaStore.EXTRA_OUTPUT,
//                Uri.fromFile(file));
//        ((Activity)context).startActivityForResult(cameraintent,
//                PHOTO_REQUEST_TAKEPHOTO);
//    }
//
//    public static void getAlbum(Context context) {
//        Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
//        getAlbum.setType("image/*");
//        System.out.println();
//        ((Activity)context).startActivityForResult(getAlbum, PHOTO_REQUEST_GALLERY);
//    }
//
//    /**
//     * 调用系统裁剪功能
//     *
//     * @param uri
//     */
//    public static void startPhotoZoom(Uri uri, Context context) {
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(uri, "image/*");
//        // crop为true是设置在开启的intent中设置显示的view可以剪裁
//        intent.putExtra("crop", "true");
//
//        // aspectX aspectY 是宽高的比例
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//
//        // outputX,outputY 是剪裁图片的宽高
//        intent.putExtra("outputX", 300);
//        intent.putExtra("outputY", 300);
//        intent.putExtra("return-data", true);
//        intent.putExtra("noFaceDetection", true);
//        System.out.println("22================");
//        ((Activity)context).startActivityForResult(intent, PHOTO_REQUEST_CUT);
//    }


    /**
     * 判断是否登陆
     * @param context
     * @return
     */
    public static boolean JudgeIsLongin(Context context){
        if(User.getInstance().getLoginToken() != null){
            return true;
        }else{
            Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    /**
     * 将User类的Json数据存到单例化的User对象中
     *
     * @param s
     * @param s2
     */
    //JsonObject to User
    public static void str_to_user(String s, String s2) {
        if (s != null) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONObject user_info = jsonObject.getJSONObject("user");
                User user = User.getInstance();
                user.setAvatar(user_info.getString(User.USER_AVATAR));
                user.setSign(user_info.getString(User.USER_SIGN));
                user.setUserName(user_info.getString(User.USER_USER_NAME));
                user.setSex(user_info.getInt(User.USER_SEX));

                JSONArray js_array_follower = jsonObject.getJSONArray("follower");
                JSONArray js_array_following = jsonObject.getJSONArray("following");
                Gson gson = new Gson();
                List<UserBase> list_user_base = new ArrayList<UserBase>();
                UserBase userBase = null;
                if(js_array_follower.length() != 0){
                    if(!js_array_follower.get(0).toString().equals("false")){
                        for(int i = 0; i < js_array_follower.length(); i++){
                            System.out.println(js_array_follower.get(i).toString() + "dscsadc");
                            userBase = gson.fromJson(js_array_follower.get(i).toString(),UserBase.class);
                            System.out.println("有人关注我 ： " + userBase.getId());
                            list_user_base.add(userBase);
                            App.Public_Care_Me.add(userBase);
                        }
                        if(list_user_base.size() != 0){
                            user.setFollower(list_user_base);
                        }
                    }
                }

                list_user_base.clear();

                if(js_array_following.length() != 0){
                    if(!js_array_following.get(0).toString().equals("false")){
                        for(int i = 0; i < js_array_following.length(); i++){
                            userBase = gson.fromJson(js_array_following.get(i).toString(),UserBase.class);
                            System.out.println("我在关注TA ： " + userBase.getId());
                            list_user_base.add(userBase);
                            App.Public_Care_Other.add(userBase);
                        }
                        if(list_user_base.size() != 0){
                            user.setFollowing(list_user_base);
                        }
                    }
                }
                user.setCollectedStoriesCount(jsonObject.getInt("collectedStoriesCount"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        if (s2 != null) {
            try {
                System.out.println(s2);
                JSONObject user_info = new JSONObject(s2);
                User user = User.getInstance();
                user.setId(user_info.getInt(User.USER_ID));
                user.setLoginToken(user_info.getString(User.USER_LOGIN_TOKEN));
                user.setToken(user_info.getString(User.USER_TOKEN));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 弹出编辑框
     *
     * @param context
     * @param content
     */
    public static void ShowDialog(final Context context, final String content, String title) {
        final MyDialog myDialog = new MyDialog(context, R.style.myDialogTheme, content, "编辑用户名", "请输入用户名",0);
        myDialog.show();
        myDialog.setClickListener(new MyDialog.ClickListenerInterface() {
            @Override
            public void doConfirm() {
                User.getInstance().setUserName(myDialog.getDialog_et().getText().toString());
                NetWorkOperator.UpDateUserInfo(context);
                myDialog.dismiss();
            }

            @Override
            public void doCancel() {
                myDialog.dismiss();
            }
        });
    }


    /**
     * 插入图片（图文混排）
     *
     * @param bitmap
     * @param path
     * @param context
     * @param tv
     */
    public static void insertPic(Bitmap bitmap, String path, Context context, EditText tv) {

        // 根据Bitmap对象创建ImageSpan对象
        ImageSpan imageSpan = new ImageSpan(context, bitmap);

        // 创建一个SpannableString对象，以便插入用ImageSpan对象封装的图像
        //System.out.println(Tool.getPath(this,uri));
        //<img alt="" src="1.jpg"></img>
        path = "<img" + path + "></img>";

        SpannableString spannableString = new SpannableString(path);

        // 用ImageSpan对象替换指定的字符串
        spannableString.setSpan(imageSpan, 0, path.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // 将选择的图片追加到EditText中光标所在位置
        int index = tv.getSelectionStart(); // 获取光标所在位置
        Editable edit_text = tv.getEditableText();
        if (index < 0 || index >= edit_text.length()) {
//            edit_text.append('\n');
            edit_text.append(spannableString);
//            edit_text.append('\n');
        } else {
            edit_text.insert(index, spannableString);
        }

        String s = tv.getText().toString();
        System.out.println(s.length());
    }


    /**
     * 重新设置图片的大小
     *
     * @param b
     * @param x
     * @param y
     * @return
     */
    public static Bitmap resize_bitmap(Bitmap b, float x, float y) {
        //b = compressImage(b);
        int w = b.getWidth();
        int h = b.getHeight();
        float sx = (float) x / w;//要强制转换，不转换我的在这总是死掉。
        float sy = (float) y / h;
        Matrix matrix = new Matrix();
        matrix.postScale(sx, sy); // 长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(b, 0, 0, w,
                h, matrix, true);
        return resizeBmp;
    }


//    public static Bitmap compressImage(Bitmap image) {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
//        int options = 100;
//        while ( baos.toByteArray().length / 1024>100) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩
//            baos.reset();//重置baos即清空baos
//            options -= 10;//每次都减少10
//            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
//
//        }
//        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
//        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
//        return bitmap;
//    }
//
//
//
//
//    /***
//     * 图片的缩放方法
//     *
//     * @param bitmap
//     *            ：源图片资源
//     * @param newWidth
//     *            ：缩放后宽度
//     * @param newHeight
//     *            ：缩放后高度
//     * @return
//     */
//    public static Bitmap zoomImage(Bitmap bitmap, double newWidth,
//                                   double newHeight) {
//
//        bitmap = compressImage(bitmap);
//        System.out.println("new" + newHeight + "  " + newWidth);
//        // 获取这个图片的宽和高
//        float width = bitmap.getWidth();
//        float height = bitmap.getHeight();
//        // 创建操作图片用的matrix对象
//        Matrix matrix = new Matrix();
//        // 计算宽高缩放率
//        float scaleWidth = ((float) newWidth) / width;
//        float scaleHeight = ((float) newHeight) / height;
//        // 缩放图片动作
//        matrix.postScale(scaleWidth, scaleHeight);
//        Bitmap bitmap1 = Bitmap.createBitmap(bitmap, 0, 0, (int) width,
//                (int) height, matrix, true);
//        return bitmap1;
//    }


    /**
     * 通过选择图片返回的字符串获取到图片所在的本地路径
     *
     * @param context
     * @param uri
     * @return
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
