package com.java2nb.novel.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 作家收入配置
 * @author cd
 */
@Component
@Data
@ConfigurationProperties(prefix = "author.income")
public class AuthorIncomeConfig {

    private BigDecimal taxRate;

    private BigDecimal shareProportion;

    private BigDecimal exchangeProportion;

}
