package com.virtualpairprogrammers.photos.services;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PhotoService {
    private final Map<String, String> drivers = Map.of(
            "Pam Parry", "https://rac-istio-course-images.s3.amazonaws.com/1.jpg",
            "Duke T. Dog", "https://rac-istio-course-images.s3.amazonaws.com/2.jpg",
            "Denzil Tulser", "https://rac-istio-course-images.s3.amazonaws.com/3.jpg",
            "Herman Boyce", "https://rac-istio-course-images.s3.amazonaws.com/4.jpg",
            "June Snell", "https://rac-istio-course-images.s3.amazonaws.com/5.jpg");

    public String getPhotoFor(String driverName) {
        return drivers.get(driverName);
    }
}
