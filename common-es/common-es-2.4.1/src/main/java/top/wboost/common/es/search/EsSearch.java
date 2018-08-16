package top.wboost.common.es.search;

import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder.Operator;
import top.wboost.common.es.entity.BaseEsIndex;
import top.wboost.common.es.entity.EsFilter;
import top.wboost.common.es.util.EsQueryAction;

import java.util.*;

/**
 * ES基本查询实体类
 * @author jwSun
 * @date 2017年4月18日 上午11:56:22
 */
public class EsSearch extends BaseEsIndex {

    /**
     * 默认查询类型,查出来的size与用户指定相同
     */
    private SearchType searchType = SearchType.QUERY_THEN_FETCH;
    //设置默认的builder
    //private Map<EsQueryType, BiFunction<EsQueryType, String, QueryBuilder>> defaultQuerybuilder = new HashMap<>();
    //过滤key使用不同的build
    //          key                     type         key     result
    //private Map<EsQueryType, List<BiFunction<EsQueryType, String, QueryBuilder>>> builderTypeChoose = new HashMap<>();
    //配置符合条件的builder
    private List<BuilderFilter> builderFilters = new ArrayList<>();
    private Map<EsQueryType, Map<String, Set<String>>> queryMap = new HashMap<>();
    private Map<EsQueryType, List<List<QueryBuilder>>> specialMap = new HashMap<>();
    /**子文档MAP key:value  子文档名:子文档search**/
    private Map<EsQueryType, Map<String, EsSearch>> childs = new LinkedHashMap<>();
    private List<EsFilter> filters = new ArrayList<>();
    /**
     * should至少匹配数量
     */
    private Integer minimumNumberShouldMatch;
    /**
     * 查询分词时使用模式(ES默认OR 本类默认AND),一般情况无需修改
     */
    private Operator operator = Operator.AND;

    {
        queryMap.put(EsQueryType.MUST, new HashMap<>());
        queryMap.put(EsQueryType.SHOULD, new HashMap<>());
        queryMap.put(EsQueryType.MUST_NOT, new HashMap<>());
    }

    public EsSearch(String index, String type, SearchType searchType) {
        super(index, type);
        this.searchType = searchType;
    }

    public EsSearch(String index, String type) {
        super(index, type);
    }

    public EsSearch addBuilderFilters(BuilderFilter builderFilter) {
        this.builderFilters.add(builderFilter);
        return this;
    }

    public List<BuilderFilter> getBuilderFilters() {
        return builderFilters;
    }

    public EsSearch must(String key, String val) {
        return this.must(key, val, Boolean.FALSE);
    }

    public EsSearch filters(EsFilter... filters) {
        this.filters.addAll(Arrays.asList(filters));
        return this;
    }

    public List<EsFilter> getFilters() {
        return this.filters;
    }

    public EsSearch must(String key, String val, Boolean cover) {
        return putToMap(key, val, cover, getMustMap());
    }

    public SpecialBuilder special(EsQueryType esQueryType) {
        return new SpecialBuilder(esQueryType, this);
    }

    protected EsSearch putToMap(String key, String val, Boolean cover, Map<String, Set<String>> map) {
        if (map.get(key) == null || cover) {
            Set<String> set = new HashSet<String>();
            set.add(val);
            map.put(key, set);
        } else {
            map.get(key).add(val);
        }
        return this;
    }

    public void merge(EsSearch search) {
        merge(this, search);
    }

    public EsSearch merge(EsSearch search1, EsSearch search2) {
        Arrays.asList(EsQueryType.values()).forEach(esQueryType -> {
            // queryMap
            if (search1.queryMap.get(esQueryType) == null) {
                search1.queryMap.put(esQueryType, search2.queryMap.get(esQueryType));
            } else {
                Map<String, Set<String>> propMap = search1.queryMap.get(esQueryType);
                Map<String, Set<String>> propMap2 = search2.queryMap.get(esQueryType);
                if (propMap2.size() != 0) {
                    propMap2.forEach((name, valColl) -> {
                        if (!propMap.containsKey(name)) {
                            propMap.put(name, new HashSet<>());
                        }
                        propMap.get(name).addAll(valColl);
                    });
                }
            }

            //specialMap
            if (search1.specialMap.get(esQueryType) == null) {
                search1.specialMap.put(esQueryType, search2.specialMap.get(esQueryType));
            } else {
                List<List<QueryBuilder>> queryBuilders = search1.specialMap.get(esQueryType);
                List<List<QueryBuilder>> queryBuilders2 = search2.specialMap.get(esQueryType);
                if (queryBuilders2 != null) {
                    queryBuilders.addAll(queryBuilders2);
                }
            }

            //childs
            if (search1.childs.get(esQueryType) == null) {
                search1.childs.put(esQueryType, search2.childs.get(esQueryType));
            } else {
                Map<String, EsSearch> typeMap = search1.childs.get(esQueryType);
                Map<String, EsSearch> typeMap2 = search2.childs.get(esQueryType);
                if (typeMap2 == null)
                    return;
                typeMap.forEach((type, childsearch) -> {
                    EsSearch childsearch2 = typeMap2.get(type);
                    if (childsearch2 == null)
                        return;
                    childsearch.merge(childsearch2);
                });
            }
        });
        search1.filters.addAll(search2.filters);
        return search1;
    }

    public Map<EsQueryType, Map<String, EsSearch>> getChilds() {
        return childs;
    }

    public EsSearch mustAll(Map<String, Set<String>> mustMap) {
        getMustMap().putAll(mustMap);
        return this;
    }

    public EsSearch mustNot(String key, String val) {
        return this.mustNot(key, val, Boolean.FALSE);
    }

    public EsSearch mustNot(String key, String val, Boolean cover) {
        return putToMap(key, val, cover, getMustNotMap());
    }

    public EsSearch mustNotAll(Map<String, Set<String>> mustMap) {
        getMustNotMap().putAll(mustMap);
        return this;
    }

    public EsSearch should(String key, String val) {
        return this.should(key, val, Boolean.FALSE);
    }

    public EsSearch should(String key, String val, Boolean cover) {
        return putToMap(key, val, cover, getShouldMap());
    }

    public EsSearch shouldAll(Map<String, Set<String>> shouldMap) {
        getShouldMap().putAll(shouldMap);
        return this;
    }

    public SearchType getSearchType() {
        return searchType;
    }

    public void setSearchType(SearchType searchType) {
        this.searchType = searchType;
    }

    public Map<String, Set<String>> getMustMap() {
        return this.queryMap.get(EsQueryType.MUST);
    }

    public Map<String, Set<String>> getMustNotMap() {
        return this.queryMap.get(EsQueryType.MUST_NOT);
    }

    public Map<String, Set<String>> getShouldMap() {
        return this.queryMap.get(EsQueryType.SHOULD);
    }

    public Map<EsQueryType, List<List<QueryBuilder>>> getSpecialMap() {
        return this.specialMap;
    }

    public Integer getMinimumNumberShouldMatch() {
        return minimumNumberShouldMatch;
    }

    public void setMinimumNumberShouldMatch(Integer minimumNumberShouldMatch) {
        this.minimumNumberShouldMatch = minimumNumberShouldMatch;
    }

    /**
     * @Description 根据参数增加最小匹配数，若minimumNumberShouldMatch为空则初始化并为参数i
     * @param i 增加最小匹配数
     */
    public void addMinimumNumberShouldMatch(int i) {
        if (this.minimumNumberShouldMatch == null) {
            this.minimumNumberShouldMatch = i;
        } else {
            this.minimumNumberShouldMatch += i;
        }
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public interface BuilderFilter {
        public boolean support(EsQueryType esQueryType, String key, Object val);

        public QueryBuilder apply(EsQueryType esQueryType, String key, Object val);
    }

    public static class SpecialBuilder {
        private EsQueryType esQueryType;
        private List<List<QueryBuilder>> queryBuilders;
        private Map<String, EsSearch> childSearch;

        public SpecialBuilder(EsQueryType esQueryType, EsSearch esSearch) {
            super();
            this.esQueryType = esQueryType;
            initSpecial(esSearch);
        }

        protected void initSpecial(EsSearch esSearch) {
            if (esSearch.specialMap == null) {
                esSearch.specialMap = new HashMap<>();
            }
            if ((!esSearch.specialMap.containsKey(esQueryType)) || esSearch.specialMap.get(esQueryType) == null) {
                this.queryBuilders = new ArrayList<>();
                esSearch.specialMap.put(esQueryType, this.queryBuilders);
            } else {
                this.queryBuilders = esSearch.specialMap.get(esQueryType);
            }
            if (!esSearch.childs.containsKey(esQueryType)) {
                this.childSearch = new HashMap<>();
                esSearch.childs.put(esQueryType, this.childSearch);
            } else {
                this.childSearch = esSearch.childs.get(esQueryType);
            }
        }

        public SpecialBuilder child(String type, EsSearch childSearch) {
            if (this.childSearch.containsKey(type)) {
                this.childSearch.get(type).merge(childSearch);
            } else {
                this.childSearch.put(type, childSearch);
            }
            return this;
        }

        public SpecialBuilder childAnd(String type, EsSearch childSearch) {
            if (this.childSearch.containsKey(type)) {
                this.childSearch.get(type).special(esQueryType).and(childSearch);
            } else {
                this.childSearch.put(type, childSearch);
            }
            return this;
        }

        private List<QueryBuilder> initBuilderList(QueryBuilder... builders) {
            List<QueryBuilder> addList = new ArrayList<>();
            Arrays.asList(builders).forEach(builder -> {
                addList.add(builder);
            });
            return addList;
        }

        public SpecialBuilder fuzzy(String key, Object val) {
            this.queryBuilders.add(initBuilderList(QueryBuilders.fuzzyQuery(key, val)));
            return this;
        }

        public SpecialBuilder wildcard(String key, String val) {
            this.queryBuilders.add(initBuilderList(QueryBuilders.wildcardQuery(key, val)));
            return this;
        }

        public SpecialBuilder regexp(String key, String regexp) {
            this.queryBuilders.add(initBuilderList(QueryBuilders.regexpQuery(key, regexp)));
            return this;
        }

        public SpecialBuilder or(EsSearch... searchs) {
            List<QueryBuilder> queryBuilders = new ArrayList<>();
            Arrays.asList(searchs).forEach(search -> {
                queryBuilders.add(EsQueryAction.getBoolQueryBuilder(search));
            });
            this.queryBuilders.add(queryBuilders);
            return this;
        }

        public SpecialBuilder and(EsSearch... searchs) {
            Arrays.asList(searchs).forEach(search -> {
                this.queryBuilders.add(initBuilderList(EsQueryAction.getBoolQueryBuilder(search)));
            });
            return this;
        }

        public SpecialBuilder and(QueryBuilder queryBuilder) {
            this.queryBuilders.add(initBuilderList(queryBuilder));
            return this;
        }
    }

}
