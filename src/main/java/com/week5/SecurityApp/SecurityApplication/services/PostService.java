package com.week5.SecurityApp.SecurityApplication.services;


import com.week5.SecurityApp.SecurityApplication.dto.PostDTO;

import java.util.List;

/**
 * The interface Post service.
 */
public interface PostService {

    /**
     * Gets post by id.
     *
     * @param postId the post id
     * @return the post by id
     */
    public PostDTO getPostById(Long postId);

    /**
     * Gets all post.
     *
     * @return the all post
     */
    public List<PostDTO> getAllPost();

    /**
     * Create new post post dto.
     *
     * @param request the request
     * @return the post dto
     */
    public PostDTO createNewPost(PostDTO request);
}
