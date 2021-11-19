package com.jcloud.common.service;

import com.jcloud.common.config.SystemProperty;
import com.jcloud.common.consts.Const;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * 系统服务
 * @author jiaxm
 * @date 2021/8/12
 */
@Service
public class SystemService {

    @Autowired
    private SystemProperty systemProperty;

    /**
     * 在系统创建文件，基于 systemProperty.extPath创建
     * @param path 格式为 /xxx 不添加/后缀
     * @param fileName
     * @return
     */
    public File createTmpFile(String path, String fileName) {
        File file = new File(systemProperty.getExtPath() + Const.EXPORT_PATH);
        if (!file.exists()) {
            file.mkdir();
        }
        return new File(systemProperty.getExtPath() + Const.EXPORT_PATH + File.separator + fileName);
    }

    /**
     * 输出到web的路径
     * @param file
     * @return
     */
    public String getWebPath(File file) {
        return Const.FILE_VISIT_PREFIX + file.getPath().replace("\\", "/").replace(systemProperty.getExtPath(), StringUtils.EMPTY);
    }
}
