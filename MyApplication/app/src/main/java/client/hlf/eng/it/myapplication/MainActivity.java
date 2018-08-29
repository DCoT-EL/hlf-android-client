package client.hlf.eng.it.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import it.eng.hlf.android.client.FabricCustodyLedgerClient;
import it.eng.hlf.android.client.exception.HLFClientException;
import it.eng.hlf.android.client.model.ChainOfCustody;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String ENG_OP = "06c1b03d-6904-4c01-8d92-ad0c27a1f6c4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            int permissionCheck = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                //this means permission is granted and you can do read and write
            } else {
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
            final FabricCustodyLedgerClient fabricCustodyLedgerClient = new FabricCustodyLedgerClient();
            final Button TestEnd2End = findViewById(R.id.TestEnd2End);
            TestEnd2End.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ChainOfCustody chainOfCustody = new ChainOfCustody();
                    chainOfCustody.setDocumentId("AHUE2-24CEIHO-33989");
                    chainOfCustody.setText("FRAGILE");
                    chainOfCustody.setCodeOwner("shdhru-u4h234-bdcbs3-b4782");
                    chainOfCustody.setTrackingId("1C1998F051209");
                    chainOfCustody.setWeightOfParcel(1.3);
                    chainOfCustody.setDistributionOfficeCode("a1234_u4347");
                    chainOfCustody.setDistributionZone("Rome");
                    try {
                        ChainOfCustody chainOfCustody1 = fabricCustodyLedgerClient.initNewChain(chainOfCustody);
                        String assetID = chainOfCustody1.getId();
                        fabricCustodyLedgerClient.commentChain(assetID, "PRIORITY");
                        fabricCustodyLedgerClient.updateDocument(assetID, "NEW-DOCUMENT-ID");
                        ChainOfCustody chainOfCustody2 = fabricCustodyLedgerClient.getAssetDetails(assetID);
                        fabricCustodyLedgerClient.startTransfer(assetID, ENG_OP);
                        fabricCustodyLedgerClient.completeTransfer(assetID);
                        fabricCustodyLedgerClient.terminateChain(assetID);
                        List<ChainOfCustody> custodyLedgerClientList = fabricCustodyLedgerClient.getChainOfEvents(assetID);

                    } catch (HLFClientException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }

            });

            final Button TestGetChainOfEvents = findViewById(R.id.TestGetChainOfEvents1);
            TestGetChainOfEvents.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String assetID;
                    String text;

                    final ChainOfCustody chainOfCustody = new ChainOfCustody();
                    chainOfCustody.setDocumentId("1234");
                    try {
                        ChainOfCustody chainOfCustody1 = fabricCustodyLedgerClient.initNewChain(chainOfCustody);
                        assetID = chainOfCustody1.getId();
                        text = "comment chain!!";
                        fabricCustodyLedgerClient.commentChain(assetID, text);
                        fabricCustodyLedgerClient.updateDocument(assetID, "doc-3265sgs");
                        fabricCustodyLedgerClient.startTransfer(assetID, ENG_OP);
                        fabricCustodyLedgerClient.completeTransfer(assetID);
                        List<ChainOfCustody> chainOfCustodyList;
                        chainOfCustodyList = fabricCustodyLedgerClient.getChainOfEvents(assetID);
                    } catch (HLFClientException e) {
                        Log.e(TAG, e.getMessage());
                    }

                }

            });

            final Button TestGetChainOfEvents2 = findViewById(R.id.TestGetChainOfEvents2);
            final TextView GetEvents2 = findViewById(R.id.ViewGetEvents2);
            TestGetChainOfEvents2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ChainOfCustody chainOfCustody = new ChainOfCustody();
                    chainOfCustody.setDocumentId("TestGetChainOfEvents2");
                    try {

                        ChainOfCustody chainOfCustody1 = fabricCustodyLedgerClient.initNewChain(chainOfCustody);
                        String assetID = chainOfCustody1.getId();
                        fabricCustodyLedgerClient.updateDocument(assetID, "firstID");
                        fabricCustodyLedgerClient.updateDocument(assetID, "ALPHA-1");
                        fabricCustodyLedgerClient.updateDocument(assetID, "ALPHA-2");
                        fabricCustodyLedgerClient.updateDocument(assetID, "ALPHA-3");
                        fabricCustodyLedgerClient.updateDocument(assetID, "ALPHA-4");
                        fabricCustodyLedgerClient.updateDocument(assetID, "ALPHA-5");
                        fabricCustodyLedgerClient.updateDocument(assetID, "ALPHA-6");
                        fabricCustodyLedgerClient.updateDocument(assetID, "ALPHA-7");
                        fabricCustodyLedgerClient.updateDocument(assetID, "lastID");
                        List<ChainOfCustody> chainOfCustodyList = new ArrayList<>();
                        chainOfCustodyList = fabricCustodyLedgerClient.getChainOfEvents(assetID);
                    } catch (HLFClientException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }

            });

        } catch (HLFClientException e) {
            Log.e(TAG, e.getMessage());
        }
    }
}