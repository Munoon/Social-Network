package com.train4game.social;

import com.train4game.social.config.AppConfig;
import com.train4game.social.config.MvcConfig;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

@SpringJUnitWebConfig(classes = {AppConfig.class, MvcConfig.class})
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
@ExtendWith(TimingExtension.class)
public abstract class AbstractTest {
}
