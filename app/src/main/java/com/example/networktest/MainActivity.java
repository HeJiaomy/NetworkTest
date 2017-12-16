package com.example.networktest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView responseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button sendRequest = findViewById(R.id.send_request);
        responseText = findViewById(R.id.response_text);
        sendRequest.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        HttpUtil.sendOkHttpRequest("http://10.0.2.2/get_data.json", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            //地址为电脑本机
                            .url("http://10.0.2.2/get_data.json")
                            .build();
                    Response response = client.newCall(request).execute();
                    if (response != null) {
                        String responseData = response.body().string();
//                        showResponse(responseData);
//                        parseXMLWithPull(responseData); //pull解析
//                        parseXMLWithSAX(responseData);  //SAX解析
//                        parseJSONWithJsonObject(responseData);  //JsonObject解析
                        parseJSONWithGson(responseData);    //Gson解析
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void parseJSONWithGson(String jsonData) {
        Gson gson= new Gson();
        List<App> appList= gson.fromJson(jsonData,new TypeToken<List<App>>(){}.getType());
        for (App app : appList) {
            String id= app.getId();
            String name= app.getName();
            String version= app.getVersion();

            Log.d("GSON","id is:"+id);
            Log.d("GSON","name is:"+name);
            Log.d("GSON","version is:"+version);
        }
    }

    private void parseJSONWithJsonObject(String jsonData) {
        try {
            JSONArray jsonArray= new JSONArray(jsonData);
            for (int i=0; i<jsonArray.length(); i++){
                JSONObject jsonObject= jsonArray.getJSONObject(i);
                String id= jsonObject.getString("id");
                String name= jsonObject.getString("name");
                String version= jsonObject.getString("version");

                Log.d("JSONObject","id is:"+id);
                Log.d("JSONObject","name is:"+name);
                Log.d("JSONObject","version is:"+version);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseXMLWithSAX(String xmlData) {
        try {
            SAXParserFactory factory= SAXParserFactory.newInstance();
            XMLReader xmlReader= factory.newSAXParser().getXMLReader();
            ContentHandler handler= new ContentHandler();
            //将ContentHandler的实例设置到XMLReader中
            xmlReader.setContentHandler(handler);
            //开始执行解析
            xmlReader.parse(new InputSource(new StringReader(xmlData)));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

    }

    private void parseXMLWithPull(String xmlData) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));
            int eventType = xmlPullParser.getEventType();
            String id = "";
            String name = "";
            String version = "";

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String nodeName = xmlPullParser.getName();
                switch (eventType) {
                    //开始解析某个节点
                    case XmlPullParser.START_TAG:
                        if ("id".equals(nodeName)) {
                            id = xmlPullParser.nextText();
                        }else if ("name".equals(nodeName)){
                            name= xmlPullParser.nextText();
                        }else if ("version".equals(nodeName)){
                            version= xmlPullParser.nextText();
                        }
                        break;
                    //完成解析某个节点
                    case XmlPullParser.END_TAG:
                        if ("app".equals(nodeName)){
                            Log.d("MainActivity","id is："+id);
                            Log.d("MainActivity","name is："+name);
                            Log.d("MainActivity","version："+version);
                        }
                        break;
                }
                eventType=xmlPullParser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showResponse(final String responseData) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                responseText.setText(responseData);
            }
        });
    }


}
