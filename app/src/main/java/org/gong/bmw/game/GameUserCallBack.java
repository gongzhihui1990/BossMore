package org.gong.bmw.game;

/**
 * @author: create by 龚志辉
 * @version: v1.0
 * @description: org.gong.bmw.game
 * @date:2019-09-03
 */
public interface GameUserCallBack {
    void onUseOil(int size);
    void onAddOil(int size);

    void onUseBomb(int size);
    void onAddBomb(int size);

    void onUseFood(int size);
    void onAddFood(int size);
}
