package com.gitauto.drivecare.page;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PageDto {
    private Long id;
    private String name;
    private String description;
}
