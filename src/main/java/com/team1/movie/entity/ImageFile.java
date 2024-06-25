package com.team1.movie.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ImageFile {
	@Id
	private String id;

	@Column(name = "trueTitle", columnDefinition = "varchar(1024)")
	private String truthTitle;

	@Column(name = "predTitle", columnDefinition = "varchar(1024)")
	private String predTitle;
	@Lob
	@Column(name = "image", columnDefinition = "LONGBLOB")
	private byte[] photo;
}
