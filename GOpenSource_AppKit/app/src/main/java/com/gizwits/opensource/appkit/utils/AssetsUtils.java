package com.gizwits.opensource.appkit.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.gizwits.opensource.appkit.CommonModule.GosDeploy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

public class AssetsUtils {

	public static void assetsDataToSD(String fileOutPutName,
			String fileInPutName, Context context) throws IOException {
		InputStream myInput;
		File file = new File(fileOutPutName);
		if (!file.exists()) {
		    file.createNewFile();
		   }else {
			return;
		}
		OutputStream myOutput = new FileOutputStream(fileOutPutName);
		myInput = context.getAssets().open(fileInPutName);
		byte[] buffer = new byte[1024];
		int length = myInput.read(buffer);
		while (length > 0) {
			myOutput.write(buffer, 0, length);
			length = myInput.read(buffer);
		}

		myOutput.flush();
		myInput.close();
		myOutput.close();
	}
	
	
	
	public static void saveFile(String str) {
		String filePath = null;
		
		
		filePath = GosDeploy.fileOutName;
		try {
			if(filePath!=null){
				File file = new File(filePath);
				if (!file.exists()) {
					File dir = new File(file.getParent());
					dir.mkdirs();
					file.createNewFile();
				}
				FileOutputStream outStream = new FileOutputStream(file);
				outStream.write(str.getBytes());
				outStream.close();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
}
	/**
	 * 将dip或dp值转换为px值，保证尺寸大小不变
	 *
	 * @param dipValue
	 * @param scale
	 *            （DisplayMetrics类中属性density）
	 * @return
	 */
	public static int diptopx(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}


	/**
	 * 将sp值转换为px值，保证文字大小不变
	 *
	 * @param spValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int sptopx(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	public  static boolean isZh(Context context) {
		Locale locale = context.getResources().getConfiguration().locale;
		String language = locale.getLanguage();
		if (language.endsWith("zh"))
			return true;
		else
			return false;
	}

	public static int getScreenWidth(Context context)
	{
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE );
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics( outMetrics);
		return outMetrics .widthPixels ;
	}


}
