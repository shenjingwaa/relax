package com.relax.relax.common.listener;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

public class BannerPrintListener implements ApplicationListener<ApplicationReadyEvent> {
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        System.out.println(RELAX_BANNER);
    }

    private final String RELAX_BANNER="\n" +
            "   ____     U _____ u    _          _        __  __   \n" +
            "U |  _\"\\ u  \\| ___\"|/   |\"|     U  /\"\\  u    \\ \\/\"/   \n" +
            " \\| |_) |/   |  _|\"   U | | u    \\/ _ \\/     /\\  /\\   \n" +
            "  |  _ <     | |___    \\| |/__   / ___ \\    U /  \\ u  \n" +
            "  |_| \\_\\    |_____|    |_____| /_/   \\_\\    /_/\\_\\   \n" +
            "  //   \\\\_   <<   >>    //  \\\\   \\\\    >>  ,-,>> \\\\_  \n" +
            " (__)  (__) (__) (__)  (_\")(\"_) (__)  (__)  \\_)  (__) \n" +
            "\n";
}
