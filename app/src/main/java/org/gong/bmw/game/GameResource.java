package org.gong.bmw.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.gong.bmw.R;

/**
 * @author: create by 龚志辉
 * @version: v1.0
 * @description: org.gong.bmw.game
 * @date:2019-09-11
 */
public class GameResource {
    public static Bitmap GameBoot;
    public static Bitmap Bombed;
    public static Bitmap BulletRun;
    public static Bitmap SUB_21;
    public static Bitmap SUB_26;
    public static Bitmap SUB_50;
    public static Bitmap Shark;
    public static Bitmap Dolphin;

    public static boolean init(Context context) {
        GameBoot = BitmapFactory.decodeResource(context.getResources(), R.mipmap.game_boot);
        Bombed = BitmapFactory.decodeResource(context.getResources(), R.mipmap.bombbed);
        BulletRun = BitmapFactory.decodeResource(context.getResources(), R.mipmap.point);
        SUB_21 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.game_sub_21);
        SUB_26 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.game_sub_u26);
        SUB_50 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.game_sub_u50);
        Shark = BitmapFactory.decodeResource(context.getResources(), R.mipmap.game_sub_shark);
        Dolphin = BitmapFactory.decodeResource(context.getResources(), R.mipmap.game_whale);
        return true;
    }
}
