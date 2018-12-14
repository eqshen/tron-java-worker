package io.mycred.tron.tronworker.demo;

import io.mycred.tron.tronworker.common.TronApi;
import io.mycred.tron.tronworker.enums.NetTypeEnum;
import io.mycred.tron.tronworker.model.AddressKey;

public class CreateAccountDemo {
    public static void main(String[] args) {
        AddressKey addressKey = TronApi.createNewAddress();
        System.out.println(addressKey);
    }
}
