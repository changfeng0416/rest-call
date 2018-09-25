package com.gavin.rest.controller;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/9/25 0025.
 */
@Controller
//@EnableAutoConfiguration
public class RestCallController {
    @RequestMapping("/index")
    public String home(){
        return "index";
    }
    //////////////////////////HttpURLConnection///////////////////////////
    //get
    public void getMethodHttpURLConnection (String url) throws IOException {
        URL restURL = new URL(url);

        HttpURLConnection conn = (HttpURLConnection) restURL.openConnection();

        conn.setRequestMethod("GET"); // POST GET PUT DELETE
        conn.setRequestProperty("Accept", "application/json");

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while((line = br.readLine()) != null ){
            System.out.println(line);
        }

        br.close();
    }

    //post
    public void postMethodHttpURLConnection (String url, String query) throws IOException {
        URL restURL = new URL(url);

        HttpURLConnection conn = (HttpURLConnection) restURL.openConnection();
        conn.setRequestMethod("POST");
        //conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        PrintStream ps = new PrintStream(conn.getOutputStream());
        ps.print(query);
        ps.close();

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException(
                    "HTTP GET Request Failed with Error code : "
                            + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while((line = br.readLine()) != null ){
            System.out.println(line);
        }

        br.close();
    }
////////////////////////HttpClient/////////////////////////////////

    public  void testHttpClient(String param) {
        try {
            HttpClient client = HttpClients.createDefault();
            if("get".equals(param)){
                HttpGet request = new HttpGet("http://47.99.79.243:8011/swagger-ui.html");
                request.setHeader("Accept", "application/json");
                HttpResponse response = client.execute(request);
                HttpEntity entity = response.getEntity();
                System.out.println(EntityUtils.toString(entity));

            }else if("post".equals(param)){
                HttpPost request2 = new HttpPost("http://47.99.79.243:8011/api/get/summary");

                List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                nvps.add(new BasicNameValuePair("text", "1 love you!"));
                nvps.add(new BasicNameValuePair("summaryType", "1"));


                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nvps, "GBK");
                request2.setEntity(formEntity);
                HttpResponse response2 = client.execute(request2);
                HttpEntity entity = response2.getEntity();

                System.out.println("---------------------------------------------");
                System.out.println(EntityUtils.toString(entity));
                System.out.println("---------------------------------------------");
            }else if("delete".equals(param)){

            }else if("put".equals(param)){

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /////////////////////udesk test/////////////////////////////////////////




    public static void main(String[] args) {
        RestCallController restUtil = new RestCallController();
        try {
            restUtil.getMethodHttpURLConnection("http://47.99.79.243:8011/swagger-ui.html");

            String url = "http://47.99.79.243:8011/api/get/summary";


            String query = "&text="+ URLEncoder.encode("1 love you!", "UTF-8");
            query+="&summaryType="+ URLEncoder.encode("1", "UTF-8");

            restUtil.postMethodHttpURLConnection (url, query);

//             restUtil.testHttpClient("get");
//            restUtil.testHttpClient("post");
//            String url = "http://18017640838.udesk.cn/open_api_v1/log_in";
//
//            String query = "&email="+ URLEncoder.encode("changfeng0416@163.com", "UTF-8");
//            query+="&password="+ URLEncoder.encode("gal$#@123456", "UTF-8");
//            restUtil.postMethodHttpURLConnection (url, query);

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
