package com.example.qjm3662.newproject.Tool;

import android.content.Context;
import com.example.qjm3662.newproject.R;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * 简单易用的提示框
 */
public class EasySweetAlertDialog {

    //github地址：https://github.com/pedant/sweet-alert-dialog/blob/master/README.zh.md




    //Waring/Tip
    public static void ShowTip(Context context, String title, String content){
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(title)
                .setContentText(content)
                .show();
    }

    public static void ShowTip(Context context, String title, String content, String ConfirmText, final SuccessCallBack callBack){
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(title)
                .setContentText(content)
                .setConfirmText(ConfirmText)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        callBack.Success();
                    }
                })
                .show();
    }

    public static void ShowTip(Context context, String content){
        ShowTip(context, "Tip", content);
    }



    //Success
    public static void ShowSuccess(Context context, String title, String content){

        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(title)
                .setContentText(content)
                .show();
    }

    public static void ShowSuccess(Context context, String content){
        ShowSuccess(context, "Success", content);
    }

    //Success
    public static void ShowSuccess(Context context, String title, String content, final SuccessCallBack callBack){

        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(title)
                .setContentText(content)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        callBack.Success();
                    }
                })
                .show();
    }

    public static void ShowSuccess(Context context, String content, SuccessCallBack callBack){
        ShowSuccess(context, "Success", content, callBack);
    }




    //Normal
    public static void ShowNormal(Context context, String title, String content){
        new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE)
                .setTitleText(title)
                .setContentText(content)
                .show();
    }

    public static void ShowNormal(Context context, String content){
        ShowNormal(context, "Normal", content);
    }




    //Custom
    public static void ShowCustom(Context context, String title, String content, int resourceId){
        new SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText(title)
                .setContentText(content)
                .setCustomImage(resourceId)
                .show();
    }

    public static void ShowCustom(Context context, String content){
        ShowCustom(context, "ShowCustom", content, R.drawable.img_warning);
    }




    //Error
    public static void ShowError(Context context, String title, String content){
        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(title)
                .setContentText(content)
                .show();
    }

    public static void ShowError(Context context, String content){
        ShowError(context, "ShowError", content);
    }



    //Process
    public static void ShowProcess(Context context, String title, String content){
        new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText(title)
                .setContentText(content)
                .show();
    }

    public static void ShowProcess(Context context, String content){
        ShowProcess(context, "ShowProcess", content);
    }


    public interface SuccessCallBack{
        void Success();
    }



}
