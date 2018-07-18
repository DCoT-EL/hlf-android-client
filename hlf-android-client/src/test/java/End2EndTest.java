import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import it.eng.hlf.android.client.FabricCustodyLedgerClient;
import it.eng.hlf.android.client.exception.HLFClientException;
import it.eng.hlf.android.client.model.ChainOfCustody;

import static org.junit.Assert.assertFalse;

public class End2EndTest {

static  FabricCustodyLedgerClient fabricCustodyLedgerClient;
    @BeforeClass
    public static void begin() {
        try {
             fabricCustodyLedgerClient = new FabricCustodyLedgerClient();
        } catch (HLFClientException e) {
            assertFalse(e.getMessage(), true);
        }
    }

    @AfterClass
    public static void end() { fabricCustodyLedgerClient = null;
    }


    @Test
    public void testInitNewChain(){
        final ChainOfCustody chainOfCustody = new ChainOfCustody();
        chainOfCustody.setTrackingId("1234");
        chainOfCustody.setCodeOwner("ABCD");
        chainOfCustody.setDocumentId("15262HSGHDGHE");
        try {
            ChainOfCustody chainOfCustody1 = fabricCustodyLedgerClient.initNewChain(chainOfCustody);
            if(chainOfCustody1.getId() != null){
                assertFalse(false);
            }
        } catch (HLFClientException e) {
            assertFalse(e.getMessage(), true);
        }

    }

    @Test
    public void testStartTransfer(){


    }

    @Test
    public void testCompleteTransfer(){

    }

    @Test
    public void testCommentChain(){

    }

    @Test
    public void testCancelTransfer(){

    }
    @Test
    public void testTerminateChain(){

    }
    @Test
    public void testUpdateDocument(){

    }

    @Test
    public void testGetAssetDetails(){

    }
    @Test
    public void testGetChainOfEvents(){

    }
}
