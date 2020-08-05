package org.fk.tdu;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.net.*;
import android.net.http.*;
import android.os.*;
import android.view.*;
import android.webkit.*;
import android.widget.*;

public class MainActivity extends Activity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        w = Helper.createWebView(this);
		w.setLayoutParams(new FrameLayout.LayoutParams(-1,-1));
		setContentView(w);
		final ProgressBar pb = Helper.createProgressBar(this);
		Helper.showSplash(this,true);
		Helper.addContentView(this,pb);
		w.setWebViewClient(new Helper.WebViewClientHelper(MainActivity.this){
			@Override
			public void onPageStarted(WebView v, String u,Bitmap i){
				pb.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onPageFinished(WebView v,String u){
				pb.setVisibility(View.INVISIBLE);
				pb.setProgress(0);
				Helper.showSplash(MainActivity.this,false);
			}
			
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request){
				return shouldOverrideUrlLoading(view, request.getUrl().toString());
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url){
				if(!url.contains("github.io")){
					view.stopLoading();
					Intent in = new Intent(Intent.ACTION_VIEW);
					in.setData(Uri.parse(url));
					startActivity(in);
					return true;
				}
				return false;
			}
		});
		w.setWebChromeClient(new WebChromeClient(){
				@Override
				public void onProgressChanged(WebView v,int p){
					pb.setProgress(p);
				}
		});
		
		if(getIntent().getDataString() == null || getIntent().getDataString().length() < 10){
			w.loadUrl("https://teamderuntergang.github.io");
		} else {
			w.loadUrl(getIntent().getDataString());
		}
    }
	
	private ValueCallback<Uri> mUploadMessage = null;
	private ValueCallback<Uri[]> mFilePathCallback = null;
	private Uri[] results = null;
	private Uri result = null;
	private String dataString = null;
	private final static int FILECHOOSER_RESULTCODE = 1;
	private WebView w = null;
	
	@Override  
	protected void onActivityResult(int requestCode, int resultCode, Intent intent){  
		if(requestCode == FILECHOOSER_RESULTCODE){
			if(Build.VERSION.SDK_INT < 21){
				if (null == mUploadMessage) return;
				result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
				mUploadMessage.onReceiveValue(result);
			} else {
				if(requestCode != FILECHOOSER_RESULTCODE || mFilePathCallback == null){
					super.onActivityResult(requestCode, resultCode, intent);
					return;
				}
				if(resultCode == Activity.RESULT_OK){
					dataString = intent.getDataString();
					if (dataString != null)
						results = new Uri[]{Uri.parse(dataString)};
				}
				mFilePathCallback.onReceiveValue(results);
			}
		}
		mUploadMessage = null;
		mFilePathCallback = null;
		results = null;
	}
	
	long time = 0;

	@Override
	public void onBackPressed(){
		if(w.canGoBack()) w.goBack();
		else {
			if((time + 3000) < System.currentTimeMillis()){
				Toast.makeText(this,"Çıkmak için geri tuşuna tekrar dokunun",Toast.LENGTH_LONG).show();
				time = System.currentTimeMillis();
			} else super.onBackPressed();
		}
	}
}
