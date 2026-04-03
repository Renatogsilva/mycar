package br.com.renatogsilva.my_car.utils.phone;

import br.com.renatogsilva.my_car.model.domain.Person;
import br.com.renatogsilva.my_car.model.domain.Phone;
import br.com.renatogsilva.my_car.model.enumerators.EnumTypePhone;

import java.util.List;

public class FactoryPhone {
    public static PhoneBuilder phone() {
        return new PhoneBuilder();
    }

    public static class PhoneBuilder {

        private final Phone phone;

        public PhoneBuilder() {
            this.phone = new Phone();

            // DEFAULT (válido)
            phone.setPhoneId(null);
            phone.setNumber("(62)98420-9870");
            phone.setTypePhone(EnumTypePhone.CELL_PHONE);
            phone.setIsMain(true);
            phone.setPerson(null);
        }

        public PhoneBuilder persisted() {
            phone.setPhoneId(1L);
            return this;
        }

        public PhoneBuilder withId(Long id) {
            phone.setPhoneId(id);
            return this;
        }

        public PhoneBuilder withNumber(String number) {
            phone.setNumber(number);
            return this;
        }

        public PhoneBuilder withType(EnumTypePhone type) {
            phone.setTypePhone(type);
            return this;
        }

        public PhoneBuilder main(boolean isMain) {
            phone.setIsMain(isMain);
            return this;
        }

        public PhoneBuilder withPerson(Person person) {
            phone.setPerson(person);
            return this;
        }

        public Phone build() {
            return phone;
        }
    }

    public static List<Phone> listToPersist() {
        return List.of(
                phone()
                        .withType(EnumTypePhone.CELL_PHONE)
                        .withNumber("(62)98420-9870")
                        .main(true)
                        .build(),

                phone()
                        .withType(EnumTypePhone.FIXED)
                        .withNumber("3353-1011")
                        .main(false)
                        .build()
        );
    }

    public static List<Phone> listPersisted() {
        return List.of(
                phone()
                        .persisted()
                        .withId(1L)
                        .withType(EnumTypePhone.CELL_PHONE)
                        .withNumber("(62)98420-9870")
                        .main(true)
                        .build(),

                phone()
                        .persisted()
                        .withId(2L)
                        .withType(EnumTypePhone.FIXED)
                        .withNumber("3353-1011")
                        .main(false)
                        .build()
        );
    }
}