package it.eng.hlf.android.client;

import java.util.List;

import it.eng.hlf.android.client.exception.HLFClientException;
import it.eng.hlf.android.client.model.ChainOfCustody;

/**
 * @author clod16
 */
public interface CustodyLedgerClient {

    ChainOfCustody initNewChain(ChainOfCustody chainOfCustody) throws HLFClientException;

    void startTransfer(String id, String receiverId) throws HLFClientException;

    void completeTransfer(String id) throws HLFClientException;

    void commentChain(String id, String text) throws HLFClientException;

    void cancelTransfer(String id) throws HLFClientException;

    void terminateChain(String id) throws HLFClientException;

    ChainOfCustody updateDocument(String id, String docuementId) throws HLFClientException;

    ChainOfCustody getAssetDetails(String id) throws HLFClientException;

    List<ChainOfCustody> getChainOfEvents(String id) throws HLFClientException;
}
