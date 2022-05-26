package com.perseus.userservice.mapper;


import com.perseus.userservice.domain.Email;
import com.perseus.userservice.service.dto.EmailDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Email} and its DTO {@link EmailDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmailMapper extends EntityMapper<EmailDTO, Email> {

}
