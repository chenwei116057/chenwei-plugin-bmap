package com.chenwei.plugin;

import android.util.JsonReader;
import android.util.JsonWriter;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.LOG;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BMapPlugin extends CordovaPlugin {
    private static final String TAG = "BMapPlugin";
    private CordovaInterface cordovaInterface;
    private CordovaWebView cordovaWebView;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        this.cordovaInterface = cordova;
        this.cordovaWebView = webView;
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        LOG.i(TAG, "目标方法：" + action);
        if ("locate".equals(action)) {
            locate(callbackContext, args);
            return true;
        } else {
            PluginResult result = new PluginResult(PluginResult.Status.INVALID_ACTION, ErrorResultUtil.toJsonString(ErrorConstant.Code.NO_ACTION, ErrorConstant.Msg.NO_ACTION));
            callbackContext.sendPluginResult(result);
            return false;
        }
    }

    private void locate(CallbackContext callbackContext, JSONArray args) throws JSONException {
        String mode = "0";
        String coordinateType = "0";
        boolean openGps = true;
        int timeOutMillis = 5 * 60 * 1000;
        boolean enableSimulateGps = false;
        if (args.length() > 0) {
            mode = args.getString(0);//0:高精度；1：低功耗；2：仅使用设备
        }
        if (args.length() > 1) {
            coordinateType = args.getString(1);//0：gcj102（国测局坐标）；1：bd09ll(百度经纬度坐标)；2：bd09(百度墨卡托坐标)
        }
        if (args.length() > 2) {
            openGps = args.getBoolean(2);//true:启用GPS，false：不启用
        }
        if (args.length() > 3) {
            timeOutMillis = args.getInt(3);//默认为5*60*1000
        }
        if (args.length() > 4) {
            enableSimulateGps = args.getBoolean(4);//默认false（过滤）
        }
        noResult(callbackContext);
        LocationClient locationClient = new LocationClient(cordovaInterface.getContext());
        BMapLocationListener bMapLocationListener = new BMapLocationListener();
        bMapLocationListener.setContext(callbackContext);
        locationClient.registerLocationListener(bMapLocationListener);
        LocationClientOption option = new LocationClientOption();
        //设置定位模式
        if ("0".equals(mode)) {
            option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
            openGps = true;
        } else if ("1".equals(mode)) {
            option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
        } else if ("2".equals(mode)) {
            option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
            openGps = true;
        }
        option.setIsNeedLocationDescribe(true);//可选，是否需要位置描述信息，默认为不需要，即参数为false；如果开发者需要获得当前点的位置信息，此处必须为true
        option.setCoorType(coordinateType);//可选，设置返回经纬度坐标类型，默认GCJ02;GCJ02：国测局坐标；BD09ll：百度经纬度坐标；BD09：百度墨卡托坐标；海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标
        option.setScanSpan(1000);//可选，设置发起定位请求的间隔，int类型，单位ms;如果设置为0，则代表单次定位，即仅定位一次，默认为0;如果设置非0，需设置1000ms以上才有效
        option.setOpenGps(openGps);//可选，设置是否使用gps，默认false;使用高精度和仅用设备两种定位模式的，参数必须设置为true
        option.setLocationNotify(true);//可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false
        option.setIgnoreKillProcess(false);//可选，定位SDK内部是一个service，并放到了独立进程。设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)
        option.SetIgnoreCacheException(false);//可选，设置是否收集Crash信息，默认收集，即参数为false
        option.setWifiCacheTimeOut(timeOutMillis);//可选，如果设置了该接口，首次启动定位时，会先判断当前Wi-Fi是否超出有效期，若超出有效期，会先重新扫描Wi-Fi，然后定位
        option.setEnableSimulateGps(enableSimulateGps);//可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false
        option.setNeedNewVersionRgc(true);//可选，设置是否需要最新版本的地址信息。默认不需要，即参数为false
        locationClient.setLocOption(option);
        locationClient.start();
    }

    private void noResult(CallbackContext callbackContext) {
        PluginResult result = new PluginResult(PluginResult.Status.NO_RESULT);
        result.setKeepCallback(true);
        callbackContext.sendPluginResult(result);
    }
}
