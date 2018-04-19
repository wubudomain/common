package top.wboost.common.kylin.api.search;

import java.util.HashSet;
import java.util.Set;

import top.wboost.common.kylin.api.KylinApi;
import top.wboost.common.kylin.entity.ProjectsReadable;
import top.wboost.common.kylin.exception.KylinParamsException;
import top.wboost.common.kylin.util.KylinBusinessUtil;
import top.wboost.common.util.StringUtil;

public class ApiTablesAndColumnsSearch extends KylinApiSearch {

    static Set<String> filterNameSet = new HashSet<String>();
    static {
        filterNameSet.add("cubeName");
    }

    public ApiTablesAndColumnsSearch() {
        super(KylinApi.TABLES_AND_COLUMNS);
    }

    private String cubeName;

    private String project;

    public String getCubeName() {
        return cubeName;
    }

    public ApiTablesAndColumnsSearch setCubeName(String cubeName) {
        this.cubeName = cubeName;
        return this;
    }

    public String getProject() {
        return project;
    }

    public ApiTablesAndColumnsSearch setProject(String project) {
        this.project = project;
        return this;
    }

    public Set<String> getFilterName() {
        return filterNameSet;
    }

    public boolean checkParams() {
        if (StringUtil.notEmpty(project)) {
            return true;
        } else if (StringUtil.notEmpty(cubeName)) {
            ProjectsReadable project = KylinBusinessUtil.queryProjectByCubeName(cubeName);
            if (project != null) {
                this.project = project.getName();
                return true;
            }
        }
        throw new KylinParamsException("projectName cant be null");
    }

}
