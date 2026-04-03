package br.com.renatogsilva.my_car.utils.phone;

import br.com.renatogsilva.my_car.model.dto.phone.PhoneRequestDTO;
import br.com.renatogsilva.my_car.model.enumerators.EnumTypePhone;

import java.util.ArrayList;
import java.util.List;

public class FactoryPhoneRequestDTO {

    public static PhoneRequestDTOBuilder phone() {
        return new PhoneRequestDTOBuilder();
    }

    public static PhoneRequestDTOListBuilder phoneList() {
        return new PhoneRequestDTOListBuilder();
    }

    public static class PhoneRequestDTOBuilder {

        private final PhoneRequestDTO dto;

        public PhoneRequestDTOBuilder() {
            this.dto = new PhoneRequestDTO();

            // DEFAULT (válido para criação)
            dto.setPhoneId(null);
            dto.setTypePhone(EnumTypePhone.CELL_PHONE);
            dto.setNumber("(62)98420-9870");
            dto.setIsMain(true);
        }

        public PhoneRequestDTOBuilder persisted() {
            dto.setPhoneId(1L);
            return this;
        }

        public PhoneRequestDTOBuilder withId(Long id) {
            dto.setPhoneId(id);
            return this;
        }

        public PhoneRequestDTOBuilder withNumber(String number) {
            dto.setNumber(number);
            return this;
        }

        public PhoneRequestDTOBuilder withType(EnumTypePhone type) {
            dto.setTypePhone(type);
            return this;
        }

        public PhoneRequestDTOBuilder asMain() {
            dto.setIsMain(true);
            return this;
        }

        public PhoneRequestDTOBuilder asSecondary() {
            dto.setIsMain(false);
            return this;
        }

        public PhoneRequestDTO build() {
            return dto;
        }
    }

    public static class PhoneRequestDTOListBuilder {

        private final List<PhoneRequestDTO> list;

        public PhoneRequestDTOListBuilder() {
            this.list = new ArrayList<>();

            list.add(
                    FactoryPhoneRequestDTO.phone()
                            .withType(EnumTypePhone.CELL_PHONE)
                            .withNumber("(62)98420-9870")
                            .asMain()
                            .build()
            );

            list.add(
                    FactoryPhoneRequestDTO.phone()
                            .withType(EnumTypePhone.FIXED)
                            .withNumber("3353-8710")
                            .asSecondary()
                            .build()
            );
        }

        public PhoneRequestDTOListBuilder persisted() {
            list.clear();

            list.add(
                    FactoryPhoneRequestDTO.phone()
                            .persisted()
                            .withId(1L)
                            .withType(EnumTypePhone.CELL_PHONE)
                            .withNumber("(62)98420-9870")
                            .asMain()
                            .build()
            );

            list.add(
                    FactoryPhoneRequestDTO.phone()
                            .withId(2L)
                            .withType(EnumTypePhone.FIXED)
                            .withNumber("3353-8710")
                            .asSecondary()
                            .build()
            );

            return this;
        }

        public PhoneRequestDTOListBuilder withPhones(List<PhoneRequestDTO> phones) {
            list.clear();
            list.addAll(phones);
            return this;
        }

        public PhoneRequestDTOListBuilder addPhone(PhoneRequestDTO phone) {
            list.add(phone);
            return this;
        }

        public List<PhoneRequestDTO> build() {
            return list;
        }
    }
}
