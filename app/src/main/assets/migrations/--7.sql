ALTER TABLE person ADD biometricStatus INTEGER;
--ALTER TABLE person ADD personUuId TEXT;
ALTER TABLE person ADD dateTime TEXT;
ALTER TABLE person ADD lastEditDateTime TEXT;
ALTER TABLE encounter ADD dateTime TEXT;
ALTER TABLE encounter ADD lastEditDateTime TEXT;
ALTER TABLE biometrics ADD dateTime TEXT;
ALTER TABLE biometricsrecapture ADD dateTime TEXT;