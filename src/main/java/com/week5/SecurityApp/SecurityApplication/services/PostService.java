package com.week5.SecurityApp.SecurityApplication.services;


import com.week5.SecurityApp.SecurityApplication.dto.PostDTO;

import java.util.List;

public interface PostService {

    public PostDTO getPostById(Long postId);

    public List<PostDTO> getAllPost();

    public PostDTO createNewPost(PostDTO request);
}
