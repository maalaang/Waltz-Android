package com.maalaang.waltz;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

/**
 * Created by User on 2015-02-09.
 */
public class PhoneNumber{
public String PhoneNumberChange(String pn){
            PhoneNumberUtil p=PhoneNumberUtil.getInstance();

            Phonenumber.PhoneNumber phNumber=null;
            try{
            phNumber=p.parse(pn,"KR");
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
