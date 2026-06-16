package com.company.ratelimite;

public class RateLimiterDemo {
    public static void main(String[] args) throws InterruptedException {

        System.out.println("########## SLIDING WINDOW LOG (3 req / 1s) ##########");
        RateLimiterController controller =
                new RateLimiterController(new SlidingWindowLogStrategy(3, 1000));

        System.out.println("-- user_A bursts 5 requests --");
        for (int i = 1; i <= 5; i++) controller.handleRequest("user_A");

        System.out.println("-- user_B is independent --");
        controller.handleRequest("user_B");

        System.out.println("-- wait 1.1s, window slides, user_A again --");
        Thread.sleep(1100);
        for (int i = 1; i <= 2; i++) controller.handleRequest("user_A");

        System.out.println("\n########## RUNTIME SWAP -> TOKEN BUCKET (cap 3, 1 token/s) ##########");
        controller.setStrategy(new TokenBucketStrategy(3, 1));

        System.out.println("-- user_C bursts 5 (cap 3 -> 3 allowed) --");
        for (int i = 1; i <= 5; i++) controller.handleRequest("user_C");

        System.out.println("-- wait 2s, ~2 tokens refill, user_C again --");
        Thread.sleep(2000);
        for (int i = 1; i <= 3; i++) controller.handleRequest("user_C");
    }
}
