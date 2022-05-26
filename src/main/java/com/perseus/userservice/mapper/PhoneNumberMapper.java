package com.perseus.userservice.mapper;


import com.perseus.userservice.domain.PhoneNumber;
import com.perseus.userservice.service.dto.PhoneNumberDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link PhoneNumber} and its DTO {@link PhoneNumberDTO}.
 */
@Mapper(componentModel = "spring")
public interface PhoneNumberMapper extends EntityMapper<PhoneNumberDTO, PhoneNumber> {

}
