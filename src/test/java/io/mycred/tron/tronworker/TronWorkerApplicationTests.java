package io.mycred.tron.tronworker;

import com.google.gson.JsonObject;
import com.google.protobuf.ByteString;
import io.mycred.tron.tronworker.common.GrpcClient;
import io.mycred.tron.tronworker.common.TronApi;
import io.mycred.tron.tronworker.common.crypto.ECKey;
import io.mycred.tron.tronworker.common.crypto.Sha256Hash;
import io.mycred.tron.tronworker.common.utils.ByteArray;
import io.mycred.tron.tronworker.common.utils.TransactionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.tron.api.GrpcAPI;
import org.tron.protos.Contract;
import org.tron.protos.Protocol;

import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TronWorkerApplicationTests {

	@Autowired
	private TronApi tronApi;

	@Autowired
	private GrpcClient grpcClient;

	/*

		1.先创建一个有trx的钱包。https://explorer.shasta.trongrid.io/wallet/new

	*/


	/**
	 * 获取区块信息
	 */
	@Test
	public void testGetBlock() {
		System.out.println("ok");

//		GrpcAPI.BlockExtention block = tronApi.getBlockByNumber(234676);
		//参数小于0，则get now block
		GrpcAPI.BlockExtention block = this.grpcClient.getBlock2(-1);
		System.out.println(block.toString());
	}

	/**
	 * 测试获取账号带宽点数
	 */
	@Test
	public void testGetBandWidthInfo(){
		GrpcAPI.AccountNetMessage message =this.grpcClient.getAccountNet(TronApi.decodeFromBase58Check("TKwT3k3zJ6PJCVC7Gmx3jXKy9phg6q2Pom"));
		System.out.println(message);
//		NetUsed: 5639161
//		NetLimit: 47798079
//		TotalNetLimit: 43200000000
//		TotalNetWeight: 903802
	}

	@Test
	public void testSendTransaction(){
//		this.grpcClient.createTransaction2(Contract.WithdrawBalanceContract.newBuilder().build());
		String privateKey = "D95611A9AF2A2A45359106222ED1AFED48853D9A44DEFF8DC7913F5CBA727366";
		String toAddress = "TKMZBoWbXbYedcBnQugYT7DaFnSgi9qg78";
		//方法1
		GrpcAPI.EasyTransferResponse response = this.grpcClient.easyTransferByPrivate(ByteArray.fromHexString(privateKey),
				ByteArray.fromHexString(toAddress),100L);
		System.out.println(response);

		//方法2
		GrpcAPI.TransactionExtention transactionExtention = this.grpcClient.createTransaction2(Contract.WithdrawBalanceContract.newBuilder().build());
		Protocol.Transaction transaction = transactionExtention.getTransaction();
		//sign method 1
		ECKey ecKey = ECKey.fromPrivate(ByteArray.fromHexString(privateKey));
		byte[] rawdata = transaction.getRawData().toByteArray();
		byte[] hash = Sha256Hash.hash(rawdata);
		byte[] sign = ecKey.sign(hash).toByteArray();
		byte[] signedTransaction = transaction.toBuilder()
				.addSignature(ByteString.copyFrom(sign))
				.build()
				.toByteArray();
		// sign method 2
		Protocol.TransactionSign.Builder builder = Protocol.TransactionSign.newBuilder();
		builder.setPrivateKey(ByteString.copyFrom(ByteArray.fromHexString(privateKey)));
		builder.setTransaction(transaction);
		GrpcAPI.TransactionExtention transactionExtentionSigned = this.grpcClient.signTransaction2(builder.build());
		Protocol.Transaction transactionSigned = transactionExtentionSigned.getTransaction();

		//验证是否相等
		if(!Arrays.equals(signedTransaction,transactionSigned.toByteArray())){
			System.out.println("两种签名方法结果不一致");
		}
		//广播交易
		if(TransactionUtils.validTransaction(transactionSigned)){
			this.grpcClient.broadcastTransaction(transactionSigned);
		}

	}

	@Test
	public void testGetTransactionInfo(){
		String trxHash = "0ed4f39d17f6e8f379a6578a277b23401f8c1ffe00e92d01b2f4ec040a44e050";
		Protocol.Transaction transaction = this.grpcClient.getTransactionById(trxHash).get();
		System.out.println(transaction.toString());
		Protocol.TransactionInfo transactionInfo = this.grpcClient.getTransactionInfoById(trxHash).get();
		System.out.println(transactionInfo.toString());
	}

	public void testGetAccount(){
	}


}
