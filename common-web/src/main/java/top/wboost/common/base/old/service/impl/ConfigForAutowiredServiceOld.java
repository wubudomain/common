package top.wboost.common.base.old.service.impl;

import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;

import top.wboost.common.base.annotation.AutoRootApplicationConfig;
import top.wboost.common.base.repository.BaseRepository;
import top.wboost.common.log.entity.Logger;
import top.wboost.common.log.util.LoggerUtil;
import top.wboost.common.system.exception.BeanNotFindException;
import top.wboost.common.system.exception.SystemException;
import top.wboost.common.utils.web.interfaces.context.EzApplicationListener;
import top.wboost.common.utils.web.utils.AopUtils;
import top.wboost.common.utils.web.utils.SpringBeanUtil;

@SuppressWarnings({ "rawtypes", "unchecked" })
@AutoRootApplicationConfig
public class ConfigForAutowiredServiceOld implements EzApplicationListener {

    private Logger log = LoggerUtil.getLogger(getClass());

    @Override
    public void onWebApplicationEvent(ContextRefreshedEvent event) {
        onRootApplicationEvent(event);
    }

    @Override
    public void onRootApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext context = event.getApplicationContext();
        Map<String, Object> servicess = context.getBeansWithAnnotation(Service.class);
        servicess.forEach((name, bean) -> {
            Object target = AopUtils.getTarget(bean);
            if (ClassUtils.isAssignable(top.wboost.common.base.old.service.impl.BaseJpaServiceImpl.class,
                    target.getClass())) {
                autowired((BaseJpaServiceImpl) target);
            }
        });
    }

    protected void autowired(BaseJpaServiceImpl service) {
        Class<?> clazz = ClassUtils.getUserClass(service);
        if (clazz.getSimpleName().indexOf("ServiceImpl") == -1) {
            throw new SystemException("服务层类名必须以ServiceImpl结尾");
        }
        String className = clazz.getSimpleName().substring(0, clazz.getSimpleName().indexOf("ServiceImpl"));
        className = Character.toLowerCase(className.charAt(0)) + className.substring(1, className.length());
        String defaultBeanName = className + "Repository";
        Object repository = SpringBeanUtil.getBean(defaultBeanName);
        if (repository == null) {
            log.warn("cant find bean:" + defaultBeanName + ",please check");
            return;
        }
        if (repository instanceof BaseRepository) {
            BaseRepository<?, ?> findRepository = (BaseRepository<?, ?>) repository;
            log.info("auto config bean: " + defaultBeanName + " to service:" + clazz.getName());
            service.setRepository(findRepository);
        } else {
            throw new BeanNotFindException(defaultBeanName, BaseRepository.class);
        }
    }

    @Override
    public void onBootApplicationEvent(ContextRefreshedEvent event) {
        onWebApplicationEvent(event);
    }

    public boolean doWebAndRootApplicationListener(ContextRefreshedEvent event) {
        return false;
    }

}
