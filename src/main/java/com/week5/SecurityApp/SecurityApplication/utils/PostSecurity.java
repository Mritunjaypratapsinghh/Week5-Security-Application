package com.week5.SecurityApp.SecurityApplication.utils;
import com.week5.SecurityApp.SecurityApplication.dto.PostDTO;
import com.week5.SecurityApp.SecurityApplication.entities.PostEntity;
import com.week5.SecurityApp.SecurityApplication.entities.UserEntity;
import com.week5.SecurityApp.SecurityApplication.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Component
@RequiredArgsConstructor
public class PostSecurity {
    private final PostService postService;

    public boolean isOwnerOfPost(Long postId){
        UserEntity userEntity = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PostDTO postEntity = postService.getPostById(postId);
        return postEntity.getAuthor().getId().equals(userEntity.getId());
    }
}
