package org.sopt.domain.like.repository;

import org.sopt.domain.like.entity.Like;
import org.sopt.domain.post.entity.Post;
import org.sopt.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    boolean existsByUserAndPost(User user, Post post);
    Optional<Like> findByUserAndPost(User user, Post post);
}