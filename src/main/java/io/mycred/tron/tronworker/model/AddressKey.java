package io.mycred.tron.tronworker.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressKey {
    private String publicKey;
    private String secretKey;
    private String address;
}
