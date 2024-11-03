package com.local.naruto.common.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.local.naruto.common.model.PageInfoModel;

/**
 * 分页结果转换工具类
 *
 * @author naruto chen
 * @since 2023-09-27
 */
public class PageResultConverterUtils {

    public static PageInfoModel transformPageModel(Page<?> pageModel) {
        PageInfoModel pageResultModel = new PageInfoModel();
        pageResultModel.setCurrent((int) pageModel.getCurrent());
        pageResultModel.setPageSize((int) pageModel.getSize());
        pageResultModel.setTotal(pageModel.getTotal());
        int total = (int) pageModel.getTotal() == 0 ? 0 : (int) pageModel.getTotal();
        if (0 == total) {
            pageResultModel.setTotalPages(0);
        } else {
            int pagesCount = total / (int) pageModel.getSize();
            int totalPages = (pagesCount == 0 ? 1 : pagesCount + 1);
            pageResultModel.setTotalPages(totalPages);
        }
        pageResultModel.setData(pageModel.getRecords());
        return pageResultModel;
    }
}
