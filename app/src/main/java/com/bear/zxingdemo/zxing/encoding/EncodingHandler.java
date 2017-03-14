//package com.bear.zxingdemo.zxing.encoding;
//
//import java.util.Hashtable;
//
//import android.graphics.Bitmap;
//import android.graphics.Canvas;
//
//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.EncodeHintType;
//import com.google.zxing.MultiFormatWriter;
//import com.google.zxing.WriterException;
//import com.google.zxing.common.BitMatrix;
///**
// * @author Ryan Tang
// *
// */
//public final class EncodingHandler {
//	private static final int BLACK = 0xff000000;
//
//	public static Bitmap createQRCode(String str,int widthAndHeight) throws WriterException {
//		Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
//        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
//		BitMatrix matrix = new MultiFormatWriter().encode(str,
//				BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight);
//		int width = matrix.getWidth();
//		int height = matrix.getHeight();
//		int[] pixels = new int[width * height];
//
//		for (int y = 0; y < height; y++) {
//			for (int x = 0; x < width; x++) {
//				if (matrix.get(x, y)) {
//					pixels[y * width + x] = BLACK;
//				}
//			}
//		}
//		Bitmap bitmap = Bitmap.createBitmap(width, height,
//				Bitmap.Config.ARGB_8888);
//		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
//		return bitmap;
//	}
//
//
//
//
//
//	/**
//	 * 在二维码中间添加Logo图案
//	 */
//	private static Bitmap addLogo(Bitmap src, Bitmap logo) {
//		if (src == null) {
//			return null;
//		}
//
//		if (logo == null) {
//			return src;
//		}
//
//		//获取图片的宽高
//		int srcWidth = src.getWidth();
//		int srcHeight = src.getHeight();
//		int logoWidth = logo.getWidth();
//		int logoHeight = logo.getHeight();
//
//		if (srcWidth == 0 || srcHeight == 0) {
//			return null;
//		}
//
//		if (logoWidth == 0 || logoHeight == 0) {
//			return src;
//		}
//
//		//logo大小为二维码整体大小的1/5
//		float scaleFactor = srcWidth * 1.0f / 5 / logoWidth;
//		Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
//		try {
//			Canvas canvas = new Canvas(bitmap);
//			canvas.drawBitmap(src, 0, 0, null);
//			canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
//			canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);
//
//			canvas.save(Canvas.ALL_SAVE_FLAG);
//			canvas.restore();
//		} catch (Exception e) {
//			bitmap = null;
//			e.getStackTrace();
//		}
//
//		return bitmap;
//	}
//}

package com.bear.zxingdemo.zxing.encoding;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.Hashtable;

/**
 * 当前类注释:生成二维码的工具类
 * 作者：jinwenfeng on 16/2/18 13:50
 * 邮箱：823546371@qq.com
 * QQ： 823546371
 * © 2016 jinwenfeng
 * ©版权所有，未经允许不得传播
 */
public class EncodingHandler {
	private static int IMAGE_HALFWIDTH = 50;//宽度值，影响中间图片大小
	/**
	 * 生成二维码，默认大小为500*500
	 *
	 * @param text 需要生成二维码的文字、网址等
	 * @return bitmap
	 */
	public static Bitmap createQRCode(String text) {
		return createQRCode(text, 500);
	}

	/**
	 * 生成二维码
	 *
	 * @param text 需要生成二维码的文字、网址等
	 * @param size 需要生成二维码的大小（）
	 * @return bitmap
	 */
	public static Bitmap createQRCode(String text, int size) {
		try {
			Hashtable<EncodeHintType, String> hints = new Hashtable<>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			BitMatrix bitMatrix = new QRCodeWriter().encode(text,
					BarcodeFormat.QR_CODE, size, size, hints);
			int[] pixels = new int[size * size];
			for (int y = 0; y < size; y++) {
				for (int x = 0; x < size; x++) {
					if (bitMatrix.get(x, y)) {
						pixels[y * size + x] = 0xff000000;
					} else {
						pixels[y * size + x] = 0xffffffff;
					}

				}
			}
			Bitmap bitmap = Bitmap.createBitmap(size, size,
					Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, size, 0, 0, size, size);
			return bitmap;
		} catch (WriterException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 生成带logo的二维码，默认二维码的大小为500，logo为二维码的1/5
	 *
	 * @param text 需要生成二维码的文字、网址等
	 * @param mBitmap logo文件
	 * @return bitmap
	 */
	public static Bitmap createQRCodeWithLogo(String text, Bitmap mBitmap) {
		return createQRCodeWithLogo(text,500,mBitmap);
	}

	/**
	 * 生成带logo的二维码，logo默认为二维码的1/5
	 *
	 * @param text 需要生成二维码的文字、网址等
	 * @param size 需要生成二维码的大小（）
	 * @param mBitmap logo文件
	 * @return bitmap
	 */
	public static Bitmap createQRCodeWithLogo(String text, int size, Bitmap mBitmap) {
		try {
			IMAGE_HALFWIDTH = size/10;
			Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            /*
             * 设置容错级别，默认为ErrorCorrectionLevel.L
             * 因为中间加入logo所以建议你把容错级别调至H,否则可能会出现识别不了
             */
			hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
			BitMatrix bitMatrix = new QRCodeWriter().encode(text,
					BarcodeFormat.QR_CODE, size, size, hints);

			int width = bitMatrix.getWidth();//矩阵高度
			int height = bitMatrix.getHeight();//矩阵宽度
			int halfW = width / 2;
			int halfH = height / 2;

			Matrix m = new Matrix();
			float sx = (float) 2 * IMAGE_HALFWIDTH / mBitmap.getWidth();
			float sy = (float) 2 * IMAGE_HALFWIDTH
					/ mBitmap.getHeight();
			m.setScale(sx, sy);
			//设置缩放信息
			//将logo图片按martix设置的信息缩放
			mBitmap = Bitmap.createBitmap(mBitmap, 0, 0,
					mBitmap.getWidth(), mBitmap.getHeight(), m, false);

			int[] pixels = new int[size * size];
			for (int y = 0; y < size; y++) {
				for (int x = 0; x < size; x++) {
					if (x > halfW - IMAGE_HALFWIDTH && x < halfW + IMAGE_HALFWIDTH
							&& y > halfH - IMAGE_HALFWIDTH
							&& y < halfH + IMAGE_HALFWIDTH) {
						//该位置用于存放图片信息
						//记录图片每个像素信息
						pixels[y * width + x] = mBitmap.getPixel(x - halfW
								+ IMAGE_HALFWIDTH, y - halfH + IMAGE_HALFWIDTH);
					} else {
						if (bitMatrix.get(x, y)) {
							pixels[y * size + x] = 0xff000000;
						} else {
							pixels[y * size + x] = 0xffffffff;
						}
					}
				}
			}
			Bitmap bitmap = Bitmap.createBitmap(size, size,
					Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, size, 0, 0, size, size);
			return bitmap;
		} catch (WriterException e) {
			e.printStackTrace();
			return null;
		}
	}
}
