package com.boldvision.ocr.repository;

import com.boldvision.ocr.model.ImageDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<ImageDetails, Integer> {

    Optional<ImageDetails> findById(int id);

    List<ImageDetails> findAllImagesByUserId(int userId);
}
