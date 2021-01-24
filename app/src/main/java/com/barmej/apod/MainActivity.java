package com.barmej.apod;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.ortiz.touchview.TouchImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private static final String URL = "https://api.nasa.gov/planetary/apod?api_key=YRLtBTu1hzCe6pCGMjHfSbQ2t1rapMM1xOF2Eobm&date=";
    public String date = "";
    RequestQueue requestQueue;
    TextView description, title;
    String test = "";
    String mediaType = "";
    TouchImageView image;
    DatePickerDialog datePickerDialog;
    WebView webView;
    private int mYear;
    private int mMonth;
    private int mDay;
    Context context = MainActivity.this;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(this);
        description = findViewById(R.id.explanation);
        title = findViewById(R.id.title);
        image = findViewById(R.id.img_picture_view);
        progressBar = findViewById(R.id.progressBar);
        webView = findViewById(R.id.wv_video_player);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        jsonParse();

        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR); // current year
        mMonth = c.get(Calendar.MONTH); // current month
        mDay = c.get(Calendar.DAY_OF_MONTH); //current Day.
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_pick_day:
                datePicker();
                break;
            case R.id.action_download_hd:
                Toast.makeText(context, "Hi", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_about:
                FragmentAbout aboutFragment = new FragmentAbout();
                FragmentManager FragmentManager = getSupportFragmentManager();
                aboutFragment.show(FragmentManager, aboutFragment.getTag());
                return true;
            case R.id.action_share:
                Toast.makeText(context, "Hi", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void jsonParse() {
        progressBar.setVisibility(View.VISIBLE);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            mediaType = response.getString("media_type");
                            Data data = new Data(response.getString("title"),response.getString("explanation") , response.getString("url"));
                            if (mediaType.equals("image")){

                                description.setText(data.getDescription());
                                title.setText(data.getTitle());
                                webView.setVisibility(View.GONE);
                                image.setVisibility(View.VISIBLE);
                                webView.loadData("", "text/html", null);
                                // i am using glide and picasso to double the speed
                                Glide.with(MainActivity.this).load(data.getImageOrVideo()).thumbnail(0.05f).into(image);
                                Picasso.with(MainActivity.this).load(data.getImageOrVideo()).into(image);
                                progressBar.setVisibility(View.GONE);
                            } else if (mediaType.equals("video")){
                                description.setText(data.getDescription());
                                title.setText(data.getTitle());
                                progressBar.setVisibility(View.GONE);
                                image.setVisibility(View.GONE);
                                webView.loadUrl(data.getImageOrVideo());
                                webView.setVisibility(View.VISIBLE);
                            }else{
                                Toast.makeText(context, "no", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(jsonObjectRequest);

    }


    public void changeDate(String chosenDate){
        progressBar.setVisibility(View.VISIBLE);
        date = chosenDate;
        String finalURL = URL+date;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, finalURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            mediaType = response.getString("media_type");
                            Data data = new Data(response.getString("title"),response.getString("explanation") , response.getString("url"));
                            if (mediaType.equals("image")){
                                String imageURL = response.getString("url");
                                description.setText(data.getDescription());
                                title.setText(data.getTitle());
                                webView.setVisibility(View.GONE);
                                image.setVisibility(View.VISIBLE);
                                webView.loadData("", "text/html", null);
                                // i am using glide and picasso to double the speed
                                Glide.with(MainActivity.this).load(data.getImageOrVideo()).thumbnail(0.005f).into(image);
                                Picasso.with(MainActivity.this).load(imageURL).into(image);
                                progressBar.setVisibility(View.GONE);
                            } else if (mediaType.equals("video")){
                                description.setText(data.getDescription());
                                title.setText(data.getTitle());
                                progressBar.setVisibility(View.GONE);
                                image.setVisibility(View.GONE);
                                webView.loadUrl(data.getImageOrVideo());
                                webView.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(jsonObjectRequest);


    }

    public void datePicker(){
        datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {


                mYear = year;
                mMonth = month;
                mDay = dayOfMonth;

                test = year + "-" + (month + 1) + "-" + dayOfMonth;
                changeDate(test);
            }

        }, mYear, mMonth, mDay);

        datePickerDialog.show();


    }

}
