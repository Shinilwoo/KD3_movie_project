package com.team1.movie.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team1.movie.entity.ImageFile;
import com.team1.movie.repository.ImageRepository;


@Service
public class ImageServiceImpl implements ImageService{

	@Autowired
    ImageRepository repository;

    @Override
    public void insert(ImageFile imageFile) {
      repository.save(imageFile);
    }

    @Override
    public ImageFile getImageById(String id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<ImageFile> getImageList() {
        return List.of();
    }
}
