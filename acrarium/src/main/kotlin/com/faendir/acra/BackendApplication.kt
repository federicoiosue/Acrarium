/*
 * (C) Copyright 2018 Lukas Morawietz (https://github.com/F43nd1r)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.faendir.acra

import com.faendir.acra.util.YamlPropertySourceFactory
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration
import org.springframework.boot.autoconfigure.mustache.MustacheAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.PropertySource
import org.springframework.context.annotation.PropertySources

@SpringBootApplication(exclude = [SecurityAutoConfiguration::class, MustacheAutoConfiguration::class, ErrorMvcAutoConfiguration::class])
@PropertySources(
    PropertySource("classpath:default.yml", factory = YamlPropertySourceFactory::class),
    PropertySource(value = ["file:\${user.home}/.config/acrarium/application.properties"], ignoreResourceNotFound = true),
    PropertySource(value = ["file:\${user.home}/.config/acrarium/application.yml"], ignoreResourceNotFound = true, factory = YamlPropertySourceFactory::class),
    PropertySource(value = ["file:\${user.home}/.acra/application.properties"], ignoreResourceNotFound = true),
    PropertySource(value = ["file:\${user.home}/.acra/application.yml"], ignoreResourceNotFound = true, factory = YamlPropertySourceFactory::class)
)
@Import(MailSenderAutoConfiguration::class)
@EnableCaching
@ConfigurationPropertiesScan
class BackendApplication : SpringBootServletInitializer() {
    override fun configure(builder: SpringApplicationBuilder): SpringApplicationBuilder = builder.sources(BackendApplication::class.java)
}

fun main(args: Array<String>) {
    SpringApplication.run(BackendApplication::class.java, *args)
}