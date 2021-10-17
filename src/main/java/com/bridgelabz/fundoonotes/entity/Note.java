package com.bridgelabz.fundoonotes.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Note {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String title;
	
	private String description;
	
	private Boolean isPinned;
	
	private Boolean isTrashed;
	
	private Boolean isArchived;
	
	private String colour;
	
	@CreationTimestamp
	private LocalDateTime createdTimeStamp;
	
	@UpdateTimestamp
	private LocalDateTime updatedTimeStamp;
	
	private String reminder;
	
//	@ManyToMany(targetEntity = Label.class,mappedBy="Note",cascade=CascadeType.ALL)
//	private List<Label> labels;
	
	@ManyToMany(targetEntity = Note.class,cascade=CascadeType.ALL)
	private List<Label> labels;
	
	@OneToMany(targetEntity = NoteImage.class)
	@JoinColumn(name="note_id")
	private List<NoteImage> noteImage;
	
	
}
