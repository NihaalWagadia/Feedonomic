package com.example.feedonomic;

public class ApiAdapter {
    public String api_title;
    public String api_link;




    public String getApi_title() {
        return api_title;
    }

    public void setApi_title(String api_title) {
        this.api_title = api_title;
    }

    public String getApi_link() {
        return api_link;
    }

    public void setApi_link(String api_link) {
        this.api_link = api_link;
    }

    public ApiAdapter(String api_title, String api_link){
        this.api_title = api_title;
        this.api_link = api_link;

    }
    public ApiAdapter(){}


}
