package com.wwdevelopers.codebook.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.wwdevelopers.codebook.R;
import com.wwdevelopers.codebook.models.BookmarkPage;
import com.wwdevelopers.codebook.models.Platform;

import org.litepal.crud.callback.SaveCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WebViewActivity extends Activity {

    Platform platform;

    Activity mActivity;
    @BindView(R.id.web_view) WebView web_view;
    @BindView(R.id.web_site_title_tv) TextView web_site_title_tv;
    @BindView(R.id.web_site_link_tv) TextView web_site_link_tv;
    @BindView(R.id.swiperefresh) SwipeRefreshLayout mySwipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        platform = (Platform) getIntent().getExtras().getSerializable("platform");

        mActivity = WebViewActivity.this;
        ButterKnife.bind(mActivity);

        web_site_title_tv.setText("Loading ... ");

        WebSettings webSettings = web_view.getSettings();
        webSettings.setJavaScriptEnabled(true);


        web_view.loadUrl( platform.getLink() );

        web_view.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if( URLUtil.isNetworkUrl(url) ) {
                    return false;
                }
                if (appInstalledOrNot(url)) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity( intent );
                } else {
                    // do something if app is not installed
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                web_site_link_tv.setText( web_view.getUrl() );
                web_site_title_tv.setText( web_view.getTitle() );
                mySwipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                web_site_link_tv.setText( url );
                web_site_title_tv.setText( "Loading ... " );

            }


        });



        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        web_view.reload();

                    }
                }
        );

    }


    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }


    @OnClick(R.id.go_back_image_button)
    public void goBackAction(){
        finish();
    }


    @OnClick(R.id.bookmark_image_button)
    public void bookmarkPage(){

        BookmarkPage bookmarkPage = new BookmarkPage();
        bookmarkPage.setTitle(web_view.getTitle());
        bookmarkPage.setUrl(web_view.getUrl());
        bookmarkPage.saveAsync().listen(new SaveCallback() {
            @Override
            public void onFinish(boolean success) {

                Toast.makeText(mActivity,"Page BookMarked",Toast.LENGTH_LONG).show();

            }
        });

    }

    @Override
    public void onBackPressed() {

        if( web_view.canGoBack() ){

            web_view.goBack();


        }else{

            super.onBackPressed();

        }



    }
}
