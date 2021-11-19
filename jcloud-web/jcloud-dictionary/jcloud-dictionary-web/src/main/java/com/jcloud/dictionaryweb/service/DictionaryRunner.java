package com.jcloud.dictionaryweb.service;

import com.jcloud.common.consts.Const;
import com.jcloud.dictionary.api.DictionaryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @author jiaxm
 * @date 2021/4/13
 */
@Service
public class DictionaryRunner implements ApplicationRunner {

    @Autowired
    private DictionaryProvider dictionaryProvider;


    @Autowired
    private Environment environment;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<String> profiles = Arrays.asList(environment.getActiveProfiles());
        if (profiles.contains(Const.PROFILE_DEV)) {
            dictionaryProvider.refreshAll();
        }
    }
}
