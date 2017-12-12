package sample.alibabacloud.kmssample;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    static DefaultAcsClient kmsClient;
    private static final String TAG = "MainActivity";

    String regionId ;
    String accessKeyId ;
    String accessKeySecret ;

    TextView output;
    TextInputEditText userName,passWord,email,address;
    Button eCredentials,dCredentials, eForm, dForm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        regionId = getString(R.string.regionId);
        accessKeyId = getString(R.string.AccessKey);
        accessKeySecret = getString(R.string.AccessKeySecret);

        output = findViewById(R.id.output);
        userName = findViewById(R.id.userName);
        passWord = findViewById(R.id.passWord);
        email = findViewById(R.id.email);
        address = findViewById(R.id.address);

        eCredentials = findViewById(R.id.eCredentials);
        dCredentials = findViewById(R.id.dCredentials);
        eForm = findViewById(R.id.eForm);
        dForm = findViewById(R.id.dForm);

        eCredentials.setOnClickListener(this);
        dCredentials.setOnClickListener(this);
        eForm.setOnClickListener(this);
        dForm.setOnClickListener(this);

        Log.d(TAG, "===========================================");
        Log.d(TAG, "KMS Service started");
        Log.d(TAG, "===========================================\n");
        /**
         * RegionId: "cn-hangzhou" and "ap-southeast-1", eg. "cn-hangzhou"
         */
         kmsClient = kmsClient(regionId, accessKeyId, accessKeySecret);





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

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.eCredentials){
            EncryptCredentials encryptCredentials = new EncryptCredentials();
            encryptCredentials.execute(kmsClient);
        }else if(id == R.id.dCredentials){
            DecryptCredentials decryptCredentials = new DecryptCredentials(this);
            decryptCredentials.execute(kmsClient);
        }else if(id == R.id.eForm){
            EncryptFormData encryptFormData = new EncryptFormData();
            encryptFormData.execute(kmsClient);
        }else if(id == R.id.dForm) {
            DecryptFormData decryptFormData = new DecryptFormData(this);
            decryptFormData.execute(kmsClient);
        }

    }

    class EncryptCredentials extends AsyncTask<DefaultAcsClient,Void,Void>{
        String keyId = null;
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
            /**
             * Encrypt the plain text and got a cipher one
             */
            try {
                EncryptResponse encResponse = Encrypt(keyId, getCredentials());
                cipherBlob = encResponse.getCiphertextBlob();
                Log.d(TAG, "CiphertextBlob: " + cipherBlob);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        output.setText("The Encrypt Credentials are\n"+cipherBlob);
                        setEncryptCredentials(cipherBlob);
                    }
                });
                Log.d(TAG, "KeyId: " + encResponse.getKeyId());
                Log.d(TAG, "===========================================");
                Log.d(TAG, "Encrypt the plain text success!");
                Log.d(TAG, "===========================================\n");
            } catch (ClientException eResponse) {
                Log.d(TAG, "Failed.");
                Log.d(TAG, "Error code: " + eResponse.getErrCode());
                Log.d(TAG, "Error message: " + eResponse.getErrMsg());
            }
            return null;
        }
    }


    class DecryptCredentials extends AsyncTask<DefaultAcsClient,Void,Void>{

        public MainActivity mainActivity;

        public DecryptCredentials(MainActivity activity){
            this.mainActivity = activity;
        }

        @Override
        protected Void doInBackground(DefaultAcsClient... defaultAcsClients) {
            try {
                final DecryptResponse decResponse = Decrypt(mainActivity.getEncryptCredentials());
                Log.d(TAG, "Plaintext: " + decResponse.getPlaintext());
                String verifyPlainText = decResponse.getPlaintext();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        output.setText("The Decrypt Credentials are \n"+decResponse.getPlaintext());
                    }
                });

                int isMatch = verifyPlainText.compareTo(mainActivity.getCredentials());
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


    class EncryptFormData extends AsyncTask<DefaultAcsClient,Void,Void>{
        String keyId = null;
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
            /**
             * Encrypt the plain text and got a cipher one
             */
            try {
                EncryptResponse encResponse = Encrypt(keyId, getFormData());
                cipherBlob = encResponse.getCiphertextBlob();
                Log.d(TAG, "CiphertextBlob: " + cipherBlob);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        output.setText("The Encrypt Credentials are\n"+cipherBlob);
                        setEncryptedFormData(cipherBlob);
                    }
                });
                Log.d(TAG, "KeyId: " + encResponse.getKeyId());
                Log.d(TAG, "===========================================");
                Log.d(TAG, "Encrypt the plain text success!");
                Log.d(TAG, "===========================================\n");
            } catch (ClientException eResponse) {
                Log.d(TAG, "Failed.");
                Log.d(TAG, "Error code: " + eResponse.getErrCode());
                Log.d(TAG, "Error message: " + eResponse.getErrMsg());
            }
            return null;
        }
    }


    class DecryptFormData extends AsyncTask<DefaultAcsClient,Void,Void>{

        private MainActivity mainActivity;

        public DecryptFormData(MainActivity activity){
            this.mainActivity = activity;
        }

        @Override
        protected Void doInBackground(DefaultAcsClient... defaultAcsClients) {
            try {
                final DecryptResponse decResponse = Decrypt(mainActivity.getEncryptedFormData());
                Log.d(TAG, "Plaintext: " + decResponse.getPlaintext());
                String verifyPlainText = decResponse.getPlaintext();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        output.setText("The Decrypt Credentials are \n"+decResponse.getPlaintext());
                    }
                });

                int isMatch = verifyPlainText.compareTo(mainActivity.getCredentials());
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


    public String getCredentials(){
            return userName.getText() + "\n" +passWord.getText();
    }

    public void setEncryptCredentials(String credentials){
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.FILE_NAME),MODE_PRIVATE);
        SharedPreferences.Editor editor  = sharedPreferences.edit();
        editor.putString(getString(R.string.CREDENTIALS),credentials);
        editor.apply();
    }

    public String getEncryptCredentials(){
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.FILE_NAME),MODE_PRIVATE);
        return sharedPreferences.getString(getString(R.string.CREDENTIALS), null);
    }

    public String getFormData(){
        return userName.getText()+"\n"+passWord.getText()+"\n"+email.getText()+"\n"+address.getText();
    }

    public void setEncryptedFormData(String formData){
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.FILE_NAME),MODE_PRIVATE);
        SharedPreferences.Editor editor  = sharedPreferences.edit();
        editor.putString(getString(R.string.FORM_DATA),formData);
        editor.apply();
    }

    public String getEncryptedFormData(){
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.FILE_NAME),MODE_PRIVATE);
        return sharedPreferences.getString(getString(R.string.FORM_DATA), null);
    }

}


