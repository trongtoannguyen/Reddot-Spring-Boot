package com.reddot.app.assembler;

import com.reddot.app.dto.response.TagDTO;
import com.reddot.app.entity.Tag;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TagAssembler {
    TagDTO toDTO(Tag tag);
}