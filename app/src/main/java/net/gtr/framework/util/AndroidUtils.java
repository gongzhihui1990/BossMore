/*
 * Copyright (c) 2017. heisenberg.gong
 */

package net.gtr.framework.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


import org.gong.bmw.R;

import java.util.List;

public class AndroidUtils {

    /**
     * @param editText    编辑
     * @param imageButton 删除按钮，需对应
     */
    public static void initEditFocusListener(@NonNull final TextView editText, @NonNull final ImageButton imageButton) {
        initEditFocusListener(editText,imageButton,null);
    }

    /**
     * @param editText    编辑
     * @param imageButton 删除按钮，需对应
     */
    public static void initEditFocusListener(@NonNull final TextView editText, @NonNull final ImageButton imageButton, final @Nullable View rootView) {
        imageButton.setTag(R.id.magic_id, imageButton.hasFocus());
        editText.setOnFocusChangeListener((view, focus) -> {
            imageButton.setTag(R.id.magic_id, focus);
            if (rootView != null) {
                rootView.setEnabled(focus);
            }
            if (TextUtils.isEmpty(editText.getText())) {
                imageButton.setVisibility(View.INVISIBLE);
                return;
            }
            if (focus) {
                imageButton.setVisibility(View.VISIBLE);
            } else {
                imageButton.setVisibility(View.INVISIBLE);
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!(boolean) imageButton.getTag(R.id.magic_id)) {
                    //没有焦点，不处理
                    return;
                }
                if (!TextUtils.isEmpty(s)) {
                    imageButton.setVisibility(View.VISIBLE);
                } else {
                    imageButton.setVisibility(View.INVISIBLE);
                }
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText("");
            }
        });
    }

    public static Intent getExplicitIntent(Context context, Intent implicitIntent) {
        // Retrieve all services that can match the given intent
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);
        // Make sure only one match was found
        if (resolveInfo == null || resolveInfo.size() != 1) {
            Loger.e("get implicitIntent");
            return implicitIntent;
        }
        // Get component info and getInstance ComponentName
        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);
        // Create a new intent. Use the old one for extras and such reuse
        Intent explicitIntent = new Intent(implicitIntent);
        // Set the component to be explicit
        explicitIntent.setComponent(component);
        Loger.e("get explicitIntent");
        return explicitIntent;
    }

    public static final float DENSITY = Resources.getSystem().getDisplayMetrics().density;

    public static int dp2px(int dp) {
        return Math.round(dp * DENSITY);
    }


    /**
     * 将彩色图转换为纯黑白二色
     *
     * @return 返回转换好的位图
     */
    public static Bitmap convertToBlackWhite(Bitmap bmp) {
        // 获取位图的宽/高
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        // 通过位图的大小创建像素点数组
        int[] pixels = new int[width * height];

        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int grey = pixels[width * i + j];

                int alpha = ((grey & 0xFF000000) >> 24);
                //分离三原色
                int red = ((grey & 0x00FF0000) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey & 0x000000FF);

                //转化成灰度像素
                grey = (int) (red * 0.3 + green * 0.59 + blue * 0.11);
                grey = alpha | (grey << 16) | (grey << 8) | grey;
                pixels[width * i + j] = grey;
            }
        }
        //新建图片
        Bitmap newBmp = Bitmap.createBitmap(width, height, Config.RGB_565);
        //设置图片数据
        newBmp.setPixels(pixels, 0, width, 0, 0, width, height);
        return newBmp;
//       Bitmap resizeBmp = ThumbnailUtils.extractThumbnail(newBmp, width, height);
//       return resizeBmp;
    }
    public static Bitmap blurBitmap(Bitmap bitmap, Context context) {
        return blurBitmapCore(bitmap,context);
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static Bitmap blurBitmapCore(Bitmap bitmap, Context context) {

        // 用需要创建高斯模糊bitmap创建一个空的bitmap
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        // 初始化Renderscript，该类提供了RenderScript context，创建其他RS类之前必须先创建这个类，其控制RenderScript的初始化，资源管理及释放
        RenderScript rs = RenderScript.create(context);

        // 创建高斯模糊对象
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        // 创建Allocations，此类是将数据传递给RenderScript内核的主要方法，并制定一个后备类型存储给定类型
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);

        //设定模糊度(注：Radius最大只能设置25.f)
        blurScript.setRadius(25.0f);

        // Perform the Renderscript
        blurScript.setInput(allIn);
        blurScript.forEach(allOut);

        // Copy the final bitmap created by the out Allocation to the outBitmap
        allOut.copyTo(outBitmap);

        // recycle the original bitmap
        bitmap.recycle();

        // After finishing everything, we destroy the Renderscript.
        rs.destroy();

        return outBitmap;
    }
}
