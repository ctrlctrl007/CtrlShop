package com.ctrl.ctrlshopmall;

import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.ctrl.ctrlshopmall.bean.Address;
import com.ctrl.ctrlshopmall.city.XmlParserHandler;
import com.ctrl.ctrlshopmall.city.model.CityModel;
import com.ctrl.ctrlshopmall.city.model.DistrictModel;
import com.ctrl.ctrlshopmall.city.model.ProvinceModel;
import com.ctrl.ctrlshopmall.http.OkHttpHelper;
import com.ctrl.ctrlshopmall.http.SpotsCallBack;
import com.ctrl.ctrlshopmall.msg.BaseResMsg;
import com.ctrl.ctrlshopmall.utils.Contants;
import com.ctrl.ctrlshopmall.utils.ToastUtils;
import com.ctrl.ctrlshopmall.utils.Utility;
import com.ctrl.ctrlshopmall.widget.CNiaoToolBar;
import com.ctrl.ctrlshopmall.widget.ClearEditText;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.okhttp.Response;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class AddAddressActivity extends AppCompatActivity {

    @ViewInject(R.id.tool_bar)
    private CNiaoToolBar mToolBar;


    @ViewInject(R.id.edit_txt_consignee)
    private ClearEditText consigneeEdit;                //收件人

    @ViewInject(R.id.edit_txt_phone)
    private ClearEditText phoneEdit;

    @ViewInject(R.id.ll_city_picker)
    private LinearLayout pickerLayout;

    @ViewInject(R.id.txt_address)
    private TextView addressTxt;

    @ViewInject(R.id.edit_txt_add)
    private ClearEditText addressEdit;

    private OptionsPickerView mPickerView;

    private String zipCode;

    private List<ProvinceModel> provinces;
    private ArrayList<ArrayList<String>> cities = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> districts = new ArrayList<>();

    private Address address; //修改地址

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        ViewUtils.inject(this);
        init();
    }
    private void init(){
        address = (Address) getIntent().getSerializableExtra("address");
        if (address!=null){
            zipCode = address.getZipCode();
            initAddress();
        }
        initToolBar();
        initProvinceData();
        mPickerView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = provinces.get(options1).getName()
                        + cities.get(options1).get(options2)
                        + districts.get(options1).get(options2).get(options3);
                zipCode = provinces.get(options1).getCityList().get(options2).getDistrictList().get(options3).getZipcode();
                addressTxt.setText(tx);
            }
        }).build();
        mPickerView.setPicker(provinces,cities,districts);
    }

    @OnClick(R.id.ll_city_picker)
    private void selectDistrict(View view){
        mPickerView.show();
    }



    private void initProvinceData(){
        AssetManager asset = getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input,handler);
            input.close();
            provinces = handler.getDataList();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        if(provinces!=null){
            for(ProvinceModel province: provinces){
                List<CityModel> cityModels = province.getCityList();
                ArrayList<String> cityStr = new ArrayList<>(cityModels.size());
                ArrayList<ArrayList<String>> province_districts = new ArrayList<>(); // 该省下面所有地区地区 List
                for (CityModel city : cityModels){
                    cityStr.add(city.getName());

                    List<DistrictModel> districts = city.getDistrictList();
                    ArrayList<String> city_districts = new ArrayList<>(districts.size()); //该市下面所有地区

                    for (DistrictModel d : districts){
                        city_districts.add(d.getName()); // 把城市名称放入 districtStrs
                    }
                   province_districts.add(city_districts);
                }
                cities.add(cityStr);
                districts.add(province_districts);
            }
        }
    }
    private void initToolBar(){
        if(address!=null){
            mToolBar.setTitle("编辑收获地址");
        }
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mToolBar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(address==null) {
                    addAddress();
                }else{
                    updateAddress();
                }
            }
        });
    }
    private void initAddress(){
        consigneeEdit.setText(address.getConsignee());
        phoneEdit.setText(address.getPhone());
        addressTxt.setText(address.getAddr().substring(0,8));
        addressEdit.setText(address.getAddr().substring(9));
    }

    private void updateAddress(){
        String consignee = consigneeEdit.getText().toString();
        if ("".equals(consignee)) {
            ToastUtils.show(this, "收件人不能为空");
            return;
        }
        String phone = phoneEdit.getText().toString().trim();
        if(!Utility.checkPhone(phone)){
            ToastUtils.show(this,"请输入正确的手机号");
            return;
        }
        String addressStr = addressTxt.getText().toString()+addressEdit.getText().toString();
        if ("".equals(addressStr)) {
            ToastUtils.show(this, "地址不能为空");
            return;
        }
        Map<String,Object> params = new HashMap<>(6);
        params.put("id",address.getId());
        params.put("consignee",consignee);
        params.put("phone",phone);
        params.put("addr",addressStr);
        params.put("zip_code",zipCode);
        params.put("is_default",address.getIsDefault());

        OkHttpHelper.getInstance().post(Contants.API.ADDRESS_UPDATE, params, new SpotsCallBack<BaseResMsg>(this) {
            @Override
            public void onSuccess(Response response, BaseResMsg o) {
                setResult(Contants.REQUEST_CODE);
                finish();
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });


    }
    private void addAddress(){
        String consignee = consigneeEdit.getText().toString();
        if ("".equals(consignee)) {
            ToastUtils.show(this, "收件人不能为空");
            return;
        }
        String phone = phoneEdit.getText().toString().trim();
        if(!Utility.checkPhone(phone)){
            ToastUtils.show(this,"请输入正确的手机号");
            return;
        }
        String address = addressTxt.getText().toString()+addressEdit.getText().toString();
        if ("".equals(address)) {
            ToastUtils.show(this, "地址不能为空");
            return;
        }
        Map<String,Object> params = new HashMap<>(5);
        params.put("user_id",MyApplication.getInstance().getUser().getId());
        params.put("consignee",consignee);
        params.put("phone",phone);
        params.put("addr",address);
        params.put("zip_code",zipCode);
        OkHttpHelper.getInstance().post(Contants.API.ADDRESS_CREATE, params, new SpotsCallBack<BaseResMsg>(this) {
            @Override
            public void onSuccess(Response response, BaseResMsg resMsg) {


                if (resMsg.getStatus() == BaseResMsg.STATUS_SUCCESS){
                    setResult(Contants.REQUEST_CODE);
                    finish();
                    ToastUtils.show(AddAddressActivity.this,"地址添加成功");
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                ToastUtils.show(AddAddressActivity.this,"错误");
            }
        });
    }
}
