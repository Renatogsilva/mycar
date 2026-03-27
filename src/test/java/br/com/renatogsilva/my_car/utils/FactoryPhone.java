package br.com.renatogsilva.my_car.utils;

import br.com.renatogsilva.my_car.model.domain.Phone;
import br.com.renatogsilva.my_car.model.dto.phone.PhoneRequestDTO;
import br.com.renatogsilva.my_car.model.dto.phone.PhoneResponseDTO;
import br.com.renatogsilva.my_car.model.enumerators.EnumTypePhone;

import java.util.ArrayList;
import java.util.List;

public class FactoryPhone {

    public static PhoneRequestDTO createPhoneRequestDTOObjectValid() {
        PhoneRequestDTO phoneRequestDTO = new PhoneRequestDTO();

        phoneRequestDTO.setPhoneId(null);
        phoneRequestDTO.setTypePhone(EnumTypePhone.FIXED);
        phoneRequestDTO.setNumber("(62)98420-9870");
        phoneRequestDTO.setIsMain(true);

        return phoneRequestDTO;
    }

    public static List<PhoneRequestDTO> createListPhoneRequestDTOObjectValid() {
        ArrayList<PhoneRequestDTO> listPhoneRequestDTO = new ArrayList<>();
        PhoneRequestDTO phoneRequestDTO = new PhoneRequestDTO();
        PhoneRequestDTO fixPhoneRequestDTO = new PhoneRequestDTO();

        phoneRequestDTO.setPhoneId(null);
        phoneRequestDTO.setTypePhone(EnumTypePhone.CELL_PHONE);
        phoneRequestDTO.setNumber("(62)98420-9870");
        phoneRequestDTO.setIsMain(true);

        fixPhoneRequestDTO.setPhoneId(null);
        fixPhoneRequestDTO.setTypePhone(EnumTypePhone.FIXED);
        fixPhoneRequestDTO.setNumber("3353-8710");
        fixPhoneRequestDTO.setIsMain(false);

        listPhoneRequestDTO.add(phoneRequestDTO);
        listPhoneRequestDTO.add(fixPhoneRequestDTO);

        return listPhoneRequestDTO;
    }

    public static List<PhoneResponseDTO> createListPhoneResponseDTOObjectValid() {
        List<PhoneResponseDTO> list = new ArrayList<>();

        PhoneResponseDTO phone = new PhoneResponseDTO();
        PhoneResponseDTO fixed = new PhoneResponseDTO();

        phone.setPhoneId(1L);
        phone.setTypePhone(EnumTypePhone.CELL_PHONE);
        phone.setNumber("(62)98420-9870");
        phone.setMain(true);

        fixed.setPhoneId(2L);
        fixed.setTypePhone(EnumTypePhone.CELL_PHONE);
        fixed.setNumber("(62)98420-9870");
        fixed.setMain(true);

        list.add(phone);
        list.add(fixed);

        return list;
    }

    public static List<Phone> createListPhoneEntityObjectValid() {
        List<Phone> phones = new ArrayList<>();
        Phone fix = new Phone(null, "3353-1011", EnumTypePhone.FIXED, false, null);
        Phone phone = new Phone(null, "(62)98451-2578", EnumTypePhone.CELL_PHONE, true, null);

        phones.add(fix);
        phones.add(phone);

        return phones;
    }

    public static List<Phone> createdListPhoneEntityObjectValid() {
        List<Phone> phones = new ArrayList<>();
        Phone fix = new Phone(1L, "3353-1011", EnumTypePhone.FIXED, false, null);
        Phone phone = new Phone(2L, "(62)9 8740-9841", EnumTypePhone.CELL_PHONE, true, null);

        phones.add(fix);
        phones.add(phone);

        return phones;
    }
}
