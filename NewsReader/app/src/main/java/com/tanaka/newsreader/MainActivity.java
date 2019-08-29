package com.tanaka.newsreader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class MainActivity extends AppCompatActivity {
    ListView newsListView;
    ArrayList<Story> articles = new ArrayList<>();
    ArrayList<String> articleTitles = new ArrayList<>();
    ArrayList<String> articleURLs = new ArrayList<>();
    ArrayAdapter<String> adapter;
    Intent intent;
    ApiInterface apiInterface;
    Retrofit retrofit;
    ProgressBar loading;
    SQLiteDatabase myDatabase;
    Cursor c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        myDatabase = this.openOrCreateDatabase("News", MODE_PRIVATE, null);
        //myDatabase.execSQL("DROP TABLE IF  EXISTS articles");
        myDatabase.execSQL("CREATE TABLE IF NOT EXISTS articles(title VARCHAR, url VARCHAR, id  INTEGER PRimary key autoincrement)");
        c = myDatabase.rawQuery("SELECT * FROM articles", null);

        setupListView();

        setUpRetrofit();
        if (c.getCount() > 0) {
            updateListView();
        } else {
            fetchNewsData();
        }
//        updateDatabase();


    }

    public void updateListView() {
        int titleIndex = c.getColumnIndex("title");
        int urlIndex = c.getColumnIndex("url");
        int idIndex = c.getColumnIndex("id");
        if (c.moveToFirst()) {
            articleTitles.clear();
            articleURLs.clear();
            for (int i = 0; i < c.getCount(); i++) {
                articleTitles.add(c.getString(titleIndex));
                articleURLs.add(c.getString(urlIndex));
                c.moveToNext();

            }
        }
        adapter.notifyDataSetChanged();
    }

    private void setupListView() {
        loading = findViewById(R.id.progressBar2);
        newsListView = findViewById(R.id.articleListView);
        //in future learn about custom adapters
        adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, articleTitles);
        newsListView.setAdapter(adapter);

        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                intent = new Intent(getApplicationContext(), WebviewActivity.class);
                intent.putExtra("url", articleURLs.get(i));
                startActivity(intent);
            }
        });

    }

    private void setUpRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://hacker-news.firebaseio.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        apiInterface = retrofit.create(ApiInterface.class);
    }

    private void fetchNewsData() {
        loading.setVisibility(View.VISIBLE);
        myDatabase.execSQL("DELETE from articles");
        apiInterface.getTopStories().enqueue(new Callback<List<Integer>>() {

            @Override
            public void onResponse(Call<List<Integer>> call, Response<List<Integer>> response) {
                List<Integer> topStoryIDs = response.body();
                for (int i = 0; i < response.body().size(); i++) {
                    apiInterface.getArticle(topStoryIDs.get(i)).enqueue(new Callback<Story>() {
                        String url = "";
                        String title = "";

                        @Override
                        public void onResponse(Call<Story> call, Response<Story> response) {
                            loading.setVisibility(View.GONE);
                            Story story = response.body();
                            title = story.getTitle();
                            url = story.getUrl();
                            articleTitles.add(title);
                            articleURLs.add(url);


//myDatabase.execSQL("INSERT INTO articles (title, url) VALUES ('"+title+"','"+url+"')");
                            String sql = "INSERT INTO articles (title,url) VALUES (?, ?)";
                            SQLiteStatement statement = myDatabase.compileStatement(sql);
                            if (url == null) {
                                url = "https://www.google.com";
                            } else if (title == null) {
                                title = "Google";
                            }


                            adapter.notifyDataSetChanged();


                            System.out.println(title + ": \n " + url);

                        }

                        @Override
                        public void onFailure(Call<Story> call, Throwable t) {

                        }

                    });
                    updateDatabase();
                }
            }

            @Override
            public void onFailure(Call<List<Integer>> call, Throwable t) {

            }
        });

    }

    public void updateDatabase() {
        String sql = "INSERT INTO articles (title,url) VALUES (?, ?)";
        SQLiteStatement statement = myDatabase.compileStatement(sql);
        String url;
        String title;
        for (int i = 0; i < articleTitles.size(); i++) {
            title = articleTitles.get(i);
            url = articleURLs.get(i);
            statement.bindString(1, title);
            statement.bindString(2, url);
            statement.execute();
        }

        updateListView();
    }


}

