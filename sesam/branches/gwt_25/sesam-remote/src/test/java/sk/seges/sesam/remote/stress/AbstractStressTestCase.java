package sk.seges.sesam.remote.stress;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import sk.seges.sesam.remote.AbstractJMSTestCase;
import sk.seges.sesam.remote.client.JMSInvocationHandler;
import sk.seges.sesam.remote.domain.JMSBridgeConfiguration;
import sk.seges.sesam.remote.mock.DummyServiceImpl;
import sk.seges.sesam.remote.mock.IDummyService;
import sk.seges.sesam.remote.server.JMSCommandListener;

public abstract class AbstractStressTestCase extends AbstractJMSTestCase {
    protected IDummyService tds;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        config = createConfig();

        tds = new JMSInvocationHandler<IDummyService>((JMSBridgeConfiguration) config).createProxy(IDummyService.class);

        DummyServiceImpl tdsi = new DummyServiceImpl();
        new JMSCommandListener<IDummyService>((JMSBridgeConfiguration) config, tdsi);
    }

    protected abstract JMSBridgeConfiguration createConfig() throws Exception;
    protected void callTest(int resultCount, AbstractStressMethodCalls methodCalls, String methodName) throws Exception {
        long[][] elapsedTimes = new long[resultCount][2];
        for (int i = 0; i < resultCount; i++) {
            elapsedTimes[i][0] = System.currentTimeMillis();
            methodCalls.callMethods();
            elapsedTimes[i][1] = System.currentTimeMillis();
            methodCalls.afterCallMethods();
        }

        System.out.println("Starting creating output.");
        printResults(elapsedTimes, methodName);
        System.out.println("Finished creating output.");
    }
    
    
    protected void printResults(long[][] elapsedTimes, String method) throws IOException {
        long min = Long.MAX_VALUE, max = 0, avgSum = 0;

        FileWriter fw = null;
        try {
            fw = new FileWriter("target/stress_" + method + "_" + new SimpleDateFormat("yyyyMMDD_HHmm").format(new Date())
                    + ".csv");

            fw.write("iteartion;start;end\n");
            long diff;
            for (int i = 0; i < elapsedTimes.length; i++) {
                fw.write(i + ";" + elapsedTimes[i][0] + ";" + elapsedTimes[i][1] + "\n");

                diff = elapsedTimes[i][1] - elapsedTimes[i][0];
                if (diff < min)
                    min = diff;
                else if (diff > max)
                    max = diff;

                avgSum += diff;
            }

            System.out.println("Min = " + min + "(ms)");
            System.out.println("Max = " + max + "(ms)");
            System.out.println("Avg = " + avgSum / elapsedTimes.length + "(ms)");

        } finally {
            if (fw != null)
                fw.close();
        }
    }
}
