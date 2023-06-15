package com.hainguyen8086.shoponline.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hainguyen8086.shoponline.R;
import com.hainguyen8086.shoponline.ultil.CheckConnection;
import com.hainguyen8086.shoponline.ultil.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


public class Thongtinkhachhang extends AppCompatActivity {
    EditText edttenkhachhang,edtemail,edtsdt;
    Button btnxascnhan,btntrove;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thongtinkhachhang);
        Anhxa();
        btntrove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (CheckConnection.haveNetworkConnection(getApplicationContext())){
            EventButton();
        }else{
            CheckConnection.ShowToast_Short(getApplicationContext(), "keim ra lai ket noi");
        }
    }

    private void EventButton() {
        btnxascnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ten = edttenkhachhang.getText().toString().trim();
                String sdt = edtsdt.getText().toString().trim();
                String email = edtemail.getText().toString().trim();
                Toast.makeText(Thongtinkhachhang.this, "data:"+ten+"-"+sdt+"-"+email, Toast.LENGTH_SHORT).show();
                if (ten.length()>0 && sdt.length()>0 && email.length()>0){
                    RequestQueue request = Volley.newRequestQueue(getApplicationContext());
                    StringRequest stringRequest=new StringRequest(Request.Method.POST, Server.Duongdandonhang, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String madonhang) {
                            Log.d("madonhang", madonhang);
                            if (Integer.parseInt(madonhang) >0){
                                RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
                                StringRequest stringRequest1=new StringRequest(Request.Method.POST, Server.Duongdanchitietdonhang, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if (response.equals("1")){
                                            MainActivity.manggiohang.clear();
                                            CheckConnection.ShowToast_Short(getApplicationContext(), "Them du lieu gio hang thanh cong");
                                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                            startActivity(intent);
                                            CheckConnection.ShowToast_Short(getApplicationContext(),"Moi ban tiep tuc mua hang");
                                        }else{
                                            CheckConnection.ShowToast_Short(getApplicationContext(),"ERROR DATA");
                                        }

                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                }){
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        JSONArray jsonArray = new JSONArray();
                                        for (int i=0;i<MainActivity.manggiohang.size();i++){
                                            JSONObject jsonObject = new JSONObject();
                                            try {
                                                jsonObject.put("madonhang",madonhang);
                                                jsonObject.put("masanpham",MainActivity.manggiohang.get(i).getIdsp());
                                                jsonObject.put("tensanpham",MainActivity.manggiohang.get(i).getTensp());
                                                jsonObject.put("giasanpham",MainActivity.manggiohang.get(i).getGiasp());
                                                jsonObject.put("soluongsanpham",MainActivity.manggiohang.get(i).getSoluongsp());

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            jsonArray.put(jsonObject);
                                        }
                                        HashMap<String,String> hashMap = new HashMap<String,String>();
                                        hashMap.put("json",jsonArray.toString());
                                        Log.d("jsonString", jsonArray.toString());
                                        return hashMap;
                                    }
                                };
                                requestQueue.add(stringRequest1);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> hashMap=new HashMap<String,String>();
                            hashMap.put("tenkhachhang", ten);
                            hashMap.put("sodienthoai", sdt);
                            hashMap.put("email", email);
                            return hashMap;
                        }
                    };
                    request.add(stringRequest);
                }else{
                    CheckConnection.ShowToast_Short(getApplicationContext(),"Kiem tra lai du lieu nhap vao");
                }
            }
        });
    }

    private void Anhxa() {
        edttenkhachhang = findViewById(R.id.edittexttenkhachhang);
        edtemail = findViewById(R.id.edittextemail);
        edtsdt = findViewById(R.id.edittextsodienthoai);
        btnxascnhan = findViewById(R.id.buttonxacnhan);
        btntrove = findViewById(R.id.buttontrove);
    }
}