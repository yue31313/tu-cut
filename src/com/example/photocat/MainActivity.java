package com.example.photocat;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends Activity implements OnClickListener {
    private ImageButton img_btn;
    private Button btn;
    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// ����
    private static final int PHOTO_REQUEST_GALLERY = 2;// �������ѡ��
    private static final int PHOTO_REQUEST_CUT = 3;// ���
    // ����һ���Ե�ǰʱ��Ϊ���Ƶ��ļ�
    File tempFile = new File(Environment.getExternalStorageDirectory(),getPhotoFileName());

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    //��ʼ���ؼ�
    private void init() {
        img_btn = (ImageButton) findViewById(R.id.img_btn);
        btn = (Button) findViewById(R.id.btn);
        
        //ΪImageButton��Button��Ӽ����¼�
        img_btn.setOnClickListener(this);
        btn.setOnClickListener(this);
    }

    //����¼�
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
        case R.id.img_btn:
            showDialog();
            break;

        case R.id.btn:
            showDialog();
            break;
        }

    }

    
    //��ʾ�Ի��򷽷�
    private void showDialog() {
        new AlertDialog.Builder(this)
                .setTitle("ͷ������")
                .setPositiveButton("����", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                        // ����ϵͳ�����չ���
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        // ָ������������պ���Ƭ�Ĵ���·��
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(tempFile));
                        startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
                    }
                })
                .setNegativeButton("���", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                        Intent intent = new Intent(Intent.ACTION_PICK, null);
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
                    }
                }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        switch (requestCode) {
        case PHOTO_REQUEST_TAKEPHOTO:
            startPhotoZoom(Uri.fromFile(tempFile), 150);
            break;

        case PHOTO_REQUEST_GALLERY:
            if (data != null)
                startPhotoZoom(data.getData(), 150);
            break;

        case PHOTO_REQUEST_CUT:
            if (data != null) 
                setPicToView(data);
            break;
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void startPhotoZoom(Uri uri, int size) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // cropΪtrue�������ڿ�����intent��������ʾ��view���Լ���
        intent.putExtra("crop", "true");

        // aspectX aspectY �ǿ�ߵı���
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY �Ǽ���ͼƬ�Ŀ��
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    //�����м��ú��ͼƬ��ʾ��UI������
    private void setPicToView(Intent picdata) {
        Bundle bundle = picdata.getExtras();
        if (bundle != null) {
            Bitmap photo = bundle.getParcelable("data");
            Drawable drawable = new BitmapDrawable(photo);
            img_btn.setBackgroundDrawable(drawable);
            btn.setBackground(drawable);
        }
    }

    // ʹ��ϵͳ��ǰ���ڼ��Ե�����Ϊ��Ƭ������
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }
}