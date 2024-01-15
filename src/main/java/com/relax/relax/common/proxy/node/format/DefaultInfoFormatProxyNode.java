package com.relax.relax.common.proxy.node.format;

import com.relax.relax.common.enums.ProxyMethodType;
import com.relax.relax.common.proxy.RelaxViewBeforeProxy;
import com.relax.relax.common.utils.BeanUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class DefaultInfoFormatProxyNode extends RelaxViewBeforeProxy {
    @Override
    public <T> T proxy(T t, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = (Map<String, Object>) t;
        T finalResult = BeanUtil.mapToBean(BeanUtil.mapKVToCamelCase((Map<String, Object>) result.get("info")), (Class<T>) getExecutor().getBaseEntityClass(), true);
        result.put("info", finalResult);
        return (T) result;
    }

    @Override
    public <T> boolean check(T t, ProxyMethodType type, HttpServletRequest request, HttpServletResponse response) {
        return type == ProxyMethodType.INFO;
    }
}
