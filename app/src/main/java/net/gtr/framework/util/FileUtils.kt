/*
 * Copyright (c) 2017. heisenberg.gong
 */

package net.gtr.framework.util

import android.os.Environment

import java.io.File
import java.io.IOException

/**
 * 文件处理的工具类
 *
 * @author heisenberg.gong
 */
object FileUtils {

    /**
     * 判断SDCard是否存在 [当没有外挂SD卡时，内置ROM也被识别为存在sd卡]
     *
     * @return
     */
    private val isSdCardExist: Boolean
        get() = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED

    /**
     * 获取SD卡根目录路径
     *
     * @return
     */
    private val sdCardPath: String
        @Throws(IOException::class)
        get() {
            val exist = isSdCardExist
            val path: String
            if (exist) {
                path = Environment.getExternalStorageDirectory()
                        .absolutePath
            } else {
                throw IOException("SD卡路径无法获取")
            }
            return path

        }

    /**
     * 获取Log根目录路径：系统提供的目录
     *
     * @return 系统提供的目录
     */
    val logSysPath: String
        @Throws(IOException::class)
        get() {
            val exist = isSdCardExist
            if (exist) {
                val path = sdCardPath + "/bugreport/yingyong/"
                val logFile = File(path)
                if (!logFile.exists()) {
                    throw IOException("系统日志文件夹无法获取")
                }
                return path
            } else {
                throw IOException("SD卡路径无法获取")
            }

        }

    /**
     * 获取Log根目录路径:自己建立的目录
     *
     * @return 自己建立的目录
     */
    internal//            return BaseApp.getContext().getFilesDir().getParentFile() + "/LogFile/HS";
    val logAppPath: String
        @Throws(IOException::class)
        get() {
            val exist = isSdCardExist
            return if (exist) {
                sdCardPath + "/AAA_HSLogFile/"
            } else {
                throw IOException("SD卡路径无法获取")
            }
        }

    /**
     * Deletes all files and subdirectories under "dir".
     *
     * @param dir Directory to be deleted
     * @return boolean Returns "true" if all deletions were successful.
     *
     *
     * If a deletion fails, the method stops attempting to
     *
     *
     * delete and returns "false".
     */
    fun deleteDir(dir: File): Boolean {
        if (dir.isDirectory) {
            val list = dir.list()
            if (list.size == 0) {
                return dir.delete()
            }
            for (children in list) {
                val success = deleteDir(File(dir, children))
                if (!success) {
                    return false
                }
            }
        }
        // The directory is now empty so now it can be smoked
        return dir.delete()

    }

    /**
     * 获取文件或文件夹的大小
     *
     * @param file 文件或文件夹
     * @return 单位byte
     */
    fun getFileSize(file: File): Long {
        return calculatorSize(file, 0)
    }

    /**
     *
     * @param file 文件
     * @param fileSize 文件初始长度
     * @return 返回累计的文件长度
     */
    private fun calculatorSize(file: File, fileSize: Long): Long {
        var fileSize = fileSize
        // 判断是不是文件
        if (file.isFile) {
            fileSize += file.length()
        } else {
            // 文件夹的方式,获取文件夹下的子文件
            val files = file.listFiles()
            if (files == null) {
                // 如果没有子文件, 文件夹大小为0
                fileSize += 0
            } else {
                for (childFile in files) {
                    fileSize = calculatorSize(childFile, fileSize)
                }
            }
        }
        return fileSize
    }
}
