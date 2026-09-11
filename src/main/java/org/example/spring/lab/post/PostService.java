package org.example.spring.lab.post;

import lombok.RequiredArgsConstructor;
import org.example.spring.lab.post.model.Post;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public Post reg(Post entity) {
            return postRepository.save(entity);
    }

}
