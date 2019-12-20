package com.wx.mini.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * @author alan.chen
 * @date 2019/12/12 6:39 PM
 */
public class PathUtil {

    /**
     * 根据路径和新的文件名称，返回新的文件名称的路径，保持文件后缀不变
     * @param sourcePath
     * @param newfilename
     * @return
     */
    public static String generateTargetPath(String sourcePath, String newfilename) {
        String pathFrefix = sourcePath.substring(0, sourcePath.lastIndexOf(File.separator));
        String[] split = sourcePath.substring(sourcePath.lastIndexOf(File.separator)).split("\\.");
        if(split.length > 0) {
            return pathFrefix + File.separator + newfilename + "." + split[1];
        }
        return null;
    }


    /**
     * 替换文件名称，并保留文件格式不变
     * @param originFullFilename
     * @param newFilename
     * @return
     */
    public static String replaceFileName(String originFullFilename, String newFilename) {
        if(StringUtils.isBlank(originFullFilename) || StringUtils.isBlank(newFilename)) {
            return null;
        }
        String suffix = originFullFilename.substring(originFullFilename.lastIndexOf("."));
        return newFilename + suffix;
    }

}
