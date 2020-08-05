package org.fk.tdu;

import android.app.*;
import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.net.*;
import android.net.http.*;
import android.util.*;
import android.view.*;
import android.webkit.*;
import android.widget.*;
import java.lang.reflect.*;
import android.os.*;
import android.content.pm.*;

public class Helper {

	private Helper(){}
	
	public static WebView createWebView(final Context c){
		final WebView v = new WebView(c);
		v.setBackgroundColor(0);
		WebSettings s = v.getSettings();
		accessToMethod(s,boolean.class,"setJavaScriptEnabled",true);
		accessToMethod(s,int.class,"setLightTouchEnabled",true);
		s.setUserAgentString("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.32 Safari/537.36");
		accessToMethod(s,boolean.class,"setBuiltInZoomControls",true);
		accessToMethod(s,boolean.class,"setSupportZoom",true);
		accessToMethod(s,boolean.class,"setDisplayZoomControls",false);
		accessToMethod(s,boolean.class,"setAllowFileAccess",true);
		accessToMethod(s,boolean.class,"setDomStorageEnabled",true);
		accessToMethod(s,boolean.class,"setDatabaseEnabled",true);
		accessToMethod(s,String.class,"setDatabasePath",c.getCacheDir().toString().replace("cache","databases"));
		accessToMethod(s,long.class,"setAppCacheMaxSize",1024*1024*8);
		accessToMethod(s,String.class,"setAppCachePath",c.getCacheDir());
		accessToMethod(s,boolean.class,"setAppCacheEnabled",true);
		accessToMethod(s,boolean.class,"setUseWideViewPort",true);
		return v;
	}
	
	public static void addContentView(Activity a, View v){
		((FrameLayout) a.findViewById(android.R.id.content)).addView(v);
	}
	
	private static RelativeLayout getSplash(Activity a){
		FrameLayout main = (FrameLayout) a.findViewById(android.R.id.content);
		return main.findViewById(android.R.id.custom);
	}
	
	public static void showSplash(Activity a, boolean show){
		RelativeLayout splash = getSplash(a);
		if(show){
			if(splash == null){
				splash = new RelativeLayout(a);
				splash.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
				splash.setBackgroundColor(0xFF212121);
				int s = (int) mp(45);
				ImageView iv = new ImageView(a);
				RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(s,s);
				rp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
				iv.setLayoutParams(rp);
				iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
				iv.setImageResource(R.drawable.splash);
				splash.addView(iv);
				s = (int) mp(35);
				iv = new ImageView(a);
				rp = new RelativeLayout.LayoutParams(s,s);
				rp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
				rp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
				iv.setLayoutParams(rp);
				iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
				iv.setImageResource(R.mipmap.devotag);
				splash.addView(iv);
				splash.setId(android.R.id.custom);
				addContentView(a, splash);
			} else {
				splash.setVisibility(View.VISIBLE);
			}
		} else if(splash != null){
			splash.setVisibility(View.GONE);
			((ViewGroup)splash.getParent()).removeView(splash);
		}
	}
	
	public static float mp(float px){
		DisplayMetrics dm = Resources.getSystem().getDisplayMetrics();
		return ((dm.widthPixels > dm.heightPixels ? dm.heightPixels : dm.widthPixels) / 100.0f) * px;
	}
	
	public static ProgressBar createProgressBar(final Context c){
		ProgressBar pb = new ProgressBar(c,null,android.R.attr.progressBarStyleHorizontal);
		pb.setLayoutParams(new FrameLayout.LayoutParams(-1,(int)dp(4)));
		pb.setMax(100);
		pb.setProgressDrawable(c.getResources().getDrawable(R.drawable.pbar));
		return pb;
	}
	
	public static float dp(float px){
		return Resources.getSystem().getDisplayMetrics().density * px;
	}

	public static void accessToMethod(Object o, Class arg, String name, Object... v){
		try {
			Method m = null;
			if(arg != null) m = o.getClass().getMethod(name, arg);
			else m = o.getClass().getMethod(name);
			m.setAccessible(true);
			if(arg != null) m.invoke(o,v);
			else m.invoke(o);
		} catch(Exception | Error e){
			Log.e(Helper.class.getName(),e.getMessage());
		}
	}
	
	public static class WebViewClientHelper extends WebViewClient {
		
		Context ctx;

		public WebViewClientHelper(Context c){
			ctx = c;
		}
		
		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error){
			handler.proceed(); // Ignore SSL certificate errors
		}

		@Override
		public void onReceivedError(WebView view, WebResourceRequest request,WebResourceError error){
			//Toast.makeText(ctx,error.getErrorCode() + " " + error.getDescription(),1).show();
		}

		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			//Toast.makeText(ctx, errorCode + " " + description, Toast.LENGTH_LONG).show();
		}
	}
}
