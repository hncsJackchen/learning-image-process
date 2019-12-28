package com.jack.learning.imageprocess;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.jack.learning.imageprocess.util.ImageUtils;

/*
 * author: Jack
 * created time:2019/12/28 19:03
 * description: 主界面
 */
public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    private static final int REQUEST_SYSTEM_PIC = 1;
    private Button mLoadDefaultImg, mLoadImg, mHandleImg;
    private ImageView mIvImage;

    private Bitmap mBitmap;
    private ImageUtils mImageUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageUtils = new ImageUtils();

        // Example of a call to a native method
        TextView tv = findViewById(R.id.tv_main_test);
        tv.setText(mImageUtils.test());

        mIvImage = findViewById(R.id.iv_main_image);

        mLoadDefaultImg = findViewById(R.id.btn_main_load_default);
        mLoadDefaultImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadDefaultImg();
            }
        });
        mLoadImg = findViewById(R.id.btn_main_load);
        mLoadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadImg();
            }
        });
        mHandleImg = findViewById(R.id.btn_main_handle_img);
        mHandleImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleImg();
            }
        });
    }

    /**
     * 加载默认图片
     */
    private void loadDefaultImg() {
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.test);
        mIvImage.setImageBitmap(mBitmap);
    }

    /**
     * 加载图片
     */
    private void loadImg() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new
                    String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_SYSTEM_PIC);
        } else {
            //打开系统相册
            openAlbum();
        }
    }

    /**
     * 处理图片
     */
    private void handleImg() {
        if (mBitmap == null) {
            Toast.makeText(this, "请加载图片...", Toast.LENGTH_SHORT).show();
            return;
        }

        mImageUtils.gray(mBitmap);
        mIvImage.setImageBitmap(mBitmap);
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_SYSTEM_PIC);//打开系统相册
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SYSTEM_PIC && resultCode == RESULT_OK && null != data) {
            if (Build.VERSION.SDK_INT >= 19) {
                handleImageOnKitkat(data);
            } else {
                handleImageBeforeKitkat(data);
            }
        }
    }

    @TargetApi(19)
    private void handleImageOnKitkat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            //如果是document类型的uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content:" +
                        "//downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是File类型的uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath);//根据图片路径显示图片

    }

    private void handleImageBeforeKitkat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);

    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            mBitmap = bitmap;
            mIvImage.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

}
