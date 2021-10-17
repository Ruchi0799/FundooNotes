package com.bridgelabz.fundoonotes.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Label {
	
@Id
@GeneratedValue(strategy=GenerationType.AUTO)
private Long id;

@Column(unique=true)
private String labelName;

@CreationTimestamp
private LocalDateTime CreatedTimeStamp;

@UpdateTimestamp
private LocalDateTime updatedTimeStamp;

//@ManyToMany(targetEntity = Note.class,cascade=CascadeType.ALL)
//private List<Note> Notes;

@ManyToMany(targetEntity = Note.class,mappedBy="labels",cascade=CascadeType.ALL)
private List<Note> Notes;
}
