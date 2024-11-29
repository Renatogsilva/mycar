package br.com.renatogsilva.my_car.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeneralFunctions {

    private static Logger logger = LoggerFactory.getLogger(GeneralFunctions.class);

    public static boolean passwordMatch(BCryptPasswordEncoder bCryptPasswordEncoder,
                                        String usernameLoginResponseDTO, String usernameLoginRequestDTO) {

        logger.info("m: passwordMatch - verify passwords match");

        return bCryptPasswordEncoder.matches(usernameLoginRequestDTO, usernameLoginResponseDTO);
    }
}
