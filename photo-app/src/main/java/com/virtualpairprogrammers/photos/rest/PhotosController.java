package com.virtualpairprogrammers.photos.rest;

import com.virtualpairprogrammers.photos.services.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PhotosController {
    private final PhotoService photoService;

    @RequestMapping(method = RequestMethod.GET, value = "/photo/{driverName}", produces = "application/json")
    public String getPhotoFor(@PathVariable String driverName) {
        return photoService.getPhotoFor(driverName);
    }
}
