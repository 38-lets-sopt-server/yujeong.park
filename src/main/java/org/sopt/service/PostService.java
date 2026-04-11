package org.sopt.service;

import org.sopt.domain.Post;
import org.sopt.dto.request.CreatePostRequest;
import org.sopt.dto.request.UpdatePostRequest;
import org.sopt.dto.response.PostResponse;
import org.sopt.exception.PostNotFoundException;
import org.sopt.repository.PostRepository;
import org.sopt.validator.PostValidator;

import java.util.List;

public class PostService {
    private final PostRepository postRepository = new PostRepository();
    private final PostValidator postValidator = new PostValidator();

    // CREATE
    public PostResponse createPost(CreatePostRequest request) {
        // 제목, 내용 유효성 검증
        postValidator.validate(request.title, request.content);

        // 게시글 생성 및 저장
        String createdAt = java.time.LocalDateTime.now().toString();
        Post post = new Post(postRepository.generateId(), request.title, request.content, request.author, createdAt);
        postRepository.save(post);
        return PostResponse.from(post);
    }

    // READ - 전체
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(PostResponse::from)
                .toList();
    }

    // READ - 단건
    public PostResponse getPost(Long id) {
        // ID로 게시글 조회, 존재하지 않으면 예외 발생
        Post post = postRepository.findById(id);
        if (post == null) {
            throw new PostNotFoundException(id);
        }
        return PostResponse.from(post);
    }

    // UPDATE
    public void updatePost(Long id, UpdatePostRequest request) {
        // ID로 게시글 조회, 존재하지 않으면 예외 발생
        Post post = postRepository.findById(id);
        if (post == null) {
            throw new PostNotFoundException(id);
        }
        // 새로운 제목, 내용 유효성 검증 후 업데이트
        postValidator.validate(request.title, request.content);
        post.update(request.title, request.content);
    }

    // DELETE
    public void deletePost(Long id) {
        // 삭제 실패 시 (존재하지 않는 ID) 예외 발생
        boolean isDeleted = postRepository.deleteById(id);
        if (!isDeleted) {
            throw new PostNotFoundException(id);
        }
    }
}