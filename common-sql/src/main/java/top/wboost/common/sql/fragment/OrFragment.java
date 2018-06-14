package top.wboost.common.sql.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import top.wboost.common.system.code.SystemCode;
import top.wboost.common.system.exception.SystemCodeException;

public class OrFragment implements Fragment {

    private List<Fragment> fragmentList = new ArrayList<>();

    public void addFragments(Fragment... fragments) {
        fragmentList.addAll(Arrays.asList(fragments));
    }

    @Override
    public String toFragmentString() {
        if (fragmentList.size() <= 1) {
            throw new SystemCodeException(SystemCode.PROMPT).setPromptMessage("or 条件至少需要2个 Fragment!");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        fragmentList.forEach(fragment -> {
            sb.append(fragment.toFragmentString());
            sb.append(" or ");
        });
        sb.delete(sb.length() - 4, sb.length());
        sb.append(")");
        return sb.toString();
    }

}
