package com.example.newMock.Controller;

import com.example.newMock.Model.RequestDTO;
import com.example.newMock.Model.ResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class MainController {
    private final Logger log = LoggerFactory.getLogger(MainController.class);
    ObjectMapper mapper = new ObjectMapper();

    @PostMapping(
            value = "/info/postBalances",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE )
    public Object postBalances(@RequestBody RequestDTO requestDTO){
        try{
            String clientId = requestDTO.getClientId();
            char firstDigit = clientId.charAt(0);
            String currency = "";
            BigDecimal maxLimit;
            switch (firstDigit){
                case '8'->{
                    maxLimit =new BigDecimal(2000);
                    currency = "US";
                }
                case '9'->{
                    maxLimit =new BigDecimal(1000);
                    currency = "EU";
                }
                default -> {
                    maxLimit =new BigDecimal(10000);
                    currency = "RUB";
                }
            }
            BigDecimal balance = generateBalance(maxLimit);
            ResponseDTO responseDTO = new ResponseDTO();
            responseDTO.setRqUID(requestDTO.getRqUID());
            responseDTO.setClientId(clientId);
            responseDTO.setAccount(requestDTO.getAccount());
            responseDTO.setCurrency(currency);
            responseDTO.setMaxLimit(maxLimit);
            responseDTO.setBalance(balance);
            log.error("************** RequestDTO ************** " + mapper.writeValueAsString(requestDTO));
            log.error("************** ResponseDTO ************** " + mapper.writeValueAsString(responseDTO));
            return responseDTO;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    private BigDecimal generateBalance(BigDecimal maxLimit){
        int minValue = maxLimit.intValue() / 10;
        int maxValue = maxLimit.intValue();
        int randomValue = minValue + (int) (Math.random() * (maxValue - minValue + 1));
        return new BigDecimal(randomValue);


    }

}
