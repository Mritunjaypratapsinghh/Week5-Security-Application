package com.week5.SecurityApp.SecurityApplication.controllers;


import com.week5.SecurityApp.SecurityApplication.dto.PostDTO;
import com.week5.SecurityApp.SecurityApplication.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/post")

public class PostController {

    private final PostService postService;


    public PostController(PostService postService){
        this.postService = postService;
    }


    @GetMapping(path = "/{postId}")
//    @PreAuthorize("hasAnyRole('USER', 'ADMIN') OR hasAuthority('POST_VIEW')")
    @PreAuthorize("@postSecurity.isOwnerOfPost(#postId)")
    public PostDTO getPostById(@PathVariable Long postId){
        return postService.getPostById(postId);
    }


    @GetMapping
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public List<PostDTO> getAllPost(){
        return postService.getAllPost();
    }

    @PostMapping
    public PostDTO createNewPost(@RequestBody PostDTO request){
        return postService.createNewPost(request);
    }
}
