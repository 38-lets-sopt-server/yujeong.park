package org.sopt.service;

import org.sopt.domain.Post;
import org.sopt.dto.request.CreatePostRequest;
import org.sopt.dto.request.UpdatePostRequest;
import org.sopt.dto.response.CreatePostResponse;
import org.sopt.dto.response.PostResponse;
import org.sopt.exception.PostNotFoundException;
import org.sopt.repository.PostRepository;
import org.sopt.validator.PostValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final PostValidator postValidator;

    public PostService(PostRepository postRepository, PostValidator postValidator) {
        this.postRepository = postRepository;
        this.postValidator = postValidator;
    }

    // CREATE
    public CreatePostResponse createPost(CreatePostRequest request) {
        // 제목, 내용 유효성 검증
        postValidator.validate(request.title(), request.content());

        // 게시글 생성 및 저장
        String createdAt = java.time.LocalDateTime.now().toString();
        Post post = new Post(postRepository.generateId(), request.title(), request.content(), request.author(), createdAt);
        postRepository.save(post);
        return new CreatePostResponse(post.getId(), "게시글 등록 완료!");
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
        return postRepository.findById(id)
                .map(PostResponse::from)
                .orElseThrow(() -> new PostNotFoundException(id));
    }

    // UPDATE
    public void updatePost(Long id, UpdatePostRequest request) {
        // ID로 게시글 조회, 존재하지 않으면 예외 발생
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));
        // 새로운 제목, 내용 유효성 검증 후 업데이트
        postValidator.validate(request.title(), request.content());
        post.update(request.title(), request.content());
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