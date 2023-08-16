package com.anii.querydsl.config.listener;

import com.anii.querydsl.common.UtilsCustomizer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApplicationInitListener implements ApplicationListener<ContextStartedEvent> {

    private List<UtilsCustomizer> utilsCustomizerList;

    public ApplicationInitListener(ObjectProvider<UtilsCustomizer> utilsCustomizers) {
        this.utilsCustomizerList = utilsCustomizers.orderedStream().toList();
    }

    @Override
    public void onApplicationEvent(ContextStartedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        utilsCustomizerList.stream()
                .forEach(utilsCustomizer -> utilsCustomizer.customize(applicationContext));
    }
}
