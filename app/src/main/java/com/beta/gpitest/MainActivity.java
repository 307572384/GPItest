package com.beta.gpitest;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import junit.framework.Assert;

import java.io.IOException;
import java.io.InputStream;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageBulgeDistortionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGrayscaleFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSaturationFilter;

public class MainActivity extends AppCompatActivity {
	private GPUImage gpuImage;
	//显示处理结果
	private ImageView resultIv;
	//进度条
	private SeekBar seekBar;
	private Button bt_net;//点击跳转到图片网络滤镜转换

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		resultIv = (ImageView)findViewById(R.id.resultIv);
		bt_net=(Button)findViewById(R.id.button) ;
		seekBar = (SeekBar)this.findViewById(R.id.seekbar);
		seekBar.setMax(10);
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				//通过进度条的值更改饱和度
				resultIv.setImageBitmap(getGPUImageFromAssets(progress));
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}
		});
		//初始化图片
		resultIv.setImageBitmap(getGPUImageFromAssets(0));
		bt_net.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,NetActivity.class);
				startActivity(intent);

			}
		});
	}

	//根据传进来的数值设置素材饱和度
	public Bitmap getGPUImageFromAssets(int progress)
	{
		//获得Assets资源文件
		AssetManager as = getAssets();
		InputStream is = null;
		Bitmap bitmap = null;
		try{
			is = as.open("link.jpg");
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		}catch (IOException e)
		{
			Log.e("GPUImage","Error");
		}
		gpuImage = new GPUImage(this);
		gpuImage.setImage(bitmap);
		gpuImage.setFilter(new GPUImageBulgeDistortionFilter());
		bitmap = gpuImage.getBitmapWithFilterApplied();
		//显示处理图片后的照片
		return bitmap;
	}

}
