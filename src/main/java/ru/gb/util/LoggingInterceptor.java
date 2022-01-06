package ru.gb.util;


import okhttp3.*;

import java.io.IOException;

import static ru.gb.util.ColouredSystemOutPrintln.*;

public class LoggingInterceptor implements Interceptor {

    @Override public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();

        long timeBefore = System.nanoTime();

        System.out.println(String.format(ANSI_BRIGHT_BLUE + "\nSending request:"
                        + ANSI_BRIGHT_YELLOW + "\n%s "
                        + ANSI_BLUE + "%s "
                        + ANSI_RESET,
                request.method(),
                request.url()
        ));

        Response response = chain.proceed(request);

        long timeAfter = System.nanoTime();

        System.out.println(String.format(ANSI_BRIGHT_BLUE + "\nReceived response:"
                        + ANSI_RESET + "\n%s in "
                        + ANSI_BRIGHT_GREEN + "%.1fms"
                        + ANSI_BRIGHT_WHITE + "\nwith headers:"
                        + ANSI_RESET + " \n%s",
                response.networkResponse(),
                (timeAfter - timeBefore) / 1e06,
                response.headers()
        ));
        return response;
    }
}
