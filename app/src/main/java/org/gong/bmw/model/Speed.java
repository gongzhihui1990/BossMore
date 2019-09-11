package org.gong.bmw.model;

/**
 * @author: create by 龚志辉
 * @version: v1.0
 * @description: org.gong.bmw.model
 * @date:2019-09-11
 */
public enum Speed {
    L1(0.0005f),
    L2(0.0008f),
    L3(0.0012f),
    L4(0.0020f),
    L5(0.0032f),
    L6(0.0040f);

    public float speed;

    Speed(float s) {
        speed = s;
    }
}
