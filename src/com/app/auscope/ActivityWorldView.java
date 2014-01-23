package com.app.auscope;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ActivityWorldView extends Activity implements StandardCallBack, OnClickListener
{
	// Google Map
	private GoogleMap mGoogleMap;
	
	//Screen elements
	private TextView mTitleDate;
	private TextView mTitleCurrentLocation;
	
	private RelativeLayout mMainMenuBottom;
	private ImageView mShowHideArrow;
	
	private enum MainMenu{NO_MENU, MAIN_MENU_ONLY, SUBMENU};
	private MainMenu mCurrentMenu;
	
	private enum SubMenuType{NONE, SEISMOMETER};
	private SubMenuType mSubmenuType = SubMenuType.NONE;
	
	private ImageView mSeismometerImage;
	private TextView mSeismometerText;
	
	private ImageView mMapLayerImage;
	private TextView mMapLayerText;
	
	private ImageView mInfoImage;
	private TextView mInfoText;
	
	//SUBMENUS
	//Seismometer
	private ImageView mAusisImage = null;
	private TextView mAusisText = null;
	private ImageView mGnssImage = null;
	private TextView mGnssText = null;
	
	
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_world_view);
		
		try 
		{
			// Loading map
			initilizeMap();

		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		mCurrentMenu = MainMenu.NO_MENU;
		
		//Get view elements.
		mTitleDate = (TextView)(this.findViewById(R.id.currentDate));
		mTitleCurrentLocation = (TextView)(this.findViewById(R.id.currentLocation));
		mMainMenuBottom = (RelativeLayout)(this.findViewById(R.id.mainMenuBar));
		mShowHideArrow = (ImageView)(this.findViewById(R.id.whiteArrowUp));
		
		//MainMenu elements
		mShowHideArrow.setOnClickListener(this);
		
		mSeismometerImage = (ImageView)(this.findViewById(R.id.seismometer_image));
		mSeismometerText = (TextView)(this.findViewById(R.id.seismometer_text));
		mSeismometerImage.setOnClickListener(this);
		mSeismometerText.setOnClickListener(this);
		
		mMapLayerImage = (ImageView)(this.findViewById(R.id.maplayer_image));
		mMapLayerText = (TextView)(this.findViewById(R.id.maylayer_text));
		mMapLayerImage.setOnClickListener(this);
		mMapLayerText.setOnClickListener(this);
		
		mInfoImage = (ImageView)(this.findViewById(R.id.infoImage));
		mInfoText = (TextView)(this.findViewById(R.id.infoText));
		mInfoImage.setOnClickListener(this);
		mInfoText.setOnClickListener(this);
	}
	
	@Override
	public void onWindowFocusChanged(boolean focus) 
	{
	    super.onWindowFocusChanged(focus);
	    makeMainMenuGray();
	    hideMainMenu();
	}
	
	private void showMainMenu()
	{
		ObjectAnimator anim = ObjectAnimator.ofFloat(mMainMenuBottom, "translationY", (float)(mMainMenuBottom.getHeight()), 0f);
	    anim.setDuration(Fn.MED_ANIMATION);
	    anim.start();
	    
	    ObjectAnimator anim2 = ObjectAnimator.ofFloat(mShowHideArrow, "translationY", (float)(mMainMenuBottom.getHeight()), 0f);
	    anim2.setDuration(Fn.MED_ANIMATION);
	    anim2.start();
	    
	    mShowHideArrow.setImageResource(R.drawable.white_arrow_down);
	    
	    mCurrentMenu = MainMenu.MAIN_MENU_ONLY;
	}
	
	private void hideMainMenu()
	{
		removeAllSubmenus();
		
		ObjectAnimator anim = ObjectAnimator.ofFloat(mMainMenuBottom, "translationY", 0, mMainMenuBottom.getHeight());
	    anim.setDuration(Fn.MED_ANIMATION);
	    anim.start();
	    
	    ObjectAnimator anim2 = ObjectAnimator.ofFloat(mShowHideArrow, "translationY", 0, mMainMenuBottom.getHeight());
	    anim2.setDuration(Fn.MED_ANIMATION);
	    anim2.start();
	    
	    mShowHideArrow.setImageResource(R.drawable.white_arrow_up);
	    
	    mCurrentMenu = MainMenu.NO_MENU;
	}
	
	private void makeMainMenuGray()
	{
		mSeismometerImage.setImageResource(R.drawable.icon_nav1_info_idle);
		mSeismometerText.setTextColor(Fn.hex2Rgb(Fn.HEX_IDLE_COLOR));
		mMapLayerImage.setImageResource(R.drawable.icon_nav1_maplayers_idle);
		mMapLayerText.setTextColor(Fn.hex2Rgb(Fn.HEX_IDLE_COLOR));
		mInfoImage.setImageResource(R.drawable.icon_nav1_info_idle);
		mInfoText.setTextColor(Fn.hex2Rgb(Fn.HEX_IDLE_COLOR));
	}
	
	//Used for multiple submenus.
	private void loadInSubMenu(SubMenuType submenu)
	{
		removeAllSubmenus();
		mCurrentMenu = MainMenu.SUBMENU;
		
		View child = null;
		RelativeLayout item = null;
		
		if(submenu == SubMenuType.SEISMOMETER)
		{
			item = (RelativeLayout)findViewById(R.id.submenu_layout);
			
			child = getLayoutInflater().inflate(R.layout.layout_mainscreen_submenu_seismometer, null);
			
			item.addView(child);
			child.setAlpha(0);
			ObjectAnimator anim = ObjectAnimator.ofFloat(child, "Alpha", 0, 1);
		    anim.setDuration(Fn.MED_ANIMATION);
		    anim.start();
		    
		    //Load the views. Do this every time, avoid null reference.
			mAusisImage = (ImageView) findViewById(R.id.ausis_image);
			mAusisText = (TextView) findViewById(R.id.ausis_text);
		    mAusisImage.setOnClickListener(this);
			mAusisText.setOnClickListener(this);
			
			mSubmenuType = SubMenuType.SEISMOMETER;
		}
		
		
		//Move Arrow
		if(child != null)
		{
		    //Move arrow.
		    ObjectAnimator anim1 = ObjectAnimator.ofFloat(mShowHideArrow, "translationY", 0, -mMainMenuBottom.getHeight());
		    anim1.setDuration(Fn.SHORT_ANIMATION);
		    anim1.start();
		}
	}
	
	private boolean mAusisStatus = false;
	private void showORhideAusis()
	{
		if(!mAusisStatus)
		{
			mAusisStatus = true;
			mAusisImage.setImageResource(R.drawable.icon_nav2_ausis_active);
			mAusisText.setTextColor(Fn.hex2Rgb(Fn.HEX_AUSIS_GREEN));
			
			GetData.getAusisStations();
		}
		else
		{
			mAusisStatus = false;
			mAusisImage.setImageResource(R.drawable.icon_nav2_ausis_idle);
			mAusisText.setTextColor(Fn.hex2Rgb(Fn.HEX_IDLE_COLOR));
		}
	}
	
	private void removeAllSubmenus()
	{
		RelativeLayout item = (RelativeLayout)findViewById(R.id.submenu_layout);
		item.removeAllViews();
		
		//Move arrow
		ObjectAnimator anim1 = ObjectAnimator.ofFloat(mShowHideArrow, "translationY", -mMainMenuBottom.getHeight(), 0);
	    anim1.setDuration(Fn.SHORT_ANIMATION);
	    anim1.start();
	    
	    mCurrentMenu = MainMenu.MAIN_MENU_ONLY;
	}
	
	private void clearAllSubmenus()
	{
		
	}
	
	/**
	 * function to load map. If map is not created it will create it for you
	 * */
	private void initilizeMap() 
	{
		if (mGoogleMap == null) 
		{
			mGoogleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

			// check if map is created successfully or not
			if (mGoogleMap == null) 
			{
				Toast.makeText(getApplicationContext(),
				this.getString(R.string.unable_to_create_maps), Toast.LENGTH_SHORT).show();
			}
			
			mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
		}
		
		// check if GPS enabled
        GPSTracker gpsTracker = new GPSTracker(this);
        
        gpsTracker.getLocality(getApplicationContext());

        if (gpsTracker.canGetLocation())
        {
        	mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()), 1));
        }
        
        placeCustomMarker(gpsTracker.getLatitude(), gpsTracker.getLongitude(), 9.9f, "TEST");
        
        setTitleDate();
        Fn.setPlaceName("vicinity", gpsTracker.getLatitude(), gpsTracker.getLongitude(), "5000", this);
	}
	
	@Override
	public void setPlaceName(String name) 
	{
		if(name != "")
		{
			mTitleCurrentLocation.setText(name.substring(0, 24));
		}
		
	}
	
	private void setTitleDate()
	{
		Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("EE, dd MMMM yyyy");
        String formattedDate = df.format(c.getTime());
        
        mTitleDate.setText(formattedDate);
	}
	
	private void placeCustomMarker(double lat, double longi, float Power, String Text)
	{
		MarkerOptions marker = new MarkerOptions().position(new LatLng(lat, longi)).title(Text);
		
		Bitmap markerBitmap = BitmapFactory.decodeResource(this.getApplicationContext().getResources(), R.drawable.pin_quake_idle);
		
		markerBitmap = Fn.scaleBimtap(markerBitmap, Fn.getDisplayPixel(30), Fn.getDisplayPixel(30));
		
		markerBitmap = Fn.drawTextToBitmap(markerBitmap, String.valueOf(Power), Color.WHITE, Color.DKGRAY);
		
		BitmapDescriptor bd = BitmapDescriptorFactory.fromBitmap(markerBitmap);
		marker.icon(bd);
		
		mGoogleMap.addMarker(marker);
	}

	private void placeCurrentLocation(double lat, double longi)
	{
		MarkerOptions marker = new MarkerOptions().position(new LatLng(lat, longi)).title(this.getString(R.string.Current_location));
		marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
		mGoogleMap.addMarker(marker);
	}
	
	@Override
	protected void onResume() 
	{
		super.onResume();
		initilizeMap();
	}

	@Override
	public void onClick(View v) 
	{
		if(
				v == mShowHideArrow && 
				mCurrentMenu == MainMenu.NO_MENU)
		{
			showMainMenu();
		}
		else if(
				v == mShowHideArrow && 
				mCurrentMenu == MainMenu.MAIN_MENU_ONLY)
		{
			makeMainMenuGray();
			hideMainMenu();
		}
		else if(
				(v == mSeismometerImage ||
				v == mSeismometerText) &&
				mCurrentMenu == MainMenu.MAIN_MENU_ONLY)
		{
			makeMainMenuGray();
			mSeismometerImage.setImageResource(R.drawable.icon_nav1_info_active);
			mSeismometerText.setTextColor(Fn.hex2Rgb(Fn.HEX_ACTIVE_COLOR));
			loadInSubMenu(SubMenuType.SEISMOMETER);
			
		}
		else if(
				(v == mMapLayerImage || v == mMapLayerText) && 
				mCurrentMenu == MainMenu.MAIN_MENU_ONLY
				)
		{
			makeMainMenuGray();
			mMapLayerImage.setImageResource(R.drawable.icon_nav1_maplayers_active);
			mMapLayerText.setTextColor(Fn.hex2Rgb(Fn.HEX_ACTIVE_COLOR));
		}
		else if(
				(v == mInfoImage || v == mInfoText) && 
				mCurrentMenu == MainMenu.MAIN_MENU_ONLY
				)
		{
			makeMainMenuGray();
			mInfoImage.setImageResource(R.drawable.icon_nav1_info_active);
			mInfoText.setTextColor(Fn.hex2Rgb(Fn.HEX_ACTIVE_COLOR));
		}
		//Arrow response on submenus
		else if(
				v == mShowHideArrow && 
				mCurrentMenu == MainMenu.SUBMENU
				)
		{
			removeAllSubmenus();
		}
		else if(
				mSubmenuType == SubMenuType.SEISMOMETER && 
				(v == mAusisImage || v == mAusisText)
				)
		{
			showORhideAusis();
		}
		
	}
	
	
}
