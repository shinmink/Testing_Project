package com.sparta.wildcard_newsfeed.domain.post.service;

import com.sparta.wildcard_newsfeed.domain.post.dto.PostPageRequestDto;
import com.sparta.wildcard_newsfeed.domain.post.dto.PostRequestDto;
import com.sparta.wildcard_newsfeed.domain.post.dto.PostResponseDto;
import com.sparta.wildcard_newsfeed.domain.post.entity.Post;
import com.sparta.wildcard_newsfeed.domain.post.repository.PostRepository;
import com.sparta.wildcard_newsfeed.domain.user.entity.User;
import com.sparta.wildcard_newsfeed.domain.user.repository.UserRepository;
import com.sparta.wildcard_newsfeed.security.AuthenticationUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public PostResponseDto addPost(PostRequestDto postRequestDto, AuthenticationUser user) {
        User byUsercode = userRepository.findByUsercode(user.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Post post = new Post(postRequestDto, byUsercode);
        postRepository.save(post);
        return new PostResponseDto(post);
    }

    public PostResponseDto findById(long id) {
        Post post = findPostById(id);
        return new PostResponseDto(post);
    }

    public List<PostResponseDto> findAll() {
        List<Post> postlist = postRepository.findAll();
        return postlist.stream()
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                .map(PostResponseDto::new)
                .toList();
    }

    @Transactional
    public PostResponseDto updatePost(PostRequestDto postRequestDto, Long postId, AuthenticationUser user) {
        Post post = findPostById(postId);

        validateUser(post, user);

        post.update(postRequestDto);
        postRepository.save(post);
        return new PostResponseDto(post);
    }

    @Transactional
    public void deletePost(Long postId, AuthenticationUser user) {
        Post post = findPostById(postId);

        validateUser(post, user);

        postRepository.delete(post);
    }

    private Post findPostById(long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));
    }

    private void validateUser(Post post, AuthenticationUser user) {
        if (!post.getUser().getUsercode().equals(user.getUsername())) {
            throw new IllegalArgumentException("작성자만 할 수 있습니다.");
        }
    }

    @Transactional
    public Page<PostResponseDto> getPostPage(PostPageRequestDto requestDto) {
        log.info(requestDto.toString());

        Sort.Direction direction = Sort.Direction.DESC; //ASC 오름차순 , DESC 내림차순
        //- 생성일자 기준 최신 - 좋아요 많은 순

        // --- 정렬 방식 ---
        //create  or  liked
        String sortBy = "createdAt";
        if(requestDto.getSortBy().equals("CREATE")){
            sortBy = "createdAt";
        }
        else if(requestDto.getSortBy().equals("LIKED")){
            sortBy = "liked";
        }
        else
            throw new IllegalArgumentException("정렬은 CREATE 또는 LIKED 만 입력 가능합니다.");

        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(requestDto.getPage()-1, requestDto.getSize(), sort);

        Page<Post> postList;

        //---날짜 부분 ---
        LocalDate lastDate;
        LocalDate firstDate;

        if (requestDto.getLastDate() == null && requestDto.getFirstDate() == null) {
            // null 이면 모든 날짜를 조회
            postList = postRepository.findAll(pageable);
        }
        else {
            // 날짜 정보가 있으면 해당 날짜만 조회
            try {
                lastDate = LocalDate.parse(requestDto.getLastDate());
                firstDate = LocalDate.parse(requestDto.getFirstDate());
            }
            catch (Exception e) {
                throw new IllegalArgumentException("날짜 포맷이 정상적이지 않습니다.");
            }

            if (firstDate.isAfter(lastDate)) {
                throw new IllegalArgumentException("시작일이 마지막일 보다 미래에 있습니다.");
            }
            postList = postRepository.findAllByCreatedAtBetween(firstDate.atStartOfDay(),lastDate.atTime(LocalTime.MAX), pageable);
            //LocalDate는 쿼리로 못쓰고 LocalDateTime만 가능한가?
        }



        log.error("total page : "+postList.getTotalPages());
        if(postList.getTotalElements() <= 0){
            log.error("페이지 없음");
            throw new IllegalArgumentException("페이지가 존재하지 않습니다.");
        }
        if(postList.getTotalPages() < requestDto.getPage() ){
            throw new IllegalArgumentException("유효한 페이지 번호가 아닙니다.");
        }


        Page<PostResponseDto> responseDtoList = postList.map(PostResponseDto::new);

        return responseDtoList;
    }
}