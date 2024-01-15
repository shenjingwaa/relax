package com.relax.relax.common.proxy.node.format;

import com.relax.relax.common.enums.ProxyMethodType;
import com.relax.relax.common.proxy.RelaxViewBeforeProxy;
import com.relax.relax.common.utils.BeanUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DefaultListFormatProxyNode extends RelaxViewBeforeProxy {
    @Override
    public boolean check(Object o, ProxyMethodType type, HttpServletRequest request, HttpServletResponse response) {
        return type == ProxyMethodType.LIST;
    }

    @Override
    public <T> T proxy(T t, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = (Map<String, Object>) t;
        List<Map<String, Object>> page = (List<Map<String, Object>>) result.get("list");
        result.put("list", page.stream()
                .map(map -> BeanUtil.mapToBean(BeanUtil.mapKVToCamelCase(map), (Class<T>) getExecutor().getBaseEntityClass(), true))
                .collect(Collectors.toList()));
        return (T) result;
    }
}
