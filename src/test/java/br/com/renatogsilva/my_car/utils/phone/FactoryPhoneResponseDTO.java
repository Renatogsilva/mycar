package br.com.renatogsilva.my_car.utils.phone;

import br.com.renatogsilva.my_car.model.dto.phone.PhoneResponseDTO;
import br.com.renatogsilva.my_car.model.enums.EnumTypePhone;

import java.util.ArrayList;
import java.util.List;

public class FactoryPhoneResponseDTO {

    public static PhoneResponseDTOBuilder phone() {
        return new PhoneResponseDTOBuilder();
    }

    public static PhoneResponseDTOListBuilder phoneList() {
        return new PhoneResponseDTOListBuilder();
    }

    // =========================
    // BUILDER UNITÁRIO
    // =========================
    public static class PhoneResponseDTOBuilder {

        private final PhoneResponseDTO dto;

        public PhoneResponseDTOBuilder() {
            this.dto = new PhoneResponseDTO();

            // DEFAULT (válido)
            dto.setPhoneId(1L);
            dto.setNumber("(62)98420-9870");
            dto.setTypePhone(EnumTypePhone.CELL_PHONE);
            dto.setMain(true);
        }

        public PhoneResponseDTOBuilder persisted() {
            dto.setPhoneId(1L);
            return this;
        }

        public PhoneResponseDTOBuilder withId(Long id) {
            dto.setPhoneId(id);
            return this;
        }

        public PhoneResponseDTOBuilder withNumber(String number) {
            dto.setNumber(number);
            return this;
        }

        public PhoneResponseDTOBuilder withType(EnumTypePhone type) {
            dto.setTypePhone(type);
            return this;
        }

        public PhoneResponseDTOBuilder main(boolean isMain) {
            dto.setMain(isMain);
            return this;
        }

        public PhoneResponseDTO build() {
            return dto;
        }
    }

    // =========================
    // BUILDER DE LISTA
    // =========================
    public static class PhoneResponseDTOListBuilder {

        private final List<PhoneResponseDTO> list;

        public PhoneResponseDTOListBuilder() {
            this.list = new ArrayList<>();

            // DEFAULT → lista com 2 telefones
            list.add(
                    FactoryPhoneResponseDTO.phone()
                            .withId(1L)
                            .withType(EnumTypePhone.CELL_PHONE)
                            .withNumber("(62)98420-9870")
                            .main(true)
                            .build()
            );

            list.add(
                    FactoryPhoneResponseDTO.phone()
                            .withId(2L)
                            .withType(EnumTypePhone.FIXED)
                            .withNumber("3353-8710")
                            .main(false)
                            .build()
            );
        }

        public PhoneResponseDTOListBuilder persisted() {
            // já está persistido por padrão (IDs definidos)
            return this;
        }

        public PhoneResponseDTOListBuilder withSingle(PhoneResponseDTO phone) {
            list.clear();
            list.add(phone);
            return this;
        }

        public PhoneResponseDTOListBuilder add(PhoneResponseDTO phone) {
            list.add(phone);
            return this;
        }

        public List<PhoneResponseDTO> build() {
            return list;
        }
    }
}
