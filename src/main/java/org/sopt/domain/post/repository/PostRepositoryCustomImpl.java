package org.sopt.domain.post.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.sopt.domain.post.entity.Post;
import org.sopt.domain.post.entity.QPost;
import org.sopt.domain.user.entity.QUser;

import java.util.List;

public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PostRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    // 전체 게시글 목록 조회
    @Override
    public List<Post> findAllWithUser() {
        return findPosts(null, null);
    }

    // 제목 또는 닉네임으로 게시글 검색 (동적 조건)
    @Override
    public List<Post> search(String title, String nickname) {
        return findPosts(title, nickname);
    }

    private List<Post> findPosts(String title, String nickname) {
        QPost post = QPost.post;
        QUser user = QUser.user;

        BooleanBuilder builder = new BooleanBuilder();

        // 제목 키워드가 있을 때만 조건 추가
        if (title != null && !title.isBlank()) {
            builder.and(post.title.containsIgnoreCase(title));
        }

        // 닉네임 키워드가 있을 때만 조건 추가
        if (nickname != null && !nickname.isBlank()) {
            builder.and(user.nickname.containsIgnoreCase(nickname));
        }

        return queryFactory
                .selectFrom(post)
                .join(post.user, user).fetchJoin()
                .where(builder)
                .fetch();
    }
}