package com.chenwei.plugin;

import com.baidu.location.Address;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.json.JSONException;
import org.json.JSONObject;

public class BMapLocationListener extends BDAbstractLocationListener {
    private CallbackContext context;

    public BMapLocationListener() {

    }

    public BMapLocationListener(CallbackContext callbackContext) {
        this.context = callbackContext;
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        double latitude = bdLocation.getLatitude();
        double altitude = bdLocation.getAltitude();
        String addrStr = bdLocation.getAddrStr();
        Address address = bdLocation.getAddress();
        int locType = bdLocation.getLocType();
        String locationDescribe = location.getLocationDescribe();
        JSONObject jsonObject = new JSONObject();
        PluginResult pluginResult;
        try {
            jsonObject.put("latitude", latitude);
            jsonObject.put("altitude", altitude);
            jsonObject.put("addrStr", addrStr);
            jsonObject.put("adcode", address.adcode);
            jsonObject.put("address", address.address);
            jsonObject.put("city", address.city);
            jsonObject.put("cityCode", address.cityCode);
            jsonObject.put("country", address.country);
            jsonObject.put("countryCode", address.countryCode);
            jsonObject.put("district", address.district);
            jsonObject.put("province", address.province);
            jsonObject.put("street", address.street);
            jsonObject.put("streetNumber", address.streetNumber);
            jsonObject.put("town", address.town);
            jsonObject.put("locationDescribe", locationDescribe);
            jsonObject.put("locType", locType);
            pluginResult = new PluginResult(PluginResult.Status.OK, jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            pluginResult = new PluginResult(PluginResult.Status.ERROR);
        }
        pluginResult.setKeepCallback(false);
        context.sendPluginResult(pluginResult);
    }

    public CallbackContext getContext() {
        return context;
    }

    public BMapLocationListener setContext(CallbackContext context) {
        this.context = context;
        return this;
    }
}
