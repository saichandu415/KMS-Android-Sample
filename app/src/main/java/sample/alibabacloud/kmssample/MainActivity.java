package sample.alibabacloud.kmssample;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.kms.model.v20160120.CreateKeyRequest;
import com.aliyuncs.kms.model.v20160120.CreateKeyResponse;
import com.aliyuncs.kms.model.v20160120.DecryptRequest;
import com.aliyuncs.kms.model.v20160120.DecryptResponse;
import com.aliyuncs.kms.model.v20160120.DescribeKeyRequest;
import com.aliyuncs.kms.model.v20160120.DescribeKeyResponse;
import com.aliyuncs.kms.model.v20160120.EncryptRequest;
import com.aliyuncs.kms.model.v20160120.EncryptResponse;
import com.aliyuncs.kms.model.v20160120.GenerateDataKeyRequest;
import com.aliyuncs.kms.model.v20160120.GenerateDataKeyResponse;
import com.aliyuncs.kms.model.v20160120.ListKeysRequest;
import com.aliyuncs.kms.model.v20160120.ListKeysResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static DefaultAcsClient kmsClient;
    private static final String TAG = "MainActivity";

    String regionId = "cn-hangzhou";
    String accessKeyId = "LTAIgHTQBOgP0wY3";
    String accessKeySecret = "gemlASUdKp39AkOrSqFAnRMiQADIBF";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "===========================================");
        Log.d(TAG, "Getting Started with KMS Service");
        Log.d(TAG, "===========================================\n");
        /**
         * RegionId: "cn-hangzhou" and "ap-southeast-1", eg. "cn-hangzhou"
         */
         kmsClient = kmsClient(regionId, accessKeyId, accessKeySecret);

//         DoOnNetwork dON = new DoOnNetwork();
//         dON.execute(kmsClient);


    }

    private static DefaultAcsClient kmsClient(String regionId, String accessKeyId, String accessKeySecret) {
        /**
         * Construct an Aliyun Client:
         * Set RegionId, AccessKeyId and AccessKeySecret
         */
        IClientProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        return client;
    }
    private static CreateKeyResponse CreateKey(String keyDesc, String keyUsage) throws ClientException {
        final CreateKeyRequest ckReq = new CreateKeyRequest();
        ckReq.setProtocol(ProtocolType.HTTPS);
        ckReq.setAcceptFormat(FormatType.JSON);
        ckReq.setMethod(MethodType.POST);
        ckReq.setDescription(keyDesc);
        ckReq.setKeyUsage(keyUsage);
        final CreateKeyResponse response = kmsClient.getAcsResponse(ckReq);
        return response;
    }
    private static DescribeKeyResponse DescribeKey(String keyId) throws ClientException {
        final DescribeKeyRequest decKeyReq = new DescribeKeyRequest();
        decKeyReq.setProtocol(ProtocolType.HTTPS);
        decKeyReq.setAcceptFormat(FormatType.JSON);
        decKeyReq.setMethod(MethodType.POST);
        decKeyReq.setKeyId(keyId);
        final DescribeKeyResponse decKeyRes = kmsClient.getAcsResponse(decKeyReq);
        return decKeyRes;
    }
    private static ListKeysResponse ListKey(int pageNumber, int pageSize) throws ClientException {
        final ListKeysRequest listKeysReq = new ListKeysRequest();
        listKeysReq.setProtocol(ProtocolType.HTTPS);
        listKeysReq.setAcceptFormat(FormatType.JSON);
        listKeysReq.setMethod(MethodType.POST);
        listKeysReq.setPageNumber(pageNumber);
        listKeysReq.setPageSize(pageSize);
        final ListKeysResponse listKeysRes = kmsClient.getAcsResponse(listKeysReq);
        return listKeysRes;
    }
    private static GenerateDataKeyResponse GenerateDataKey(String keyId, String keyDesc, int numOfBytes) throws ClientException {
        final GenerateDataKeyRequest genDKReq = new GenerateDataKeyRequest();
        genDKReq.setProtocol(ProtocolType.HTTPS);
        genDKReq.setAcceptFormat(FormatType.JSON);
        genDKReq.setMethod(MethodType.POST);
        /**
         * Set parameter according to KMS openAPI document:
         * 1.KeyId
         * 2.KeyDescription
         * 3.NumberOfBytes
         */
        genDKReq.setKeySpec(keyDesc);
        genDKReq.setKeyId(keyId);
        genDKReq.setNumberOfBytes(numOfBytes);
        final GenerateDataKeyResponse genDKRes = kmsClient.getAcsResponse(genDKReq);
        return genDKRes;
    }
    private static EncryptResponse Encrypt(String keyId, String plainText) throws ClientException {
        final EncryptRequest encReq = new EncryptRequest();
        encReq.setProtocol(ProtocolType.HTTPS);
        encReq.setAcceptFormat(FormatType.JSON);
        encReq.setMethod(MethodType.POST);
        encReq.setKeyId(keyId);
        encReq.setPlaintext(plainText);
        final EncryptResponse encResponse = kmsClient.getAcsResponse(encReq);
        return encResponse;
    }
    private static DecryptResponse Decrypt(String cipherBlob) throws ClientException {
        final DecryptRequest decReq = new DecryptRequest();
        decReq.setProtocol(ProtocolType.HTTPS);
        decReq.setAcceptFormat(FormatType.JSON);
        decReq.setMethod(MethodType.POST);
        decReq.setCiphertextBlob(cipherBlob);
        final DecryptResponse decResponse = kmsClient.getAcsResponse(decReq);
        return decResponse;
    }

    static class DoOnNetwork extends AsyncTask<DefaultAcsClient,Void,Void>{
        String keyId = null;
        String plainText = "hello world";
        String cipherBlob = null;

        @Override
        protected Void doInBackground(DefaultAcsClient... defaultAcsClients) {

            kmsClient = defaultAcsClients[0];

            /*Create a Key*/
        try {
            final CreateKeyResponse response = CreateKey("testkey", "ENCRYPT/DECRYPT");

            /**
             * Parse response and do more further
             */
            System.out.println(response.getKeyMetadata());
            CreateKeyResponse.KeyMetadata meta = response.getKeyMetadata();

            System.out.println("CreateTime: " + meta.getCreationDate());
            System.out.println("Description: " + meta.getDescription());
            System.out.println("KeyId: " + meta.getKeyId());
            keyId = meta.getKeyId();
            System.out.println("KeyState: " + meta.getKeyState());
            System.out.println("KeyUsage: " + meta.getKeyUsage());

            System.out.println("===========================================");
            System.out.println("Create MasterKey Success!");
            System.out.println("===========================================\n");
        } catch (ClientException eResponse) {
            System.out.println("Failed.");
            System.out.println("Error code: " + eResponse.getErrCode());
            System.out.println("Error message: " + eResponse.getErrMsg());
        }

            try {
                final ListKeysResponse listKeysRes = ListKey(1, 100);
                /**
                 * Parse response and do more further
                 */
                Log.d(TAG, "TotalCount: " + listKeysRes.getTotalCount());
                Log.d(TAG, "PageNumber: " + listKeysRes.getPageNumber());
                Log.d(TAG, "PageSize: " + listKeysRes.getPageSize());
                List<ListKeysResponse.Key> keys = listKeysRes.getKeys();
                Iterator<ListKeysResponse.Key> iterator = keys.iterator();
                while (iterator.hasNext()) {
                    keyId = iterator.next().getKeyId();
                    Log.d(TAG, "KeyId: " + keyId);
                }
                Log.d(TAG, "===========================================");
                Log.d(TAG, "List All MasterKeys success!\n");
                Log.d(TAG, "===========================================\n");
            } catch (ClientException eResponse) {
                Log.d(TAG, "Failed.");
                Log.d(TAG, "Error code: " + eResponse.getErrCode());
                Log.d(TAG, "Error message: " + eResponse.getErrMsg());
            }
        /*Describe the Key */
            try {
                final DescribeKeyResponse decKeyRes = DescribeKey(keyId);
                /**
                 * Parse response and do more further
                 */
                Log.d(TAG, "DescribeKey Response: ");
                DescribeKeyResponse.KeyMetadata meta = decKeyRes.getKeyMetadata();
                Log.d(TAG, "KeyId: " + meta.getKeyId());
                Log.d(TAG, "Description: " + meta.getDescription());
                Log.d(TAG, "KeyState: " + meta.getKeyState());
                Log.d(TAG, "KeyUsage: " + meta.getKeyUsage());
                Log.d(TAG, "===========================================");
                Log.d(TAG, "Describe the MasterKey success!");
                Log.d(TAG, "===========================================\n");
            } catch (ClientException eResponse) {
                Log.d(TAG, "Failed.");
                Log.d(TAG, "Error code: " + eResponse.getErrCode());
                Log.d(TAG, "Error message: " + eResponse.getErrMsg());
            }
        /*Generate DataKey*/
            /**
             * Request and got response
             */
            try {
                final GenerateDataKeyResponse genDKResponse = GenerateDataKey(keyId, "AES_256", 64);
                /**
                 * Parse response and do more further
                 */
                Log.d(TAG, "CiphertextBlob: " + genDKResponse.getCiphertextBlob());
                Log.d(TAG, "KeyId: " + genDKResponse.getKeyId());
                Log.d(TAG, "Plaintext: " + genDKResponse.getPlaintext());
                Log.d(TAG, "===========================================");
                Log.d(TAG, "Generate DataKey success!");
                Log.d(TAG, "===========================================\n");
            } catch (ClientException eResponse) {
                Log.d(TAG, "Failed.");
                Log.d(TAG, "Error code: " + eResponse.getErrCode());
                Log.d(TAG, "Error message: " + eResponse.getErrMsg());
            }
            /**
             * Encrypt the plain text and got a cipher one
             */
            try {
                EncryptResponse encResponse = Encrypt(keyId, plainText);
                cipherBlob = encResponse.getCiphertextBlob();
                Log.d(TAG, "CiphertextBlob: " + cipherBlob);
                Log.d(TAG, "KeyId: " + encResponse.getKeyId());
                Log.d(TAG, "===========================================");
                Log.d(TAG, "Encrypt the plain text success!");
                Log.d(TAG, "===========================================\n");
            } catch (ClientException eResponse) {
                Log.d(TAG, "Failed.");
                Log.d(TAG, "Error code: " + eResponse.getErrCode());
                Log.d(TAG, "Error message: " + eResponse.getErrMsg());
            }
            /**
             * Decrypt the cipher text and verify result with original plain text.
             */
            try {
                DecryptResponse decResponse = Decrypt(cipherBlob);
                Log.d(TAG, "Plaintext: " + decResponse.getPlaintext());
                String verifyPlainText = decResponse.getPlaintext();
                int isMatch = verifyPlainText.compareTo(plainText);
                Log.d(TAG, "KeyId: " + decResponse.getKeyId());
                Log.d(TAG, "===========================================");
                Log.d(TAG, "Decrypt the cipher text success, result " + (isMatch == 0 ? "match" : "mismatch" + "\n"));
                Log.d(TAG, "===========================================\n");
            } catch (ClientException eResponse) {
                Log.d(TAG, "Failed.");
                Log.d(TAG, "Error code: " + eResponse.getErrCode());
                Log.d(TAG, "Error message: " + eResponse.getErrMsg());
            }

            return null;
        }
    }

}


