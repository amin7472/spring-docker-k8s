package com.perseus.userservice.mapper;


import com.perseus.userservice.domain.Contact;
import com.perseus.userservice.service.dto.ContactDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Contact} and its DTO {@link ContactDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ContactMapper extends EntityMapper<ContactDTO, Contact> {

}
