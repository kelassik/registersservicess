package com.digital.solution.generalservice.exception;

import com.digital.solution.generalservice.domain.dto.error.ApiError;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpMessageConverterExtractor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ApiErrorRestErrorHandler<T extends ApiError> extends DefaultResponseErrorHandler {

    private final HttpMessageConverterExtractor<T> jacksonMessageConverter;

    public ApiErrorRestErrorHandler(Class<T> responseType) {
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        messageConverters.add(new MappingJackson2HttpMessageConverter());
        this.jacksonMessageConverter = new HttpMessageConverterExtractor<>(responseType, messageConverters);
    }

    @Override
    public void handleError(@NotNull ClientHttpResponse response) throws IOException {
        ResponseEntity<ApiError> responseEntity = new ResponseEntity<>(this.jacksonMessageConverter.extractData(response),
                response.getHeaders(), response.getStatusCode());

        ApiErrorRestException apiErrorRestException = new ApiErrorRestException(StringUtils.join("-", response.getStatusText()));

        apiErrorRestException.setResponseEntity(responseEntity);
        throw apiErrorRestException;
//    }oFilter(ApplicationFilterChain.java:189)
//    at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162)
//    at org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:93)
//    at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119)
//    at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189)
//    at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162)
//    at org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:201)
//    at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119)
//    at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189)
//    at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162)
//    at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:197)
//    at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:97)
//    at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:540)
//    at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:135)
//    at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:92)
//    at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:78)
//    at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:357)
//    at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:382)
//    at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:65)
//    at org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:895)
//    at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1722)
//    at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:49)
//    at org.apache.tomcat.util.threads.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1191)
//    at org.apache.tomcat.util.threads.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:659)
//    at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)
//    at java.base/java.lang.Thread.run(Thread.java:829)
//    Caused by: org.hibernate.exception.DataException: could not execute statement
//    at org.hibernate.exception.internal.SQLExceptionTypeDelegate.convert(SQLExceptionTypeDelegate.java:52)
//    at org.hibernate.exception.internal.StandardSQLExceptionConverter.convert(StandardSQLExceptionConverter.java:42)
//    at org.hibernate.engine.jdbc.spi.SqlExceptionHelper.convert(SqlExceptionHelper.java:113)
//    at org.hibernate.engine.jdbc.spi.SqlExceptionHelper.convert(SqlExceptionHelper.java:99)
//    at org.hibernate.engine.jdbc.internal.ResultSetReturnImpl.executeUpdate(ResultSetReturnImpl.java:200)
//    at org.hibernate.dialect.identity.GetGeneratedKeysDelegate.executeAndExtract(GetGeneratedKeysDelegate.java:57)
//    at org.hibernate.id.insert.AbstractReturningDelegate.performInsert(AbstractReturningDelegate.java:43)
//    at org.hibernate.persister.entity.AbstractEntityPersister.insert(AbstractEntityPersister.java:3196)
//    at org.hibernate.persister.entity.AbstractEntityPersister.insert(AbstractEntityPersister.java:3802)
//    at org.hibernate.action.internal.EntityIdentityInsertAction.execute(EntityIdentityInsertAction.java:84)
//    at org.hibernate.engine.spi.ActionQueue.execute(ActionQueue.java:645)
//    at org.hibernate.engine.spi.ActionQueue.addResolvedEntityInsertAction(ActionQueue.java:282)
//    at org.hibernate.engine.spi.ActionQueue.addInsertAction(ActionQueue.java:263)
//    at org.hibernate.engine.spi.ActionQueue.addAction(ActionQueue.java:317)
//    at org.hibernate.event.internal.AbstractSaveEventListener.addInsertAction(AbstractSaveEventListener.java:330)
//    at org.hibernate.event.internal.AbstractSaveEventListener.performSaveOrReplicate(AbstractSaveEventListener.java:287)
//    at org.hibernate.event.internal.AbstractSaveEventListener.performSave(AbstractSaveEventListener.java:193)
//    at org.hibernate.event.internal.AbstractSaveEventListener.saveWithGeneratedId(AbstractSaveEventListener.java:123)
//    at org.hibernate.event.internal.DefaultPersistEventListener.entityIsTransient(DefaultPersistEventListener.java:185)
//    at org.hibernate.event.internal.DefaultPersistEventListener.onPersist(DefaultPersistEventListener.java:128)
//    at org.hibernate.event.internal.DefaultPersistEventListener.onPersist(DefaultPersistEventListener.java:55)
//    at org.hibernate.event.service.internal.EventListenerGroupImpl.fireEventOnEachListener(EventListenerGroupImpl.java:99)
//    at org.hibernate.internal.SessionImpl.firePersist(SessionImpl.java:720)
//    at org.hibernate.internal.SessionImpl.persist(SessionImpl.java:706)
//    at jdk.internal.reflect.GeneratedMethodAccessor69.invoke(Unknown Source)
//    at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
//    at java.base/java.lang.reflect.Method.invoke(Method.java:566)
//    at org.springframework.orm.jpa.ExtendedEntityManagerCreator$ExtendedEntityManagerInvocationHandler.invoke(ExtendedEntityManagerCreator.java:362)
//    at com.sun.proxy.$Proxy100.persist(Unknown Source)
//    at jdk.internal.reflect.GeneratedMethodAccessor69.invoke(Unknown Source)
//    at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
//    at java.base/java.lang.reflect.Method.invoke(Method.java:566)
//    at org.springframework.orm.jpa.SharedEntityManagerCreator$SharedEntityManagerInvocationHandler.invoke(SharedEntityManagerCreator.java:311)
//    at com.sun.proxy.$Proxy100.persist(Unknown Source)
//    at org.springframework.data.jpa.repository.support.SimpleJpaRepository.save(SimpleJpaRepository.java:597)
//    at jdk.internal.reflect.GeneratedMethodAccessor85.invoke(Unknown Source)
//    at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
//    at java.base/java.lang.reflect.Method.invoke(Method.java:566)
//    at org.springframework.data.repository.core.support.RepositoryMethodInvoker$RepositoryFragmentMethodInvoker.lambda$new$0(RepositoryMethodInvoker.java:289)
//    at org.springframework.data.repository.core.support.RepositoryMethodInvoker.doInvoke(RepositoryMethodInvoker.java:137)
//    at org.springframework.data.repository.core.support.RepositoryMethodInvoker.invoke(RepositoryMethodInvoker.java:121)
//    at org.springframework.data.repository.core.support.RepositoryComposition$RepositoryFragments.invoke(RepositoryComposition.java:529)
//    at org.springframework.data.repository.core.support.RepositoryComposition.invoke(RepositoryComposition.java:285)
//    at org.springframework.data.repository.core.support.RepositoryFactorySupport$ImplementationMethodExecutionInterceptor.invoke(RepositoryFactorySupport.java:599)
//    at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)
//    at org.springframework.data.repository.core.support.QueryExecutorMethodInterceptor.doInvoke(QueryExecutorMethodInterceptor.java:163)
//    at org.springframework.data.repository.core.support.QueryExecutorMethodInterceptor.invoke(QueryExecutorMethodInterceptor.java:138)
//    at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)
//    at org.springframework.data.projection.DefaultMethodInvokingMethodInterceptor.invoke(DefaultMethodInvokingMethodInterceptor.java:80)
//    at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)
//    at org.springframework.transaction.interceptor.TransactionInterceptor$1.proceedWithInvocation(TransactionInterceptor.java:123)
//    at org.springframework.transaction.interceptor.TransactionAspectSupport.invokeWithinTransaction(TransactionAspectSupport.java:388)
//    at org.springframework.transaction.interceptor.TransactionInterceptor.invoke(TransactionInterceptor.java:119)
//    at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)
//    at org.springframework.dao.support.PersistenceExceptionTranslationInterceptor.invoke(PersistenceExceptionTranslationInterceptor.java:137)
//            ... 64 common frames omitted
//    Caused by: com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Data too long for column 'user_agent' at row 1
//    at com.mysql.cj.jdbc.exceptions.SQLExceptionsMapping.translateException(SQLExceptionsMapping.java:104)
//    at com.mysql.cj.jdbc.ClientPreparedStatement.executeInternal(ClientPreparedStatement.java:916)
//    at com.mysql.cj.jdbc.ClientPreparedStatement.executeUpdateInternal(ClientPreparedStatement.java:1061)
//    at com.mysql.cj.jdbc.ClientPreparedStatement.executeUpdateInternal(ClientPreparedStatement.java:1009)
//    at com.mysql.cj.jdbc.ClientPreparedStatement.executeLargeUpdate(ClientPreparedStatement.java:1320)
//    at com.mysql.cj.jdbc.ClientPreparedStatement.executeUpdate(ClientPreparedStatement.java:994)
//    at com.zaxxer.hikari.pool.ProxyPreparedStatement.executeUpdate(ProxyPreparedStatement.java:61)
//    at com.zaxxer.hikari.pool.HikariProxyPreparedStatement.executeUpdate(HikariProxyPreparedStatement.java)
//    at org.hibernate.engine.jdbc.internal.ResultSetReturnImpl.executeUpdate(ResultSetReturnImpl.java:197)
//            ... 114 common frames omitted
//2023-Apr-14  02:14:27.594 INFO  [http-nio-8080-exec-9] [app_name=GENERAL-SERVICE] [trace_id=] c.d.s.g.utils.ReadAndWriteFileUtils - start readFileTransaction
//2023-Apr-14  02:14:27.595 INFO  [http-nio-8080-exec-9] [app_name=GENERAL-SERVICE] [trace_id=] c.d.s.g.u.l.ControllerRequestResponseLogger -
//            ==========================INCOMING Request Begin============================
//    URI          : {/api/generalservice/v1/user/registration}
//    Method       : {POST}
//    Headers      : {[host:"localhost:8080", content-type:"application/json", connection:"keep-alive", accept:"*/*", user-agent:"IPriceChecker/1.0 (com.yopa.cullun.IPriceChecker; build:1; iOS 16.2.0) Alamofire/5.6.1", accept-language:"en;q=1.0", content-length:"231", accept-encoding:"br;q=1.0, gzip;q=0.9, deflate;q=0.8"]}
//    Request body : {{"mobilePhone":"08564***4234","fullName":"mans poll","email":"ko*****l@gmail.com","cif":"1234567","placeOfBirth":"Rembang","dateOfBirth":"1992-01-04","residentType":"WNI","shortName":"werrw","maritalStatus":"SINGLE","gender":"MAN"}}
//==========================INCOMING Request End==============================
//            2023-Apr-14  02:14:27.596 INFO  [http-nio-8080-exec-9] [app_name=GENERAL-SERVICE] [trace_id=] c.d.s.g.u.l.ControllerRequestResponseLogger -
//            ==========================INCOMING Response Begin===========================
//    Status code  : {400}
//    Headers      : {[]}
    }
}
