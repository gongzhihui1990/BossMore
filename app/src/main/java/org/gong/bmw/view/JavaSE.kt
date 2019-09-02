package org.gong.bmw.view

import java.util.*
import java.util.concurrent.BlockingQueue

/**
 * Created by caroline on 2018/7/13.
 */

class JavaSE {
    private var treeMap: TreeMap<String, String> = TreeMap()

    private var hashMap: HashMap<String, String> = HashMap()
    private var hasnSet: HashSet<String>? = null
    private var arrayList: ArrayList<String>? = null
    private var linkedList: LinkedList<String>? = null
    private var blockingQueue: BlockingQueue<String>? = null

    fun nativeMethod(): Boolean {
        return false
    }


    fun add() {
        treeMap.put("1", "1")
        hashMap.put("1", "1")
        var key = "hello"
        var h: Int
        h = key.hashCode()
        System.out.println("hashCode  " + formatX(h))
        System.out.println("hashCode  " + formatX(h ushr (16)))
        h = (h) or (h ushr (16))
        System.out.println("hashCode3 " + formatX(h))
        System.out.println("treeMap size" + treeMap.size)
        System.out.println("hashMap size" + hashMap.size)
    }

    fun formatX(d: Int): String {
        return String.format("%x", d) + " \n" + String.format("%1$32s", Integer.toBinaryString(d))

//        return String.format("%x", d)
    }

    fun merge() {
    }
}
