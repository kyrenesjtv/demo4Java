package me.kyrene.demo.jdk8NewFunction.classlib;

/**
 * Created by wanglin on 2017/11/16.
 */
public class Streams {
    public  enum Status{
        OPEN,CLOSE
    };
    public  static final class Task{
        private final Status status;
        private final Integer points;
        Task( final Status status, final Integer points ) {
            this.status = status;
            this.points = points;
        }
        public Integer getPoints(){
            return points;
        }
        public Status getStatus(){
            return status;
        }

        @Override
        public String toString() {
            return "Task{" +
                    "status=" + status +
                    ", points=" + points +
                    '}';
        }
    }
}
