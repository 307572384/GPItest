package com.beta.gpitest;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageGrayscaleFilter;


public class NetActivity  extends AppCompatActivity {


	private GPUImage gpuImage;
	//显示处理结果
	private ImageView resultIv;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.net_main);
		resultIv = (ImageView) findViewById(R.id.imagex);

		//开启异步线程加载图片并处理
		MyAsynTask asynTask = new MyAsynTask();
		asynTask.execute();

	}

	class MyAsynTask extends AsyncTask<Integer,Integer,Bitmap>{

		@Override
		protected Bitmap doInBackground(Integer... params) {
			//写入图片的url
			Bitmap bitmap = getGPUImageFromURL("https://img-blog.csdn.net/20180422104130848?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MTEwMTE3Mw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70");
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			// 使用GPUImage处理图像
			gpuImage = new GPUImage(getApplicationContext());
			gpuImage.setImage(bitmap);
			gpuImage.setFilter(new GPUImageGrayscaleFilter());
			bitmap = gpuImage.getBitmapWithFilterApplied();
			//显示处理后的图片存储到bitmap
			resultIv.setImageBitmap(bitmap);
		}
	}

	public static Bitmap getGPUImageFromURL(String url) {
		Bitmap bitmap = null;
		try {
			URL iconUrl = new URL(url);
			URLConnection conn = iconUrl.openConnection();
			HttpURLConnection http = (HttpURLConnection) conn;
			int length = http.getContentLength();
			conn.connect();
			// 获得图像的字符流
			InputStream is = conn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is, length);
			bitmap = BitmapFactory.decodeStream(bis);
			bis.close();
			is.close();// 关闭流
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}


}
