package com.week5.SecurityApp.SecurityApplication.utils;

import com.week5.SecurityApp.SecurityApplication.dto.PostDTO;
import com.week5.SecurityApp.SecurityApplication.entities.PostEntity;
import com.week5.SecurityApp.SecurityApplication.entities.UserEntity;
import com.week5.SecurityApp.SecurityApplication.entities.enums.SubscriptionPlan;
import com.week5.SecurityApp.SecurityApplication.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


/**
 * The type Post security.
 */
@Component
@RequiredArgsConstructor
public class PostSecurity {
    private final PostService postService;

    /**
     * Is owner of post boolean.
     *
     * @param postId the post id
     * @return the boolean
     */
    public boolean isOwnerOfPost(Long postId) {
        UserEntity userEntity = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PostDTO postEntity = postService.getPostById(postId);
        return postEntity.getAuthor().getId().equals(userEntity.getId());
    }


    /**
     * Has permission boolean.
     *
     * @return the boolean
     */
    public boolean hasPermission(){
        UserEntity userEntity = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userEntity.getSubscriptionPlan() == SubscriptionPlan.BASIC || userEntity.getSubscriptionPlan()
                == SubscriptionPlan.PREMIUM;
    }
}
