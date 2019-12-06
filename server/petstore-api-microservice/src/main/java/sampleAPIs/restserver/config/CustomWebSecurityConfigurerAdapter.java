package sampleAPIs.restserver.config;

import org.springframework.beans.factory.annotation.Value;
import sampleAPIs.restserver.filters.CsrfTokenResponseHeaderBindingFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfFilter;


@Configuration
public class CustomWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {


  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.addFilterAfter(new CsrfTokenResponseHeaderBindingFilter(), CsrfFilter.class);
    http.csrf().disable();
  }
}