package com.dsaclock.services;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Service
public class OtpService {

    private final SecureRandom secureRandom = new SecureRandom();

    private final Map<String, OtpRecord> otpStore =
            new ConcurrentHashMap<>();

    private static final Duration OTP_COOLDOWN = Duration.ofSeconds(60);

    private static final Duration OTP_VALIDITY =
            Duration.ofMinutes(5);

    public synchronized String generateAndStoreOtp(String email) { //synchronized prevents simultaneous request for
                                                                   // the same email
        String key = email.toLowerCase();
        Instant now = Instant.now();

        OtpRecord existingRecord = otpStore.get(key);

        if (existingRecord != null) { //if a previous generated otp exists, check if it was more than
                                      //60 seconds ago or not, if not then reject to generate again
            Duration elapsed = Duration.between(
                    existingRecord.createdAt(), now
            );

            if (elapsed.compareTo(OTP_COOLDOWN) < 0) {
                throw new IllegalStateException( //throws exception as cooldown is not over
                        "Please wait before requesting another OTP."
                );
            }
        }

        String otp = String.format(
                "%06d",
                secureRandom.nextInt(1_000_000)
        );

        Instant expiresAt = now.plus(OTP_VALIDITY);

        System.out.println("OTP created at: " + now);
        System.out.println("OTP expires at: " + expiresAt);

        otpStore.put(
                key,
                new OtpRecord(otp, now, expiresAt)
        );

        return otp;
    }

    public boolean verifyOtp(String email, String submittedOtp) {
        System.out.println("verify otp reached");

        String key = email.toLowerCase();
        OtpRecord record = otpStore.get(key);

        if (record == null) { //no recorded otp found: failed
            System.out.println("null otp");
            return false;
        }

        if (!Instant.now().isBefore(record.expiresAt())) { //time up: failed
            System.out.println("Current time: " + Instant.now());
            System.out.println("Expiry time: " + record.expiresAt());
            otpStore.remove(key, record);
            return false;
        }

        if (!record.otp().equals(submittedOtp)) { //mismatch: failed
            System.out.println("otp mismatch");
            return false;
        }

        System.out.println("otp verified");
        otpStore.remove(key, record);
        return true;
    }

    private record OtpRecord(String otp, Instant createdAt, Instant expiresAt) {}
}