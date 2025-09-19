package com.gitauto.drivecare.page;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PageService {

    private final PageRepository pageRepository;

    public PageService(PageRepository pageRepository) {
        this.pageRepository = pageRepository;
    }

    public PageEntity save(PageDto dto) {
        PageEntity entity = PageEntity.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
        return pageRepository.save(entity);
    }

    public List<PageEntity> findAll() {
        return pageRepository.findAll();
    }
}
