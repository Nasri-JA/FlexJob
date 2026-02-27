-- FlexJob Database Initialization Script
-- Creates separate databases for each microservice

-- User Service Database
CREATE DATABASE flexjob_users;
GRANT ALL PRIVILEGES ON DATABASE flexjob_users TO flexjob;

-- Job Service Database
CREATE DATABASE flexjob_jobs;
GRANT ALL PRIVILEGES ON DATABASE flexjob_jobs TO flexjob;

-- Engagement Service Database (applications, bookings, reviews)
CREATE DATABASE flexjob_engagements;
GRANT ALL PRIVILEGES ON DATABASE flexjob_engagements TO flexjob;

-- Payment Service Database
CREATE DATABASE flexjob_payments;
GRANT ALL PRIVILEGES ON DATABASE flexjob_payments TO flexjob;

-- Notification Service Database
CREATE DATABASE flexjob_notifications;
GRANT ALL PRIVILEGES ON DATABASE flexjob_notifications TO flexjob;
