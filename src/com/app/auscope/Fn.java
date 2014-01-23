package com.app.auscope;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class Fn 
{
	private static Context mContext;
	
	public static RectF mScreenDimentions;
	
	public final static long SHORT_ANIMATION = 100, MED_ANIMATION = 250;
	public static final String 
		HEX_IDLE_COLOR = "#5D6468", 
		HEX_ACTIVE_COLOR = "#FFFFFF",
		HEX_AUSIS_GREEN = "#4BD964";
	
	public static void init(Context context)
	{
		mContext = context;
		
		mScreenDimentions = new RectF(
				0,
				0,
				mContext.getResources().getDisplayMetrics().widthPixels,
				mContext.getResources().getDisplayMetrics().heightPixels
			);
	}
	
	public static Bitmap scaleBimtap(Bitmap bitmap, int width, int height) 
	{
	    final int bitmapWidth = bitmap.getWidth();
	    final int bitmapHeight = bitmap.getHeight();

	    final float scale = Math.min((float) width / (float) bitmapWidth, (float) height / (float) bitmapHeight);

	    final int scaledWidth = (int) (bitmapWidth * scale);
	    final int scaledHeight = (int) (bitmapHeight * scale);

	    final Bitmap decoded = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
	    @SuppressWarnings("unused")
		final Canvas canvas = new Canvas(decoded);

	    return decoded;
	}
	
	public static int hex2Rgb(String colorStr) 
	{
		int RED = Integer.valueOf( colorStr.substring( 1, 3 ), 16);
		int GREEN = Integer.valueOf( colorStr.substring( 3, 5 ), 16 );
		int BLUE = Integer.valueOf( colorStr.substring( 5, 7 ), 16 );

	    return Color.rgb(RED, GREEN, BLUE);
	}
	
	public static Bitmap drawTextToBitmap(Bitmap bitmap,  String Text, int TextColor, int ShadowColor) 
	{
	    try 
	    {
			Resources resources = mContext.getResources();
			
			float scale = resources.getDisplayMetrics().density;
			
			android.graphics.Bitmap.Config bitmapConfig =   bitmap.getConfig();
			// set default bitmap config if none
			if(bitmapConfig == null) 
			{
			  bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
			}
			// resource bitmaps are imutable,
			// so we need to convert it to mutable one
			bitmap = bitmap.copy(bitmapConfig, true);
			
			Canvas canvas = new Canvas(bitmap);
			// new antialised Paint
			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			// text color - #3D3D3D
			paint.setColor(TextColor);
			// text size in pixels
			//paint.setTextSize((int) (10 * scale));
			paint.setTextSize((int) (bitmap.getHeight() / 3));
			// text shadow
			paint.setShadowLayer(1f, 0f, 1f, ShadowColor);
			
			// draw text to the Canvas center
			Rect textBounds = new Rect();
			paint.getTextBounds(Text, 0, Text.length(), textBounds);
			
			Point centerOfBitmap = new Point(bitmap.getWidth() / 2, bitmap.getHeight() / 2);
			
			//Position of text.
			int x = centerOfBitmap.x - (textBounds.width() / 2);
			int y = centerOfBitmap.y + (textBounds.height() / 2);
			
			canvas.drawText(Text, x, y, paint);
	        return bitmap;
	    } 
	    catch (Exception e) 
	    {
	        return null;
	    }
	  }
	
	public static int getDisplayPixel(int AmountInPixels)
	{
		final float scale = mContext.getResources().getDisplayMetrics().density;
		return (int) (AmountInPixels * scale + 0.5f);
	}
	
	public static void Out(Object object)
	{
		System.out.println("SYSTEM_OUT: " + object);
	}
	
	private static final String API_KEY1 = "AIzaSyBawWulajDTt-7SwH4iaf_5fsrKjsqr_RQ";
	private static StandardCallBack tempCallBack;
	public static void setPlaceName(String PlaceType, double lat, double longi, String Radius, StandardCallBack activity)
	{
		/*
		String RequestLink = "https://maps.googleapis.com/maps/api/place/search/json?";
		RequestLink += "types=" + PlaceType + "&";
		RequestLink += "location=" + lat + "," + longi + "&";
		RequestLink += "radius=" + Radius + "&";
		RequestLink += "sensor=false&";
		RequestLink += "key=" + API_KEY1;
		*/
		
		tempCallBack = activity;
		
		String RequestLink = "https://maps.googleapis.com/maps/api/place/search/json?types=cafe&location=-37.818461666666664,145.00854333333334&radius=5000&sensor=false&key=AIzaSyBawWulajDTt-7SwH4iaf_5fsrKjsqr_RQ";
		
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(RequestLink, new AsyncHttpResponseHandler() 
		{
		    @Override
		    public void onSuccess(String response) 
		    {
		    	JSONObject jsonObjLoc = null;
		    	try 
		    	{
					jsonObjLoc = new JSONObject(response);
				} 
		    	catch (JSONException e) 
				{
					e.printStackTrace();
				}
		    	
		    	JSONArray MyList = null;
				try {
					MyList = jsonObjLoc.getJSONArray("results");
				} 
				catch (JSONException e1) 
				{
					Fn.Out("Failed results");
					e1.printStackTrace();
				}
		    	
		    	try {
					tempCallBack.setPlaceName((String)(((JSONObject) MyList.get(1)).get("vicinity")));
				} catch (JSONException e) 
				{
					Fn.Out("Failed return");
					e.printStackTrace();
				}
		    	
		    }
		    
		});
	}
}
