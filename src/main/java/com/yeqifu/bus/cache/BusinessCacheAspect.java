package com.yeqifu.bus.cache;

import com.yeqifu.bus.entity.Customer;
import com.yeqifu.bus.entity.Provider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 落亦-
 * @Date: 2019/12/5 16:39
 */
@Aspect
@Component
@EnableAspectJAutoProxy
public class BusinessCacheAspect {
    /**
     * 日志出处
     */
    private Log log = LogFactory.getLog(BusinessCacheAspect.class);

    /**
     * 声明一个缓存容器
     */
    private Map<String,Object> CACHE_CONTAINER = new HashMap<>();


    /**
     * 声明客户的切面表达式
     */
    private static final String POINTCUT_CUSTOMER_ADD="execution(* com.yeqifu.bus.service.impl.CustomerServiceImpl.save(..))";
    private static final String POINTCUT_CUSTOMER_UPDATE="execution(* com.yeqifu.bus.service.impl.CustomerServiceImpl.updateById(..))";
    private static final String POINTCUT_CUSTOMER_GET="execution(* com.yeqifu.bus.service.impl.CustomerServiceImpl.getById(..))";
    private static final String POINTCUT_CUSTOMER_DELETE="execution(* com.yeqifu.bus.service.impl.CustomerServiceImpl.removeById(..))";
    private static final String POINTCUT_CUSTOMER_BATCHDELETE="execution(* com.yeqifu.bus.service.impl.CustomerServiceImpl.removeByIds(..))";

    private static final String CACHE_CUSTOMER_PROFIX="customer:";

    /**
     * 添加客户切入
     * @param joinPoint
     * @return
     */
    @Around(value = POINTCUT_CUSTOMER_ADD)
    public Object cacheCustomerAdd(ProceedingJoinPoint joinPoint) throws Throwable {
        //取出第一个参数
        Customer object = (Customer) joinPoint.getArgs()[0];
        Boolean res = (Boolean) joinPoint.proceed();
        if (res){
            CACHE_CONTAINER.put(CACHE_CUSTOMER_PROFIX + object.getId(),object);
        }
        return res;
    }

    /**
     * 查询客户切入
     * @param joinPoint
     * @return
     */
    @Around(value = POINTCUT_CUSTOMER_GET)
    public Object cacheCustomerGet(ProceedingJoinPoint joinPoint) throws Throwable {
        //取出第一个参数
        Integer object = (Integer) joinPoint.getArgs()[0];
        //从缓存里面取
        Object res1 = CACHE_CONTAINER.get(CACHE_CUSTOMER_PROFIX + object);
        if (res1!=null){
            log.info("已从缓存里面找到客户对象"+CACHE_CUSTOMER_PROFIX + object);
            return res1;
        }else {
            log.info("未从缓存里面找到客户对象，从数据库中查询并放入缓存");
            Customer res2 =(Customer) joinPoint.proceed();
            CACHE_CONTAINER.put(CACHE_CUSTOMER_PROFIX+res2.getId(),res2);
            return res2;
        }
    }

    /**
     * 更新客户切入
     * @param joinPoint
     * @return
     */
    @Around(value = POINTCUT_CUSTOMER_UPDATE)
    public Object cacheCustomerUpdate(ProceedingJoinPoint joinPoint) throws Throwable {
        //取出第一个参数
        Customer customerVo = (Customer) joinPoint.getArgs()[0];
        Boolean isSuccess = (Boolean) joinPoint.proceed();
        if (isSuccess){
            Customer customer =(Customer) CACHE_CONTAINER.get(CACHE_CUSTOMER_PROFIX + customerVo.getId());
            if (null==customer){
                customer=new Customer();
            }
            BeanUtils.copyProperties(customerVo,customer);
            log.info("客户对象缓存已更新"+CACHE_CUSTOMER_PROFIX + customerVo.getId());
            CACHE_CONTAINER.put(CACHE_CUSTOMER_PROFIX+customer.getId(),customer);
        }
        return isSuccess;
    }

    /**
     * 删除客户切入
     * @param joinPoint
     * @return
     */
    @Around(value = POINTCUT_CUSTOMER_DELETE)
    public Object cacheCustomerDelete(ProceedingJoinPoint joinPoint) throws Throwable {

        //取出第一个参数
        Integer id = (Integer) joinPoint.getArgs()[0];
        Boolean isSuccess = (Boolean) joinPoint.proceed();
        if (isSuccess){
            //删除缓存
            CACHE_CONTAINER.remove(CACHE_CUSTOMER_PROFIX+id);
        }
        return isSuccess;
    }

    /**
     * 批量删除客户切入
     *
     * @throws Throwable
     */
    @Around(value = POINTCUT_CUSTOMER_BATCHDELETE)
    public Object cacheCustomerBatchDelete(ProceedingJoinPoint joinPoint) throws Throwable {
        // 取出第一个参数
        @SuppressWarnings("unchecked")
        Collection<Serializable> idList = (Collection<Serializable>) joinPoint.getArgs()[0];
        Boolean isSuccess = (Boolean) joinPoint.proceed();
        if (isSuccess) {
            for (Serializable id : idList) {
                // 删除缓存
                CACHE_CONTAINER.remove(CACHE_CUSTOMER_PROFIX + id);
                log.info("客户对象缓存已删除" + CACHE_CUSTOMER_PROFIX + id);
            }
        }
        return isSuccess;
    }


    /**
     * 声明供应商的切面表达式
     */
    private static final String POINTCUT_PROVIDER_ADD="execution(* com.yeqifu.bus.service.impl.ProviderServiceImpl.save(..))";
    private static final String POINTCUT_PROVIDER_UPDATE="execution(* com.yeqifu.bus.service.impl.ProviderServiceImpl.updateById(..))";
    private static final String POINTCUT_PROVIDER_GET="execution(* com.yeqifu.bus.service.impl.ProviderServiceImpl.getById(..))";
    private static final String POINTCUT_PROVIDER_DELETE="execution(* com.yeqifu.bus.service.impl.ProviderServiceImpl.removeById(..))";
    private static final String POINTCUT_PROVIDER_BATCHDELETE="execution(* com.yeqifu.bus.service.impl.ProviderServiceImpl.removeByIds(..))";

    private static final String CACHE_PROVIDER_PROFIX="provider:";

    /**
     * 添加供应商切入
     * @param joinPoint
     * @return
     */
    @Around(value = POINTCUT_PROVIDER_ADD)
    public Object cacheProviderAdd(ProceedingJoinPoint joinPoint) throws Throwable {
        //取出第一个参数
        Provider object = (Provider) joinPoint.getArgs()[0];
        Boolean res = (Boolean) joinPoint.proceed();
        if (res){
            CACHE_CONTAINER.put(CACHE_PROVIDER_PROFIX + object.getId(),object);
        }
        return res;
    }

    /**
     * 查询供应商切入
     * @param joinPoint
     * @return
     */
    @Around(value = POINTCUT_PROVIDER_GET)
    public Object cacheProviderGet(ProceedingJoinPoint joinPoint) throws Throwable {
        //取出第一个参数
        Integer object = (Integer) joinPoint.getArgs()[0];
        //从缓存里面取
        Object res1 = CACHE_CONTAINER.get(CACHE_PROVIDER_PROFIX + object);
        if (res1!=null){
            log.info("已从缓存里面找到供应商对象"+CACHE_PROVIDER_PROFIX + object);
            return res1;
        }else {
            log.info("未从缓存里面找到供应商对象，从数据库中查询并放入缓存");
            Provider res2 =(Provider) joinPoint.proceed();
            CACHE_CONTAINER.put(CACHE_PROVIDER_PROFIX+res2.getId(),res2);
            return res2;
        }
    }

    /**
     * 更新供应商切入
     * @param joinPoint
     * @return
     */
    @Around(value = POINTCUT_PROVIDER_UPDATE)
    public Object cacheProviderUpdate(ProceedingJoinPoint joinPoint) throws Throwable {
        //取出第一个参数
        Provider providerVo = (Provider) joinPoint.getArgs()[0];
        Boolean isSuccess = (Boolean) joinPoint.proceed();
        if (isSuccess){
            Provider provider =(Provider) CACHE_CONTAINER.get(CACHE_PROVIDER_PROFIX + providerVo.getId());
            if (null==provider){
                provider=new Provider();
            }
            BeanUtils.copyProperties(providerVo,provider);
            log.info("供应商对象缓存已更新"+CACHE_PROVIDER_PROFIX + providerVo.getId());
            CACHE_CONTAINER.put(CACHE_PROVIDER_PROFIX+provider.getId(),provider);
        }
        return isSuccess;
    }

    /**
     * 删除供应商切入
     * @param joinPoint
     * @return
     */
    @Around(value = POINTCUT_PROVIDER_DELETE)
    public Object cacheProviderDelete(ProceedingJoinPoint joinPoint) throws Throwable {

        //取出第一个参数
        Integer id = (Integer) joinPoint.getArgs()[0];
        Boolean isSuccess = (Boolean) joinPoint.proceed();
        if (isSuccess){
            //删除缓存
            CACHE_CONTAINER.remove(CACHE_PROVIDER_PROFIX+id);
        }
        return isSuccess;
    }

    /**
     * 批量删除供应商切入
     *
     * @throws Throwable
     */
    @Around(value = POINTCUT_PROVIDER_BATCHDELETE)
    public Object cacheProviderBatchDelete(ProceedingJoinPoint joinPoint) throws Throwable {
        // 取出第一个参数
        @SuppressWarnings("unchecked")
        Collection<Serializable> idList = (Collection<Serializable>) joinPoint.getArgs()[0];
        Boolean isSuccess = (Boolean) joinPoint.proceed();
        if (isSuccess) {
            for (Serializable id : idList) {
                // 删除缓存
                CACHE_CONTAINER.remove(CACHE_PROVIDER_PROFIX + id);
                log.info("供应商对象缓存已删除" + CACHE_PROVIDER_PROFIX + id);
            }
        }
        return isSuccess;
    }
    
    
}
