package com.example.aqueryroboguice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import roboguice.activity.RoboActivity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.os.Vibrator;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.auth.FacebookHandle;
import com.androidquery.callback.AjaxStatus;
import com.google.inject.Inject;

public class MainActivity extends RoboActivity {
	
	AQuery a;
	
	@Inject
	Vibrator vibrar;
	
	FacebookHandle face;
	String APP_ID = "602936446384967";
	String permissoes = "read_stream, user_photos, friends_photos";
	
	
    @SuppressLint("NewApi") @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        a = new AQuery(this);
        
        ThreadPolicy t = ThreadPolicy.LAX;
        StrictMode.setThreadPolicy(t);
        
        face = new FacebookHandle(this, APP_ID, permissoes);
        
        Button botaoAQuery = (Button)findViewById(R.id.button1);
        botaoAQuery.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Toast.makeText(MainActivity.this, "Click sem AQ", 1000).show();
			}
		});
        a.id(R.id.button2).clickable(true).clicked(this, "click");
    }
    
    public void click(){
		Toast.makeText(MainActivity.this, "Click com AQ", 1000).show();
		vibrar.vibrate(2000);
		meuFace();
		meusAmigos();
    }
    
    public void meuFace(){
    		String url = "https://graph.facebook.com/me?fields=id,name,picture";
    		a.auth(face).ajax(url, JSONObject.class, this, "retornoFace");
    }
    public void retornoFace(String url, JSONObject json, AjaxStatus status) throws JSONException{
    		if(json != null){
    			String urlImagem = json
    					.getJSONObject("picture")
    					.getJSONObject("data")
    					.getString("url");
    			
    			a.id(R.id.imageView1).image(urlImagem,
    					false, false, 0, 0, null, AQuery.FADE_IN);
    			
    		}
    	
    }
    
    
    
    public void meusAmigos(){
    		String url = "https://graph.facebook.com/me/friends?limit=3&fields=picture, first_name";
    		a.auth(face).progress(R.id.progressBar1)
    		.ajax(url, JSONObject.class, this, "retornoMeusAmigos");
    }
    
    public void retornoMeusAmigos(String url, 
    		JSONObject json, 
    		AjaxStatus status) throws JSONException{
    	
    		JSONArray array = json.getJSONArray("data");
    		for(int i=0; i < array.length(); i++){
    			String urlAmigo = array
    						.getJSONObject(i)
    						.getJSONObject("picture")
    						.getJSONObject("data")
    						.getString("url");
    			if(i == 0){
    				a.id(R.id.imageView2).image(urlAmigo);
    			}else if(i == 1){
    				a.id(R.id.imageView3).image(urlAmigo);
    			}else if(i == 2){
    				a.id(R.id.imageView4).image(urlAmigo);
    			}
    			
    		}
    	
    }
    
    


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
