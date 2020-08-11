package com.test.ocr;

/**
 * sdk reference linking：https://support.huaweicloud.com/sdkreference-ocr/ocr_04_0016.html
 **/

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private String result = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //aksk auth ocr service
        akskOcrService();

        //token auth ocr service
        //tokenOcrService();

        //token auth-classification service
        //tokenAutoClassificationService();
    }

    /**
     * aksk auth ocr service
     */
    public void akskOcrService() {
        // TODO: Set required parameters
        String AK = "xxx";// AK from authentication
        String SK = "xxx";// SK from authentication
        String region = "cn-north-4";
        // initialize HWOcrClientAKSK from ak,sk and endpoint information
        HWOcrClientAKSK hwOcrClientAKSK = new HWOcrClientAKSK(this, AK, SK, region);
        // ocr service
        String uri = "/v1.0/ocr/id-card";
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.idcarddemo);
        TextView resultView = findViewById(R.id.requestUriTextView);
        resultView.setText(uri);
        ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageBitmap(bitmap);

        // Set params except image
        JSONObject params = new JSONObject();
        // params.put("side", "front");

        hwOcrClientAKSK.requestOcrAkskService(uri, bitmap, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result = e.toString();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView resultView = findViewById(R.id.resultTextView);
                        resultView.setText(result);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                result = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView resultView = findViewById(R.id.resultTextView);
                        resultView.setText(result);
                    }
                });

            }
        });
    }

    /**
     * token auth ocr service
     */
    public void tokenOcrService() {
        // TODO: Set required parameters
        String domainName = "xxx"; // if the user isn't IAM user, domain_name is the same with username
        String userName = "xxx";
        String password = "xxx";
        String region = "cn-north-4";
        // get ocr service token
        HWOcrClientToken ocrToken = new HWOcrClientToken(this, domainName, userName, password, region);
        // ocr service
        String uri = "/v1.0/ocr/id-card";
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.idcarddemo);
        TextView resultView = findViewById(R.id.requestUriTextView);
        resultView.setText(uri);
        ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageBitmap(bitmap);

        // Set params except image
        JSONObject params = new JSONObject();
        // params.put("side", "front");

        ocrToken.requestOcrTokenService(uri, bitmap, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result = e.toString();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView resultView = findViewById(R.id.resultTextView);
                        resultView.setText(result);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int code = response.code();
                if (code ==200) {
                    result = response.body().string();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TextView resultView = findViewById(R.id.resultTextView);
                            resultView.setText(result);
                        }
                    });
                }
            }
        });
    }

    /**
     * token auth auto-classification service
     */
    public void tokenAutoClassificationService() {
        // TODO: Set required parameters
        String domainName = "xxx"; // if the user isn't IAM user, domain_name is the same with username
        String userName = "xxx";
        String password = "xxx";
        String region = "cn-north-4";
        String uri = "/v1.0/ocr/auto-classification"; // ocr service uri
        // get ocr service token
        HWOcrClientToken ocrToken = new HWOcrClientToken(this, domainName, userName, password, region);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.vatinvoicedemo);
        TextView resultView = findViewById(R.id.requestUriTextView);
        resultView.setText(uri);
        ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageBitmap(bitmap);

        // Set params except image
        JSONObject params = new JSONObject();
        List<String> typeList = new ArrayList<>();
        typeList.add("vat_invoice");
        typeList.add("id_card_portrait_side");
        params.put("type_list", typeList);

        ocrToken.requestOcrTokenService(uri, bitmap, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result = e.toString();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView resultView = findViewById(R.id.resultTextView);
                        resultView.setText(result);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int code = response.code();
                if (code ==200) {
                    result = response.body().string();
                    DecodeResponse(result);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TextView resultView = findViewById(R.id.resultTextView);
                            resultView.setText(result);

                        }
                    });
                }
            }
        });
    }

    private void DecodeResponse(String response) {
        JSONObject responseObject = JSON.parseObject(response);
        System.out.println("****** responseObject = " + responseObject);
        String error_code = responseObject.getString("error_code");

        /*
         * data analysis
         * */
        if ("AIS.0000" == error_code || error_code == null || error_code == "") {
            JSONArray resultArr = responseObject.getJSONArray("result");

            for (int i = 0; i < resultArr.size(); i++) {
                JSONObject resultDict = resultArr.getJSONObject(i);
                JSONObject contentDict = resultDict.getJSONObject("content");
                String type = resultDict.getString("type");
                System.out.println("&&&& contentDict = " + contentDict);

                if ("id_card_portrait_side".equals(type)) {
                    IdCardItem idcardItem = new IdCardItem();
                    idcardItem.setName(contentDict.getString("name"));
                    idcardItem.setSex(contentDict.getString("sex"));
                    idcardItem.setEthnicity(contentDict.getString("ethnicity"));
                    idcardItem.setBirth(contentDict.getString("birth"));
                    idcardItem.setAddress(contentDict.getString("address"));
                    idcardItem.setNumber(contentDict.getString("number"));
                } else if ("vat_invoice".equals(type)) {
                    VatInvoiceItem vatInvoice = new VatInvoiceItem();
                    vatInvoice.setType(contentDict.getString("type"));
                    vatInvoice.setSeller_address(contentDict.getString("seller_address"));
                    vatInvoice.setAttribution(contentDict.getString("attribution"));
                    vatInvoice.setCode(contentDict.getString("code"));
                    vatInvoice.setCheck_code(contentDict.getString("check_code"));
                    vatInvoice.setMachine_number(contentDict.getString("machine_number"));
                    vatInvoice.setPrint_number(contentDict.getString("print_number"));
                    vatInvoice.setNumber(contentDict.getString("number"));
                    vatInvoice.setIssue_date(contentDict.getString("issue_date"));
                    vatInvoice.setEncryption_block(contentDict.getString("encryption_block"));
                    vatInvoice.setBuyer_name(contentDict.getString("buyer_name"));
                    vatInvoice.setBuyer_id(contentDict.getString("buyer_id"));
                    vatInvoice.setBuyer_address(contentDict.getString("buyer_address"));
                    vatInvoice.setBuyer_bank(contentDict.getString("buyer_bank"));
                    vatInvoice.setSeller_name(contentDict.getString("seller_name"));
                    vatInvoice.setSeller_id(contentDict.getString("seller_id"));
                    vatInvoice.setSeller_address(contentDict.getString("seller_address"));
                    vatInvoice.setSeller_bank(contentDict.getString("seller_bank"));
                    vatInvoice.setSubtotal_amount(contentDict.getString("subtotal_amount"));
                    vatInvoice.setSubtotal_tax(contentDict.getString("subtotal_tax"));
                    vatInvoice.setTotal(contentDict.getString("total"));
                    vatInvoice.setTotal_in_words(contentDict.getString("total_in_words"));
                    vatInvoice.setRemarks(contentDict.getString("remarks"));
                    vatInvoice.setReceiver(contentDict.getString("receiver"));
                    vatInvoice.setIssuer(contentDict.getString("issuer"));

                    JSONArray supervision_seal = contentDict.getJSONArray("supervision_seal");
                    for (Object object : supervision_seal) {
                        vatInvoice.getSupervision_seal().add(object.toString());
                    }

                    JSONArray seller_seal = contentDict.getJSONArray("seller_seal");
                    for (Object object : seller_seal) {
                        vatInvoice.getSeller_seal().add(object.toString());
                    }

                    JSONArray item_list = contentDict.getJSONArray("item_list");
                    for (Object object : item_list) {
                        JSONObject jsonItem = (JSONObject)object;
                        System.out.println("&&&& jsonItem = " + jsonItem);
                        VatInvoiceListItem listItem = new VatInvoiceListItem();
                        listItem.setName(jsonItem.getString("name"));
                        listItem.setSpecification(jsonItem.getString("specification"));
                        listItem.setQuantity(jsonItem.getString("unit"));
                        listItem.setQuantity(jsonItem.getString("quantity"));
                        listItem.setUnit_price(jsonItem.getString("unit_price"));
                        listItem.setLicense_plate_number(jsonItem.getString("license_plate_number"));
                        listItem.setVehicle_type(jsonItem.getString("license_plate_number"));
                        listItem.setStart_date(jsonItem.getString("start_date"));
                        listItem.setEnd_date(jsonItem.getString("end_date"));
                        listItem.setAmount(jsonItem.getString("amount"));
                        listItem.setTax_rate(jsonItem.getString("tax_rate"));
                        listItem.setTax(jsonItem.getString("tax"));

                        vatInvoice.getItem_list().add(listItem);
                    }
                }
            }
        } else {
            System.out.println( "Interface call error：" +responseObject);
        }
    }
}
