package com.team1.movie.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.team1.movie.entity.ImageFile;


public interface ImageRepository 
extends JpaRepository<ImageFile,String>, CrudRepository<ImageFile,String>{

}