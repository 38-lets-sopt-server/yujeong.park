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

    @Override
    public List<Post> searchByTitleAndNickname(String title, String nickname) {
        QPost post = QPost.post;
        QUser user = QUser.user;

        BooleanBuilder builder = new BooleanBuilder();

        if (title != null && !title.isBlank()) {
            builder.and(post.title.containsIgnoreCase(title));
        }

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