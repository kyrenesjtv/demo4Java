package me.kyrene.demo.jdk8NewFunction.DateTime;

import org.junit.Test;

import java.awt.*;
import java.time.*;

/**
 * Created by wanglin on 2017/12/6.
 */
public class Date {
    @Test
    public void test01() {
        //Clock类，它通过指定一个时区，然后就可以获取到当前的时刻，日期与时间
        final Clock clock = Clock.systemUTC();
        //这个是什么时区的?
        System.out.println(clock.instant());
        //1980开始的毫秒值
        System.out.println(clock.millis());
    }

    @Test
    public void test02() {
        final Clock clock = Clock.systemUTC();
        //获取的是你当前的时区
        final LocalDate date = LocalDate.now();
        //获取的是你指定的(这里应该是默认的)
        final LocalDate dateFromClock = LocalDate.now(clock);
        System.out.println(date);//2017-12-06
        System.out.println(dateFromClock);//2017-12-06
        //获取的是你当前的时区
        final LocalTime time = LocalTime.now();
        //获取的是你指定的(这里应该是默认的)
        final LocalTime timeFromClock = LocalTime.now(clock);
        System.out.println(time);//10:58:48.061 (东八区)
        System.out.println(timeFromClock);//02:58:48.062
        //本地的
        final LocalDateTime dateTime = LocalDateTime.now();
        //0时区的(默认的)
        final LocalDateTime dateTimeFromClocl = LocalDateTime.now(clock);
        System.out.println(dateTime);//2017-12-06T10:58:48.062
        System.out.println(dateTimeFromClocl);//2017-12-06T02:58:48.062
        //当地时区
        final ZonedDateTime zonedDateTime = ZonedDateTime.now();
        //0时区
        final ZonedDateTime zonedDatetimeFromClock = ZonedDateTime.now( clock );
        //手动设置时区
        final ZonedDateTime zonedDatetimeFromZone = ZonedDateTime.now( ZoneId.of( "Asia/Shanghai" ) );
        System.out.println(zonedDateTime);//把你本地的时区给打印出来
        System.out.println(zonedDatetimeFromClock);
        System.out.println(zonedDatetimeFromZone);//在ZoneId 这个类里面可以找到大部分的设定时区的stromg/map
        //年 月 日 ， 时，分，秒
        final LocalDateTime from = LocalDateTime.of( 2016, Month.APRIL, 16, 0, 0, 0 );
        final LocalDateTime to = LocalDateTime.of( 2017, Month.APRIL, 16, 23, 59, 59 );

        final Duration duration = Duration.between( from, to );
        System.out.println( "Duration in days: " + duration.toDays() );//差的天数
        System.out.println( "Duration in hours: " + duration.toHours() );//差的小时
    }
}
