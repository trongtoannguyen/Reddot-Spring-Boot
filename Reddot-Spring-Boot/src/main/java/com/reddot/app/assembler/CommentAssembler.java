package com.reddot.app.assembler;

import com.reddot.app.assembler.decorator.CommentMapperDecorator;
import com.reddot.app.dto.response.CommentDTO;
import com.reddot.app.entity.Comment;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserAssembler.class})
@DecoratedWith(CommentMapperDecorator.class)
public interface CommentAssembler {

    @Mapping(target = "commentId", source = "id")
    @Mapping(target = "questionId", source = "question.id")
    @Mapping(target = "author", source = "user")
    @Mapping(target = "creationDate", source = "createdAt")
    @Mapping(target = "lastEditDate", source = "updatedAt")
    @Mapping(target = "upvoted", ignore = true)
    @Mapping(target = "downvoted", ignore = true)
    CommentDTO toDTO(Comment comment);

    List<CommentDTO> toListDTO(List<Comment> comments);

}
