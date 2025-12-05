package com.example.insta_share_app.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary getCloudinary(){
        Map map=new HashMap();
        map.put("cloud_name","dzfwhbukv");
        map.put("api_key","373465495131555");
        map.put("api_secret","JlA9EajV5c0GrNazGV2tG8ugtaI");
        map.put("secure",true);
        return new Cloudinary(map);
    }
}
