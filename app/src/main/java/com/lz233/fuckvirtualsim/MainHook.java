package com.lz233.fuckvirtualsim;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainHook implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if(lpparam.packageName.equals("com.miui.virtualsim")){
            /*File file = new File("/data/data/com.lz233.fuckvirtualsim/com.lz233.fuckvirtualsim/shared_prefs/setting.xml");
            final XSharedPreferences xSharedPreferences = new XSharedPreferences(file);
            xSharedPreferences.makeWorldReadable();
            xSharedPreferences.reload();*/
            //Toast crack_root = Toast.makeText(this, String.valueOf(xSharedPreferences.getBoolean("crack_root", true)), Toast.LENGTH_SHORT).show();
            if (!ReadStringFromFile(Environment.getExternalStorageDirectory().toString()+"/Android/data/com.lz233.fuckvirtualsim/crack_root.txt").equals("0")) {
                XposedHelpers.findAndHookMethod("com.miui.mimobile.utils.RootUtil", lpparam.classLoader, "isDeviceRooted", XC_MethodReplacement.returnConstant(false));
            }
            if (!ReadStringFromFile(Environment.getExternalStorageDirectory().toString()+"/Android/data/com.lz233.fuckvirtualsim/hide_discovery.txt").equals("0")) {
                XposedHelpers.findAndHookMethod("com.miui.mimobile.ui.MmMainActivity", lpparam.classLoader,
                        "onCreate", Bundle.class,
                        new XC_MethodHook() {
                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                //找到发现
                                LinearLayout linearLayout = getHookView(param, "mFindTabView");
                                // 设置属性,隐藏
                                if (linearLayout != null) {
                                    linearLayout.setVisibility(View.GONE);
                                }

                            }
                        }
                );
            }
        }
    }
    public static <T> T getHookView(XC_MethodHook.MethodHookParam param, String name) throws NoSuchFieldException, IllegalAccessException {
        Class clazz = param.thisObject.getClass();
        // 通过反射获取控件，无论private或者public
        Field field = clazz.getDeclaredField(name);
        field.setAccessible(true);
        return  (T) field.get(param.thisObject);
    }
    //写数据到文件
    private static void WriteStringToFile(String string,String path){
        try {
            FileOutputStream out = new FileOutputStream(path);
            byte[] b = string.getBytes();
            for (int i = 0; i < b.length; i++) {
                out.write(b[i]);
            }
            out.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //读数据
    private String ReadStringFromFile (String path) {
        String re = "";
        try {
            FileReader reader = new FileReader(path);
            int ch ;
            while ((ch = reader.read()) != -1) {
                re = String.valueOf((char)ch);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return re;
    }
}