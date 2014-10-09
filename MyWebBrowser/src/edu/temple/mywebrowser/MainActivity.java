package edu.temple.mywebrowser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

	Button send;
	EditText urlText;
	WebView webContent;
	URL url = null;
	String webpage = "";
	String tmppage;
	Message msg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		send = (Button) findViewById(R.id.send);
		webContent = (WebView) findViewById(R.id.webContent);
		urlText = (EditText) findViewById(R.id.urlText);
		urlText.setText("https://");
		
		WebSettings ws = webContent.getSettings();
		ws.setJavaScriptEnabled(true);


		send.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Thread loadContent = new Thread() {

					@Override
					public void run(){

						try {
							url = new URL(urlText.getText().toString());
							BufferedReader br = new BufferedReader(
									new InputStreamReader(url.openStream()));

							while(br.readLine() != null) {
								tmppage = br.readLine();
								webpage +=tmppage;
							}
							msg = Message.obtain();
							msg.obj = webpage;

							displayURL.sendMessage(msg);


						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				};//end thread

				loadContent.start();

			}//end onClick
		});//end onClickListener


	}

	public boolean isNetworkActive(){
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		} else {
			return false;
		}
	}

	Handler displayURL = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			webContent.loadData((String) msg.obj, "text/html", "UTF-8");
			return false;
		}
	});
}
