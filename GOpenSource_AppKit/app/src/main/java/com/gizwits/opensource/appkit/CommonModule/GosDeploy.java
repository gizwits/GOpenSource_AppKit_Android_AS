package com.gizwits.opensource.appkit.CommonModule;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.Log;

import com.gizwits.opensource.appkit.utils.AssetsUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class GosDeploy {

    public static HashMap<String, Object> allMap;
    public static HashMap<String, Object> infoMap;
    public static HashMap<String, Object> moduleMap;
    public static HashMap<String, Object> uiMap;
    public static HashMap<String, Object> templateMap;
    public static HashMap<String, Object> functionMap;
    public static HashMap<String, Object> viewMap;
    public static List<HashMap<String, Object>> deviceList;
    private static final String TAG = "GosDeploy";

    static Context context;

    // 配置文件名称
    private static final String fileName = "appConfig.json";

    // 输出json的路径
    public static String fileOutName = null;

    public GosDeploy(Context context) {
        super();
        GosDeploy.context = context;

        fileOutName = (context.getFilesDir().getAbsolutePath() + fileName);
        copyJson();
        readJSON();
    }
    /*
     * ======================================================================
     * 以下Key值用来对应JSON文件中的个配置信息的名称，用于配置App主要参数
     * ======================================================================
     */
    /**
     * The App_Info Key   应用信息
     */
    private static final String AppInfo_Key = "appInfo";

    /**
     * The TemplateSelect Key   模版选择
     */
    private static final String TemplateSelect_Key = "templateSelect";

    /**
     * The FunctionConfig Key    功能选择
     */
    private static final String FunctionConfig_Key = "functionConfig";

    /**
     * The ViewConfig Key    视图配置
     */
    private static final String ViewConfig_Key = "viewConfig";

    /**
     * The DeviceInfo Key   设备定制信息
     */
    private static final String DeviceInfo_Key = "deviceInfo";

    /**
     * The CloudService Key   云服务域名信息
     */
    private static final String CloudService_Key = "cloudService";

    /**
     * The API Key   openapi域名信息，形如：myapi.mygizwits.com 或 myapi.mycompany.com:8081&8443。
     */
    private static final String API_Key = "api";

    /**
     * The Site Key  site域名信息，形如：mysite.mygizwits.com或mysite.mycompany.com:8081&8443。
     */
    private static final String SITE_Key = "site";

    /**
     * The PUSH Key  push域名信息，形如：mypush.mygizwits.com或mypush.mycompany.com:8081&8443
     */
    private static final String PUSH_Key = "push";

    /**
     * The Gizwits_Info Key  机智云应用标识
     */
    private static final String GizwitsInfo_Key = "gizwitsInfo";

    /**
     * The AppID Key  应用标识
     */
    private static final String AppID_Key = "appId";

    /**
     * The AppSecret Key   应用密钥
     */
    private static final String AppSecret_Key = "appSecret";

    /**
     * The ProductInfo Key  机智云产品标识
     */
    private static final String ProductInfo_Key = "productInfo";

    /**
     * The ProductKey Key    产品标识
     */
    private static final String ProductKey_Key = "productKey";

    /**
     * The ProductSecret Key   产品密钥
     */
    private static final String ProductSecret_Key = "productSecret";

    /**
     * The UmengInfo Key    友盟应用标识
     */
    private static final String UmengInfo_Key = "umengInfo";

    /**
     * The AppKey Key   友盟ID
     */
    private static final String AppKey_Key = "appKey";

    /**
     * The MessageKey Key     友盟密钥
     */
    private static final String MessageKey_Key = "messageKey";

    /**
     * The TencentInfo Key    QQ应用标识
     */
    private static final String TencentInfo_Key = "tencentInfo";

    /**
     * The WechatInfo Key    微信应用标识
     */
    private static final String WechatInfo_Key = "weChatInfo";

    /**
     * The FacebookInfo Key    facebook应用标识
     */
    private static final String FacebookInfo_Key = "facebookInfo";

    /**
     * The TwitterInfo Key   推特应用标识
     */
    private static final String TwitterInfo_Key = "twitterInfo";

    /**
     * The PushInfo Key   推送应用标识
     */
    private static final String PushInfo_Key = "pushInfo";

    /**
     * The JpushAppKey Key   极光推送应用ID
     */
    private static final String JpushAppKey_Key = "jpushAppKey";

    /**
     * The BpushAppKey Key   百度推送应用ID
     */
    private static final String BpushAppKey_Key = "bpushAppKey";

    /**
     * The DeviceList Key    设备列表模版：提供两种显示方式，一种显示新设备和常用设备，一种显示在线设备和离线设备。
     */
    private static final String DeviceList_Key = "deviceList";

    /**
     * The AutoSubscribe Key   自动订阅设备
     */
    private static final String AutoSubscribe_Key = "autoSubscribe";

    /**
     * The UnbindDevice Key    解绑设备的按钮
     */
    private static final String UnbindDevice_Key = "unbindDevice";

    /**
     * The DisplayMac Key    设备Mac地址
     */
    private static final String DisplayMac_Key = "displayMac";

    /**
     * The ShortCutButton Key    快捷操作按钮的数据点标识
     */
    private static final String ShortCutButton_Key = "shortCutButton";

    /**
     * The DataPointID Key    快捷操作的数据点标识
     */
    private static final String DataPointID_Key = "dataPointID";

    /**
     * The Product_Light Key   灯模版_圆环样式
     */
    private static final String Product_Light_Key = "product_light";

    /**
     * The Color Key    配置彩光效果
     */
    private static final String Color_Key = "color";

    /**
     * The HueConvert Key    RGB与Hue值的转换
     */
    private static final String HueConvert_Key = "hueConvert";

    /**
     * The RGBButton Key    R、G、B按钮
     */
    private static final String RGBButton_Key = "rgbButton";

    /**
     * The ColorTemprature Key    配置色温效果
     */
    private static final String ColorTemprature_Key = "colorTemprature";

    /**
     * The CttLevelValue Key  色温档位值
     */
    private static final String CttLevelValue_Key = "cttLevelValue";

    /**
     * The DisplayName Key  档位值对应的显示名称
     */
    private static final String DisplayName_Key = "displayName";

    /**
     * The BindDevice_Qrcode Key 扫码绑定设备
     */
    private static final String BindDevice_Qrcode_Key = "bindDevice_qrcode";

    /**
     * The DeviceOnboarding Key 设备配网
     */
    private static final String DeviceOnboarding_Key = "deviceOnboarding";

    /**
     * The Config_softap Key 设备softap配网
     */
    private static final String Config_softap_Key = "config_softap";

    /**
     * The Config_Airlink_Key Key 设备airlink配网
     */
    private static final String Config_Airlink_Key = "config_airlink";

    /**
     * The WifiModuleType Key   模组类型
     */
    private static final String WifiModuleType_Key = "wifiModuleType";

    /**
     * The DeviceOnboarding Key 使用新的配网部署接口
     */
    private static final String UseOnboardingDeploy_Key = "useOnboardingDeploy";

    /**
     * The OnboardingBind Key 配网时是否自动绑定
     */
    private static final String OnboardingBind_Key = "onboardingBind";

    /**
     * The Login_Anonymou Key    匿名登录
     */
    private static final String Login_Anonymous_Key = "login_anonymous";

    /**
     * The Login_QQ Key    qq登录
     */
    private static final String Login_QQ_Key = "login_qq";

    /**
     * The Login_WeChat Key    微信登录
     */
    private static final String Login_WeChat_Key = "login_weChat";

    /**
     * The Register_PhoneUser Key    手机用户注册
     */
    private static final String Register_PhoneUser_Key = "register_phoneUser";


    /**
     * The ResetPassword_PhoneUser Key    手机用户找回密码
     */
    private static final String ResetPassword_PhoneUser_Key = "resetPassword_phoneUser";

    /**
     * The PersonalCenter_ChangePassword Key
     */
    private static final String PersonalCenter_ChangePassword_Key = "personalCenter_changePassword";

    /**
     * The Push_BaiDu Key
     */
    private static final String Push_BaiDu_Key = "push_baidu";

    /**
     * The Push_JiGuang_Key Key
     */
    private static final String Push_JiGuang_Key = "push_jiguang";

    /**
     * The DisableLan Key
     */
    private static final String DisableLan_Key = "disableLan";

    /**
     * The UmengSupport Key
     */
    private static final String UmengSupport_Key = "umengSupport";

    /**
     * The UsingTabSet_Switch Key
     */
    private static final String UsingTabSet = "usingTabSet";

    /**
     * The Device_OnBoarding Key
     */
    private static final String Device_OnBoarding_Key = "deviceOnboarding";


    /**
     * The ViewColor Key
     */
    private static final String ViewColor_Key = "viewColor";

    /**
     * The Background Key
     */
    private static final String Background_Key = "background";

    /**
     * The Contrast Key
     */
    private static final String Contrast_Key = "contrast";

    /**
     * The TextContent Key
     */
    private static final String TextContent_Key = "textContent";

    /**
     * The AboutInfo Key
     */
    private static final String AboutInfo_Key = "aboutInfo";

    /**
     * The AboutInfo Key
     */
    private static final String LaunchPageInfo_Key = "launchPageInfo";

    /**
     * The CH Key
     */
    private static final String CH_Key = "ch";

    /**
     * The EN Key
     */
    private static final String EN_Key = "en";

    /**
     * The StatusBarStyle Key
     */
    private static final String StatusBarStyle_Key = "statusBarStyle";

    /**
     * The ProductName Key
     */
    private static final String ProductName_Key = "productName";

    /**
     * The DataPoint Key
     */
    private static final String DataPoint_Key = "dataPoint";

    /**
     * The ID Key
     */
    private static final String ID_Key = "id";

    /**
     * The Name Key
     */
    private static final String Name_Key = "name";

    /**
     * The GatewaySupport Key
     */
    private static final String GatewaySupport_Key = "gatewaySupport";

    /**
     * The ShowGatewayDataPoint Key
     */
    private static final String ShowGatewayDataPoint_Key = "showGatewayDataPoint";

    /**
     * The UsingDevicePageTemplate Key
     */
    private static final String UsingDevicePageTemplate_Key = "usingDevicePageTemplate";

    /**
     * The UsingUnbindButton Key
     */
    private static final String UsingPowerOnShortcutButton_Key = "usingPowerOnShortcutButton";

    /**
     * The UsingUnbindButton Key
     */
    private static final String DataPointName_Key = "dataPointName";

    /**
     * The Text Key
     */
    private static final String Text_Key = "text";

    /**
     * The LaunchPage_Text Key
     */
    private static final String LaunchPage_Text_Key = "launchPageText";

    /**
     * The AboutPage Key
     */
    private static final String AboutPage_Key = "aboutPage";


    /**
     * The ShowSDKVersion Key
     */
    private static final String ShowSDKVersion_Key = "showSDKVersion";


    /**
     * The ShowAppVersion Key
     */
    private static final String ShowAppVersion_Key = "showAppVersion";


    /**
     * The PowerOn Key
     */
    private static final String PowerOn_Key = "powerOn";

    /**
     * The PowerOff Key
     */
    private static final String PowerOff_Key = "powerOff";


    /** The StatusBarStyle Key */
    // private static final String StatusBarStyle_Key = "statusBarStyle";

    /*
     * ===================================================================
     * 以下是解析配置文件后，对各配置信息赋值的方法。
     * ===================================================================
     */

    /**
     * 设置CloudService参数--ApiUrl
     *
     * @return
     */
    public static String appConfig_CloudServiceApi() {
        String api = null;
        if (infoMap != null) {
            if (infoMap.containsKey(CloudService_Key)) {
                try {
                    if (infoMap.get(CloudService_Key) != null) {
                        JSONObject jo = new JSONObject(infoMap.get(CloudService_Key).toString());
                        if (jo.has(API_Key)) {
                            api = (String) jo.get(API_Key);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return api;
    }

    /**
     * 设置CloudService参数--SiteUrl
     *
     * @return
     */
    public static String appConfig_CloudServiceSite() {
        String site = null;
        if (infoMap != null) {
            try {
                if (infoMap.get(CloudService_Key) != null) {
                    JSONObject jo = new JSONObject(infoMap.get(CloudService_Key).toString());
                    if (jo.has(SITE_Key)) {
                        site = (String) jo.get(SITE_Key);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return site;
    }

    /**
     * 设置CloudService参数--PushUrl
     *
     * @return
     */
    public static String appConfig_CloudServicePush() {
        String push = null;
        if (infoMap != null) {
            try {
                if (infoMap.get(CloudService_Key) != null) {
                    JSONObject jo = new JSONObject(infoMap.get(CloudService_Key).toString());
                    if (jo.has(PUSH_Key)) {
                        push = (String) jo.get(PUSH_Key);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return push;
    }

    /**
     * 设置SDK参数--AppID（必填）
     *
     * @return
     */
    public static String appConfig_GizwitsInfoAppID() {
        String id = null;
        if (infoMap != null) {
            if (infoMap.containsKey(GizwitsInfo_Key)) {
                try {
                    if (infoMap.get(GizwitsInfo_Key) != null) {
                        JSONObject jo = new JSONObject(infoMap.get(GizwitsInfo_Key).toString());
                        if (jo.has(AppID_Key)) {
                            id = (String) jo.get(AppID_Key);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return id;
    }

    /**
     * 设置SDK参数--AppSecret（必填且必须与上述AppKey匹配）
     *
     * @return
     */
    public static String appConfig_GizwitsInfoAppSecret() {
        String secret = null;
        if (infoMap != null) {
            if (infoMap.containsKey(GizwitsInfo_Key)) {
                try {
                    if (infoMap.get(GizwitsInfo_Key) != null) {
                        JSONObject jo = new JSONObject(infoMap.get(GizwitsInfo_Key).toString());
                        if (jo.has(AppSecret_Key)) {
                            secret = (String) jo.get(AppSecret_Key);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return secret;
    }

    /**
     * 设置ProductInfo
     *
     * @return
     */
    public static List<Map<String, String>> appConfig_ProductList() {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        if (infoMap != null) {
            if (infoMap.containsKey(ProductInfo_Key)) {
                JSONArray array = (JSONArray) infoMap.get(ProductInfo_Key);
                if (array != null) {
                    for (int i = 0; i < array.length(); i++) {
                        HashMap<String, String> product = new HashMap<String, String>();
                        JSONObject jo = null;
                        try {
                            jo = new JSONObject(array.get(i).toString());
                            String productkey = null, productsecret = null;
                            if (jo.has(ProductKey_Key)) {
                                productkey = (String) jo.get(ProductKey_Key);
                            }
                            if (jo.has(ProductSecret_Key)) {
                                productsecret = (String) jo.get(ProductSecret_Key);
                            }
                            product.put(productkey, productsecret);
                            list.add(product);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return list;
    }

    /**
     * 设置ProductKeyInfo
     *
     * @return
     */
    public static List<ConcurrentHashMap<String, String>> appConfig_ProductInfoList() {
        List<ConcurrentHashMap<String, String>> productInfoList = new ArrayList<ConcurrentHashMap<String, String>>();
        if (infoMap != null) {
            if (infoMap.containsKey(ProductInfo_Key)) {
                JSONArray array = (JSONArray) infoMap.get(ProductInfo_Key);
                if (array != null) {
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jo = null;
                        try {
                            ConcurrentHashMap<String, String> map = new ConcurrentHashMap<String, String>();
                            jo = new JSONObject(array.get(i).toString());
                            if (jo.has(ProductKey_Key)) {
                                String productkey = (String) jo.get(ProductKey_Key);
                                if (productkey != null) {
                                    map.put("productKey", productkey);
                                }
                            }
                            if (jo.has(ProductSecret_Key)) {
                                String productsecret = (String) jo.get(ProductSecret_Key);
                                if (productsecret != null) {
                                    map.put("productSecret", productsecret);
                                }
                            }
                            productInfoList.add(map);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        }
        return productInfoList;
    }


    /**
     * 设置TencentID
     *
     * @return
     */
    public static String appConfig_TencentAppID() {
        String id = null;
        if (infoMap != null) {
            try {
                if (infoMap.get(TencentInfo_Key) != null) {
                    JSONObject jo = new JSONObject(infoMap.get(TencentInfo_Key).toString());
                    if (jo.has(AppID_Key)) {
                        id = (String) jo.get(AppID_Key);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return id;
    }

    /**
     * 设置WechatAppID
     *
     * @return
     */
    public static String appConfig_WechatAppID() {
        String id = null;
        if (infoMap != null) {
            try {
                if (infoMap.get(WechatInfo_Key) != null) {
                    JSONObject jo = new JSONObject(infoMap.get(WechatInfo_Key).toString());
                    if (jo.has(AppID_Key)) {
                        id = (String) jo.get(AppID_Key);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return id;
    }

    /**
     * 设置WeChatAppSecret
     *
     * @return
     */
    public static String appConfig_WechatAppSecret() {
        String id = null;
        if (infoMap != null) {
            try {
                if (infoMap.get(WechatInfo_Key) != null) {
                    JSONObject jo = new JSONObject(infoMap.get(WechatInfo_Key).toString());
                    if (jo.has(AppSecret_Key)) {
                        id = (String) jo.get(AppSecret_Key);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return id;
    }

    /**
     * 设置FacebookAppID
     *
     * @return
     */
    public static String appConfig_FacebookAppID() {
        String id = null;
        if (infoMap != null) {
            try {
                if (infoMap.get(FacebookInfo_Key) != null) {
                    JSONObject jo = new JSONObject(infoMap.get(FacebookInfo_Key).toString());
                    if (jo.has(AppID_Key)) {
                        id = (String) jo.get(AppID_Key);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return id;
    }

    /**
     * 设置TwitterAppID
     *
     * @return
     */
    public static String appConfig_TwitterAppID() {
        String id = null;
        if (infoMap != null) {
            try {
                if (infoMap.get(TwitterInfo_Key) != null) {
                    JSONObject jo = new JSONObject(infoMap.get(TwitterInfo_Key).toString());
                    if (jo.has(AppID_Key)) {
                        id = (String) jo.get(AppID_Key);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return id;
    }

    /**
     * 设置TwitterAppSecret
     *
     * @return
     */
    public static String appConfig_TwitterAppSecret() {
        String id = null;
        if (infoMap != null) {
            try {
                if (infoMap.get(TwitterInfo_Key) != null) {
                    JSONObject jo = new JSONObject(infoMap.get(TwitterInfo_Key).toString());
                    if (jo.has(AppSecret_Key)) {
                        id = (String) jo.get(AppSecret_Key);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return id;
    }

    /**
     * 设置JiGuangPushAppKey
     *
     * @return
     */
    public static String appConfig_JpushAppKey() {
        String key = null;
        if (infoMap != null) {
            try {
                if (infoMap.get(PushInfo_Key) != null) {
                    JSONObject jo = new JSONObject(infoMap.get(PushInfo_Key).toString());
                    if (jo.has(JpushAppKey_Key)) {
                        key = (String) jo.get(JpushAppKey_Key);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return key;
    }

    /**
     * 设置BaiDuPushAppKey
     *
     * @return
     */
    public static String appConfig_BpushAppKey() {
        String key = null;
        if (infoMap != null) {
            try {
                if (infoMap.get(PushInfo_Key) != null) {
                    JSONObject jo = new JSONObject(infoMap.get(PushInfo_Key).toString());
                    if (jo.has(BpushAppKey_Key)) {
                        key = (String) jo.get(BpushAppKey_Key);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return key;
    }

    /**
     * 是否自动订阅设备
     *
     * @return boolean
     */
    public static boolean appConfig_AutoSubscribe() {
        boolean isOn = false;
        if (templateMap != null) {
            if (templateMap.get(DeviceList_Key) != null) {
                try {
                    JSONObject jo = new JSONObject(templateMap.get(DeviceList_Key).toString());
                    if (jo.has(AutoSubscribe_Key)) {
                        if (jo.get(AutoSubscribe_Key) != null) {
                            isOn = (Boolean) jo.get(AutoSubscribe_Key);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return isOn;
    }

    /**
     * 是否显示解绑设备的按钮
     *
     * @return boolean
     */
    public static boolean appConfig_UnbindDevice() {
        boolean isOn = false;
        if (templateMap != null) {
            if (templateMap.get(DeviceList_Key) != null) {
                try {
                    JSONObject jo = new JSONObject(templateMap.get(DeviceList_Key).toString());
                    if (jo.has(UnbindDevice_Key)) {
                        if (jo.get(UnbindDevice_Key) != null) {
                            isOn = (Boolean) jo.get(UnbindDevice_Key);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return isOn;
    }

    /**
     * 是否显示设备Mac地址
     *
     * @return boolean
     */
    public static boolean appConfig_DisplayMac() {
        boolean isOn = false;
        if (templateMap != null) {
            if (templateMap.get(DeviceList_Key) != null) {
                try {
                    JSONObject jo = new JSONObject(templateMap.get(DeviceList_Key).toString());
                    if (jo.has(DisplayMac_Key)) {
                        if (jo.get(DisplayMac_Key) != null) {
                            isOn = (Boolean) jo.get(DisplayMac_Key);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return isOn;
    }

    /**
     * 快捷操作按钮的数据点标识
     *
     * @return List<Map < String ,   String>>
     */
    public static List<Map<String, String>> appConfig_ShortCutButton() {
        List<Map<String, String>> colorTemprature = new ArrayList<Map<String, String>>();
        if (templateMap != null) {
            if (templateMap.get(deviceList) != null) {
                try {
                    JSONObject jo = new JSONObject(templateMap.get(deviceList).toString());
                    if (jo.has(ShortCutButton_Key)) {
                        if (jo.get(ShortCutButton_Key) != null) {
                            JSONArray array = (JSONArray) jo.get(ShortCutButton_Key);
                            for (int i = 0; i < array.length(); i++) {
                                Map<String, String> map = new HashMap<String, String>();
                                JSONObject object = (JSONObject) array.get(i);
                                if (object.has(ProductKey_Key)) {
                                    if (object.get(ProductKey_Key) != null) {
                                        String value = (String) object.get(ProductKey_Key);
                                        map.put("productKey", value);
                                    }
                                }
                                if (object.has(DataPointID_Key)) {
                                    if (object.get(DataPointID_Key) != null) {
                                        String dataPoint = (String) object.get(DataPoint_Key);
                                        map.put("dataPointID", dataPoint);
                                    }
                                }
                                if (!colorTemprature.contains(map)) {
                                    colorTemprature.add(map);
                                }
                                Log.e(TAG, "appConfig_ColorTemprature------: " + map);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return colorTemprature;
    }

    /**
     * 是否做RGB与Hue值的转换
     *
     * @return boolean
     */
    public static boolean appConfig_HueConvert() {
        boolean isOn = false;
        if (templateMap != null) {
            if (templateMap.get(Product_Light_Key) != null) {
                try {
                    JSONObject jo = new JSONObject(templateMap.get(Product_Light_Key).toString());
                    if (jo.has(Color_Key)) {
                        if (jo.get(Color_Key) != null) {
                            JSONObject jo1 = (JSONObject) jo.get(Color_Key);
                            if (jo1.has(HueConvert_Key)) {
                                if (jo1.get(HueConvert_Key) != null) {
                                    isOn = (Boolean) jo1.get(HueConvert_Key);
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return isOn;
    }

    /**
     * 是否需要R、G、B按钮
     *
     * @return boolean
     */
    public static boolean appConfig_RGBButton() {
        boolean isOn = false;
        if (templateMap != null) {
            if (templateMap.get(Product_Light_Key) != null) {
                try {
                    JSONObject jo = new JSONObject(templateMap.get(Product_Light_Key).toString());
                    if (jo.has(Color_Key)) {
                        if (jo.get(Color_Key) != null) {
                            JSONObject jo1 = (JSONObject) jo.get(Color_Key);
                            if (jo1.has(RGBButton_Key)) {
                                if (jo1.get(RGBButton_Key) != null) {
                                    isOn = (Boolean) jo1.get(RGBButton_Key);
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return isOn;
    }

    /**
     * 色温档位值
     *
     * @return boolean
     */
    public static List<Map<String, Object>> appConfig_ColorTemprature() {
        List<Map<String, Object>> colorTemprature = new ArrayList<Map<String, Object>>();
        if (templateMap != null) {
            if (templateMap.get(Product_Light_Key) != null) {
                try {
                    JSONObject jo = new JSONObject(templateMap.get(Product_Light_Key).toString());
                    if (jo.has(ColorTemprature_Key)) {
                        if (jo.get(ColorTemprature_Key) != null) {
                            JSONArray array = (JSONArray) jo.get(ColorTemprature_Key);
                            for (int i = 0; i < array.length(); i++) {
                                Map<String, Object> map = new HashMap<String, Object>();
                                JSONObject object = (JSONObject) array.get(i);
                                if (object.has(CttLevelValue_Key)) {
                                    if (object.get(CttLevelValue_Key) != null) {
                                        String value = object.get(CttLevelValue_Key) + "";
                                        map.put("cttLevelValue", value);
                                    }
                                }
                                if (object.has(DisplayName_Key)) {
                                    if (object.get(DisplayName_Key) != null) {
                                        String name = (String) object.get(DisplayName_Key);
                                        map.put("displayName", name);
                                    }
                                }
                                colorTemprature.add(map);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return colorTemprature;
    }

    /**
     * 是否需要扫码绑定设备
     *
     * @return boolean
     */
    public static boolean appConfig_BindDevice_Qrcode() {
        boolean isOn = false;
        if (functionMap != null) {
            if (functionMap.containsKey(BindDevice_Qrcode_Key)) {
                if (functionMap.get(BindDevice_Qrcode_Key) != null) {
                    isOn = (Boolean) functionMap.get(BindDevice_Qrcode_Key);
                }
            }
        }
        return isOn;
    }

    /**
     * 设备softap配网
     *
     * @return boolean
     */
    public static boolean appConfig_Config_Softap() {
        boolean isOn = false;
        if (functionMap != null) {
            if (functionMap.containsKey(DeviceOnboarding_Key)) {
                if (functionMap.get(DeviceOnboarding_Key) != null) {
                    JSONObject ob = (JSONObject) functionMap.get(DeviceOnboarding_Key);
                    try {
                        if (ob.has(Config_softap_Key)) {
                            if (ob.get(Config_softap_Key) != null) {
                                isOn = (Boolean) ob.get(Config_softap_Key);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return isOn;
    }

    /**
     * 设备airlink配网
     *
     * @return boolean
     */
    public static boolean appConfig_Config_Airlink() {
        boolean isOn = false;
        if (functionMap != null) {
            if (functionMap.containsKey(DeviceOnboarding_Key)) {
                if (functionMap.get(DeviceOnboarding_Key) != null) {
                    JSONObject ob = (JSONObject) functionMap.get(DeviceOnboarding_Key);
                    try {
                        if (ob.has(Config_Airlink_Key)) {
                            if (ob.get(Config_Airlink_Key) != null) {
                                isOn = (Boolean) ob.get(Config_Airlink_Key);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return isOn;
    }

    /**
     * 设置模组类型
     *
     * @return
     */
    public static List<Integer> appConfig_WifiModuleType() {
        List<Integer> types = new ArrayList<Integer>();
        if (functionMap != null) {
            if (functionMap.get(Device_OnBoarding_Key) != null) {
                JSONObject jo = null;
                try {
                    jo = new JSONObject(functionMap.get(Device_OnBoarding_Key).toString());
                    if (jo.has(WifiModuleType_Key)) {
                        if (jo.get(WifiModuleType_Key) != null) {
                            JSONArray array = (JSONArray) jo.get(WifiModuleType_Key);
                            for (int i = 0; i < array.length(); i++) {
                                types.add((Integer) array.get(i));
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.e(TAG, "toAirlinkReady----------: " + types);
        return types;
    }

    /**
     * 使用新的配网部署接口
     *
     * @return boolean
     */
    public static boolean appConfig_UseOnboardingDeploy() {
        boolean isOn = false;
        if (functionMap != null) {
            if (functionMap.containsKey(DeviceOnboarding_Key)) {
                if (functionMap.get(DeviceOnboarding_Key) != null) {
                    JSONObject ob = (JSONObject) functionMap.get(DeviceOnboarding_Key);
                    try {
                        if (ob.has(UseOnboardingDeploy_Key)) {
                            if (ob.get(UseOnboardingDeploy_Key) != null) {
                                isOn = (Boolean) ob.get(UseOnboardingDeploy_Key);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return isOn;
    }

    /**
     * 配网时是否自动绑定
     *
     * @return boolean
     */
    public static boolean appConfig_OnboardingBind() {
        boolean isOn = false;
        if (functionMap != null) {
            if (functionMap.containsKey(DeviceOnboarding_Key)) {
                if (functionMap.get(DeviceOnboarding_Key) != null) {
                    JSONObject ob = (JSONObject) functionMap.get(DeviceOnboarding_Key);
                    try {
                        if (ob.has(OnboardingBind_Key)) {
                            if (ob.get(OnboardingBind_Key) != null) {
                                isOn = (Boolean) ob.get(OnboardingBind_Key);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return isOn;
    }

    /**
     * 用来判断是否需要打开匿名登录
     *
     * @return boolean
     */
    public static boolean appConfig_Login_Anonymous() {
        boolean isOn = false;
        if (functionMap != null) {
            if (functionMap.containsKey(Login_Anonymous_Key)) {
                if (functionMap.get(Login_Anonymous_Key) != null) {
                    isOn = (Boolean) functionMap.get(Login_Anonymous_Key);
                }
            }
        }
        return isOn;
    }


    /**
     * 用来判断是否需要qq登录
     *
     * @return boolean
     */
    public static boolean appConfig_Login_QQ() {
        boolean isOn = false;
        if (functionMap != null) {
            if (functionMap.containsKey(Login_QQ_Key)) {
                if (functionMap.get(Login_QQ_Key) != null) {
                    isOn = (Boolean) functionMap.get(Login_QQ_Key);
                }
            }
        }
        return isOn;
    }


    /**
     * 用来判断是否需要weChat登录
     *
     * @return boolean
     */
    public static boolean appConfig_Login_Wechat() {
        boolean isOn = false;
        if (functionMap != null) {
            if (functionMap.containsKey(Login_WeChat_Key)) {
                if (functionMap.get(Login_WeChat_Key) != null) {
                    isOn = (Boolean) functionMap.get(Login_WeChat_Key);
                }
            }
        }
        return isOn;
    }

    /**
     * 是否需要手机用户注册
     *
     * @return boolean
     */
    public static boolean appConfig_Register_PhoneUser() {
        boolean isOn = false;
        if (functionMap != null) {
            if (functionMap.containsKey(Register_PhoneUser_Key)) {
                if (functionMap.get(Register_PhoneUser_Key) != null) {
                    isOn = (Boolean) functionMap.get(Register_PhoneUser_Key);
                }
            }
        }
        return isOn;
    }

    /**
     * 是否需要手机用户找回密码
     *
     * @return boolean
     */
    public static boolean appConfig_ResetPassword_PhoneUser() {
        boolean isOn = false;
        if (functionMap != null) {
            if (functionMap.containsKey(ResetPassword_PhoneUser_Key)) {
                if (functionMap.get(ResetPassword_PhoneUser_Key) != null) {
                    isOn = (Boolean) functionMap.get(ResetPassword_PhoneUser_Key);
                }
            }
        }
        return isOn;
    }


    /**
     * 是否需要修改密码
     *
     * @return boolean
     */
    public static boolean appConfig_PersonalCenter_ChangePassword() {
        boolean isOn = false;
        if (functionMap != null) {
            if (functionMap.containsKey(PersonalCenter_ChangePassword_Key)) {
                if (functionMap.get(PersonalCenter_ChangePassword_Key) != null) {
                    isOn = (Boolean) functionMap.get(PersonalCenter_ChangePassword_Key);
                }
            }
        }
        return isOn;
    }

    /**
     * 是否需要百度推送功能
     *
     * @return boolean
     */
    public static boolean appConfig_Push_BaiDu() {
        boolean isOn = false;
        if (functionMap != null) {
            if (functionMap.containsKey(Push_BaiDu_Key)) {
                if (functionMap.get(Push_BaiDu_Key) != null) {
                    isOn = (Boolean) functionMap.get(Push_BaiDu_Key);
                }
            }
        }
        return isOn;
    }

    /**
     * 是否需要极光推送功能
     *
     * @return boolean
     */
    public static boolean appConfig_Push_JiGuang() {
        boolean isOn = false;
        if (functionMap != null) {
            if (functionMap.containsKey(Push_JiGuang_Key)) {
                if (functionMap.get(Push_JiGuang_Key) != null) {
                    isOn = (Boolean) functionMap.get(Push_JiGuang_Key);
                }
            }
        }
        return isOn;
    }


    /**
     * 设置背景颜色(返回int 型)
     *
     * @return
     */
    public static int appConfig_Background() {
        int color = Color.parseColor("#FBDA51");
        if (viewMap != null) {
            String ButtonTextColor_FromMap = null;
            if (viewMap.get(ViewColor_Key) != null) {
                JSONObject jo = null;
                try {
                    jo = new JSONObject(viewMap.get(ViewColor_Key).toString());
                    if (jo.get(Background_Key) != null) {
                        ButtonTextColor_FromMap = (String) jo.get(Background_Key);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (!TextUtils.isEmpty(ButtonTextColor_FromMap)) {
                color = Color.parseColor("#" + ButtonTextColor_FromMap);
            }
        }
        return color;
    }

    /**
     * 设置背景颜色（返回 Drawable 型）
     *
     * @return
     */
    public static Drawable appConfig_BackgroundColor() {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(100);
        if (viewMap != null) {
            String ButtonColor_FromMap = null;
            if (viewMap.get(ViewColor_Key) != null) {
                JSONObject jo = null;
                try {
                    jo = new JSONObject(viewMap.get(ViewColor_Key).toString());
                    if (jo.get(Background_Key) != null) {
                        ButtonColor_FromMap = (String) jo.get(Background_Key);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (!TextUtils.isEmpty(ButtonColor_FromMap)) {
                drawable.setColor(Color.parseColor("#" + ButtonColor_FromMap));
            } else {
                drawable.setColor(Color.argb(255, 248, 220, 38));
            }
        }
        return drawable;
    }

    /**
     * 设置背景对应颜色
     *
     * @return
     */
    public static int appConfig_Contrast() {
        int color = Color.parseColor("#333333");
        if (viewMap != null) {
            String ButtonTextColor_FromMap = null;
            if (viewMap.get(ViewColor_Key) != null) {
                JSONObject jo = null;
                try {
                    jo = new JSONObject(viewMap.get(ViewColor_Key).toString());
                    if (jo.get(Contrast_Key) != null) {
                        ButtonTextColor_FromMap = (String) jo.get(Contrast_Key);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (!TextUtils.isEmpty(ButtonTextColor_FromMap)) {
                color = Color.parseColor("#" + ButtonTextColor_FromMap);
            }
        }
        return color;
    }

    /**
     * 设置关于页面文本中文文字
     *
     * @return
     */
    public static String appConfig_AboutInfoCH() {
        String text = null;
        JSONObject jo = null;
        JSONObject jo1 = null;
        if (viewMap != null) {
            try {
                if (viewMap.containsKey(TextContent_Key)) {
                    if (viewMap.get(TextContent_Key) != null) {
                        jo = new JSONObject(viewMap.get(TextContent_Key).toString());
                        if (jo.get(AboutInfo_Key) != null) {
                            jo1 = new JSONObject(jo.get(AboutInfo_Key).toString());
                            if (jo1.get(CH_Key) != null) {
                                text = (String) jo1.get(CH_Key);
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return text;
    }

    /**
     * 设置关于页面文本英文文字
     *
     * @return
     */
    public static String appConfig_AboutInfoEN() {
        String text = null;
        JSONObject jo = null;
        JSONObject jo1 = null;
        if (viewMap != null) {
            try {
                if (viewMap.containsKey(TextContent_Key)) {
                    if (viewMap.get(TextContent_Key) != null) {
                        jo = new JSONObject(viewMap.get(TextContent_Key).toString());
                        if (jo.get(LaunchPageInfo_Key) != null) {
                            jo1 = new JSONObject(jo.get(LaunchPageInfo_Key).toString());
                            if (jo1.get(EN_Key) != null) {
                                text = (String) jo1.get(EN_Key);
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return text;
    }

    /**
     * 设置启动页中文文字
     *
     * @return
     */
    public static String appConfig_LaunchInfoCH() {
        String text = null;
        JSONObject jo = null;
        JSONObject jo1 = null;
        if (viewMap != null) {
            try {
                if (viewMap.containsKey(TextContent_Key)) {
                    if (viewMap.get(TextContent_Key) != null) {
                        jo = new JSONObject(viewMap.get(TextContent_Key).toString());
                        if (jo.get(LaunchPageInfo_Key) != null) {
                            jo1 = new JSONObject(jo.get(LaunchPageInfo_Key).toString());
                            if (jo1.get(CH_Key) != null) {
                                text = (String) jo1.get(CH_Key);
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return text;
    }

    /**
     * 设置启动页英文文字
     *
     * @return
     */
    public static String appConfig_LaunchInfoEN() {
        String text = null;
        JSONObject jo = null;
        JSONObject jo1 = null;
        if (viewMap != null) {
            try {
                if (viewMap.containsKey(TextContent_Key)) {
                    if (viewMap.get(TextContent_Key) != null) {
                        jo = new JSONObject(viewMap.get(TextContent_Key).toString());
                        if (jo.get(LaunchPageInfo_Key) != null) {
                            jo1 = new JSONObject(jo.get(LaunchPageInfo_Key).toString());
                            if (jo1.get(EN_Key) != null) {
                                text = (String) jo1.get(EN_Key);
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return text;
    }

    /**
     * 状态栏样式
     *
     * @return boolean
     */
    public static String appConfig_StatusBarStyle() {
        String style = "default";
        if (viewMap != null) {
            if (viewMap.containsKey(StatusBarStyle_Key)) {
                if (viewMap.get(StatusBarStyle_Key) != null) {
                    style = (String) functionMap.get(StatusBarStyle_Key);
                }
            }
        }
        return style;
    }

    /**
     * 可在此填写设备需要显示的产品名称或者操作名称
     *
     * @return boolean
     */
    public static List<Map<String, Object>> appConfig_DeviceInfo() {
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        if (deviceList != null) {
            for (Map<String, Object> device : deviceList) {
                Map<String, Object> map = new HashMap<String, Object>();
                if (device.containsKey(ProductKey_Key)) {
                    if (device.get(ProductKey_Key) != null) {
                        String productKey = (String) device.get(ProductKey_Key);
                        map.put(ProductKey_Key, productKey);
                    }
                }
                if (device.containsKey(ProductName_Key)) {
                    if (device.get(ProductName_Key) != null) {
                        JSONObject productName = (JSONObject) device.get(ProductName_Key);
                        try {
                            if (productName.get(CH_Key) != null) {
                                String text = (String) productName.get(CH_Key);
                                map.put("productNameCH", text);
                            }
                            if (productName.get(EN_Key) != null) {
                                String text = (String) productName.get(EN_Key);
                                map.put("productNameEN", text);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                mapList.add(map);
            }
        }
        return mapList;
    }

//    /**
//     * 设置UsingTab 开关
//     *
//     * @return
//     */
//    public static int appConfig_UsingTabSetOn() {
//        int modeOnOff = View.GONE;
//        boolean isOn = false;
//        if (uiMap != null) {
//            if (uiMap.get(UsingTabSet) == null) {
//                isOn = false;
//            } else {
//                isOn = (Boolean) uiMap.get(UsingTabSet);
//            }
//            if (isOn) {
//                modeOnOff = View.VISIBLE;
//            }
//        }
//        return modeOnOff;
//    }

//    /**
//     * 用来判断是否需要打开设备控制界面
//     *
//     * @return
//     */
//    public static boolean setUsingDevicePageTemplate() {
//        boolean isOn = false;
//        if (uiMap != null) {
//            if (uiMap.get(UsingDevicePageTemplate_Key) == null) {
//                isOn = false;
//            } else {
//                isOn = (Boolean) uiMap.get(UsingDevicePageTemplate_Key);
//            }
//        }
//        return isOn;
//    }

    /**
     * 用来判断是否要在设备（子设备）列表中显示指定品类的快捷开关机按钮
     *
     * @return
     */
    public static List<Map<String, String>> appConfig_UsingPowerOnShortcutButton() {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        List<String> list1 = new ArrayList<String>();
        if (uiMap != null) {
            JSONArray jsonArray = (JSONArray) uiMap.get(UsingPowerOnShortcutButton_Key);
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    HashMap<String, String> product = new HashMap<String, String>();
                    JSONObject jo = null;
                    try {
                        jo = new JSONObject(jsonArray.get(i).toString());
                        String productkey = (String) jo.get(ProductKey_Key);
                        String datapointname = (String) jo.get(DataPointName_Key);
                        product.put(productkey, datapointname);
                        if (!list1.contains(productkey)) {
                            list1.add(productkey);
                            list.add(product);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return list;
    }

    /**
     * 设置ProductKey
     *
     * @return
     */
    public static List<String> appConfig_ProductKeyList() {
        //entity.getAppInfo().getProductInfo();
        List<String> productKeyList = new ArrayList<String>();
        if (uiMap != null) {
            JSONArray array = (JSONArray) infoMap.get(ProductInfo_Key);
            if (array != null) {
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jo = null;
                    try {
                        jo = new JSONObject(array.get(i).toString());
                        if (jo.has(ProductKey_Key)) {
                            String productkey = (String) jo.get(ProductKey_Key);
                            productKeyList.add(productkey);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
        return productKeyList;
    }

    /**
     * 是否显示SDK版本号
     *
     * @return
     */
    public static boolean appConfig_ShowSDKVersion() {
        boolean isOn = false;
        JSONObject jo = null;
        JSONObject jo1 = null;
        if (uiMap != null) {
            try {
                if (uiMap.containsKey(Text_Key)) {
                    if (uiMap.get(Text_Key) != null) {
                        jo = new JSONObject(uiMap.get(Text_Key).toString());
                        if (jo.get(AboutPage_Key) != null) {
                            jo1 = new JSONObject(jo.get(AboutPage_Key).toString());
                            if (jo1.get(ShowSDKVersion_Key) != null) {
                                isOn = (Boolean) jo1.get(ShowSDKVersion_Key);
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return isOn;
    }


    /**
     * 是否显示APP版本号
     *
     * @return
     */
    public static boolean appConfig_ShowAPPVersion() {
        boolean isOn = false;
        JSONObject jo = null;
        JSONObject jo1 = null;
        if (uiMap != null) {
            try {
                if (uiMap.containsKey(Text_Key)) {
                    if (uiMap.get(Text_Key) != null) {
                        jo = new JSONObject(uiMap.get(Text_Key).toString());
                        if (jo.get(AboutPage_Key) != null) {
                            jo1 = new JSONObject(jo.get(AboutPage_Key).toString());
                            if (jo1.get(ShowAppVersion_Key) != null) {
                                isOn = (Boolean) jo1.get(ShowAppVersion_Key);
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return isOn;
    }

//    /**
//     * 设置添加设备标题
//     *
//     * @return
//     */
//    public static String setAddDeviceTitle() {
//
//        String addDeviceTitle = context.getResources().getString(
//                R.string.addDeviceTitle);
//        String AddDeviceTitle_FromMap = infoMap.get(AddDeviceTitle_Key)
//                .toString();
//        if (!TextUtils.isEmpty(AddDeviceTitle_FromMap)) {
//            addDeviceTitle = AddDeviceTitle_FromMap;
//        }
//
//        return addDeviceTitle;
//
//    }

    /**
     * 读取本地的JSON文件
     */
    public static void readJSON() {
        try {
            FileInputStream input = new FileInputStream(fileOutName);
            InputStreamReader inputStreamReader = new InputStreamReader(input, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 加载JSON数据到Entity
            //setEntity(stringBuilder);
            setMap(stringBuilder);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


//    public static void setEntity(StringBuilder stringBuilder) {
//        // 加载JSON数据到Map
//        Gson gson = new Gson();
//
//        entity = gson.fromJson(stringBuilder.toString(), Entity.class);
//    }


    //拷贝json文件
    private void copyJson() {
        try {
            AssetsUtils.assetsDataToSD(fileOutName, fileName, context);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static void setMap(StringBuilder stringBuilder) {
        infoMap = new HashMap<String, Object>();
        moduleMap = new HashMap<String, Object>();
        uiMap = new HashMap<String, Object>();
        templateMap = new HashMap<String, Object>();
        functionMap = new HashMap<String, Object>();
        viewMap = new HashMap<String, Object>();
        deviceList = new ArrayList<HashMap<String, Object>>();

        try {
            JSONObject root = new JSONObject(stringBuilder.toString());
            Iterator actions = root.keys();
            while (actions.hasNext()) {
                String param = actions.next().toString();
                Object valus = root.get(param);
                String info = valus.toString();
                if (param.matches(DeviceInfo_Key)) {
                    JSONArray array = new JSONArray(info);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject data = (JSONObject) array.get(i);
                        Iterator action = data.keys();
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        while (action.hasNext()) {
                            String param1 = action.next().toString();
                            Object valus1 = data.get(param1);
                            map.put(param1, valus1);
                        }
                        deviceList.add(map);
                    }
                    Log.e(TAG, "setMap----: " + deviceList);
                } else {
                    JSONObject data = new JSONObject(info);
                    Iterator action = data.keys();
                    Log.e(TAG, "param-----: " + param);
                    Log.e(TAG, "info------: " + info);
                    if (param.matches(AppInfo_Key)) {
                        while (action.hasNext()) {
                            String param1 = action.next().toString();
                            Object valus1 = data.get(param1);
                            infoMap.put(param1, valus1);
                        }
                    }
                    if (param.matches(TemplateSelect_Key)) {
                        while (action.hasNext()) {
                            String param1 = action.next().toString();
                            Object valus1 = data.get(param1);
                            templateMap.put(param1, valus1);
                        }
                    }
                    if (param.matches(FunctionConfig_Key)) {
                        while (action.hasNext()) {
                            String param1 = action.next().toString();
                            Object valus1 = data.get(param1);
                            functionMap.put(param1, valus1);
                        }
                    }
                    if (param.matches(ViewConfig_Key)) {
                        while (action.hasNext()) {
                            String param1 = action.next().toString();
                            Object valus1 = data.get(param1);
                            viewMap.put(param1, valus1);
                        }
                    }
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
