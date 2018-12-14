package io.mycred.tron.tronworker;

import io.mycred.tron.tronworker.common.TronApi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.tron.protos.Protocol;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TronWorkerApplicationTests {

	@Autowired
	private TronApi tronApi;

	/*

		1.先创建一个有trx的钱包。https://explorer.shasta.trongrid.io/wallet/new

	*/


	@Test
	public void testGetBlock() {
		System.out.println("ok");

		Protocol.Block block = tronApi.getBlockByNumber(434441);
		System.out.println(block);
	}




}
