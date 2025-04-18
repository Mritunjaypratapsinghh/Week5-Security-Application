package com.week5.SecurityApp.SecurityApplication.controllers;


import com.week5.SecurityApp.SecurityApplication.dto.PostDTO;
import com.week5.SecurityApp.SecurityApplication.services.PostService;
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
    public PostDTO getPostById(@PathVariable Long postId){
        return postService.getPostById(postId);
    }


    @GetMapping
    public List<PostDTO> getAllPost(){
        return postService.getAllPost();
    }

    @PostMapping
    public PostDTO createNewPost(@RequestBody PostDTO request){
        return postService.createNewPost(request);
    }
}
