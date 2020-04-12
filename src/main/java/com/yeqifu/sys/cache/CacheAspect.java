package com.yeqifu.sys.cache;

import com.yeqifu.sys.entity.Dept;
import com.yeqifu.sys.entity.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author: 落亦-
 * @Date: 2019/11/27 18:42
 */
@Aspect
@Component
@EnableAspectJAutoProxy
public class CacheAspect {

    /**
     * 日志出处
     */
    private Log log = LogFactory.getLog(CacheAspect.class);

    /**
     * 声明一个缓存容器
     */
    private Map<String,Object> CACHE_CONTAINER = CachePool.CACHE_CONTAINER;

    /**
     * 声明部门的切面表达式
     */
    private static final String POINTCUT_DEPT_ADD="execution(* com.yeqifu.sys.service.impl.DeptServiceImpl.save(..))";
    private static final String POINTCUT_DEPT_UPDATE="execution(* com.yeqifu.sys.service.impl.DeptServiceImpl.updateById(..))";
    private static final String POINTCUT_DEPT_GET="execution(* com.yeqifu.sys.service.impl.DeptServiceImpl.getById(..))";
    private static final String POINTCUT_DEPT_DELETE="execution(* com.yeqifu.sys.service.impl.DeptServiceImpl.removeById(..))";

    private static final String CACHE_DEPT_PROFIX="dept:";

    /**
     * 添加部门切入
     * @param joinPoint
     * @return
     */
    @Around(value = POINTCUT_DEPT_ADD)
    public Object cacheDeptAdd(ProceedingJoinPoint joinPoint) throws Throwable {
        //取出第一个参数
        Dept object = (Dept) joinPoint.getArgs()[0];
        Boolean res = (Boolean) joinPoint.proceed();
        if (res){
            CACHE_CONTAINER.put(CACHE_DEPT_PROFIX + object.getId(),object);
        }
        return res;
    }

    /**
     * 查询部门切入
     * @param joinPoint
     * @return
     */
    @Around(value = POINTCUT_DEPT_GET)
    public Object cacheDeptGet(ProceedingJoinPoint joinPoint) throws Throwable {
        //取出第一个参数
        Integer object = (Integer) joinPoint.getArgs()[0];
        //从缓存里面取
        Object res1 = CACHE_CONTAINER.get(CACHE_DEPT_PROFIX + object);
        if (res1!=null){
            log.info("已从缓存里面找到部门对象"+CACHE_DEPT_PROFIX + object);
            return res1;
        }else {
            log.info("未从缓存里面找到部门对象，从数据库中查询并放入缓存");
            Dept res2 =(Dept) joinPoint.proceed();
            CACHE_CONTAINER.put(CACHE_DEPT_PROFIX+res2.getId(),res2);
            return res2;
        }
    }

    /**
     * 更新部门切入
     * @param joinPoint
     * @return
     */
    @Around(value = POINTCUT_DEPT_UPDATE)
    public Object cacheDeptUpdate(ProceedingJoinPoint joinPoint) throws Throwable {
        //取出第一个参数
        Dept deptVo = (Dept) joinPoint.getArgs()[0];
        Boolean isSuccess = (Boolean) joinPoint.proceed();
        if (isSuccess){
            Dept dept =(Dept) CACHE_CONTAINER.get(CACHE_DEPT_PROFIX + deptVo.getId());
            if (null==dept){
                dept=new Dept();
            }
            BeanUtils.copyProperties(deptVo,dept);
            log.info("部门对象缓存已更新"+CACHE_DEPT_PROFIX + deptVo.getId());
            CACHE_CONTAINER.put(CACHE_DEPT_PROFIX+dept.getId(),dept);
        }
        return isSuccess;
    }

    /**
     * 删除部门切入
     * @param joinPoint
     * @return
     */
    @Around(value = POINTCUT_DEPT_DELETE)
    public Object cacheDeptDelete(ProceedingJoinPoint joinPoint) throws Throwable {

        //取出第一个参数
        Integer id = (Integer) joinPoint.getArgs()[0];
        Boolean isSuccess = (Boolean) joinPoint.proceed();
        if (isSuccess){
            //删除缓存
            CACHE_CONTAINER.remove(CACHE_DEPT_PROFIX+id);
        }
        return isSuccess;
    }

    /**
     * 声明用户的切面表达式
     */
    private static final String POINTCUT_USER_UPDATE="execution(* com.yeqifu.sys.service.impl.UserServiceImpl.updateById(..))";
    private static final String POINTCUT_USER_ADD="execution(* com.yeqifu.sys.service.impl.UserServiceImpl.updateById(..))";
    private static final String POINTCUT_USER_GET="execution(* com.yeqifu.sys.service.impl.UserServiceImpl.getById(..))";
    private static final String POINTCUT_USER_DELETE="execution(* com.yeqifu.sys.service.impl.UserServiceImpl.removeById(..))";

    private static final String CACHE_USER_PROFIX="user:";

    /**
     * 添加用户切入
     * @param joinPoint
     * @return
     */
    @Around(value = POINTCUT_USER_ADD)
    public Object cacheUserAdd(ProceedingJoinPoint joinPoint) throws Throwable {
        //取出第一个参数
        User object = (User) joinPoint.getArgs()[0];
        Boolean res = (Boolean) joinPoint.proceed();
        if (res){
            CACHE_CONTAINER.put(CACHE_USER_PROFIX + object.getId(),object);
        }
        return res;
    }

    /**
     * 查询用户切入
     * @param joinPoint
     * @return
     */
    @Around(value = POINTCUT_USER_GET)
    public Object cacheUserGet(ProceedingJoinPoint joinPoint) throws Throwable {
        //取出第一个参数
        Integer object = (Integer) joinPoint.getArgs()[0];
        //从缓存里面取
        Object res1 = CACHE_CONTAINER.get(CACHE_USER_PROFIX + object);
        if (res1!=null){
            log.info("已从缓存里面找到用户对象"+CACHE_USER_PROFIX + object);
            return res1;
        }else {
            log.info("未从缓存里面找到用户对象，从数据库中查询并放入缓存");
            User res2 =(User) joinPoint.proceed();
            CACHE_CONTAINER.put(CACHE_USER_PROFIX+res2.getId(),res2);
            return res2;
        }
    }

    /**
     * 更新用户切入
     * @param joinPoint
     * @return
     */
    @Around(value = POINTCUT_USER_UPDATE)
    public Object cacheUserUpdate(ProceedingJoinPoint joinPoint) throws Throwable {
        //取出第一个参数
        User userVo = (User) joinPoint.getArgs()[0];
        Boolean isSuccess = (Boolean) joinPoint.proceed();
        if (isSuccess){
            User user =(User) CACHE_CONTAINER.get(CACHE_USER_PROFIX + userVo.getId());
            if (null==user){
                user=new User();
            }
            BeanUtils.copyProperties(userVo,user);
            log.info("用户对象缓存已更新"+CACHE_USER_PROFIX + userVo.getId());
            CACHE_CONTAINER.put(CACHE_USER_PROFIX+user.getId(),user);
        }
        return isSuccess;
    }

    /**
     * 删除用户切入
     * @param joinPoint
     * @return
     */
    @Around(value = POINTCUT_USER_DELETE)
    public Object cacheUserDelete(ProceedingJoinPoint joinPoint) throws Throwable {

        //取出第一个参数
        Integer id = (Integer) joinPoint.getArgs()[0];
        Boolean isSuccess = (Boolean) joinPoint.proceed();
        if (isSuccess){
            //删除缓存
            CACHE_CONTAINER.remove(CACHE_USER_PROFIX+id);
        }
        return isSuccess;
    }

}
