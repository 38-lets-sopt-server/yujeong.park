package org.sopt.domain.post.service;

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
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // CREATE
    @Transactional
    public CreatePostResponse createPost(CreatePostRequest request) {
        // ID로 유저 조회, 존재하지 않으면 예외 발생
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        // 게시글 생성 및 저장
        Post post = new Post(request.title(), request.content(), user);
        postRepository.save(post);
        return CreatePostResponse.from(post);
    }

    // READ - 전체
    @Transactional(readOnly = true)
    public List<PostListResponse> getAllPosts() {
        return postRepository.findAllWithUser()
                .stream()
                .map(PostListResponse::from)
                .toList();
    }

    // READ - 단건
    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        // ID로 게시글 조회, 존재하지 않으면 예외 발생
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));

        return PostResponse.from(post);
    }

    // UPDATE
    @Transactional
    public UpdatePostResponse updatePost(Long id, UpdatePostRequest request) {
        // ID로 게시글 조회, 존재하지 않으면 예외 발생
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));

        // 새로운 제목, 내용 업데이트
        post.update(request.title(), request.content());
        return UpdatePostResponse.from(post);
    }

    // DELETE
    @Transactional
    public void deletePost(Long id) {
        // ID로 게시글 조회, 존재하지 않으면 예외 발생
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));
        postRepository.delete(post);
    }

    // 게시글 검색
    @Transactional(readOnly = true)
    public List<PostListResponse> search(String title, String nickname) {
        return postRepository.searchByTitleAndNickname(title, nickname)
                .stream()
                .map(PostListResponse::from)
                .toList();
    }
}