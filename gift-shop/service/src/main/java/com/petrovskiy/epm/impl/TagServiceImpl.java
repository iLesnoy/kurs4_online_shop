package com.petrovskiy.epm.impl;

import com.petrovskiy.epm.TagService;
import com.petrovskiy.epm.dao.GiftCertificateRepository;
import com.petrovskiy.epm.dao.TagRepository;
import com.petrovskiy.epm.dto.TagDto;
import com.petrovskiy.epm.exception.SystemException;
import com.petrovskiy.epm.mapper.TagMapper;
import com.petrovskiy.epm.model.Tag;
import com.petrovskiy.epm.validator.EntityValidator;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.petrovskiy.epm.exception.ExceptionCode.NON_EXISTENT_ENTITY;
import static com.petrovskiy.epm.exception.ExceptionCode.USED_ENTITY;


@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final EntityValidator entityValidator;
    private final GiftCertificateRepository giftCertificateRepository;
    private final TagMapper tagMapper;


    @Autowired
    public TagServiceImpl(TagRepository tagRepository,EntityValidator entityValidator,
                          GiftCertificateRepository giftCertificateRepository,TagMapper tagMapper) {
        this.tagRepository = tagRepository;
        this.entityValidator = entityValidator;
        this.giftCertificateRepository = giftCertificateRepository;
        this.tagMapper = tagMapper;
    }

    @Override
    @Transactional
    public TagDto create(TagDto tagDto) {
        Tag tag = createTag(tagMapper.dtoToTag(tagDto));
        return tagMapper.tagToDto(tag);
    }

    public Tag createTag(Tag tag) {
        return tagRepository.findByName(tag.getName()).orElseGet(() -> tagRepository.save(tag));
    }

    @Override
    public TagDto update(Long id, TagDto tagDto) {
        Tag updatedTag = tagRepository.findById(id).orElseGet(()-> tagRepository.save(tagMapper.dtoToTag(tagDto)));
        return tagMapper.tagToDto(updatedTag);
    }

    @Override
    @Transactional
    public Page<TagDto> findAll(Pageable pageable) {
        Page<Tag> tagPage = tagRepository.findAll(pageable);
        return new PageImpl<>(tagPage.getContent(), tagPage.getPageable(), tagPage.getTotalElements())
                .map(tagMapper::tagToDto);
    }


    @Override
    public TagDto findById(Long id) {
        return tagMapper.tagToDto(findTagById(id));
    }

    private Tag findTagById(Long id) {
        return tagRepository.findById(id).orElseThrow(() -> new SystemException(NON_EXISTENT_ENTITY));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Optional<Tag> optionalTag = tagRepository.findById(id);
        if (optionalTag.isPresent()) {
            if (giftCertificateRepository.findFirstByTagList_Id(id).isPresent()) {
                throw new SystemException(USED_ENTITY);
            }
            tagRepository.delete(optionalTag.get());
        }else throw new SystemException(NON_EXISTENT_ENTITY);
    }

}
