-- FlexJob Database Initialization Script
-- Creates separate databases for each microservice

-- User Service Database
CREATE DATABASE flexjob_users;
GRANT ALL PRIVILEGES ON DATABASE flexjob_users TO flexjob;

-- Job Service Database
CREATE DATABASE flexjob_jobs;
GRANT ALL PRIVILEGES ON DATABASE flexjob_jobs TO flexjob;

-- Application Service Database
CREATE DATABASE flexjob_applications;
GRANT ALL PRIVILEGES ON DATABASE flexjob_applications TO flexjob;

-- Booking Service Database
CREATE DATABASE flexjob_bookings;
GRANT ALL PRIVILEGES ON DATABASE flexjob_bookings TO flexjob;

-- Payment Service Database
CREATE DATABASE flexjob_payments;
GRANT ALL PRIVILEGES ON DATABASE flexjob_payments TO flexjob;

-- Notification Service Database
CREATE DATABASE flexjob_notifications;
GRANT ALL PRIVILEGES ON DATABASE flexjob_notifications TO flexjob;

-- Review Service Database
CREATE DATABASE flexjob_reviews;
GRANT ALL PRIVILEGES ON DATABASE flexjob_reviews TO flexjob;
