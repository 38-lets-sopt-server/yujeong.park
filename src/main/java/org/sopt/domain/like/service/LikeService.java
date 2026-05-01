package org.sopt.domain.like.service;

import org.sopt.domain.like.entity.Like;
import org.sopt.domain.like.exception.LikeErrorCode;
import org.sopt.domain.like.exception.LikeException;
import org.sopt.domain.like.repository.LikeRepository;
import org.sopt.domain.post.entity.Post;
import org.sopt.domain.post.exception.PostErrorCode;
import org.sopt.domain.post.exception.PostException;
import org.sopt.domain.post.repository.PostRepository;
import org.sopt.domain.user.entity.User;
import org.sopt.domain.user.exception.UserErrorCode;
import org.sopt.domain.user.exception.UserException;
import org.sopt.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public LikeService(LikeRepository likeRepository, PostRepository postRepository, UserRepository userRepository) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // 좋아요 추가
    @Transactional
    public void addLike(Long postId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));

        // 이미 좋아요를 누른 게시글일 경우 예외 발생
        if (likeRepository.existsByUserAndPost(user, post)) {
            throw new LikeException(LikeErrorCode.LIKE_ALREADY_EXISTS);
        }

        likeRepository.save(new Like(user, post));
    }

    // 좋아요 취소
    @Transactional
    public void cancelLike(Long postId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));

        // 기존에 좋아요를 누르지 않았던 게시글일 경우 에외 발생
        Like like = likeRepository.findByUserAndPost(user, post)
                .orElseThrow(() -> new LikeException(LikeErrorCode.LIKE_NOT_FOUND));

        likeRepository.delete(like);
    }

}
