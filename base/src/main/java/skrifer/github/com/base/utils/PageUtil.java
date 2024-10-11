package skrifer.github.com.base.utils;

import java.util.ArrayList;
import java.util.List;

public class PageUtil<T> {

    private int currentPage; // 当前页码
    private int pageSize;    // 每页大小
    private long totalRecords; // 总记录数
    private List<T> records;   // 当前页的数据列表

    // records 已经经过分页处理
    public PageUtil(int currentPage, int pageSize, long totalRecords, List<T> pagedRecords) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalRecords = totalRecords;
        this.records = pagedRecords;
    }

    // records 未经过分页处理
    public PageUtil(int currentPage, int pageSize, List<T> totalRecords) {
        this(currentPage, pageSize, totalRecords.size(), paginate(totalRecords, pageSize, currentPage));
    }

    // Getter 和 Setter 方法
    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(long totalRecords) {
        this.totalRecords = totalRecords;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    // 可以添加其他有用的方法，比如计算总页数等
    public int getTotalPages() {
        return (int) Math.ceil((double) totalRecords / pageSize);
    }

    // 判断是否是第一页
    public boolean isFirstPage() {
        return currentPage == 1;
    }

    // 判断是否是最后一页
    public boolean isLastPage() {
        return currentPage == getTotalPages();
    }

    /**
     * 模拟分页
     *
     * @param list       原始数据列表
     * @param pageSize   每页显示的条数
     * @param pageNumber 需要获取的页码 从1开始！！！！！！
     * @return 指定页码的数据列表
     */
    private static <T> List<T> paginate(List<T> list, int pageSize, int pageNumber) {
        // 计算跳过的条目数
        int skip = (pageNumber - 1) * pageSize;

        // 检查是否越界
        if (skip >= list.size() || list.isEmpty()) {
            return new ArrayList<>();
        }

        // 计算当前页可以包含的最大条目数
        int end = Math.min(skip + pageSize, list.size());

        // 通过subList获取分页后的列表
        return list.subList(skip, end);
    }
}
