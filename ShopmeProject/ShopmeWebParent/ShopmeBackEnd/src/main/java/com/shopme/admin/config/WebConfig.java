package com.shopme.admin.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring MVC configuration to expose user photo
 * @author kkm
 */

@Configuration
public class WebConfig implements WebMvcConfigurer {
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String dirName="user-photos";
		//System.out.println(dirName);
		Path userPhotosDir = Paths.get(dirName);
		String userPhotosPath	=userPhotosDir.toFile().getAbsolutePath();
		//System.out.println(userPhotosPath);
		registry.addResourceHandler("/"+dirName+"/**").addResourceLocations("file:"+userPhotosPath+"/");
		WebMvcConfigurer.super.addResourceHandlers(registry);
	}
}
