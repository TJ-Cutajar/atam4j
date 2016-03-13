package me.atam.atam4jsampleapp;

import com.google.common.io.Resources;
import io.dropwizard.Application;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import me.atam.atam4j.Atam4j;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class Atam4jTestApplication extends Application<ApplicationConfiguration> {

    public static void main(String[] args) throws Exception {
        if (args == null || args.length == 0) {
            args = new String[]{"server", new File(Resources.getResource("app-config.yml").toURI()).getAbsolutePath()};
        }

        new Atam4jTestApplication().run(args);
    }

    @Override
    public void initialize(final Bootstrap bootstrap) {

    }

    @Override
    public void run(final ApplicationConfiguration configuration, final Environment environment) throws Exception {
        Atam4j atam4j = new Atam4j.Atam4jBuilder(environment.jersey())
                .withUnit(TimeUnit.MILLISECONDS)
                .withInitialDelay(configuration.getInitialDelayInMillis())
                .withPeriod(5000)
                .withTestClasses(configuration.getTestClasses())
                .build()
                .initialise();

        environment.lifecycle().manage(new Managed() {
            @Override
            public void start() throws Exception {

            }

            @Override
            public void stop() throws Exception {
                atam4j.stop();
            }
        });
    }
}