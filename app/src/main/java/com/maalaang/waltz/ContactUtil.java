package com.maalaang.waltz;

/**
 * Created by User on 2015-02-12.
 */
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

public class ContactUtil {

    public static String myPhoneNumber(Context context){
        TelephonyManager phone = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return phone.getLine1Number();
    }


    public static Map<String, String> getAddressBook(Context context){
        Map<String, String> result = new HashMap<String, String>();
        Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while(cursor.moveToNext())
        {
            int phone_idx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int name_idx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            String phone = cursor.getString(phone_idx);
            String name = cursor.getString(name_idx);
            result.put(phone, name);
        }

        return result;
    }

    public static String PhoneNumberChange(String pn, String cn){
        PhoneNumberUtil p=PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber phNumber=null;
        try{
            phNumber=p.parse(pn,cn);
        }catch(NumberParseException e){

        }

        boolean isValid=p.isValidNumber(phNumber);
        if(isValid){
            pn=p.format(phNumber,PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
            return pn;
        }else{
            return pn;
        }
    }
}
