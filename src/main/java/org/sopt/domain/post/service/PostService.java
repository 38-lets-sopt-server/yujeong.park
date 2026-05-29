package org.sopt.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.sopt.domain.post.dto.request.CreatePostRequest;
import org.sopt.domain.post.dto.request.UpdatePostRequest;
import org.sopt.domain.post.dto.response.CreatePostResponse;
import org.sopt.domain.post.dto.response.PostListResponse;
import org.sopt.domain.post.dto.response.PostResponse;
import org.sopt.domain.post.dto.response.UpdatePostResponse;
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

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // CREATE
    @Transactional
    public CreatePostResponse createPost(Long userId, CreatePostRequest request) {
        // ID로 유저 조회, 존재하지 않으면 예외 발생
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        // 게시글 생성 및 저장
        Post post = new Post(request.title(), request.content(), user);
        postRepository.save(post);
        return CreatePostResponse.from(post);
    }

    // READ - 전체
    public List<PostListResponse> getAllPosts() {
        return postRepository.findAllWithUser()
                .stream()
                .map(PostListResponse::from)
                .toList();
    }

    // READ - 단건
    public PostResponse getPost(Long id) {
        // ID로 게시글 조회, 존재하지 않으면 예외 발생
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));

        return PostResponse.from(post);
    }

    // UPDATE
    @Transactional
    public UpdatePostResponse updatePost(Long userId, Long postId, UpdatePostRequest request) {
        // ID로 게시글 조회, 존재하지 않으면 예외 발생
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));

        // 게시글 작성자와 요청자가 다를 경우 예외 발생
        if (!post.getUser().getId().equals(userId)) {
            throw new PostException(PostErrorCode.POST_FORBIDDEN);
        }

        // 새로운 제목, 내용 업데이트
        post.update(request.title(), request.content());
        return UpdatePostResponse.from(post);
    }

    // DELETE
    @Transactional
    public void deletePost(Long userId, Long postId) {
        // ID로 게시글 조회, 존재하지 않으면 예외 발생
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));

        // 게시글 작성자와 요청자가 다를 경우 예외 발생
        if (!post.getUser().getId().equals(userId)) {
            throw new PostException(PostErrorCode.POST_FORBIDDEN);
        }

        postRepository.delete(post);
    }

    // 게시글 검색
    public List<PostListResponse> search(String title, String nickname) {
        return postRepository.searchByTitleAndNickname(title, nickname)
                .stream()
                .map(PostListResponse::from)
                .toList();
    }
}