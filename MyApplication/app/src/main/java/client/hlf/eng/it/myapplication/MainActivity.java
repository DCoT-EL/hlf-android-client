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

import it.eng.hlf.android.client.FabricCustodyLedgerClient;
import it.eng.hlf.android.client.exception.HLFClientException;
import it.eng.hlf.android.client.model.ChainOfCustody;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MY-APPLICATION";


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

            FabricCustodyLedgerClient fabricCustodyLedgerClient = new FabricCustodyLedgerClient();

            final Button init = findViewById(R.id.initNewChain);
            init.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        ChainOfCustody chainOfCustody = new ChainOfCustody();
                        chainOfCustody.setTrackingId("APPQP23D#");
                        fabricCustodyLedgerClient.initNewChain(chainOfCustody);

                    } catch (HLFClientException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }

            });

            final Button start = findViewById(R.id.startTransfer);
            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        fabricCustodyLedgerClient.startTransfer("123haghhd", "Franco");
                    } catch (HLFClientException e) {
                        Log.e(TAG, e.getMessage());
                    }

                }

            });

            final Button comment = findViewById(R.id.commentChain);
            init.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        fabricCustodyLedgerClient.commentChain("123haghhd", "contains glass!!Watch out!!!");

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
