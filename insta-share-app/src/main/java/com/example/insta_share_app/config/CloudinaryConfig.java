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
        map.put("cloud_name","");
        map.put("api_key","");
        map.put("api_secret","");
        map.put("secure",true);
        return new Cloudinary(map);
    }
}
