package com.sparta.wildcard_newsfeed.domain.post.repository;

import com.sparta.wildcard_newsfeed.domain.post.dto.PostPageResponseDto;
import com.sparta.wildcard_newsfeed.domain.post.entity.Post;
import com.sparta.wildcard_newsfeed.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryRewriter;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findPostById(Long postId);

    Page<PostPageResponseDto> findAllByCreatedAtBetween(LocalDateTime firstDate, LocalDateTime lastDate, Pageable pageable);

    @Query(value = "select p.id, u.id 'user_id', p.title, p.content, u.name as username, " +
            "p.created_at as created_at, p.updated_at as updated_at, " +
            "IF(c.count is null, 0, c.count) as likecount " +
            "from (select id, count(*) as count from " +
            "(select a.id, a.title from post a " +
            "left join liked b on a.id = b.contents_id " +
            "where b.content_type = 'POST') a " +
            "group by 1) c " +
            "right join post p on c.id = p.id " +
            "left join user u on u.id = p.user_id " +
            "where p.created_at between :startDate AND :endDate ", nativeQuery = true)
    Page<PostPageResponseDto> findPostPages(@Param("startDate") String startDate,
                                           @Param("endDate") String endDate,
                                           Pageable pageable);
}

/*  like 만드는 쿼리
    select p.id,
       u.id 'user_id',
       p.title,
       p.content,
       u.name username,
       p.created_at createAt,
       p.updated_at updateAt,
       IF(c.count is null,0,c.count) likecount
    from
        (select id, count(*) count
        from
        (select a.id, a.title
        from post a left join liked b on a.id=b.contents_id
        where b.contents_type='POST') a group by 1) c
            right join post p on c.id=p.id left join user u on u.id = p.user_id
    where p.created_at between '2024-05-01' AND '2024-06-09'
    order by
        likecount desc
    limit
        0, 5;
 */