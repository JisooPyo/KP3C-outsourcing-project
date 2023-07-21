package com.example.kp3coutsourcingproject.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CommentRequestDto {
	private Long parentId;
	@NotBlank
	private String content;
}
