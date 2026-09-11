package org.example.spring.lab.post.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class PostDto {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class AddPostReq {
        private String title;
        private String contents;

        public Post toEntity() {
            return Post.builder()
                    .title(this.title)
                    .contents(this.contents)
                    .build();
        }
    }

    @Getter
    @Builder
    public static class AddPostRes {
        private Long id;
        private String title;
        private String contents;

        public static AddPostRes from(Post entity) {
            return AddPostRes.builder()
                    .id(entity.getId())
                    .title(entity.getTitle())
                    .contents(entity.getContents())
                    .build();
        }

    }

}
