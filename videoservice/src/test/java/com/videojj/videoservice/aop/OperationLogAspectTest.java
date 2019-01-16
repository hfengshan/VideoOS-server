package com.videojj.videoservice.aop;

import com.videojj.videoservice.annotation.OperationLogAnnotationService;
import com.videojj.videoservice.enums.OperationLogTypeEnum;
import com.videojj.videoservice.service.OperationLogService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.text.MessageFormat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;


@RunWith(JUnit4.class)
public class OperationLogAspectTest {

    @InjectMocks
    private OperationLogAspect operationLogAspect;

    @Mock
    private OperationLogService operationLogService;

    @Mock
    private OperationLogAnnotationService operationLogAnnotationService;

    @Mock
    private JoinPoint joinPoint;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        Mockito.when(joinPoint.getArgs()).thenReturn(new Object[]{"位置1",2,new NestedClass4Test_1()});
    }

    /**
     * 测试descArgPositions和fieldNames都是默认值的情况
     */
    @Test
    public void testAfterReturning_1() {
        Mockito.when(operationLogAnnotationService.type()).thenReturn(OperationLogTypeEnum._1);
        Mockito.when(operationLogAnnotationService.descArgPositions()).thenReturn(new int[]{-1});
        Mockito.when(operationLogAnnotationService.fieldNames()).thenReturn(new String[]{StringUtils.EMPTY});
        operationLogAspect.afterReturning(joinPoint,operationLogAnnotationService);
        verify(operationLogService,times(1)).addOperationLog(eq(MessageFormat.format(OperationLogTypeEnum._1.getOperationDescTemplate(), "null")),any());
    }

    /**
     * 测试descArgPositions和fieldNames都有值的情况
     */
    @Test
    public void testAfterReturning_2() {
        Mockito.when(operationLogAnnotationService.type()).thenReturn(OperationLogTypeEnum._102);
        Mockito.when(operationLogAnnotationService.descArgPositions()).thenReturn(new int[]{0,1,2});
        Mockito.when(operationLogAnnotationService.fieldNames()).thenReturn(new String[]{StringUtils.EMPTY,StringUtils.EMPTY,"attr3.attr5"});
        operationLogAspect.afterReturning(joinPoint,operationLogAnnotationService);
        verify(operationLogService,times(1))
                .addOperationLog(eq(MessageFormat.format(OperationLogTypeEnum._102.getOperationDescTemplate(), "位置1",2,5)),any());
    }

    /**
     * 测试descArgPositions有值和fieldNames没有值的情况
     */
    @Test
    public void testAfterReturning_3() {
        Mockito.when(operationLogAnnotationService.type()).thenReturn(OperationLogTypeEnum._1);
        Mockito.when(operationLogAnnotationService.descArgPositions()).thenReturn(new int[]{0});
        Mockito.when(operationLogAnnotationService.fieldNames()).thenReturn(new String[]{StringUtils.EMPTY});
        operationLogAspect.afterReturning(joinPoint,operationLogAnnotationService);
        verify(operationLogService,times(1))
                .addOperationLog(eq(MessageFormat.format(OperationLogTypeEnum._1.getOperationDescTemplate(), "位置1")),any());
    }

    /**
     * 测试descArgPositions没有值和fieldNames有值的情况
     */
    @Test
    public void testAfterReturning_4() {
        Mockito.when(operationLogAnnotationService.type()).thenReturn(OperationLogTypeEnum._1);
        Mockito.when(operationLogAnnotationService.descArgPositions()).thenReturn(new int[]{-1});
        Mockito.when(operationLogAnnotationService.fieldNames()).thenReturn(new String[]{"attr3.attr4"});
        operationLogAspect.afterReturning(joinPoint,operationLogAnnotationService);
        verify(operationLogService,times(1))
                .addOperationLog(eq(MessageFormat.format(OperationLogTypeEnum._1.getOperationDescTemplate(), "null")),any());
    }

    private class NestedClass4Test_1{
        private String attr1 = "attr1";
        private int attr2 = 2;
        private NestedClass4Test_2 attr3 = new NestedClass4Test_2("4",5);

        public String getAttr1() {
            return attr1;
        }

        public void setAttr1(String attr1) {
            this.attr1 = attr1;
        }

        public int getAttr2() {
            return attr2;
        }

        public void setAttr2(int attr2) {
            this.attr2 = attr2;
        }

        public NestedClass4Test_2 getAttr3() {
            return attr3;
        }

        public void setAttr3(NestedClass4Test_2 attr3) {
            this.attr3 = attr3;
        }
    }

    @AllArgsConstructor
    private class NestedClass4Test_2{
        private String attr4 = "attr4";
        private int attr5 = 5;

        public String getAttr4() {
            return attr4;
        }

        public void setAttr4(String attr4) {
            this.attr4 = attr4;
        }

        public int getAttr5() {
            return attr5;
        }

        public void setAttr5(int attr5) {
            this.attr5 = attr5;
        }
    }

}
