package com.week5.SecurityApp.SecurityApplication.services;

import com.week5.SecurityApp.SecurityApplication.SecurityApplication;
import com.week5.SecurityApp.SecurityApplication.dto.PostDTO;
import com.week5.SecurityApp.SecurityApplication.entities.PostEntity;
import com.week5.SecurityApp.SecurityApplication.entities.UserEntity;
import com.week5.SecurityApp.SecurityApplication.exceptions.ResourceNotFoundException;
import com.week5.SecurityApp.SecurityApplication.repositories.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PostServiceImpl implements PostService {

    private final ModelMapper modelMapper;
    private final PostRepository postRepository;


    public PostServiceImpl(ModelMapper modelMapper, PostRepository postRepository) {
        this.modelMapper = modelMapper;
        this.postRepository = postRepository;
    }


    public void existPostById(Long postId) {
        boolean exists = postRepository.existsById(postId);
        if (!exists) throw new ResourceNotFoundException("post with id " + postId + " does not exists");
    }

    @Override
    public PostDTO getPostById(Long postId) {
        UserEntity userEntity = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("user {}", userEntity);
        existPostById(postId);
        PostEntity postEntity = postRepository.findById(postId).get();
        return modelMapper.map(postEntity, PostDTO.class);
    }

    @Override
    public List<PostDTO> getAllPost() {
        List<PostEntity> postEntities = postRepository.findAll();
        List<PostDTO> postDTOS = postEntities.stream().map(
                postEntity -> modelMapper.map(postEntity, PostDTO.class)
        ).collect(Collectors.toList());
        return postDTOS;
    }

    @Override
    public PostDTO createNewPost(PostDTO request) {
        UserEntity userEntity= (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PostEntity requestEntity =modelMapper.map(request,PostEntity.class);
        requestEntity.setAuthor(userEntity);
        PostEntity postEntity = postRepository.save(requestEntity);
        return modelMapper.map(postEntity,PostDTO.class);
    }
}
