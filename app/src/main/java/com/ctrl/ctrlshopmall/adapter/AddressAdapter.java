package com.ctrl.ctrlshopmall.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import com.ctrl.ctrlshopmall.R;
import com.ctrl.ctrlshopmall.bean.Address;

import java.util.List;

/**
 * Created by ctrlc on 2017/12/11.
 */

public class AddressAdapter extends SimpleAdapter<Address> {

    private AddressListener listener;

    public AddressAdapter(List<Address> datas, Context context,AddressListener listener) {
        super(datas, R.layout.template_address, context);
        this.listener = listener;
    }

    @Override
    public void bindData(BaseViewHolder holder, final Address item) {
        holder.getTextView(R.id.txt_name).setText(item.getConsignee());
        holder.getTextView(R.id.txt_phone).setText(repalacePhoneNum(item.getPhone()));
        holder.getTextView(R.id.txt_address).setText(item.getAddr());
        final CheckBox checkBox = (CheckBox) holder.getView(R.id.cb_is_defualt);
        checkBox.setChecked(item.getIsDefault());
        if (item.getIsDefault()){
            checkBox.setText("默认地址");
        }else{
            checkBox.setClickable(true);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(checkBox.isChecked()&&listener!=null){
                        item.setIsDefault(true);
                        listener.chooseDefault(item);
                    }
                }
            });
        }
        holder.getTextView(R.id.txt_del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener!=null){
                    listener.deleteAddress(item);
                }
            }
        });

        holder.getTextView(R.id.txt_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener!=null){
                    listener.updateAddress(item);
                }
            }
        });
    }


    public String repalacePhoneNum(String phone){
        return phone.substring(0,phone.length()-(phone.substring(3)).length())+"****"+phone.substring(7);
    }
    public interface AddressListener{
         void chooseDefault(Address address);
         void deleteAddress(Address address);
        void updateAddress(Address address);
    }

}
