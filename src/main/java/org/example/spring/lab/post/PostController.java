package org.example.spring.lab.post;


import lombok.RequiredArgsConstructor;
import org.example.spring.lab.post.model.Post;
import org.example.spring.lab.post.model.PostDto;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/new")
    public PostDto.AddPostRes addPostRes(@RequestBody PostDto.AddPostReq dto) {

        Post post = dto.toEntity();
        Post savedPost = postService.reg(post);
        return PostDto.AddPostRes.from(savedPost);

    }

}
