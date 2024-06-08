package com.sparta.wildcard_newsfeed.domain.post.service;

import com.sparta.wildcard_newsfeed.domain.file.service.FileService;
import com.sparta.wildcard_newsfeed.domain.post.dto.PostRequestDto;
import com.sparta.wildcard_newsfeed.domain.post.dto.PostResponseDto;
import com.sparta.wildcard_newsfeed.domain.post.entity.Post;
import com.sparta.wildcard_newsfeed.domain.post.entity.PostMedia;
import com.sparta.wildcard_newsfeed.domain.post.repository.PostMediaRepository;
import com.sparta.wildcard_newsfeed.domain.post.repository.PostRepository;
import com.sparta.wildcard_newsfeed.domain.user.entity.User;
import com.sparta.wildcard_newsfeed.domain.user.repository.UserRepository;
import com.sparta.wildcard_newsfeed.exception.customexception.FileException;
import com.sparta.wildcard_newsfeed.security.AuthenticationUser;
import com.sparta.wildcard_newsfeed.util.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostMediaRepository postMediaRepository;
    private final FileService fileService;
    private final FileUtils fileUtils;

    @Transactional
    public PostResponseDto addPost(PostRequestDto postRequestDto, AuthenticationUser user) {
        User byUsercode = userRepository.findByUsercode(user.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Post post = new Post(postRequestDto, byUsercode);
        List<PostMedia> postMediaList = new ArrayList<>();
        if (!postRequestDto.getFiles().isEmpty()) {
            if (postRequestDto.getFiles().size() > 5) {
                throw new FileException("한 게시물당 최대 5개까지만 저장 가능합니다.");
            }
            fileUtils.validFile(postRequestDto.getFiles());

            for (MultipartFile file : postRequestDto.getFiles()) {
                String s3Url = fileService.uploadFileToS3(file);
                PostMedia postMedia = new PostMedia();
                postMedia.setUrl(s3Url);
                postMedia.setPost(post);
                postMedia.setType(fileUtils.extractExtension(file.getOriginalFilename()));
                postMediaList.add(postMedia);
            }
        }
        postRepository.save(post);
        List<String> s3Urls = new ArrayList<>();
        if (!postMediaList.isEmpty()) {
            postMediaRepository.saveAll(postMediaList);
             s3Urls = postMediaList.stream().map(PostMedia::getUrl).toList();
        }
        return new PostResponseDto(post, s3Urls);
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
}