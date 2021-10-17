package com.bridgelabz.fundoonotes.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class S3Config {

	
	@Autowired
	private ApplicationPropertiesReader applicationPropertiesReader;
	
	@Bean
	public AmazonS3 s3client() {
		BasicAWSCredentials awsCreds=new BasicAWSCredentials(applicationPropertiesReader.getS3Access(),
				applicationPropertiesReader.getS3secretKey());
		
		return AmazonS3ClientBuilder.standard()
				.withRegion(Regions.AP_SOUTH_1)
				.withCredentials(new AWSStaticCredentialsProvider(awsCreds))
				.build();
	}
	}
