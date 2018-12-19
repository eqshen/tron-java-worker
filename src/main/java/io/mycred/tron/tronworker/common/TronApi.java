package io.mycred.tron.tronworker.common;

import io.mycred.tron.tronworker.common.crypto.ECKey;
import io.mycred.tron.tronworker.common.crypto.Sha256Hash;
import io.mycred.tron.tronworker.common.utils.Base58;
import io.mycred.tron.tronworker.common.utils.ByteArray;
import io.mycred.tron.tronworker.common.utils.Utils;
import io.mycred.tron.tronworker.config.TronConfiguration;
import io.mycred.tron.tronworker.enums.CommonConstant;
import io.mycred.tron.tronworker.enums.NetTypeEnum;
import io.mycred.tron.tronworker.model.AddressKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.tron.api.GrpcAPI;
import org.tron.protos.Protocol;

import javax.annotation.PostConstruct;

@Service
public class TronApi {

    @Autowired
    private TronConfiguration tronConfiguration;

    @Autowired
    private GrpcClient grpcClient;

    public static  NetTypeEnum netTypeEnum = NetTypeEnum.TESTNET;

    @PostConstruct
    private void init(){
        netTypeEnum = tronConfiguration.getNetType();
    }


    public GrpcAPI.BlockExtention getBlockByNumber(long blockNumber){
        return grpcClient.getBlock2(blockNumber);
    }




    public static  boolean addressValid(byte[] address) {
        if (address == null || address.length == 0) {
            return false;
        }
        if (address.length != CommonConstant.ADDRESS_SIZE) {
            return false;
        }
        byte preFixbyte = address[0];
        if (preFixbyte != getAddressPreFixByte()) {
            return false;
        }
        //Other rule;
        return true;
    }


    public static  byte[] decodeFromBase58Check(String addressBase58) {
        if (StringUtils.isEmpty(addressBase58)) {
            return null;
        }
        byte[] address = decode58Check(addressBase58);
        if (!addressValid(address)) {
            return null;
        }
        return address;
    }

    public static String encode58Check(byte[] input) {
        byte[] hash0 = Sha256Hash.hash(input);
        byte[] hash1 = Sha256Hash.hash(hash0);
        byte[] inputCheck = new byte[input.length + 4];
        System.arraycopy(input, 0, inputCheck, 0, input.length);
        System.arraycopy(hash1, 0, inputCheck, input.length, 4);
        return Base58.encode(inputCheck);
    }

    private static byte[] decode58Check(String input) {
        byte[] decodeCheck = Base58.decode(input);
        if (decodeCheck.length <= 4) {
            return null;
        }
        byte[] decodeData = new byte[decodeCheck.length - 4];
        System.arraycopy(decodeCheck, 0, decodeData, 0, decodeData.length);
        byte[] hash0 = Sha256Hash.hash(decodeData);
        byte[] hash1 = Sha256Hash.hash(hash0);
        if (hash1[0] == decodeCheck[decodeData.length] &&
                hash1[1] == decodeCheck[decodeData.length + 1] &&
                hash1[2] == decodeCheck[decodeData.length + 2] &&
                hash1[3] == decodeCheck[decodeData.length + 3]) {
            return decodeData;
        }
        return null;
    }


    public static  byte getAddressPreFixByte() {
        if(NetTypeEnum.TESTNET == netTypeEnum){
            return CommonConstant.ADD_PRE_FIX_BYTE_TESTNET;
        }else{
            return CommonConstant.ADD_PRE_FIX_BYTE_MAINNET;
        }
    }

    public static AddressKey createNewAddress(){
        ECKey ecKey = new ECKey(Utils.getRandom());
        byte[] publicKey = ecKey.getPubKey();
        byte[] privateKey = ecKey.getPrivKeyBytes();
        String address = encode58Check(ecKey.getAddress());
        return AddressKey.builder()
                .address(address)
                .secretKey(ByteArray.toHexString(privateKey))
                .publicKey(ByteArray.toHexString(publicKey))
                .build();
    }

    public static void main(String[] args) {
        AddressKey addressKey = createNewAddress();
        System.out.println(addressKey);
    }

}
