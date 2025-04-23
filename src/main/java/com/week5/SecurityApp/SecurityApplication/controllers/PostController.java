package com.week5.SecurityApp.SecurityApplication.controllers;


import com.week5.SecurityApp.SecurityApplication.dto.PostDTO;
import com.week5.SecurityApp.SecurityApplication.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The type Post controller.
 */
@RestController
@RequestMapping(path = "/post")

public class PostController {

    private final PostService postService;


    /**
     * Instantiates a new Post controller.
     *
     * @param postService the post service
     */
    public PostController(PostService postService) {
        this.postService = postService;
    }


    /**
     * Get post by id post dto.
     *
     * @param postId the post id
     * @return the post dto
     */
    @GetMapping(path = "/{postId}")
//    @PreAuthorize("hasAnyRole('USER', 'ADMIN') OR hasAuthority('POST_VIEW')")
    @PreAuthorize("@postSecurity.isOwnerOfPost(#postId)")
    public PostDTO getPostById(@PathVariable Long postId) {
        return postService.getPostById(postId);
    }


    /**
     * Get all post list.
     *
     * @return the list
     */
    @GetMapping
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public List<PostDTO> getAllPost() {
        return postService.getAllPost();
    }

    /**
     * Create new post dto.
     *
     * @param request the request
     * @return the post dto
     */
    @PostMapping
    @PreAuthorize("@postSecurity.hasPermission")
    public PostDTO createNewPost(@RequestBody PostDTO request) {
        return postService.createNewPost(request);
    }
}
