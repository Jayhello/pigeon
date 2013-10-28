/**
 * 
 */
package com.dianping.pigeon.test.client_1.x.benchmark.call;

import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;

import com.dianping.pigeon.test.client_1.x.AnnotationBaseInvokerTest;
import com.dianping.pigeon.test.client_1.x.PigeonAutoTest;
import com.dianping.pigeon.test.service.EchoService;

/**
 * @author xiangwu
 * 
 */
public class DefaultTest extends AnnotationBaseInvokerTest {

	@PigeonAutoTest(serviceName = "http://service.dianping.com/testService/echoService_1.0.0", timeout = 500000)
	public EchoService echoService;
	
	static AtomicLong counter = new AtomicLong(0);

	@Test
	public void test() throws Throwable {
		int threads = 10;
		for (int i = 0; i < threads; i++) {
			ClientThread thread = new ClientThread(echoService);
			thread.start();
		}
	}

	public static void main(String[] args) throws Exception {

	}

	class ClientThread extends Thread {

		EchoService service = null;
		long startTime = 0;

		public ClientThread(EchoService service) {
			this.service = service;
			startTime = System.currentTimeMillis();
		}

		public void run() {
			try {
				while (true) {

					String msg = System.currentTimeMillis() + ""
							+ Math.abs(RandomUtils.nextLong());
					//System.out.println(msg);
					String echo = service.echo(msg);
					//System.out.println(echo);
					//Assert.assertEquals("echo:" + msg, echo);
					long count = counter.addAndGet(1);
					if(count % 1000 == 0) {
						long cost = System.currentTimeMillis() - startTime;
						float tps = count / (cost/1000);
						System.out.println("count:" + count + ",tps:" + tps);
					}
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}
}