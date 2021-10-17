package com.bridgelabz.fundoonotes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundoonotes.entity.NoteImage;

@Repository
public interface NoteImageRepository extends JpaRepository<NoteImage,Long> {

}
